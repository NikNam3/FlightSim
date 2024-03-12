package mcg.in4.projekte_23_24.FlightSim.engine.terrain;


import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.Texture2D;

class TerrainTile {

 //   HeightMap heightMap;
    final Texture2D albedoMap;

    public TerrainTile(Texture2D albedoMap){
    //    heightMap = new HeightMap(0, null, 0);
        this.albedoMap = albedoMap;
    }
}
