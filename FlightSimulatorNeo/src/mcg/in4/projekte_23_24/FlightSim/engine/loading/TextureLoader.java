package mcg.in4.projekte_23_24.FlightSim.engine.loading;

import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.Texture2D;

import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

public class TextureLoader {
    public static Texture2D load(String file) throws Exception{
        int[] widthA    = {0};
        int[] heightA   = {0};
        int[] channelsA = {0};
        stbi_set_flip_vertically_on_load(true);
        var data = stbi_load(file, widthA, heightA, channelsA, STBI_rgb_alpha);
        if(data == null)
            throw new Exception(file + " could not be read!");

        int width  = widthA[0];
        int height = heightA[0];

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
}
