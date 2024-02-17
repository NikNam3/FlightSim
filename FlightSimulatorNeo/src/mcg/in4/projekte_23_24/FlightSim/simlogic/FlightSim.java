package mcg.in4.projekte_23_24.FlightSim.simlogic;

import mcg.in4.projekte_23_24.FlightSim.engine.Math3d;
import mcg.in4.projekte_23_24.FlightSim.engine.Window;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.Renderer;
import mcg.in4.projekte_23_24.FlightSim.engine.loading.AsynchronousLoader;
import mcg.in4.projekte_23_24.FlightSim.engine.structures.*;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Mesh;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.RigidBody;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Surfaces;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Transform;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.Aircraft;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.EntityBehavior;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.aircrafts.Cessna172;
import mcg.in4.projekte_23_24.FlightSim.simlogic.physics.Physics;
import mcg.in4.projekte_23_24.FlightSim.simlogic.util_scripts.FreeCameraScript;

import java.time.Duration;
import java.time.Instant;


public class FlightSim {

    private static Scene scene;
    private static int activeCamera;
    
    public static void main(String[] args) {
        createSceneStructure();

        for(int entity : scene.getAll()) {
            if (scene.hasComponent(entity, EntityBehavior.class))
                scene.getComponent(entity, EntityBehavior.class).onCreate();
        }

        Window.initWindow(500, 500, "FS");

        Renderer.init();

        Duration deltaTime = Duration.ZERO;
        Instant beginTime = Instant.now();

        while(!Window.exitRequested()){
            Window.newFrame();
            AsynchronousLoader.updateQueue();

            Renderer.prepareRender(scene, activeCamera);
            for(int entity : scene.getAll()){
                // TODO: Update Physics, ...
                Physics.update(scene, entity, deltaTime.getSeconds());


                if(scene.hasComponent(entity, EntityBehavior.class))
                    scene.getComponent(entity, EntityBehavior.class).onUpdate(deltaTime.getSeconds());
                Renderer.render(scene, entity);
            }

            Window.refresh();

            deltaTime = Duration.between(beginTime, Instant.now());

        }
    }

    private static void createSceneStructure(){
        scene = new Scene();

        int player = scene.createEntity();
        scene.addComponent(player, new Transform(), Transform.class);
        scene.addComponent(player, new Mesh(), Mesh.class);
        AsynchronousLoader.enqueue("content/Metroliner.obj", scene.getComponent(player, Mesh.class));


        activeCamera = scene.createEntity();
        scene.addComponent(activeCamera, new Camera(0.7f, 0.1f, 1000.f), Camera.class);
        scene.addComponent(activeCamera, new FreeCameraScript(), EntityBehavior.class);
        scene.getComponent(activeCamera, EntityBehavior.class).bind(scene, activeCamera);

        scene.addComponent(activeCamera, new Transform(), Transform.class);
        scene.getComponent(activeCamera, Transform.class).matrixOffset = Math3d.translation(-0.2f, 0.3f, -0.1f);
        scene.getComponent(activeCamera, Transform.class).matrixRotate = Math3d.rotationY(1.6f);

        int chessna172 = scene.createEntity();
        scene.addComponent(chessna172, new Cessna172(), EntityBehavior.class);
        scene.getComponent(chessna172, EntityBehavior.class).bind(scene, chessna172);

        scene.addComponent(chessna172, new Surfaces(), Surfaces.class);
        scene.addComponent(chessna172, new Transform(), Transform.class);
        scene.addComponent(chessna172, new RigidBody(), RigidBody.class);
        scene.addComponent(chessna172, new Mesh(), Mesh.class);
        // AsynchronousLoader.enqueue("content/berge.obj", scene.getComponent(chessna172, Mesh.class)); TODO add meshes for main body and control surfacesIds




    }
}