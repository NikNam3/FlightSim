package mcg.in4.projekte_23_24.FlightSim.engine.loading;

import java.nio.ByteBuffer;

import static org.lwjgl.stb.STBImage.*;

class TextureParserThread extends Thread{
    private ByteBuffer data;
    private int width;
    private int height;
    private int channels;
    private String    file;
    private Exception exception;

    TextureParserThread(String file){
        this.file = file;
    }

    @Override
    public void run(){
        try {
            int[] width    = {0};
            int[] height   = {0};
            int[] channels = {0};
            stbi_set_flip_vertically_on_load(true);
            this.data   = stbi_load(this.file, width, height, channels, STBI_rgb_alpha);
            this.width  = width[0];
            this.height = height[0];
            this.channels = 4;
        }
        catch (Exception e){
            this.exception = e;
        }
    }

    boolean hasFailed(){
        return exception != null;
    }

    Exception getException(){
        return exception;
    }

    ByteBuffer getData(){
        return data;
    }

    int getWidth(){
        return width;
    }

    int getHeight(){
        return height;
    }

    int getChannels(){
        return channels;
    }
}
