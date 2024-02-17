package mcg.in4.projekte_23_24.FlightSim.simlogic.physics;

import mcg.in4.projekte_23_24.FlightSim.engine.Math3d;
import mcg.in4.projekte_23_24.FlightSim.engine.structures.Scene;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.RigidBody;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Surfaces;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Transform;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.Surface;

public class Physics {
    public static void update(Scene scene, int entity, float deltaTime) {
        // Update the physics simulation


        // Calculate the forces acting on Surfaces and add them to the corresponding RigidBody
        if (scene.hasComponent(entity, Surfaces.class)) {

            Surfaces surfaces = scene.getComponent(entity, Surfaces.class);
            RigidBody rigidBody = scene.getComponent(entity, RigidBody.class);

            for(int surfaceId : surfaces.surfacesIds) {
                Surface surface = scene.getComponent(surfaceId, Surface.class);

                // TODO

                addForce(rigidBody, new float[][]{
                                calculateSurfaceLift(surface),
                                Math3d.getVec3(scene.getComponent(surfaceId, Transform.class).matrixOffset)

                });
            }


        }

        // Sum up all forces and calculate the acceleration
        // Update the position and rotation of the RigidBody
        if (scene.hasComponent(entity, RigidBody.class)) {
            RigidBody rigidBody = scene.getComponent(entity, RigidBody.class);
            float[] acceleration = new float[3];
            float[] angularAcceleration = new float[3];
            for (float[][] force : rigidBody.forces) {
                for (int i = 0; i < 3; i++) {
                    acceleration[i] += force[0][i] / rigidBody.mass; // a = F/m
                }
                for (int i = 0; i < 3; i++) {
                    //float[] torque = Math3d.mul(force[0], )
                    angularAcceleration[i] += force[1][i] / force[2][i]; // alpha = T/I
                }

            }
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

    private static float[] calculateSurfaceDrag(Surface surface) {
        // Calculate the drag force for the given surface
        return new float[3];
    }

    private static float[] calculateSurfaceLift(Surface surface) {
        // Calculate the lift force for the given surface
        return new float[3];
    }
    private static void addForce(RigidBody rigidBody, float[][] force) {
        // Add the given force to the rigidBody
    }
}
