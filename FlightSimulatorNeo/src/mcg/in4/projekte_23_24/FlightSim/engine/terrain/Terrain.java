package mcg.in4.projekte_23_24.FlightSim.engine.terrain;


import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.LightingEnvironment;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.Program;
import mcg.in4.projekte_23_24.FlightSim.engine.loading.ProgramLoader;

import java.util.ArrayList;
import java.util.List;

import static mcg.in4.projekte_23_24.FlightSim.engine.base.Math3d.inverse;

public class Terrain {

    private static Program shaderProgram;

    private static List<CascadeLayer> cascadeLayers;

    private static float offsetX = 0;
    private static float offsetZ = 0;

    public static void init(){
        shaderProgram = ProgramLoader.load("shaders/terrain/vert_terrain.txt", "shaders/terrain/frag_terrain.txt");

        cascadeLayers = new ArrayList<>();

        CascadeLayer layer0 = new CascadeLayer(0);
        cascadeLayers.add(layer0);
        CascadeLayer layer1 = new CascadeLayer(1);
        cascadeLayers.add(layer1);
    }

    public static float getHeightAtPoint(float x, float z){
    //    return cascadeLayers.get(0).getHeight(x, z);
        return 0.f;
    }


    public static void render(LightingEnvironment lightingEnvironment, float[][] cameraModel, float[][] cameraProjection){
        shaderProgram.makeActive();
        shaderProgram.setMat4("u_matrix_view", inverse(cameraModel));
        shaderProgram.setMat4("u_matrix_perspective", cameraProjection);
        shaderProgram.setVec3("u_light_direction", lightingEnvironment.getSunLightDirection());
        shaderProgram.setVec3("u_ambient",         lightingEnvironment.getAmbientLighting());
        shaderProgram.setVec3("u_light_color", lightingEnvironment.getSunColor());

        for(CascadeLayer layer : cascadeLayers){
            layer.update(cameraModel[0][3], cameraModel[2][3]);
            layer.render(shaderProgram);
        }
    }
}
