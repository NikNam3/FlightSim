package mcg.in4.projekte_23_24.FlightSim.logic;



import mcg.in4.projekte_23_24.FlightSim.engine.base.*;
import mcg.in4.projekte_23_24.FlightSim.engine.components.general.Transform;
import mcg.in4.projekte_23_24.FlightSim.engine.components.physics.EngineModel;
import mcg.in4.projekte_23_24.FlightSim.engine.components.physics.SurfaceModel;
import mcg.in4.projekte_23_24.FlightSim.engine.components.render.MeshArray;
import mcg.in4.projekte_23_24.FlightSim.engine.flightphysics.Engine;
import mcg.in4.projekte_23_24.FlightSim.engine.flightphysics.Physics;
import mcg.in4.projekte_23_24.FlightSim.engine.components.physics.RigidBody;
import mcg.in4.projekte_23_24.FlightSim.engine.flightphysics.Wing;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.rendering.SceneRenderer;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.LightingEnvironment;
import mcg.in4.projekte_23_24.FlightSim.engine.loading.MeshArrayLoader;
import mcg.in4.projekte_23_24.FlightSim.engine.postprocessing.AtmosphereEffect;
import mcg.in4.projekte_23_24.FlightSim.engine.terrain.Terrain;

import static mcg.in4.projekte_23_24.FlightSim.engine.base.Math3d.*;

public class Main {

    public static final float CLIP_START = 0.1f;
    public static final float CLIP_END   = 10000.f;

    public static void main(String[] args) throws Exception {
        Window.create(500, 500, "FS");
        Input.init(Window.getGlfwWindowAddress());


        Scene scene = new Scene();
        int cameraEntity = scene.createEntity();
        scene.addComponent(cameraEntity, new Transform(), Transform.class);
        scene.getComponent(cameraEntity, Transform.class).matrixPosition = Math3d.translation(-0.5f, 0.5f, -0.6f);
        scene.getComponent(cameraEntity, Transform.class).matrixRotation = Math3d.mat4();
        int playerAircraft = createCessna(scene);
        scene.adopt(playerAircraft, cameraEntity);


        float fov = (float)Math.toRadians(90.f);

        LightingEnvironment lightingEnvironment = new LightingEnvironment();
        lightingEnvironment.setTimeOfDay(0.5f);

        SceneRenderer.init();
        Terrain.init();
        AtmosphereEffect.init();


        float deltaTime;
        long lastFrameStartTime = System.nanoTime();
        while(!Window.exitRequested()){
            Window.newFrame();

            // updating delta time
            long currentTimeNano = System.nanoTime();
            long deltaTimeNano   = currentTimeNano - lastFrameStartTime;
            deltaTime = deltaTimeNano / 1E9f;
            deltaTime = Math.min(deltaTime, 1 / 20.f);
            lastFrameStartTime = currentTimeNano;

            if(Input.isKeyDown(Input.KEY_ID_M))
                lightingEnvironment.increaseTimeOfDay( 0.1f * deltaTime);
            if(Input.isKeyDown(Input.KEY_ID_N))
                lightingEnvironment.increaseTimeOfDay(-0.1f * deltaTime);

            for(int entity : scene.getAll()){
                Physics.update(scene, entity, deltaTime);
            }
            //updateCamera(scene, playerAircraft, deltaTime);
            updateAircraftInput(scene, playerAircraft, deltaTime);
            // floating point error mitigation
            // shiftPositionsOverThreshold(scene, cameraEntity, 100);

            float[][] cameraModel = Utils.getWorldSpaceModel(scene, cameraEntity);
            float aspectRatio = Window.getContentArea()[0] / (float)Window.getContentArea()[1];
            float[][] cameraProj  = perspective(fov, .1f, 10000f, aspectRatio);

            AtmosphereEffect.listen(Window.getContentArea()[0], Window.getContentArea()[1]);
            SceneRenderer.render(scene, lightingEnvironment, cameraModel, cameraProj);
            Terrain.render(lightingEnvironment, cameraModel, cameraProj);
            AtmosphereEffect.keep();
            AtmosphereEffect.apply(lightingEnvironment, cameraModel, CLIP_START, CLIP_END, fov);
            Window.showFrame();
        }
    }

