package mcg.in4.projekte_23_24.FlightSim.engine.terrain;

import mcg.in4.projekte_23_24.FlightSim.engine.Window;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.shader.ShaderProgram;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.shader.ShaderProgramLoader;
import mcg.in4.projekte_23_24.FlightSim.engine.structures.Camera;
import mcg.in4.projekte_23_24.FlightSim.engine.structures.Scene;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Transform;

import java.util.ArrayList;
import java.util.List;

import static mcg.in4.projekte_23_24.FlightSim.engine.Math3d.*;
import static org.lwjgl.opengl.GL41.*;

public class Terrain {

    private static ShaderProgram shaderProgram;

    private static List<CascadeLayer> cascadeLayers;

    private static float offsetX = 0;
    private static float offsetZ = 0;

    public static void init(){
        shaderProgram = new ShaderProgram(0);
        ShaderProgramLoader.load("shaders/vert_terrain.txt", "shaders/frag_terrain.txt", shaderProgram);

        cascadeLayers = new ArrayList<>();

        CascadeLayer layer0 = new CascadeLayer(0);
        cascadeLayers.add(layer0);
        CascadeLayer layer1 = new CascadeLayer(1);
        cascadeLayers.add(layer1);
    }

    public static float getHeightAtPoint(float x, float z){
        return cascadeLayers.get(0).getHeight(x, z);
    }

    private static float[][] toModelMatrix(Transform transform){
        return mul(transform.matrixOffset, transform.matrixRotate);
    }

    public static void render(Scene scene, int cameraId){
        Camera camera = scene.getComponent(cameraId, Camera.class);
        Transform transform = new Transform();
        if(scene.hasComponent(cameraId, Transform.class))
            transform = scene.getComponent(cameraId, Transform.class);
        float aspectRatio = Window.getWidth() / (float)Window.getHeight();
        float[][] viewMat = inverse(toModelMatrix(transform));
        float[][] projMat = perspective(camera.fov, camera.nearClip, camera.farClip, aspectRatio);
        glUseProgram(shaderProgram.glId);
        shaderProgram.uploadMat4("u_matrix_view", viewMat);
        shaderProgram.uploadMat4("u_matrix_perspective", projMat);

        for(CascadeLayer layer : cascadeLayers){
            layer.update(transform.matrixOffset[0][3], transform.matrixOffset[2][3]);
            layer.render(shaderProgram);
        }
    }
}
