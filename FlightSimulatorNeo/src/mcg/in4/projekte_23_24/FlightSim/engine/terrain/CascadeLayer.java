package mcg.in4.projekte_23_24.FlightSim.engine.terrain;



import mcg.in4.projekte_23_24.FlightSim.engine.components.render.MeshArray;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.Mesh;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.Program;
import mcg.in4.projekte_23_24.FlightSim.engine.loading.MeshArrayLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mcg.in4.projekte_23_24.FlightSim.engine.base.Math3d.translation;
import static org.lwjgl.opengl.GL41.*;

/**
 * Class representing a detail level of terrain
 */
class CascadeLayer {
    private final MeshArray baseMesh;
    private final Map<Integer, TerrainTile> tiles;
    private final int level;
    private final float originOffsetFromTerrainX =   2000.f;
    private final float originOffsetFromTerrainZ = -10000.f;

    CascadeLayer(int level){
        String baseMeshFile = "content/terrain/lv" + level + "_base.obj";
        MeshArray buffer = null;
        try {
            buffer = MeshArrayLoader.loadMesh(baseMeshFile);
        } catch (Exception e) {
            System.err.println("Terrain base mesh could not be opened");
        }
        baseMesh = buffer;
        tiles = new HashMap<>();
        this.level = level;
    }

    void update(float playerX, float playerZ) {
        playerX += originOffsetFromTerrainX;
        playerZ += originOffsetFromTerrainZ;

        List<Integer> currentTiles = new ArrayList<>();
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                int playerTileX = i + (int)( playerX / (2000 * Math.pow(3, level)));
                int playerTileZ = j + (int)(-playerZ / (2000 * Math.pow(3, level)));
                int coordinate = toTileCoordinate((short)playerTileX, (short)playerTileZ);

                if (!tiles.containsKey(playerTileX << 16 | playerTileZ)) {
                    String tileKey = "lv" + level + "_" + playerTileX + "_" + playerTileZ;

                    TerrainTile tile = new TerrainTile();
                    TerrainTileLoader.enqueue(tile, tileKey);

                    tiles.put(coordinate, tile);
                }
                currentTiles.add(coordinate);
            }
        }

        List<Integer> removalQueue = new ArrayList<>();
        for(int coord : tiles.keySet()){
            if(!currentTiles.contains(coord))
                removalQueue.add(coord);
        }

        for(int coord : removalQueue){
            if(!currentTiles.contains(coord))
                removeTile(coord);
        }

    }

    void render(Program shaderProgram){
        shaderProgram.makeActive();

        shaderProgram.setInt("u_layer_idx", level);
        int tileCount = tiles.size() + 1;
        int tileIdx = 1;
        for(Map.Entry<Integer, TerrainTile> entry : tiles.entrySet()){
                TerrainTile tile = entry.getValue();
                int tileCoordinate = entry.getKey();

                if (tile.albedoMap == null)
                    continue;

                int offsetX = ((tileCoordinate >> 16))    * (int)(2000 * Math.pow(3, level));
                int offsetY = ((tileCoordinate & 0xFFFF)) * (int)(2000 * Math.pow(3, level));

                float shade = tileIdx / (float)tileCount;
                shaderProgram.setFloat("u_shade", shade);
                shaderProgram.setMat4("u_matrix_model", translation(offsetX - originOffsetFromTerrainX, -10 * level, -offsetY - originOffsetFromTerrainZ));


                shaderProgram.setInt("u_albedo_map", 0);
                shaderProgram.setInt("u_height_map", 1);


                if(tile.albedoMap != null) {
                    glActiveTexture(GL_TEXTURE0);
                    shaderProgram.setInt("u_has_albedo_map", 1);
                    tile.albedoMap.makeActive();
                }
                else
                    shaderProgram.setInt("u_has_albedo_map", 0);

                if(tile.heightMap != null) {
                    glActiveTexture(GL_TEXTURE1);
                    shaderProgram.setInt("u_has_height_map", 0);
                    tile.heightMap.makeActive();
                }
                else
                    shaderProgram.setInt("u_has_height_map", 0);

                for(Mesh submesh : baseMesh.submeshes) {
                    submesh.makeActive();
                    glDrawArrays(GL_TRIANGLES, 0, submesh.vertexCount);
                }

                tileIdx++;
        }
        glActiveTexture(GL_TEXTURE0);
        glBindVertexArray(0);
    }

    private void removeTile(int coordinate){
        // TODO: Check if tile is being loaded

        if(!tiles.containsKey(coordinate))
            return;
        TerrainTile tile = tiles.get(coordinate);
        if(tile.albedoMap != null)
            tile.albedoMap.clearFromDevice();
        if(tile.heightMap != null)
            tile.heightMap.clearFromDevice();
        tiles.remove(coordinate);
    }

    private int toTileCoordinate(short x, short z){
        return (x << 16) | z;
    }

}
