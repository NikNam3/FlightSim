package mcg.in4.projekte_23_24.FlightSim.engine.postprocessing;

import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.LightingEnvironment;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.FrameBuffer;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.Program;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.Texture2D;
import mcg.in4.projekte_23_24.FlightSim.engine.loading.ProgramLoader;

import static org.lwjgl.opengl.GL41.*;
import static mcg.in4.projekte_23_24.FlightSim.engine.base.Math3d.*;

/**
 * Class for rendering the atmosphere effect<br>
 * The atmosphere effect is a post processing effect that simulates the atmosphere of a planet<br>
 * The effect is applied to the screen after all other rendering calls<br>
 *
 * @version 1.0
 * @since 1.0
 * @author Vincent Lahmann
 */
public class AtmosphereEffect {
    private static int quadVaoId;
    private static Program   shaderProgram;
    private static FrameBuffer frameBuffer;
    private static Texture2D colorBufferTexture;
    private static Texture2D depthBufferTexture;
    private static int lastWidth;
    private static int lastHeight;

    /**
     * Initializes variables<br>Must be the first function called in this class
     *
     * @author Vincent Lahmann
     */
    public static void init(){
        shaderProgram = ProgramLoader.load("shaders/atmosphere/atmosphere_vertex.glsl", "shaders/atmosphere/atmosphere_fragment.glsl");
        frameBuffer   = new FrameBuffer();
        createQuadVertexArray();
    }

    /**
     * Stops all rendering calls from being drawn to the screen<br>
     * Everything that would be drawn to the screen will be rendered into an internal buffer<br>
     * Method must be called each frame before calling apply
     * @param width Current width of content area (usually width of window)
     * @param height Current height of content area (usually height of window)
     *
     * @author Vincent Lahmann
     */
    public static void listen(int width, int height){
        frameBuffer.makeActive();
        frameBuffer.clearBuffers();
        // reattach buffers if size has changed
        if(width != lastWidth || height != lastHeight){
            destroyBufferTextures();
            colorBufferTexture = frameBuffer.attachmentColorBuffer(width, height, 0);
            depthBufferTexture = frameBuffer.attachDepthBuffer(width, height);

            lastWidth  = width;
            lastHeight = height;
        }
        shaderProgram.setFloat("u_aspect_ratio", width / (float)height);
    }

    /**
     * Reverts listen<br>Everything after calling this method will be drawn to the screen
     *
     * @author Vincent Lahmann
     */
    public static void keep(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    /**
     * Applies an Atmosphere effect over everything that was rendered after listen()
     * @param lightingEnvironment The lighting environment
     * @param cameraModelMatrix The world space model matrix of the camera
     * @param nearClip The distance of the near clipping plane used for all other rendering calls
     * @param farClip The distance of the far clipping plane used for all other rendering calls
     * @param fov The FOV of the camera in radians
     *
     * @author Vincent Lahmann
     */
    public static void apply(LightingEnvironment lightingEnvironment, float[][] cameraModelMatrix, float nearClip, float farClip, float fov){
        keep();
        glDepthMask(false);
        glDisable(GL_DEPTH_TEST);

        glBindVertexArray(quadVaoId);
        shaderProgram.makeActive();

        float[] cameraPos = vec3(cameraModelMatrix[0][3], cameraModelMatrix[1][3], cameraModelMatrix[2][3]);

        glActiveTexture(GL_TEXTURE0);
        colorBufferTexture.makeActive();
        glActiveTexture(GL_TEXTURE1);
        depthBufferTexture.makeActive();

        shaderProgram.setMat4("u_camera", cameraModelMatrix);

        shaderProgram.setInt("u_color_buffer", 0);
        shaderProgram.setInt("u_depth_buffer", 1);

        shaderProgram.setVec3("u_from_sun", lightingEnvironment.getSunLightDirection());
        shaderProgram.setVec3("u_camera_pos", cameraPos);

        shaderProgram.setFloat("u_fov", fov);
        shaderProgram.setFloat("u_near", nearClip);
        shaderProgram.setFloat("u_far",  farClip);

        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);
        glUseProgram(0);
        glActiveTexture(GL_TEXTURE0);

        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
    }


    /**
     * Creates a quad vertex array
     * @author Vincent Lahmann
     */
    private static void createQuadVertexArray(){
        quadVaoId = glGenVertexArrays();
        glBindVertexArray(quadVaoId);
        float[] posData = {-1,-1,-1, 1, 1, 1,-1,-1, 1, 1, 1,-1};
        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glEnableVertexAttribArray(0);
        glBufferData(GL_ARRAY_BUFFER, posData , GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    /**
     * Destroys the Buffered Textures
     * @author Vincent Lahmann
     */
    private static void destroyBufferTextures(){
        if(colorBufferTexture != null)
            colorBufferTexture.clearFromDevice();
        if(depthBufferTexture != null)
            depthBufferTexture.clearFromDevice();
    }
}
