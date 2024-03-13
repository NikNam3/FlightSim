package mcg.in4.projekte_23_24.FlightSim.engine.terrain;

import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.Texture2D;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.GL41.*;

/**
 * Loader for asynchronous loading of terrain tiles
 */
class TileLoader {
    private static final Map<String, HostLoaderAlbedo> albedoLoadingProcesses;
    private static final Map<String, HostLoaderHeight> heightLoadingProcesses;
    private static final Map<String, Tile> loadingResultTiles;

    static{
        albedoLoadingProcesses = new HashMap<>();
        heightLoadingProcesses = new HashMap<>();
        loadingResultTiles     = new HashMap<>();
    }

    /**
     * Starts a new loading process
     * @param tile The tile which will be loaded
     * @param tileKey The key of the tile - e.g. lv0_2_3
     */
    static void enqueue(Tile tile, String tileKey){
        String albedoFile = "content/terrain/orthos/" + tileKey + ".jpg";
        String heightFile = "content/terrain/heights/" + tileKey + ".png";
        HostLoaderAlbedo albedoLoader = new HostLoaderAlbedo(albedoFile);
        HostLoaderHeight heightLoader = new HostLoaderHeight(heightFile);

        albedoLoader.start();
        heightLoader.start();

        albedoLoadingProcesses.put(tileKey, albedoLoader);
        heightLoadingProcesses.put(tileKey, heightLoader);
        loadingResultTiles.put(tileKey, tile);
    }

    /**
     * Checks the status of every loading process<br>Writes data to the tile and uploads graphics structure to the device
     */
    static void update(){
        updateAlbedoLoading();
        updateHeightLoading();

        List<String> removalQueue = new ArrayList<>();
        for(String key : loadingResultTiles.keySet()){
            if(!albedoLoadingProcesses.containsKey(key) && !heightLoadingProcesses.containsKey(key))
                removalQueue.add(key);
        }
        for(String key : removalQueue)
            loadingResultTiles.remove(key);
    }

    /**
     * Checks if a tile is currently being loaded
     * @param key Key of the tile
     * @return True if tile is loading right now
     */
    static boolean isTileBeingLoaded(String key){
        return loadingResultTiles.containsKey(key);
    }

    private static void updateAlbedoLoading(){
        List<String> removalQueue = new ArrayList<>();

        for(String tileKey : albedoLoadingProcesses.keySet()){
            HostLoaderAlbedo loader = albedoLoadingProcesses.get(tileKey);
            if(loader.isAlive())
                continue;
            if(loader.wasSuccessful()) {
                loadingResultTiles.get(tileKey).albedoMap = createAlbedoMap(loader.getTextureData(), loader.getTextureWidth(), loader.getTextureHeight());
            }
            removalQueue.add(tileKey);
        }
        for(String key : removalQueue){
            albedoLoadingProcesses.remove(key);
        }
    }

    private static void updateHeightLoading(){
        List<String> removalQueue = new ArrayList<>();
        for(String tileKey : heightLoadingProcesses.keySet()){
            HostLoaderHeight loader = heightLoadingProcesses.get(tileKey);
            if(loader.isAlive())
                continue;
            if(loader.wasSuccessful())
                loadingResultTiles.get(tileKey).heightMap = createHeightMap(loader.getTextureData(), loader.getTextureWidth(), loader.getTextureHeight());
            removalQueue.add(tileKey);
        }
        for(String key : removalQueue){
            heightLoadingProcesses.remove(key);
        }
    }

    private static Texture2D createAlbedoMap(ByteBuffer data, int width, int height){
        int glTextureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, glTextureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        float anisotropyLevel = 8;
        float[] maxAnisotropyA = new float[1];
        glGetFloatv(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropyA);
        anisotropyLevel = Math.min(anisotropyLevel, maxAnisotropyA[0]);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, anisotropyLevel);
        glGenerateMipmap(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        return new Texture2D(glTextureId, width, height);
    }

    private static Texture2D createHeightMap(float[] data, int width, int height){

        int glTextureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, glTextureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, width, height, 0, GL_RED, GL_FLOAT, data);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        return new Texture2D(glTextureId, width, height);
    }

}
