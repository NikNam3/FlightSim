package simlogic;

import engine.Math3d;
import engine.Window;
import engine.graphics.Renderer;
import engine.loading.AsynchronousLoader;
import engine.structures.*;
import simlogic.scripts.FreeCameraScript;


public class FlightSim {

    private static Scene scene;
    private static int activeCamera;
    
    public static void main(String[] args) {
        createSceneStructure();

        Window.initWindow(500, 500, "FS");

        Renderer.init();
        
        while(!Window.exitRequested()){
            Window.newFrame();
            AsynchronousLoader.updateQueue();

            Renderer.prepareRender(scene, activeCamera);
            for(int entity : scene.getAll()){
                // TODO: Update Physics, ...
                if(scene.hasComponent(entity, EntityBehavior.class))
                    scene.getComponent(entity, EntityBehavior.class).onUpdate(1 / 60.f);
                Renderer.render(scene, entity);
            }


            Window.refresh();
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
    }
}