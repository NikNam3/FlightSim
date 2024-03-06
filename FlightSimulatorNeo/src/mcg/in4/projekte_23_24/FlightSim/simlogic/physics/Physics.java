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

import java.util.Arrays;

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
                //System.out.println("Surface: " + surfaceId);
                //System.out.println("    Lift: " + Math3d.string(calculateWingLift(surface, rigidBody.velocity, transform), false));
                //System.out.println("    Drag: " + Math3d.string(calculateWingDrag(surface, rigidBody.velocity, transform), false));

                // Add Lift to the RigidBody
                rigidBody.forces.add(new float[][] {
                        calculateWingLift(surface, rigidBody.velocity, transform),
                        offsetVector,
                });

                // Add Drag to the RigidBody
                rigidBody.forces.add(new float[][] {
                        calculateWingDrag(surface, rigidBody.velocity, transform),
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

                //System.out.println("Thrust: " + Math3d.string(Math3d.mul(thrust, getNormalizedForwardVector(transform)), false));
                rigidBody.forces.add(new float[][]{
                        Math3d.mul(thrust, getNormalizedForwardVector(transform)),
                        new float[] {0, 0, 0},
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
            //System.out.println("Gravity: " + Math3d.string(Math3d.mul(9.81f*rigidBody.mass, new float[] {0, -1, 0}), false));
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
            //System.out.println("Total Force: " + Math3d.string(totalForce, false));
            System.out.println("Total Torque: " + Math3d.string(totalTorque, false));

            float[] acceleration = Math3d.div(totalForce, rigidBody.mass);
            float[] angularAcceleration = Math3d.mul(Math3d.inverse(rigidBody.inertiaTensor), totalTorque);

            System.out.println("Angular Acceleration: " + Math3d.string(angularAcceleration, false));


            rigidBody.velocity = Math3d.add(rigidBody.velocity, Math3d.mul(acceleration, deltaTime));
            rigidBody.angularVelocity = Math3d.add(rigidBody.angularVelocity, Math3d.mul(angularAcceleration, deltaTime));

            //System.out.println("Velocity: " + Arrays.toString(rigidBody.velocity));
            //  TEMP GROUND TODO REMOVE
            if (transform.matrixOffset[1][3] < 0) {
                transform.matrixOffset[1][3] = 0;
                rigidBody.velocity[1] = 0;
                rigidBody.angularVelocity[0] = 0;
            }


            transform.matrixOffset[0][3] = transform.matrixOffset[0][3] + rigidBody.velocity[0] * deltaTime;
            transform.matrixOffset[1][3] = transform.matrixOffset[1][3] + rigidBody.velocity[1] * deltaTime;
            transform.matrixOffset[2][3] = transform.matrixOffset[2][3] + rigidBody.velocity[2] * deltaTime;

            transform.matrixRotate = Math3d.add(transform.matrixRotate, Math3d.rotationMatrix(rigidBody.angularVelocity[0]*deltaTime, rigidBody.angularVelocity[1]*deltaTime, rigidBody.angularVelocity[2]*deltaTime));

        }

    }

    private static float[] calculateWingDrag(Surface surface, float[] aircraftVelocity, Transform aircraftTransform) {
        float[] airflow = calculateAirflow(new float[]{0, 0, 0}, aircraftVelocity);
        float airDensity = Weather.getAirDensityAtPosition(new float[]{0, 0, 0});
        float aoa = calculateAngleOfAttack(aircraftTransform, airflow);
        float dragCoefficient = surface.calculateDragCoefficient(aoa);

        // Drag = -1 * 0.5 * Cd * A * p * V^2
        float dragMagnitude = -1*dragCoefficient * surface.SURFACE_AREA * Math3d.dot(airflow, airflow) * airDensity / 2;

        float[] airflowDirection = Math3d.normalize(airflow);

        // Drag vector = direction * magnitude
        return Math3d.mul(airflowDirection, dragMagnitude);
    }

    private static float[] calculateWingLift(Surface surface, float[] aircraftVelocity, Transform aircraftTransform) {
        float[] airflow = calculateAirflow(new float[]{0, 0, 0}, aircraftVelocity);


        float[] forwardAirflow = getRelVecInDirection(airflow, getNormalizedForwardVector(aircraftTransform));


        float airDensity = Weather.getAirDensityAtPosition(new float[]{0, 0, 0});
        float aoa = calculateAngleOfAttack(aircraftTransform, airflow);
        float liftCoefficient = surface.calculateLiftCoefficient(aoa);
        // Lift = 0.5 * Cl * A * p * V^2
        float liftMagnitude = liftCoefficient * surface.SURFACE_AREA * Math3d.dot(forwardAirflow, forwardAirflow) * airDensity / 2;

        // Lift vector = direction * magnitude
        return Math3d.mul(new float[] {0, 1, 0}, liftMagnitude);
    }
    // Returns the aoa in rad
    private static float calculateAngleOfAttack(Transform aircraftTransform, float[] airflow) {
        float[] airflowDirection = Math3d.normalize(airflow);
        float[] chord_line = getNormalizedForwardVector(aircraftTransform);
        return (float) Math.acos(Math3d.dot(airflowDirection, chord_line)) * (float) (Math.PI/180);

    }

    private static float[] calculateAirflow(float[] worldPosition, float[] aircraftVelocity) {
        float[] wind = Weather.getWindAtPosition(worldPosition);
        return Math3d.add(wind, aircraftVelocity);
    }
    private static float[] getNormalizedForwardVector(Transform transform) {
        float[] forward = new float[]{0, 0, -1, 0};
        return Math3d.normalize(
                Math3d.mul(transform.matrixRotate, forward)
        );

    }
    private static float[] getNormalizedUpVector(Transform transform) {
        float[] up = new float[]{0, 1, 0, 0};
        return Math3d.normalize(
                Math3d.mul(transform.matrixRotate, up)
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
    private static float[] getRelVecInDirection(float[] vec, float[] direction) {
        direction = Math3d.normalize(direction);
        float dot = Math3d.dot(vec, direction);
        return Math3d.mul(direction, dot);
    }

    private static float calculateThrust(Engine engine, float[] velocity) {
        if (engine.getCurrentRPM() <= 0) {
            return 0;
        }

        return (float) ((4.392e-8f * engine.getCurrentRPM() *
                                (Math.pow(engine.getDIAMETER() * 39.3701f, 3.5f)  / Math.sqrt(engine.getPitch())) *
                                (4.233e-4 * engine.getCurrentRPM() * engine.getPitch() -
                                        Math3d.length(calculateAirflow(new float[] {0,0,0}, velocity)) / 2) * 1.4));

    }
}
