package mcg.in4.projekte_23_24.FlightSim.simlogic.physics;

import mcg.in4.projekte_23_24.FlightSim.Enviroment.Weather;
import mcg.in4.projekte_23_24.FlightSim.engine.Math3d;
import mcg.in4.projekte_23_24.FlightSim.engine.structures.Scene;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Engines;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.RigidBody;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Surfaces;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Transform;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.Engine;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.surfaces.ControlledWingSurface;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.surfaces.Surface;

import java.util.Arrays;

public class Physics {
    public static void update(Scene scene, int entity, float deltaTime) {
        //System.out.println("Physics.update() called");

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
                float[] offsetVector = getCOGOffsetVector(surfaceTransform, rigidBody.relCenterOfGravity, transform);

                // Get the actual Surface entity
                Surface surface = scene.getComponentByParentClass(surfaceId, Surface.class);
                if (surfaceId == 9) {
                    //System.out.println("Surface: " + surfaceId);
                    //System.out.println("    Lift: " + Math3d.string(calculateWingLift(surface, rigidBody.velocity, transform), false));
                    //System.out.println("    Drag: " + Math3d.string(calculateWingDrag(surface, rigidBody.velocity, transform), false));
                    //System.out.println("    TorqueLift: " + Math3d.string(Math3d.cross(offsetVector, calculateWingLift(surface, rigidBody.velocity, transform)), false) + " offsetVector: " + Math3d.string(offsetVector, false));
                    //System.out.println("    TorqueDrag: " + Math3d.string(Math3d.cross(offsetVector, calculateWingDrag(surface, rigidBody.velocity, transform)), false));
                }
                System.out.println("Surface: " + surfaceId);
                System.out.println("    Lift: " + Math3d.string(calculateWingLift(surface, rigidBody.velocity, transform), false));
                //System.out.println("    Drag: " + Math3d.string(calculateWingDrag(surface, rigidBody.velocity, transform), false));
                System.out.println("    TorqueLift: " + Math3d.string(Math3d.cross(offsetVector, calculateWingLift(surface, rigidBody.velocity, transform)), false) + " offsetVector: " + Math3d.string(offsetVector, false));
                //System.out.println("    TorqueDrag: " + Math3d.string(Math3d.cross(offsetVector, calculateWingDrag(surface, rigidBody.velocity, transform)), false));
                // Add Lift to the RigidBody
                rigidBody.forces.add(new float[][] {
                        calculateWingLift(surface, rigidBody.velocity, transform),
                        offsetVector,
                });


                // Add Drag to the RigidBody
                rigidBody.forces.add(new float[][] {
                        calculateWingDrag(surface, rigidBody.velocity, transform),
                        Math3d.vec3(0,0,0),
                });


            }


            for(int surfaceId : surfaces.dragSurfaceIds) {
                Transform surfaceTransform = scene.getComponent(surfaceId, Transform.class);
                float[] offsetVector = getCOGOffsetVector(surfaceTransform, rigidBody.relCenterOfGravity, transform);
                Surface surface = scene.getComponentByParentClass(surfaceId, Surface.class);
                //System.out.println("Surface: " + surfaceId);
                //System.out.println("    Lift: " + Math3d.string(calculateWingLift(surface, rigidBody.velocity, transform), false));
                //System.out.println("    Drag: " + Math3d.string(calculateWingDrag(surface, rigidBody.velocity, transform), false));
                //System.out.println("    TorqueLift: " + Math3d.string(Math3d.cross(offsetVector, calculateWingLift(surface, rigidBody.velocity, transform)), false));
                //System.out.println("    TorqueDrag: " + Math3d.string(Math3d.cross(offsetVector, calculateFormDrag(surface, rigidBody.velocity, transform)), false));

                rigidBody.forces.add(new float[][] {
                       calculateFormDrag(surface, rigidBody.velocity, transform),
                        offsetVector,
                });

            }

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

