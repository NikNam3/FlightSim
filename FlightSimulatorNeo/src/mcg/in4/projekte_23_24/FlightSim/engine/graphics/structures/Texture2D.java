package mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures;

import static org.lwjgl.opengl.GL41.*;

public class Texture2D extends GLStructure {

    public final int width;
    public final int height;

    public Texture2D(int glApiId, int width, int height) {
        super(glApiId);
        this.width  = width;
        this.height = height;
    }

    @Override
    public void makeActive() {
        glBindTexture(GL_TEXTURE_2D, glApiId);
    }

    @Override
    public void clearFromDevice() {
        glDeleteTextures(glApiId);
    }
}
