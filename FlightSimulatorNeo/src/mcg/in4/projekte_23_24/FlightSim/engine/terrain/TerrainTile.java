package mcg.in4.projekte_23_24.FlightSim.engine.terrain;

import mcg.in4.projekte_23_24.FlightSim.engine.structures.HeightMap;
import mcg.in4.projekte_23_24.FlightSim.engine.structures.Texture2D;

class TerrainTile {

    HeightMap heightMap;
    Texture2D albedoMap;

    public TerrainTile(){
        heightMap = new HeightMap(0, null, 0);
        albedoMap = new Texture2D();
    }
}
