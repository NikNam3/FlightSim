package mcg.in4.projekte_23_24.FlightSim.engine.terrain;

import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.Texture2D;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL30.GL_R32F;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

/**
 * Asynchronous Loader class for terrain tiles
 */
class TerrainTileLoader {
    private static final Map<String, AsynchronousTextureLoader> albedoLoadingProcesses;
    private static final Map<String, AsynchronousTextureLoader> heightLoadingProcesses;
    private static final Map<String, TerrainTile> loadingResultTiles;

    static{
        albedoLoadingProcesses = new HashMap<>();
        heightLoadingProcesses = new HashMap<>();
        loadingResultTiles     = new HashMap<>();
    }

    static void enqueue(TerrainTile tile, String tileKey){
        String albedoFile = "content/terrain/orthos/" + tileKey + ".jpg";
        String heightFile = "content/terrain/heights/" + tileKey + ".png";
        AsynchronousTextureLoader albedoLoader = new AsynchronousTextureLoader(albedoFile);
        AsynchronousTextureLoader heightLoader = new AsynchronousTextureLoader(heightFile);

        albedoLoader.start();
        heightLoader.start();

        albedoLoadingProcesses.put(tileKey, albedoLoader);
        heightLoadingProcesses.put(tileKey, heightLoader);
        loadingResultTiles.put(tileKey, tile);
    }

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

    static boolean isTileBeingLoaded(String key){
        return loadingResultTiles.containsKey(key);
    }

    private static void updateAlbedoLoading(){
        List<String> removalQueue = new ArrayList<>();

        for(String tileKey : albedoLoadingProcesses.keySet()){
            AsynchronousTextureLoader loader = albedoLoadingProcesses.get(tileKey);
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
            AsynchronousTextureLoader loader = heightLoadingProcesses.get(tileKey);
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
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        return new Texture2D(glTextureId, width, height);
    }

    private static Texture2D createHeightMap(ByteBuffer data, int width, int height){
        int glTextureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, glTextureId);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_R32F, width, height, 0, GL_RED, GL_FLOAT, data);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        return new Texture2D(glTextureId, width, height);
    }

}
