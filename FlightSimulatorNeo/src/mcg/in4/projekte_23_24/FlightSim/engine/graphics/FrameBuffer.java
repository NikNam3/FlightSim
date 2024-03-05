package mcg.in4.projekte_23_24.FlightSim.engine.graphics;

import mcg.in4.projekte_23_24.FlightSim.engine.structures.Texture2D;
import org.lwjgl.opengl.GL41;

import static org.lwjgl.opengl.GL41.*;

public class FrameBuffer {
    private int glId;

    public FrameBuffer(){
        glId = glGenFramebuffers();
        bind();
    }

    public void clear(){
        bind();
        GL41.glClear(GL41.GL_COLOR_BUFFER_BIT | GL41.GL_DEPTH_BUFFER_BIT);
    }

    public void attachTexture2D(Texture2D texture2D, int attachmentIdx){
        bind();
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + attachmentIdx, GL_TEXTURE_2D, texture2D.glId, 0);
    }

    public void attachDepthBufferTexture(Texture2D texture2D){
        bind();
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, texture2D.glId, 0);
    }

    public void bind(){
        glBindFramebuffer(GL_FRAMEBUFFER, glId);
    }

    public static void unbind(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void destroy(){
        glDeleteFramebuffers(glId);
    }
}
