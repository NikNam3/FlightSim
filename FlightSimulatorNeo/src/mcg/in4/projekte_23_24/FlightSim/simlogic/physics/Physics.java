package mcg.in4.projekte_23_24.FlightSim.simlogic.physics;

import mcg.in4.projekte_23_24.FlightSim.Enviroment.Weather;
import mcg.in4.projekte_23_24.FlightSim.engine.Math3d;
import mcg.in4.projekte_23_24.FlightSim.engine.structures.Scene;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Engines;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.RigidBody;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Surfaces;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Transform;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.Engine;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.Surface;

public class Physics {
    public static void update(Scene scene, int entity, float deltaTime) {
        // Lift and Drag
        if (scene.hasComponent(entity, RigidBody.class) && scene.hasComponent(entity, Transform.class) && scene.hasComponent(entity, Surfaces.class)) {
            Surfaces surfaces = scene.getComponent(entity, Surfaces.class); // Get the Component which contains all surfaces of the aircraft
            RigidBody rigidBody = scene.getComponent(entity, RigidBody.class); // Get the RigidBody of the aircraft
            Transform transform = scene.getComponent(entity, Transform.class); // Get the Transform of the aircraft

            // Iterate through all surfaces of the aircraft
            for(int surfaceId : surfaces.wingSurfaceIds) {
                // Get the Transform of the surface (holds the relative position of the surface to the aircraft)
                Transform surfaceTransform = scene.getComponent(surfaceId, Transform.class);
                // Convert the matrix to a vector
                float[] offsetVector = getOffsetVector(surfaceTransform);

                // Get the actual Surface entity
                Surface surface = scene.getComponentByParentClass(surfaceId, Surface.class);

                // Add Lift to the RigidBody
                rigidBody.forces.add(new float[][] {
                        calculateWingLift(surface, rigidBody.velocity),
                        offsetVector,
                });

                // Add Drag to the RigidBody
                rigidBody.forces.add(new float[][] {
                        calculateWingDrag(surface, rigidBody.velocity),
                        offsetVector,
                });

            }
            //for(int surfaceId : surfaces.dragSurfaceIds) {
            //    continue; // TODO: Implement drag surfaces
            //}

        }

        // Thrust
        if (scene.hasComponent(entity, Engines.class) && scene.hasComponent(entity, RigidBody.class) && scene.hasComponent(entity, Transform.class)) {
            RigidBody rigidBody = scene.getComponent(entity, RigidBody.class);
            int[] engines = scene.getComponent(entity, Engines.class).engineIds;
            Transform transform = scene.getComponent(entity, Transform.class);

            for(int engineId: engines) {
                Engine engine = scene.getComponent(engineId, Engine.class);


                if (engine.getCurrentRPM() < engine.getSetRPM()) {
                    engine.setCurrentRPM(engine.getCurrentRPM() + engine.getRPMChangeRate() * deltaTime);
                } else if (engine.getCurrentRPM() > engine.getSetRPM()) {
                    engine.setCurrentRPM(engine.getCurrentRPM() - engine.getRPMChangeRate() * deltaTime);
                }

                float thrust = calculateThrust(engine, rigidBody.velocity);
                System.out.println("Thrust: " + thrust);
                rigidBody.forces.add(new float[][]{
                        Math3d.mul(thrust, getNormalizedForwardVector(transform)),
                        getOffsetVector(transform),
                });
            }
        }

        // Gravity
        if (scene.hasComponent(entity, RigidBody.class)) {
            RigidBody rigidBody = scene.getComponent(entity, RigidBody.class);
            rigidBody.forces.add(new float[][] {
                Math3d.mul(9.81f*rigidBody.mass, new float[] {0, -1, 0}),
                rigidBody.relCenterOfGravity,
            });
        }

        // Update the RigidBody
        if (scene.hasComponent(entity, RigidBody.class)) {
            RigidBody rigidBody = scene.getComponent(entity, RigidBody.class);
            Transform transform = scene.getComponent(entity, Transform.class); // Get the Transform of the aircraft


            float[] totalForce = new float[] {0, 0, 0};
            float[] totalTorque = new float[] {0, 0, 0};


            for (float[][] force : rigidBody.forces) {
                float[] forceVector = force[0];
                float[] offsetVector = force[1];
                totalForce = Math3d.add(totalForce, forceVector);
                totalTorque = Math3d.add(totalTorque, Math3d.cross(offsetVector, forceVector));
            }

            rigidBody.forces.clear();

            float[] acceleration = Math3d.div(totalForce, rigidBody.mass);
            float[] angularAcceleration = Math3d.mul(Math3d.inverse(rigidBody.inertiaTensor), totalTorque);

            rigidBody.velocity = Math3d.add(rigidBody.velocity, Math3d.mul(acceleration, deltaTime));
            rigidBody.angularVelocity = Math3d.add(rigidBody.angularVelocity, Math3d.mul(angularAcceleration, deltaTime));
            transform.matrixOffset[0][3] = transform.matrixOffset[0][3] + rigidBody.velocity[0] * deltaTime;
            transform.matrixOffset[1][3] = transform.matrixOffset[1][3] + rigidBody.velocity[1] * deltaTime;
            transform.matrixOffset[2][3] = transform.matrixOffset[2][3] + rigidBody.velocity[2] * deltaTime;

            transform.matrixRotate = Math3d.add(transform.matrixRotate, Math3d.rotationMatrix(rigidBody.angularVelocity[0]*deltaTime, rigidBody.angularVelocity[1]*deltaTime, rigidBody.angularVelocity[2]*deltaTime));

        }

    }

