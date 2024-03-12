package mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures;

import static org.lwjgl.opengl.GL41.*;

/**
 * Wrapper for a 2D texture
 * @version 1.0
 * @since 1.0
 * @see GLStructure
 * @author Theo Kamp
 */

public class Texture2D extends GLStructure {

    public final int width;
    public final int height;

    /**
     * Constructor
     * @param glApiId OpenGL id of the texture
     * @param width Width of the texture
     * @param height Height of the texture
     */
    public Texture2D(int glApiId, int width, int height) {
        super(glApiId);
        this.width  = width;
        this.height = height;
    }

    /**
     * @author Theo Kamp
     */
    @Override
    public void makeActive() {
        glBindTexture(GL_TEXTURE_2D, glApiId);
    }

    /**
     * @author Theo Kamp
     */
    @Override
    public void clearFromDevice() {
        glDeleteTextures(glApiId);
    }
}
