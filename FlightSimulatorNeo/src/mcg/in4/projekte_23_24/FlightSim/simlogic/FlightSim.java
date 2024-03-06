package mcg.in4.projekte_23_24.FlightSim.simlogic;

import mcg.in4.projekte_23_24.FlightSim.engine.Math3d;
import mcg.in4.projekte_23_24.FlightSim.engine.Window;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.FrameBuffer;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.RenderUtils;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.Renderer;
import mcg.in4.projekte_23_24.FlightSim.engine.loading.AsynchronousLoader;
import mcg.in4.projekte_23_24.FlightSim.engine.loading2.AsynchronousLoader2;
import mcg.in4.projekte_23_24.FlightSim.engine.structures.*;
import mcg.in4.projekte_23_24.FlightSim.engine.terrain.Terrain;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.*;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.EntityBehavior;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.aircrafts.Cessna172;
import mcg.in4.projekte_23_24.FlightSim.simlogic.physics.Physics;
import mcg.in4.projekte_23_24.FlightSim.simlogic.util_scripts.FreeCameraScript;
import mcg.in4.projekte_23_24.FlightSim.simlogic.util_scripts.FreeCameraScript2;
import mcg.in4.projekte_23_24.FlightSim.simlogic.util_scripts.HelicopterScript;


public class FlightSim {

    private static Scene scene;
    private static int activeCamera;
    
    public static void main(String[] args) {
        createSceneStructure();

        // Define the Window
        int windowWidth  = 500;
        int windowHeight = 500;
        Window.initWindow(windowWidth, windowHeight, "FS");

        // Initialize the Renderer
        Renderer.init();
        Terrain.init();
        RenderUtils.init();

        // TODO framebufferstuff bitte kommentieren
        Texture2D color = Texture2D.createFloat32(windowWidth*2, windowHeight*2, 3);
        Texture2D depth = Texture2D.createDepth(windowWidth*2, windowHeight*2);
        FrameBuffer fb = new FrameBuffer();
        fb.attachTexture2D(color, 0);
        fb.attachDepthBufferTexture(depth);
        FrameBuffer.unbind();

        // Call onCreate for all entities
        for (int entity : scene.getAll()) {
            if (scene.hasComponent(entity, EntityBehavior.class))
                scene.getComponent(entity, EntityBehavior.class).onCreate();
        }

        // Main Loop
        while(!Window.exitRequested()){
            // TODO bitte kommentieren
            if(Window.getWidth() != windowWidth || Window.getHeight() != windowHeight){
                // All window size change events
                windowWidth  = Window.getWidth();
                windowHeight = Window.getHeight();

                color.destroy();
                depth.destroy();

                color = Texture2D.createFloat32(windowWidth*2, windowHeight*2, 3);
                depth = Texture2D.createDepth(windowWidth*2, windowHeight*2);

                fb.attachTexture2D(color, 0);
                fb.attachDepthBufferTexture(depth);
            }

            FrameBuffer.unbind();

            Window.newFrame();

            AsynchronousLoader.updateQueue();
            AsynchronousLoader2.update();

            // Update all entities
            for(int entity : scene.getAll()){
                // TODO: Update Physics, ...
                Physics.update(scene, entity, 1/60f);
                if(scene.hasComponentByParentClass(entity, EntityBehavior.class))
                    scene.getComponentByParentClass(entity, EntityBehavior.class).onUpdate(1 / 60.f);
            }

            testHeights(); // TODO: remove


            // TODO bitte kommentieren
            fb.bind();
            fb.clear();
            Renderer.prepareRender(scene, activeCamera);
            for(int entity : scene.getAll()){
                Renderer.render(scene, entity);
            }
            Terrain.render(scene, activeCamera);

            FrameBuffer.unbind();
            RenderUtils.renderScreenQuad(color);
            Window.refresh();
        }
    }

    private static void testHeights(){
        Transform cameraTransform = scene.getComponent(activeCamera, Transform.class);
        float cameraX = cameraTransform.matrixOffset[0][3];
        float cameraZ = cameraTransform.matrixOffset[2][3];
        float terrHeight = Terrain.getHeightAtPoint(cameraX, cameraZ);

    //    cameraTransform.matrixOffset[1][3] -= 10.f * (1 / 60.f);

        cameraTransform.matrixOffset[1][3] = Math.max(terrHeight + 1.88f, cameraTransform.matrixOffset[1][3]);
    }

    private static void createSceneStructure(){
        scene = new Scene();

        int player = scene.createEntity();
        scene.addComponent(player, new Transform(), Transform.class);
        scene.addComponent(player, new Mesh(), Mesh.class);
        scene.addComponent(player, new HelicopterScript(), EntityBehavior.class);
        scene.getComponent(player, EntityBehavior.class).bind(scene, player);
        AsynchronousLoader.enqueue("content/Grosskugel.obj", scene.getComponent(player, Mesh.class));


        activeCamera = scene.createEntity();
        scene.addComponent(activeCamera, new Camera(0.7f, 0.5f, 20000.f), Camera.class);
        scene.addComponent(activeCamera, new FreeCameraScript2(), EntityBehavior.class);
        scene.getComponent(activeCamera, EntityBehavior.class).bind(scene, activeCamera);

        scene.addComponent(activeCamera, new Transform(), Transform.class);
        scene.getComponent(activeCamera, Transform.class).matrixOffset = Math3d.translation(-0.2f, 0.3f, -0.1f);
        scene.getComponent(activeCamera, Transform.class).matrixRotate = Math3d.rotationY(1.6f);


        int cessna172 = scene.createEntity();
        scene.addComponent(cessna172, new Transform(), Transform.class);
        scene.addComponent(cessna172, new RigidBody(), RigidBody.class);
        scene.addComponent(cessna172, new Mesh(), Mesh.class);
        AsynchronousLoader.enqueue("content/Metroliner.obj", scene.getComponent(cessna172, Mesh.class));
        scene.addComponent(cessna172, new Cessna172(), EntityBehavior.class);
        scene.getComponent(cessna172, EntityBehavior.class).bind(scene, cessna172);

        scene.addComponent(cessna172, new Surfaces(), Surfaces.class);
        scene.addComponent(cessna172, new Engines(), Engines.class);
    }
}