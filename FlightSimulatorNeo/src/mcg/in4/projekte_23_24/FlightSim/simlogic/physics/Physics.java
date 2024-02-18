package mcg.in4.projekte_23_24.FlightSim.simlogic.physics;

import mcg.in4.projekte_23_24.FlightSim.engine.Math3d;
import mcg.in4.projekte_23_24.FlightSim.engine.structures.Scene;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.RigidBody;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Surfaces;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Transform;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.Surface;

public class Physics {
    public static void update(Scene scene, int entity, float deltaTime) {
        if (scene.hasComponent(entity, Surfaces.class) && scene.hasComponent(entity, RigidBody.class)) {

            Surfaces surfaces = scene.getComponent(entity, Surfaces.class); // Get the Component which contains all surfaces of the aircraft
            RigidBody rigidBody = scene.getComponent(entity, RigidBody.class); // Get the RigidBody of the aircraft



            // Iterate through all surfaces of the aircraft
            for(int surfaceId : surfaces.surfacesIds) {

                // Get the Transform of the surface (holds the relative position of the surface to the aircraft)
                Transform surfaceTransform = scene.getComponent(surfaceId, Transform.class);
                // Convert the matrix to a vector
                float[] offsetVector = new float[] {
                    surfaceTransform.matrixOffset[0][3],
                    surfaceTransform.matrixOffset[1][3],
                    surfaceTransform.matrixOffset[2][3]
                };

                // Get the actual Surface entity
                Surface surface = scene.getComponent(surfaceId, Surface.class);

                // Calculate Roll, Pitch and Yaw inertia
                float[] inertia = new float[] {
                    calculateInertia(getAxisOfRotation(surfaceTransform)[0], rigidBody.relCenterOfGravity, offsetVector, rigidBody.mass),
                    calculateInertia(getAxisOfRotation(surfaceTransform)[1], rigidBody.relCenterOfGravity, offsetVector, rigidBody.mass),
                    calculateInertia(getAxisOfRotation(surfaceTransform)[2], rigidBody.relCenterOfGravity, offsetVector, rigidBody.mass)
                };

                // Add Lift to the RigidBody
                rigidBody.forces.add(new float[][] {
                        calculateSurfaceLift(surface),
                        offsetVector,
                        inertia
                });

                // Add Drag to the RigidBody
                rigidBody.forces.add(new float[][] {
                        calculateSurfaceDrag(surface),
                        offsetVector,
                        inertia
                });

            }


        }

        if (scene.hasComponent(entity, RigidBody.class)) {
            RigidBody rigidBody = scene.getComponent(entity, RigidBody.class);
            Transform transform = scene.getComponent(entity, Transform.class); // Get the Transform of the aircraft

            float[] totalForce = new float[3];
            float[] totalTorque = new float[3];
            float[] totalInertia = new float[3];

            for (float[][] force : rigidBody.forces) {
                float[] forceVector = force[0];
                float[] offsetVector = force[1];
                float[] inertia = force[2];

                totalForce = Math3d.add(totalForce, forceVector);
                totalTorque = Math3d.add(totalTorque, Math3d.cross(offsetVector, forceVector));
                totalInertia = Math3d.add(totalInertia, inertia);
            }

            float[] acceleration = Math3d.div(totalForce, rigidBody.mass);
            float[] angularAcceleration = Math3d.div(totalTorque, totalInertia);

            rigidBody.velocity = Math3d.add(rigidBody.velocity, Math3d.mul(acceleration, deltaTime));
            rigidBody.angularVelocity = Math3d.add(rigidBody.angularVelocity, Math3d.mul(angularAcceleration, deltaTime));
            transform.matrixOffset[0][3] = transform.matrixOffset[0][3] + rigidBody.velocity[0] * deltaTime;
            transform.matrixOffset[1][3] = transform.matrixOffset[1][3] + rigidBody.velocity[1] * deltaTime;
            transform.matrixOffset[2][3] = transform.matrixOffset[2][3] + rigidBody.velocity[2] * deltaTime;

            transform.matrixRotate = Math3d.add(transform.matrixRotate, Math3d.rotationMatrix(rigidBody.angularVelocity[0]*deltaTime, rigidBody.angularVelocity[1]*deltaTime, rigidBody.angularVelocity[2]*deltaTime));

        }

    }
    private static float calculateInertia(float[] axis, float[] centerOfMass, float[] attachmentPoint, float mass) {
        axis = Math3d.normalize(axis);
        float[] cogOffset = Math3d.sub(attachmentPoint, centerOfMass);
        float[] relAxisOffset = Math3d.mul(Math3d.mul(cogOffset, axis), axis);
        float[] r = Math3d.sub(attachmentPoint, relAxisOffset);
        float r2 = Math3d.dot(r, r);
        return mass * r2;

    }
    // Noch so ne Methode von soner Ai die das alles besser können als wir
    private static float[][] getAxisOfRotation(Transform transform) {
        float[][] axis = new float[3][3];
        axis[0] = Math3d.mul(transform.matrixRotate, new float[]{1, 0, 0, 0});
        axis[1] = Math3d.mul(transform.matrixRotate, new float[]{0, 1, 0, 0});
        axis[2] = Math3d.mul(transform.matrixRotate, new float[]{0, 0, 1, 0});
        return axis;
    }

    private static float[] calculateSurfaceDrag(Surface surface) {
        // TODO: Implement the drag calculation
        // Calculate the drag force for the given surface
        return new float[3];
    }

    private static float[] calculateSurfaceLift(Surface surface) {
        // TODO: Implement the lift calculation
        // Calculate the lift force for the given surface
        return new float[3];
    }
}
