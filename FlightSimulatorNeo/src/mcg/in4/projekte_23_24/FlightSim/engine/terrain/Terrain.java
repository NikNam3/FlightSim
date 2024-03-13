package mcg.in4.projekte_23_24.FlightSim.engine.terrain;


import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.LightingEnvironment;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.Program;
import mcg.in4.projekte_23_24.FlightSim.engine.loading.ProgramLoader;

import java.util.ArrayList;
import java.util.List;

import static mcg.in4.projekte_23_24.FlightSim.engine.base.Math3d.*;
import static mcg.in4.projekte_23_24.FlightSim.engine.base.Math3d.inverse;
import static mcg.in4.projekte_23_24.FlightSim.engine.base.Math3d.inverse;

/**
 * Static class for handling terrain
 */
public class Terrain {

    private static Program shaderProgram;
    private static List<mcg.in4.projekte_23_24.FlightSim.engine.terrain.DetailLevel> detailLevels;

    /**
     * Initializes the variables<br>Must be called first
     */
    public static void init(){
        shaderProgram = ProgramLoader.load("shaders/terrain/terrain_vertex.glsl", "shaders/terrain/terrain_frag.glsl");

        detailLevels = new ArrayList<>();

        mcg.in4.projekte_23_24.FlightSim.engine.terrain.DetailLevel layer0 = new mcg.in4.projekte_23_24.FlightSim.engine.terrain.DetailLevel(0);
        detailLevels.add(layer0);
        mcg.in4.projekte_23_24.FlightSim.engine.terrain.DetailLevel layer1 = new mcg.in4.projekte_23_24.FlightSim.engine.terrain.DetailLevel(1);
        detailLevels.add(layer1);
        mcg.in4.projekte_23_24.FlightSim.engine.terrain.DetailLevel layer2 = new mcg.in4.projekte_23_24.FlightSim.engine.terrain.DetailLevel(2);
        detailLevels.add(layer2);
    }

    /**
     * Updates and renders the terrain
     * @param lightingEnvironment Current world lighting environment
     * @param cameraModel Model matrix of camera
     * @param cameraProjection Projection matrix of camera
     */
    public static void updateAndRender(LightingEnvironment lightingEnvironment, float[][] cameraModel, float[][] cameraProjection){
        mcg.in4.projekte_23_24.FlightSim.engine.terrain.TileLoader.update();

        shaderProgram.makeActive();
        shaderProgram.setMat4("u_matrix_view", inverse(cameraModel));
        shaderProgram.setMat4("u_matrix_perspective", cameraProjection);
        shaderProgram.setVec3("u_light_direction", lightingEnvironment.getSunLightDirection());
        shaderProgram.setVec3("u_ambient",         lightingEnvironment.getAmbientLighting());
        shaderProgram.setVec3("u_light_color", lightingEnvironment.getSunColor());

        for(mcg.in4.projekte_23_24.FlightSim.engine.terrain.DetailLevel layer : detailLevels){
            layer.update(cameraModel[0][3], cameraModel[2][3]);
            layer.render(shaderProgram);
        }
    }
}
