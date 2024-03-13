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

/**
 * This is the Main class of the FlightSim project.
 * It creates a window and initializes the input.
 * It contains the main method which starts the main loop.
 *
 * The main loop updates the delta time and checks the input. As well as updating the physics and rendering the scene.
 *
 * @version 1.0
 * @since 1.0
 * @author Nikolas Kühnlein, Vincent Lahmann, Maximo Tewes, Theo Kamp
 */
public class Main {
    public static final float CLIP_START = 0.1f;
    public static final float CLIP_END   = 10000.f;

    /**
     * This is the Main method of the FlightSim project.
     *
     * It creates a window and initializes the input.
     * Then it creates a scene and adds a camera and a player aircraft to it.
     * Then it initializes the lighting environment, the scene renderer, the terrain and the atmosphere effect.
     * Then some variables which are used in the main loop are initialized and the main loop is started.
     *
     * In the main loop the delta time is updated and the input is checked.
     *
     * @param args The command line arguments
     * @throws Exception If a GLFW error occurs (ex. a mesh could not be loaded)
     */
    public static void main(String[] args) throws Exception {
        System.out.println("------------------------- FlightSim --------------------------");
        System.out.println(" | Welcome to our little flight simulator!");
        System.out.println(" | You can Control the aircraft with the W, A, S and D keys. |");
        System.out.println(" | You can control the engine with the R and F keys.         |");
        System.out.println(" | You can control the time of day with the N and M keys.    |");
        System.out.println(" | Have fun!                                                 |");
        System.out.println(" -------------------------------------------------------------");
        // Create a window and initialize the input
        Window.create(500, 500, "FS");
        Input.init(Window.getGlfwWindowAddress());

        // Create a scene and add a camera and a player aircraft to it
        Scene scene = new Scene();
        int cameraEntity = scene.createEntity();
        scene.addComponent(cameraEntity, new Transform(), Transform.class);
        scene.getComponent(cameraEntity, Transform.class).matrixPosition = Math3d.translation(-0.5f, 0.5f, -0.6f);
        scene.getComponent(cameraEntity, Transform.class).matrixRotation = Math3d.mat4();
        int playerAircraft = createCessna(scene);
        scene.adopt(playerAircraft, cameraEntity);

        // Initialize the lighting environment, the scene renderer, the terrain and the atmosphere effect
        float fov = (float)Math.toRadians(90.f);

        LightingEnvironment lightingEnvironment = new LightingEnvironment();
        lightingEnvironment.setTimeOfDay(0.5f);

        SceneRenderer.init();
        Terrain.init();
        AtmosphereEffect.init();

        // Initialize some variables which are used in the main loop
        float[] totalCameraShift = vec3();

        float deltaTime;
        long lastFrameStartTime = System.nanoTime();

        // Start the main loop
        while(!Window.exitRequested()){
            Window.newFrame();

            // updating delta time
            long currentTimeNano = System.nanoTime();
            long deltaTimeNano   = currentTimeNano - lastFrameStartTime;
            deltaTime = deltaTimeNano / 1E9f;
            deltaTime = Math.min(deltaTime, 1 / 20.f);
            lastFrameStartTime = currentTimeNano;

            // checking input
            updateEnvironmentInput(lightingEnvironment, deltaTime);
            updateAircraftInput(scene, playerAircraft);

            // updating physics
            for(int entity : scene.getAll()){
                Physics.update(scene, entity, deltaTime);
            }

            // floating point error mitigation
            float[] cameraShift = shiftPositionsOverThreshold(scene, cameraEntity, 100);
            totalCameraShift = add(cameraShift, totalCameraShift);

            // rendering
            float[][] cameraModel = Utils.getWorldSpaceModel(scene, cameraEntity);
            float aspectRatio     = Window.getContentArea()[0] / (float)Window.getContentArea()[1];
            float[][] cameraProj  = perspective(fov, CLIP_START, CLIP_END, aspectRatio);

            AtmosphereEffect.listen(Window.getContentArea()[0], Window.getContentArea()[1]);
            SceneRenderer.render(scene, lightingEnvironment, cameraModel, cameraProj);

            // Apply camera shift to the model matrix for every renderer that isn't using the scene system
            cameraModel = mul(translation(totalCameraShift), cameraModel);

            Terrain.updateAndRender(lightingEnvironment, cameraModel, cameraProj);
            AtmosphereEffect.keep();
            AtmosphereEffect.apply(lightingEnvironment, cameraModel, CLIP_START, CLIP_END, fov);
            Window.showFrame();
        }
    }

    /**
     * Shifts all entities without parent, so that the camera ends up near the world origin
     * @param scene The scene
     * @param cameraEntity Entity of the camera
     * @param thresholdDistance Distance of the camera from world origin for shift to take place
     * @return The shift that was applied as a vector
     *
     * @author Vincent Lahmann
     */
    private static float[] shiftPositionsOverThreshold(Scene scene, int cameraEntity, float thresholdDistance){
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
            return cameraOffset;
        }
        return vec3();
    }

    /**
     * This method handles the Keyboard input for the aircraft
     * It updates the flap angle of the corresponding flaps
     * It also updates the engine power
     *
     * if the W key is pressed, the planes pulls up
     * if the S key is pressed, the plane pushes down
     * if the A key is pressed, the plane rolls left
     * if the D key is pressed, the plane rolls right
     * if the R key is pressed, the plane increases the engine power
     * if the F key is pressed, the plane decreases the engine power
     *
     * @param scene The scene in which the aircraft is located
     * @param aircraft The entity of the aircraft
     *
     * @author Nikolas Kühnlein
     */
    private static void updateAircraftInput(Scene scene, int aircraft) {
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
            wing.flapAngle = 0.075f;
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

    /**
     * This method handles the Keyboard input for the environment
     * It updates the time of day
     * if the M key is pressed, the time of day increases
     * if the N key is pressed, the time of day decreases
     *
     * @param lightingEnvironment The lighting environment
     * @param deltaTime The time that has passed since the last frame
     *
     * @author Nikolas Kühnlein
     */
    private static void updateEnvironmentInput(LightingEnvironment lightingEnvironment, float deltaTime) {
        if(Input.isKeyDown(Input.KEY_ID_M))
            lightingEnvironment.increaseTimeOfDay( 0.1f * deltaTime);
        if(Input.isKeyDown(Input.KEY_ID_N))
            lightingEnvironment.increaseTimeOfDay(-0.1f * deltaTime);
    }

    /**
     * This method creates a cessna172 aircraft
     * First the entity is created
     * Then the components are added
     *  - Transform
     *  - SurfaceModel
     *    - left_outer_wing
     *    - left_inner_wing
     *    - right_outer_wing
     *    - right_inner_wing
     *    - left_horizontal_stabilizer
     *    - right_horizontal_stabilizer
     *    - vertical_stabilizer
     *  - EngineModel
     *    - main_engine
     *  - RigidBody
     *  - Aircraft Mesh
     *
     * @param scene The scene in which the aircraft is located
     * @return The entity of the aircraft
     * @throws Exception throws an exception if the mesh could not be loaded
     */
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