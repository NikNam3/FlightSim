package mcg.in4.projekte_23_24.FlightSim.engine.terrain;

import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBImage.*;

/**
 * Class for executing texture loading tasks on another thread<br>
 * Texture data is only written to the host memory (RAM)
 */
class AsynchronousTextureLoader extends Thread{
    private int        textureWidth;
    private int        textureHeight;
    private ByteBuffer textureData;
    private final String file;
    private boolean successFlag;

    AsynchronousTextureLoader(String file){
        this.file = file;
        successFlag = false;
    }

    @Override
    public void run(){
        stbi_set_flip_vertically_on_load(true);
        int[] widthA  = new int[1];
        int[] heightA = new int[1];
        textureData = stbi_load(file, widthA, heightA, new int[1], STBI_rgb_alpha);
        if(textureData == null){
            successFlag = false;
            return;
        }
        textureWidth  = widthA[0];
        textureHeight = heightA[0];
        successFlag   = true;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

    public ByteBuffer getTextureData() {
        return textureData;
    }

    public boolean wasSuccessful(){
        return successFlag;
    }

    public String getFile() {
        return file;
    }
}
