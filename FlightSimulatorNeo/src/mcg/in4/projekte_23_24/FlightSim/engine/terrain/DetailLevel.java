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
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 * Class representing a detail level of terrain
 */
class DetailLevel {
    private final MeshArray baseMesh;
    private final Map<Integer, Tile> tiles;
    private final int level;
    private final float tileSizeMeters;

    DetailLevel(int level){
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

        tileSizeMeters = (float)(2000 * Math.pow(3, level));
    }

    /**
     * Updates the state of the layer
     * @param playerX Player position x
     * @param playerZ Player position z
     */
    void update(float playerX, float playerZ) {

        // Tiles that are requested during this frame
        List<Integer> currentTiles = new ArrayList<>();
        // iterate through every tile that is supposed to be rendered
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                int playerTileX = i + (int)( playerX / (tileSizeMeters));
                int playerTileZ = j + (int)(-playerZ / (tileSizeMeters));
                int coordinate = toTileCoordinate((short)playerTileX, (short)playerTileZ);

                // Load tile if it does not exist yet
                if (!tiles.containsKey(playerTileX << 16 | playerTileZ)) {
                    String tileKey = "lv" + level + "_" + playerTileX + "_" + playerTileZ;

                    Tile tile = new Tile();
                    TileLoader.enqueue(tile, tileKey);

                    tiles.put(coordinate, tile);
                }
                currentTiles.add(coordinate);
            }
        }

        List<Integer> removalQueue = new ArrayList<>();
        // put all tiles into removal queue that remain from last frame and are not needed anymore
        for(int coordinate : tiles.keySet()){
            if(!currentTiles.contains(coordinate))
                removalQueue.add(coordinate);
        }

        // delete unnecessary tiles
        for(int coordinate : removalQueue){
            removeTile(coordinate);
        }

    }

    /**
     * Renders all tiles in the layer
     * @param shaderProgram Shader program that will be used for rendering
     */
    void render(Program shaderProgram){
        shaderProgram.makeActive();
        shaderProgram.setInt("u_layer_idx", level);
        int tileCount = tiles.size() + 1;
        int tileIdx = 1;
        for(Map.Entry<Integer, Tile> entry : tiles.entrySet()){
                Tile tile = entry.getValue();
                int tileCoordinate = entry.getKey();

                if (tile.albedoMap == null)
                    continue;

                int offsetX = ((tileCoordinate >> 16))    * (int)(tileSizeMeters);
                int offsetY = ((tileCoordinate & 0xFFFF)) * (int)(tileSizeMeters);

                float shade = tileIdx / (float)tileCount;
                shaderProgram.setFloat("u_shade", shade);
                shaderProgram.setMat4("u_matrix_model", translation(offsetX, -2 * level, -offsetY));
                shaderProgram.setInt("u_albedo_map", 0);
                shaderProgram.setInt("u_height_map", 1);
                shaderProgram.setFloat("u_sinking_threshold", (float)(500 * (Math.pow(3, level) - 1)));

                // set albedo map active in first slot and set uniform
                if(tile.albedoMap != null) {
                    glActiveTexture(GL_TEXTURE0);
                    shaderProgram.setInt("u_has_albedo_map", 1);
                    tile.albedoMap.makeActive();
                }
                else
                    shaderProgram.setInt("u_has_albedo_map", 0);

            // set albedo map active in first slot and set uniform
                if(tile.heightMap != null) {
                    glActiveTexture(GL_TEXTURE1);
                    shaderProgram.setInt("u_has_height_map", 1);
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
        Tile tile = tiles.get(coordinate);
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
