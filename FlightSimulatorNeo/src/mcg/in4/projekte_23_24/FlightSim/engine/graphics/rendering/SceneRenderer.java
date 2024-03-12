package mcg.in4.projekte_23_24.FlightSim.engine.graphics.rendering;

import mcg.in4.projekte_23_24.FlightSim.engine.base.Scene;
import mcg.in4.projekte_23_24.FlightSim.engine.components.render.MeshArray;
import mcg.in4.projekte_23_24.FlightSim.engine.components.general.Transform;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.*;
import mcg.in4.projekte_23_24.FlightSim.engine.loading.ProgramLoader;

import java.util.Objects;

import static mcg.in4.projekte_23_24.FlightSim.engine.base.Math3d.*;

import static org.lwjgl.opengl.GL41.*;

/**
 * Class to render a scene
 * @version 1.0
 * @since 1.0
 * @author Vincent Lahmann
 *
 */
public class SceneRenderer {


    private static Program objectShader;

    /**
     * Initializes class for later use<br>Must called first before using other methods
     */
    public static void init(){
        objectShader = ProgramLoader.load("shaders/object/object_vertex.glsl", "shaders/object/object_fragment.glsl");
    }

    /**
     * Renders every entity in the scene
     * @param scene Scene that will be rendered
     * @param cameraModel Model-matrix of camera
     * @param cameraPerspective Perspective-Matrix of camera
     *
     * @author Vincent Lahmann
     */
    public static void render(Scene scene, LightingEnvironment lightingEnvironment, float[][] cameraModel, float[][] cameraPerspective){
        objectShader.makeActive();

        // Set uniforms related to the camera
        objectShader.setMat4("u_matrix_view", inverse(cameraModel));
        objectShader.setMat4("u_matrix_perspective", cameraPerspective);
        objectShader.setVec3("u_light_direction", lightingEnvironment.getSunLightDirection());
        objectShader.setVec3("u_ambient",         lightingEnvironment.getAmbientLighting());
        objectShader.setVec3("u_light_color", lightingEnvironment.getSunColor());

        for(int entity : scene.getAll()){
            // draw each renderable component of entity

            float[][] modelMatrix = mat4();

            if(scene.hasComponent(entity, Transform.class)) {
                Transform objectTransform = scene.getComponent(entity, Transform.class);
                modelMatrix = mul(objectTransform.matrixPosition, objectTransform.matrixRotation);
            }

            objectShader.setMat4("u_matrix_model", modelMatrix);
            if(scene.hasComponent(entity, MeshArray.class))
                renderMeshArray(scene.getComponent(entity, MeshArray.class));
        }
    }

    /**
     * Renders a mesh array
     * @param meshArray MeshArray that will be rendered
     *
     * @author Vincent Lahmann
     */
    private static void renderMeshArray(MeshArray meshArray){
        for(Mesh mesh : meshArray.submeshes){
            mesh.makeActive();
            Material material = mesh.material;
            if(material != null){
                glActiveTexture(GL_TEXTURE0);
                Texture2D albedoMap = material.texture;
                float[] albedo      = material.albedo;
                if(albedoMap != null) {
                    material.texture.makeActive();
                    objectShader.setInt("u_has_albedo_map", 1);
                    objectShader.setInt("u_albedo_map", 0);
                }
                else{
                    objectShader.setInt("u_has_albedo_map", 0);
                }
                objectShader.setVec3("u_albedo", Objects.requireNonNullElseGet(albedo, () -> new float[]{0.8f, 0.8f, 0.8f}));
            }
            glDrawArrays(GL_TRIANGLES, 0, mesh.vertexCount);
            glBindTexture(GL_TEXTURE_2D, 0);
        }
    }
}