                //System.out.println("Thrust: " + Math3d.string(Math3d.mul(thrust, getNormalizedForwardVector(transform)), false)+ "Forward: " +  Math3d.string(getNormalizedForwardVector(transform), false));
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
                new float[] {0, 0, 0},
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
                //System.out.println("Torque: " + Math3d.string(Math3d.cross(offsetVector, forceVector), false) + " Offset: " + Math3d.string(offsetVector, false) + " Force: " + Math3d.string(forceVector, false));
            }

            rigidBody.forces.clear();
            //System.out.println("Total Force: " + Math3d.string(totalForce, false));
            //System.out.println("Total Torque: " + Math3d.string(totalTorque, false));


            float[] acceleration = Math3d.div(totalForce, rigidBody.mass);
            float[] angularAcceleration = Math3d.mul(Math3d.inverse(rigidBody.inertiaTensor), totalTorque);

            //System.out.println("Angular Acceleration: " + Math3d.string(angularAcceleration, false));<


            rigidBody.velocity = Math3d.add(rigidBody.velocity, Math3d.mul(acceleration, deltaTime));
            //System.out.println("angularVelocitybefore: " + Arrays.toString(rigidBody.angularVelocity));
            rigidBody.angularVelocity = Math3d.add(rigidBody.angularVelocity, Math3d.mul(angularAcceleration, deltaTime));
            //System.out.println("angularVelocity +: " + Arrays.toString(Math3d.mul(angularAcceleration, deltaTime)));
            //System.out.println("angularVelocityafter: " + Arrays.toString(rigidBody.angularVelocity));
            System.out.println("Velocity: " + Arrays.toString(rigidBody.velocity));
            //System.out.println("Forward: " + Arrays.toString(getNormalizedForwardVector(transform)));
            //  TEMP GROUND TODO REMOVE
            if (transform.matrixOffset[1][3] <= 0) {
                transform.matrixOffset[1][3] = 0;
                if (rigidBody.velocity[1] < 0) {
                    rigidBody.velocity[1] = 0;
                }

            }


            transform.matrixOffset[0][3] = transform.matrixOffset[0][3] + rigidBody.velocity[0] * deltaTime;
            transform.matrixOffset[1][3] = transform.matrixOffset[1][3] + rigidBody.velocity[1] * deltaTime;
            transform.matrixOffset[2][3] = transform.matrixOffset[2][3] + rigidBody.velocity[2] * deltaTime;

            transform.matrixRotate = Math3d.mul(transform.matrixRotate, Math3d.rotationMatrix(rigidBody.angularVelocity[0]*deltaTime, rigidBody.angularVelocity[1]*deltaTime, rigidBody.angularVelocity[2]*deltaTime));
            //System.out.println("Rotation: " + Math3d.string(transform.matrixRotate, false));
        }

    }
    private static float[] calculateWingLift(Surface surface, float[] aircraftVelocity, Transform aircraftTransform) {
        float[] worldPosition = Math3d.getVec3(aircraftTransform.matrixOffset);

        float   A = surface.SURFACE_AREA;
        float   p = Weather.getAirDensityAtPosition(worldPosition);
        float[] v = calculateAirflow(aircraftVelocity, worldPosition);
        float  Cl = surface.calculateLiftCoefficient(
                calculateAngleOfAttack(surface, aircraftTransform, aircraftVelocity)
        );
        //if (surface.NORMAL[0] == 1) {
        //    System.out.println("LiftCoefficient: " + Cl + "AOA: " + calculateAngleOfAttack(surface, aircraftTransform, aircraftVelocity) + " AirflowSpeed: " + Math3d.dot(v,v));
        //}
        //System.out.println("LiftCoefficient: " + Cl + "AOA: " + calculateAngleOfAttack(surface, aircraftTransform, aircraftVelocity), );

        float   LiftMagnitude = (float) (0.5*Cl*A*p*Math3d.dot(v,v));
        float[] LiftDirection = Math3d.normalize(Math3d.rotateVec3(surface.NORMAL, aircraftTransform.matrixRotate));

        return Math3d.mul(LiftDirection, LiftMagnitude);
    }
    private static float[] calculateWingDrag(Surface surface, float[] aircraftVelocity, Transform aircraftTransform) {
        float[] worldPosition = Math3d.getVec3(aircraftTransform.matrixOffset);

        float   A = surface.SURFACE_AREA;
        float   p = Weather.getAirDensityAtPosition(worldPosition);
        float[] v = calculateAirflow(aircraftVelocity, worldPosition);
        float  Cd = surface.calculateDragCoefficient(
                calculateAngleOfAttack(surface, aircraftTransform, aircraftVelocity)
        );

        float   DragMagnitude = (float) (-1*0.5*Cd*A*p*Math3d.dot(v,v));
        float[] DragDirection = Math3d.normalize(aircraftVelocity);

        return Math3d.mul(DragDirection, DragMagnitude);
    }

    private static float calculateAngleOfAttack(Surface surface, Transform aircraftTransform, float[] aircraftVelocity) {
        aircraftVelocity = Math3d.normalize(aircraftVelocity);
        float[] airflow = calculateAirflow(aircraftVelocity, Math3d.getVec3(aircraftTransform.matrixOffset));
        float[] normal = Math3d.rotateVec3(surface.NORMAL, aircraftTransform.matrixRotate);

        airflow = Math3d.normalize(airflow);
        normal = Math3d.normalize(normal);

        if (surface.NORMAL[0] == 1) {
            //System.out.println("Airflow: " + Math3d.string(airflow, false) + " Normal: " + Math3d.string(normal, false));
        }

        float aoa = (float) Math.asin(Math3d.dot(normal, airflow));
        return aoa;


    }

    private static float[] calculateAirflow(float[] aircraftVelocity, float[] worldPosition) {
        float[] negativeVelocity = Math3d.mul(aircraftVelocity, -1);
        float[] wind = Weather.getWindAtPosition(worldPosition);
        return Math3d.add(negativeVelocity, wind);
    }

    private static float[] calculateAirflowOverTheWing(float[] aircraftVelocity, Transform aircraftTransform) {
        float[] worldPosition = Math3d.getVec3(aircraftTransform.matrixOffset);
        float[] negativeVelocity = Math3d.mul(aircraftVelocity, -1);
        float[] wind = Weather.getWindAtPosition(worldPosition);

        float[] airflow = Math3d.add(negativeVelocity, wind);
        float[] forwards = getNormalizedForwardVector(aircraftTransform);
        float[] relAirflow = getRelVecInDirection(airflow, forwards);
        return relAirflow;
    }







    private static float[] calculateFormDrag(Surface surface, float[] aircraftVelocity, Transform aircraftTransform) {
        float[] airflow = calculateAirflow(new float[]{0, 0, 0}, aircraftVelocity);

        float[] directedAirflow = getRelVecInDirection(airflow, Math3d.rotateVec3(surface.NORMAL, aircraftTransform.matrixRotate));

        float airDensity = Weather.getAirDensityAtPosition(new float[]{0, 0, 0});
        float dragCoefficient = surface.calculateDragCoefficient(0);

        // Drag = -1 * 0.5 * Cd * A * p * V^2
        float dragMagnitude = -1*dragCoefficient * surface.SURFACE_AREA * Math3d.dot(directedAirflow, directedAirflow) * airDensity / 2;

        float[] movementDirection = Math3d.normalize(directedAirflow);

        // Drag vector = direction * magnitude
        return Math3d.mul(movementDirection, dragMagnitude);
    }



    private static float[] getNormalizedForwardVector(Transform transform) {
        float[] forward = new float[]{0, 0, -1, 0};
        return Math3d.normalize(
                Math3d.mul(transform.matrixRotate, forward)
        );

    }
    private static float[] getNormalizedRightVector(Transform transform) {
        float[] up = new float[]{1, 0, 0, 0};
        return Math3d.normalize(
                Math3d.mul(transform.matrixRotate, up)
        );
    }

    private static float[] getCOGOffsetVector(Transform surfaceOffsetTransform, float[] cogOffset, Transform aircraftTransform) {
        // Convert the matrix to a vector
        // Rotate the vector
        float[] offset = Math3d.getVec3(surfaceOffsetTransform.matrixOffset);
        float[] rotatedOffset = Math3d.mul(aircraftTransform.matrixRotate, offset);
        float[] rotatedCogOffset = Math3d.mul(aircraftTransform.matrixRotate, cogOffset);
        return Math3d.add(rotatedOffset, rotatedCogOffset);


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
                                        Math3d.length(calculateAirflow(new float[] {0,0,0}, velocity)))));

    }
}
