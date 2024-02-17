package mcg.in4.projekte_23_24.FlightSim.engine.graphics;

import mcg.in4.projekte_23_24.FlightSim.engine.Window;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.shader.ShaderProgram;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.shader.ShaderProgramLoader;
import mcg.in4.projekte_23_24.FlightSim.engine.structures.Camera;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Mesh;
import mcg.in4.projekte_23_24.FlightSim.engine.structures.Scene;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Transform;

import static org.lwjgl.opengl.GL41.*;
import static mcg.in4.projekte_23_24.FlightSim.engine.Math3d.*;

public class Renderer {

    private static ShaderProgram shader;

    public static void init(){
        shader = new ShaderProgram(0);
        ShaderProgramLoader.load("shaders/vert.txt", "shaders/frag.txt", shader);
    }

    private static float[][] toModelMatrix(Transform transform){
        return mul(transform.matrixOffset, transform.matrixRotate);
    }

    public static void prepareRender(Scene scene, int cameraEntity){
        Camera camera = scene.getComponent(cameraEntity, Camera.class);
        Transform transform = new Transform();
        if(scene.hasComponent(cameraEntity, Transform.class))
            transform = scene.getComponent(cameraEntity, Transform.class);

        float aspectRatio = Window.getWidth() / (float)Window.getHeight();

        float[][] viewMat = inverse(toModelMatrix(transform));
        float[][] projMat = perspective(camera.fov, camera.nearClip, camera.farClip, aspectRatio);
        shader.uploadMat4("u_matrix_view", viewMat);
        shader.uploadMat4("u_matrix_perspective", projMat);
    }

    public static void render(Scene scene, int entity){
        if(!scene.hasComponent(entity, Mesh.class))
            return;

        Mesh mesh = scene.getComponent(entity, Mesh.class);
        Transform transform = new Transform();
        if(scene.hasComponent(entity, Transform.class))
            transform = scene.getComponent(entity, Transform.class);

        float[][] modelMat = toModelMatrix(transform);
        shader.uploadMat4("u_matrix_model", modelMat);

        if(mesh.textureMaps != null && mesh.textureMaps.length > 0) {
            glBindTexture(GL_TEXTURE_2D, mesh.textureMaps[0].glId);
            shader.uploadInt("u_has_albedo_map", 1);
        }
        else {
            glBindTexture(GL_TEXTURE_2D, 0);
            shader.uploadInt("u_has_albedo_map", 0);
        }

        glBindVertexArray(mesh.glId);
        glDrawArrays(GL_TRIANGLES, 0, mesh.vertexCount);
        glBindVertexArray(0);
    }
}