    private static void shiftPositionsOverThreshold(Scene scene, int cameraEntity, float thresholdDistance){
        float[][] cameraModel = Utils.getWorldSpaceModel(scene, cameraEntity);
        float[] cameraOffset  = vec3(cameraModel[0][3], cameraModel[1][3], cameraModel[2][3]);
        if(length(cameraOffset) > thresholdDistance){
            // shift all entities without parent
            for(int entity : scene.getAll()){
                if(!scene.hasComponent(entity, Transform.class) || scene.hasParent(entity))
                    continue;
                Transform transform = scene.getComponent(entity, Transform.class);
                transform.matrixPosition = mul(transform.matrixPosition, translation(mul(cameraOffset, -1)));
            }
        }
    }
    private static void updateAircraftInput(Scene scene, int aircraft, float deltaTime) {
        SurfaceModel surfaceModel = scene.getComponent(aircraft, SurfaceModel.class);
        EngineModel engineModel = scene.getComponent(aircraft, EngineModel.class);


        Wing wing = (Wing) surfaceModel.surfaces.get("left_horizontal_stabilizer");
        if (Input.isKeyDown(Input.KEY_ID_W)) {
            wing.flapAngle = 1f;
        } else if (Input.isKeyDown(Input.KEY_ID_S)) {
            wing.flapAngle = -1f;
        } else {
            wing.flapAngle = 0;
        }
        wing = (Wing) surfaceModel.surfaces.get("right_horizontal_stabilizer");
        if (Input.isKeyDown(Input.KEY_ID_W)) {
            wing.flapAngle = 1f;
        } else if (Input.isKeyDown(Input.KEY_ID_S)) {
            wing.flapAngle = -1;
        } else {
            wing.flapAngle = 0;
        }
        wing = (Wing) surfaceModel.surfaces.get("left_outer_wing");
        if (Input.isKeyDown(Input.KEY_ID_A)) {
            wing.flapAngle = 0.1f;
        } else if (Input.isKeyDown(Input.KEY_ID_D)) {
            wing.flapAngle = -0.1f;
        } else {
            wing.flapAngle = 0;
        }
        wing = (Wing) surfaceModel.surfaces.get("right_outer_wing");
        if (Input.isKeyDown(Input.KEY_ID_A)) {
            wing.flapAngle = -0.075f;
        } else if (Input.isKeyDown(Input.KEY_ID_D)) {
            wing.flapAngle = +0.075f;
        } else {
            wing.flapAngle = 0;
        }
        Engine engine = engineModel.engines.get("main_engine");
        if (Input.isKeyDown(Input.KEY_ID_F)) {
            engine.setRPM = engine.MAX_RPM;
        } else if (Input.isKeyDown(Input.KEY_ID_R)) {
            engine.setRPM = 0;
        } else {
            engine.setRPM = engine.MAX_RPM / 2;
        }

    }


