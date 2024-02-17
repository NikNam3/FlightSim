package engine.loading;

import engine.structures.Mesh;
import engine.structures.Texture2D;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL41.*;

public class AsynchronousLoader {
    private static class LoadingQueueEntryMesh {
        Mesh result;
        MeshFileParserThread thread;
    }

    private static class LoadingQueueEntryTexture {
        Texture2D result;
        TextureParserThread thread;
    }


    private static List<LoadingQueueEntryMesh>    loadingQueueMesh;
    private static List<LoadingQueueEntryTexture> loadingQueueTexture;

    static {
        loadingQueueMesh    = new ArrayList<>();
        loadingQueueTexture = new ArrayList<>();
    }

    public static void enqueue(String file, Mesh result){
        LoadingQueueEntryMesh entry = new LoadingQueueEntryMesh();
        entry.result = result;
        entry.thread = new MeshFileParserThread(file);
        entry.thread.start();
        loadingQueueMesh.add(entry);
    }

    public static void enqueue(String file, Texture2D result){
        LoadingQueueEntryTexture entry = new LoadingQueueEntryTexture();
        entry.result = result;
        entry.thread = new TextureParserThread(file);
        entry.thread.start();
        loadingQueueTexture.add(entry);
    }

    public static void updateQueue(){
        updateMeshQueue();
        updateTextureQueue();
    }

    private static void updateMeshQueue(){
        List<Integer> deleteQueue = new ArrayList<>();
        for(int i = 0; i < loadingQueueMesh.size(); i++){
            LoadingQueueEntryMesh entry = loadingQueueMesh.get(i);
            MeshFileParserThread thread = entry.thread;
            if(thread.isAlive())
                continue;

            if(!thread.hasFailed()){
                loadMeshToDevice(entry.result, thread.getPositionData(), thread.getNormalData(), thread.getUvData());
            }
            else{
                thread.getException().printStackTrace();
            }
            deleteQueue.add(i);
        }
        int offset = 0;
        for(int i : deleteQueue){
            loadingQueueMesh.remove(i - offset);
            offset++;
        }
    }

    private static void updateTextureQueue(){
        List<Integer> deleteQueue = new ArrayList<>();
        for(int i = 0; i < loadingQueueTexture.size(); i++){
            LoadingQueueEntryTexture entry = loadingQueueTexture.get(i);
            TextureParserThread thread = entry.thread;
            if(thread.isAlive())
                continue;

            if(!thread.hasFailed()){
                loadTextureToDevice(entry.result, thread.getData(), thread.getWidth(), thread.getHeight(), thread.getChannels());
            }
            else{
                thread.getException().printStackTrace();
            }
            deleteQueue.add(i);
        }
        int offset = 0;
        for(int i : deleteQueue){
            loadingQueueTexture.remove(i - offset);
            offset++;
        }
    }

    private static void loadMeshToDevice(Mesh result, float[] positions, float[] normals, float[] uvs){
        int glId = glGenVertexArrays();
        glBindVertexArray(glId);
        addVb(positions, 0, 3, false, 0, 0);
        addVb(normals,   1, 3, false, 0, 0);
        addVb(uvs,       2, 2, false, 0, 0);

        result.vertexCount = positions.length / 3;
        result.glId        = glId;
    }

    private static void addVb(float[] data, int index, int count, boolean normalized, int stride, int offset) {
        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glEnableVertexAttribArray(index);
        glBufferData(GL_ARRAY_BUFFER, data , GL_STATIC_DRAW);
        glVertexAttribPointer(index, count, GL_FLOAT, normalized, stride, offset);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private static void loadTextureToDevice(Texture2D texture, ByteBuffer data, int width, int height, int channels){
        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        if(channels == 4)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        else
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, data);
        /*
        float[] maxAnisotropyA = new float[1];
        glGetFloatv(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropyA);
        anisotropyLevel = (anisotropyLevel > maxAnisotropyA[0]) ? maxAnisotropyA[0] : anisotropyLevel;
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, anisotropyLevel);
        */

        /*
        glGenerateMipmap(GL_TEXTURE_2D);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR);
         */

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        texture.glId = id;
    }
}
