package mcg.in4.projekte_23_24.FlightSim.engine.structures;

import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.GL41.*;

public class Texture2D {
    public int glId;

    public Texture2D(){
        this.glId = 0;
    }

    public Texture2D(int glId){
        this.glId = glId;
    }

    public static Texture2D createFloat32(int width, int height, int nChannels){
        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        int format;
        int internalFormat;
        switch (nChannels){
            case 1:
                format = GL_RED;
                internalFormat = GL_R32F;
                break;
            case 2:
                format = GL_RG;
                internalFormat = GL_RG32F;
                break;
            case 3:
                format = GL_RGB;
                internalFormat = GL_RGB32F;
                break;
            case 4:
                format = GL_RGBA;
                internalFormat = GL_RGBA32F;
                break;
            default:
                throw new IllegalArgumentException("Unsupported number of channels!");
        }

        glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_FLOAT, 0);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        return new Texture2D(id);
    }

    public static Texture2D createDepth(int width, int height){
        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32F, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, 0);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        return new Texture2D(id);
    }

    public static Texture2D createUINT8(int width, int height, int nChannels){
        int id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        int format;
        int internalFormat;
        switch (nChannels){
            case 1:
                format = GL_RED;
                internalFormat = GL_R8UI;
                break;
            case 2:
                format = GL_RG;
                internalFormat = GL_RG8UI;
                break;
            case 3:
                format = GL_RGB;
                internalFormat = GL_RGB8UI;
                break;
            case 4:
                format = GL_RGBA;
                internalFormat = GL_RGBA8UI;
                break;
            default:
                throw new IllegalArgumentException("Unsupported number of channels!");
        }

        glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, 0);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        return new Texture2D(id);
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, glId);
    }

    public static void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void destroy(){
        glDeleteTextures(glId);
    }
}
