package mcg.in4.projekte_23_24.FlightSim.engine.graphics;

import mcg.in4.projekte_23_24.FlightSim.engine.graphics.shader.ShaderProgram;
import mcg.in4.projekte_23_24.FlightSim.engine.graphics.shader.ShaderProgramLoader;
import mcg.in4.projekte_23_24.FlightSim.engine.structures.Texture2D;

import static org.lwjgl.opengl.GL41.*;

public class RenderUtils {

    private static int quadVaoId;
    private static ShaderProgram shaderProgram;

    public static void init(){
        quadVaoId = glGenVertexArrays();
        glBindVertexArray(quadVaoId);

        float[] posData = {
                -1, -1,
                -1, 1,
                1, 1,

                -1, -1,
                1, 1,
                1, -1
        };

        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glEnableVertexAttribArray(0);
        glBufferData(GL_ARRAY_BUFFER, posData , GL_STATIC_DRAW);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);

        shaderProgram = new ShaderProgram(0);
        ShaderProgramLoader.load("shaders/screen_vertex.txt", "shaders/screen_fragment.txt", shaderProgram);
    }

    public static void renderScreenQuad(Texture2D texture){
        glBindVertexArray(quadVaoId);
        glUseProgram(shaderProgram.glId);
        texture.bind();
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindVertexArray(0);
        glUseProgram(0);
    }
}