    private static void updateCamera(Scene scene, int camera, float deltaTime){
        Transform transform = scene.getComponent(camera, Transform.class);

        float moveSpeed = 5;
        float rotSpeed  = 0.8f;

        if(Input.isKeyDown(Input.KEY_ID_SHIFT)){
            moveSpeed *= 50;
            rotSpeed  *= 2f;
        }

        var deltaPositionVector = vec3();
        if(Input.isKeyDown(Input.KEY_ID_W))
            deltaPositionVector = add(deltaPositionVector, vec3(0, 0, -moveSpeed * deltaTime));
        if(Input.isKeyDown(Input.KEY_ID_S))
            deltaPositionVector = add(deltaPositionVector, vec3(0, 0,  moveSpeed * deltaTime));
        if(Input.isKeyDown(Input.KEY_ID_A))
            deltaPositionVector = add(deltaPositionVector, vec3(-moveSpeed * deltaTime));
        if(Input.isKeyDown(Input.KEY_ID_D))
            deltaPositionVector = add(deltaPositionVector, vec3(moveSpeed * deltaTime));
        if(Input.isKeyDown(Input.KEY_ID_SPACE))
            deltaPositionVector = add(deltaPositionVector, vec3(0, moveSpeed * deltaTime));
        if(Input.isKeyDown(Input.KEY_ID_C))
            deltaPositionVector = add(deltaPositionVector, vec3(0,-moveSpeed * deltaTime));


        if(Input.isKeyDown(Input.KEY_ID_Q))
            transform.matrixRotation = mul(rotationY(rotSpeed * deltaTime), transform.matrixRotation);
        if(Input.isKeyDown(Input.KEY_ID_E))
            transform.matrixRotation = mul(rotationY(rotSpeed * -deltaTime), transform.matrixRotation);
        if(Input.isKeyDown(Input.KEY_ID_R))
            transform.matrixRotation = mul(transform.matrixRotation, rotationX(rotSpeed * -deltaTime));
        if(Input.isKeyDown(Input.KEY_ID_F))
            transform.matrixRotation = mul(transform.matrixRotation, rotationX(rotSpeed * deltaTime));

        var dpv4 = vec4(deltaPositionVector, 0);
        deltaPositionVector = vec3(mul(transform.matrixRotation, dpv4));
        transform.matrixPosition = mul(translation(deltaPositionVector), transform.matrixPosition);
    }

    private static int createCessna(Scene scene) throws Exception{
        int root = scene.createEntity();

        SurfaceModel surfaceModel = new SurfaceModel();
        surfaceModel.surfaces.put("left_outer_wing",            new Wing(vec3(-3.75f, 1, 0), vec3(0, 1, 0), 4.95f, 7.37f, Wing.NORMAL_WING));
        surfaceModel.surfaces.put("left_inner_wing",            new Wing(vec3(-1.5f, 1, 0), vec3(0, 1, 0), 3.3f, 7.37f, Wing.NORMAL_WING));
        surfaceModel.surfaces.put("right_outer_wing",           new Wing(vec3(3.75f, 1, 0), vec3(0, 1, 0), 4.95f, 7.37f, Wing.NORMAL_WING));
        surfaceModel.surfaces.put("right_inner_wing",           new Wing(vec3(1.5f, 1, 0), vec3(0, 1, 0), 3.3f, 7.37f, Wing.NORMAL_WING));
        surfaceModel.surfaces.put("left_horizontal_stabilizer", new Wing(vec3(-0.75f, 0, 6), vec3(0, 1, 0), -0.16f, 7.37f, Wing.SYMMETRIC_WING));
        surfaceModel.surfaces.put("right_horizontal_stabilizer",new Wing(vec3(0.75f, 1, 6), vec3(0, 1, 0), -0.16f, 7.37f, Wing.SYMMETRIC_WING));
        surfaceModel.surfaces.put("vertical_stabilizer",        new Wing(vec3(0, 1.5f, 6), vec3(1, 0, 0), 0.15f, 7.37f, Wing.SYMMETRIC_WING));

        scene.addComponent(root, surfaceModel, SurfaceModel.class);

        EngineModel engineModel = new EngineModel();
        engineModel.engines.put("main_engine", new Engine(2700f, 500f, 1.8796f, 50f));
        scene.addComponent(root, engineModel, EngineModel.class);

        RigidBody rigidBody = new RigidBody();
        rigidBody.mass = 800;
        rigidBody.velocity  = vec3(0, 0,-50);
        rigidBody.cgOffset  = vec3(0, 0, -0.12f);
        rigidBody.momentOfInertia = new float[][]{
                {1285, 0, 0},
                {0, 1824, 0},
                {0, 0, 2666}};


        scene.addComponent(root, rigidBody, RigidBody.class);
        scene.addComponent(root, new Transform(), Transform.class);
        scene.getComponent(root, Transform.class).matrixPosition = new float[][] {
                {1, 0, 0, 5000},
                {0, 1, 0, 600},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        scene.addComponent(root, MeshArrayLoader.loadMesh("content/cessna_panel.obj"), MeshArray.class);
        return root;
    }
}