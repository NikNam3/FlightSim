package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.aircrafts;

import mcg.in4.projekte_23_24.FlightSim.engine.Math3d;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.*;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.Aircraft;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.ControlSurface;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.Engine;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.EntityBehavior;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class Cessna172 extends Aircraft {
    @Override
    public void onCreate() {
        // Create the aircraft's left wing Surface
        int leftOuterWing = scene.createEntity();
        scene.addComponent(leftOuterWing, new Transform(Math3d.vec3(-3.75f, 1, 0)), Transform.class);
        scene.addComponent(leftOuterWing, new ControlSurface(Math3d.vec3up,6.07f,-0.5f,0.5f,GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_D), EntityBehavior.class);
        scene.addComponent(leftOuterWing, new Mesh(), Mesh.class);

        scene.adopt(hostId, leftOuterWing);
        scene.getComponent(leftOuterWing, EntityBehavior.class).bind(scene, leftOuterWing);

        int leftInnerWing = scene.createEntity();
        scene.addComponent(leftInnerWing, new Transform(Math3d.vec3(-1.5f, 1, 0)), Transform.class);
        scene.addComponent(leftInnerWing, new ControlSurface(Math3d.vec3up, 2.96f, -1f, 0, GLFW.GLFW_KEY_F,GLFW.GLFW_KEY_R), EntityBehavior.class);
        scene.addComponent(leftInnerWing, new Mesh(), Mesh.class);

        scene.adopt(hostId, leftInnerWing);
        scene.getComponent(leftInnerWing, EntityBehavior.class).bind(scene, leftInnerWing);

        int rightOuterWing = scene.createEntity();
        scene.addComponent(rightOuterWing, new Transform(Math3d.vec3(3.75f, 1, 0)), Transform.class);
        scene.addComponent(rightOuterWing, new ControlSurface(Math3d.vec3up,6.07f,-0.5f,0.5f,GLFW.GLFW_KEY_D,GLFW.GLFW_KEY_A), EntityBehavior.class);
        scene.addComponent(rightOuterWing, new Mesh(), Mesh.class);

        scene.adopt(hostId, rightOuterWing);
        scene.getComponent(rightOuterWing, EntityBehavior.class).bind(scene, rightOuterWing);

        int rightInnerWing = scene.createEntity();
        scene.addComponent(rightInnerWing, new Transform(Math3d.vec3(1.5f, 1, 0)), Transform.class);
        scene.addComponent(rightInnerWing, new ControlSurface(Math3d.vec3up, 2.96f, -1f, 0,GLFW.GLFW_KEY_F,GLFW.GLFW_KEY_R), EntityBehavior.class);
        scene.addComponent(rightInnerWing, new Mesh(), Mesh.class);

        scene.adopt(hostId, rightInnerWing);
        scene.getComponent(rightInnerWing, EntityBehavior.class).bind(scene, rightInnerWing);

        int leftElevator = scene.createEntity();
        scene.addComponent(leftElevator, new Transform(Math3d.vec3(-0.75f, 0, 6)), Transform.class);
        scene.addComponent(leftElevator, new ControlSurface(Math3d.vec3up, 2.47f, -0.5f, 0.5f,GLFW.GLFW_KEY_S,GLFW.GLFW_KEY_W), EntityBehavior.class);
        scene.addComponent(leftElevator, new Mesh(), Mesh.class);

        scene.adopt(hostId, leftElevator);
        scene.getComponent(leftElevator, EntityBehavior.class).bind(scene, leftElevator);

        int rightElevator = scene.createEntity();
        scene.addComponent(rightElevator, new Transform(Math3d.vec3(0.75f, 0, 6)), Transform.class);
        scene.addComponent(rightElevator, new ControlSurface(Math3d.vec3up, 2.47f, -0.5f, 0.5f,GLFW.GLFW_KEY_S,GLFW.GLFW_KEY_W), EntityBehavior.class);
        scene.addComponent(rightElevator, new Mesh(), Mesh.class);

        scene.adopt(hostId, rightElevator);
        scene.getComponent(rightElevator, EntityBehavior.class).bind(scene, rightElevator);

        //int rudder = scene.createEntity();
        //scene.addComponent(rudder, new Transform(), Transform.class);
        //scene.addComponent(rudder, new ControlSurface(new float[] {1, 0, 0}, 2.67f, -0.5f, 0.5f,GLFW.GLFW_KEY_Q,GLFW.GLFW_KEY_E), EntityBehavior.class);
        //scene.addComponent(rudder, new Mesh(), Mesh.class);

        //scene.adopt(hostId, rudder);
        //scene.getComponent(rudder, EntityBehavior.class).bind(scene, rudder);

        int engine = scene.createEntity();
        scene.addComponent(engine, new Transform(), Transform.class);
        scene.addComponent(engine, new Engine(
                1.8796f,
                50f,
                2700f,
                500f,
                GLFW.GLFW_KEY_1,
                GLFW.GLFW_KEY_2),
                Engine.class);
        scene.addComponent(engine, new Mesh(), Mesh.class);

        scene.adopt(hostId, engine);
        scene.getComponent(engine, Engine.class).bind(scene, engine);

        // Safe all Surfaces in the Surfaces component for easy access
        int[] surfaces = new int[6]; //  TODO add all wingSurfaceIds
        surfaces[0] = leftOuterWing;
        surfaces[1] = leftInnerWing;
        surfaces[2] = rightOuterWing;
        surfaces[3] = rightInnerWing;
        surfaces[4] = leftElevator;
        surfaces[5] = rightElevator;
        //surfaces[6] = rudder;

        // Set the values in all Components
        // Surfaces
        scene.getComponent(hostId, Surfaces.class).wingSurfaceIds = surfaces;

        // Safe all engines in the Engines component for easy access
        int[] engines = new int[1];
        engines[0] = engine;

        scene.getComponent(hostId, Engines.class).engineIds = engines;

        // RigidBody
        scene.getComponent(hostId, RigidBody.class).mass = 1112; // 1112 kg
        scene.getComponent(hostId, RigidBody.class).relCenterOfGravity = new float[]{0, 0, 0}; // Center of Gravity is in the middle of the aircraft TODO change
        scene.getComponent(hostId, RigidBody.class).velocity = new float[]{0, 0, 0}; // 0 m/s
        scene.getComponent(hostId, RigidBody.class).angularVelocity = new float[]{0, 0, 0}; // 0 rad/s
        scene.getComponent(hostId, RigidBody.class).forces = new ArrayList<>();
        scene.getComponent(hostId, RigidBody.class).inertiaTensor = new float[][]{
                {2000, 0, 0},
                {0, 3000, 0},
                {0, 0, 2500}}; // TODO magic numbers
        scene.getComponent(hostId, RigidBody.class).inertiaTensor = new float[][]{
                {2000, 0, 0},
                {0, 3000, 0},
                {0, 0, 2500}}; // TODO magic numbers


        scene.getComponent(hostId, Transform.class).matrixOffset = new float[][] {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };

        System.out.println("Cessna172 created");
    }


}
