package mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures;

import static org.lwjgl.opengl.GL41.*;

/**
 * Class to represent a FrameBuffer
 * Extends GLStructure as it is a GL object
 *
 * @version 1.0
 * @since 1.0
 * @author Vincent Lahmann
 */
public class FrameBuffer extends GLStructure{
    public FrameBuffer() {
        super(glGenFramebuffers());
    }

    /**
     * Creates and attaches an empty 8-Bit RGB texture
     * @param width Width of the texture
     * @param height Height of the texture
     * @param slotIdx Index of the attachment slot
     * @return The attached texture
     *
     * @author Vincent Lahmann
     */
    public Texture2D attachmentColorBuffer(int width, int height, int slotIdx){
        // create the texture
        Texture2D texture = new Texture2D(glGenTextures(), width, height);
        texture.makeActive();
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        // attach it to the fb
        makeActive();
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + slotIdx, GL_TEXTURE_2D, texture.glApiId, 0);
        return texture;
    }

    /**
     * Creates and attaches an empty float32 depth texture as the depth buffer
     * @param width width of the texture
     * @param height height of the texture
     * @return The attached texture
     *
     * @author Vincent Lahmann
     */
    public Texture2D attachDepthBuffer(int width, int height){
        // create the texture
        Texture2D texture = new Texture2D(glGenTextures(), width, height);
        texture.makeActive();
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32F, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, 0);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        // attach it to the fb
        makeActive();
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, texture.glApiId, 0);

        return texture;
    }


    /**
     * Clears all the buffers
     * @author Vincent Lahmann
     */
    public void clearBuffers(){
        makeActive();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    /**
     * Makes the FrameBuffer active
     * @author Vincent Lahmann
     */
    @Override
    public void makeActive() {
        glBindFramebuffer(GL_FRAMEBUFFER, glApiId);
    }

    /**
     * Clears the FrameBuffer from the GPU
     * @author Vincent Lahmann
     */
    @Override
    public void clearFromDevice() {
        glDeleteFramebuffers(glApiId);
    }
}