    private static float[] calculateWingDrag(Surface surface, float[] aircraftVelocity) {
        float[] airflow = calculateAirflow(new float[]{0, 0, 0}, aircraftVelocity);
        float airDensity = Weather.getAirDensityAtPosition(new float[]{0, 0, 0});
        float aoa = calculateAngleOfAttack(surface.NORMAL, airflow);
        float dragCoefficient = surface.calculateLiftCoefficient(aoa);

        // Drag = -1 * 0.5 * Cd * A * p * V^2
        float dragMagnitude = -1*dragCoefficient * surface.SURFACE_AREA * Math3d.dot(airflow, airflow) * airDensity / 2;

        float[] directionOfMotion = Math3d.normalize(aircraftVelocity);
        // Drag vector = direction * magnitude
        return Math3d.mul(directionOfMotion, dragMagnitude);
    }

    private static float[] calculateWingLift(Surface surface, float[] aircraftVelocity) {
        float[] airflow = calculateAirflow(new float[]{0, 0, 0}, aircraftVelocity);
        float airDensity = Weather.getAirDensityAtPosition(new float[]{0, 0, 0});
        float aoa = calculateAngleOfAttack(surface.NORMAL, airflow);
        float liftCoefficient = surface.calculateLiftCoefficient(aoa);

        // Lift = 0.5 * Cl * A * p * V^2
        float liftMagnitude = liftCoefficient * surface.SURFACE_AREA * Math3d.dot(airflow, airflow) * airDensity / 2;

        // Lift vector = direction * magnitude
        return Math3d.mul(surface.NORMAL, liftMagnitude);
    }

    private static float calculateAngleOfAttack(float[] surfaceNormal, float[] windVector) {
        return (float) Math.acos(Math3d.dot(surfaceNormal, windVector) / (Math3d.length(surfaceNormal) * Math3d.length(windVector))); // NOTE: WIND CAN'T BE ZERO
    }

    private static float[] calculateAirflow(float[] worldPosition, float[] aircraftVelocity) {
        float[] wind = Weather.getWindAtPosition(worldPosition);
        return Math3d.sub(wind, aircraftVelocity);
    }
    private static float[] getNormalizedForwardVector(Transform transform) {
        float[] forward = new float[]{0, 0, -1, 0};
        return Math3d.normalize(
                Math3d.mul(transform.matrixRotate, forward)
        );
    }
    private static float[] getOffsetVector(Transform transform) {
        // Convert the matrix to a vector
        return new float[] {
                transform.matrixOffset[0][3],
                transform.matrixOffset[1][3],
                transform.matrixOffset[2][3]
        };
    }

    private static float calculateThrust(Engine engine, float[] velocity) {
        if (engine.getCurrentRPM() <= 0) {
            return 0;
        }

        return (float)
                (4.392e-8f * engine.getCurrentRPM() *
                        (Math.pow(engine.getDIAMETER() * 39.3701f, 3.5f)  / Math.sqrt(engine.getPitch())) *
                        (4.233e-4 * engine.getCurrentRPM() * engine.getPitch() -
                                Math3d.length(calculateAirflow(new float[] {0,0,0}, velocity)))) * 3;

    }
}
