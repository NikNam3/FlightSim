package mcg.in4.projekte_23_24.FlightSim.engine.loading;

import mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures.Program;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

/**
 * Class for loading a shader program
 * @version 1.0
 * @since 1.0
 * @see Program
 * @author Vincent Lahmann
 */
public class ProgramLoader {
    public static Program load(String vertexFile, String fragmentFile){
        String vertSource = fileToString(vertexFile);
        String fragSource = fileToString(fragmentFile);

        int programId = glCreateProgram();
        int vs = compileShader(GL_VERTEX_SHADER, vertSource);
        int fs = compileShader(GL_FRAGMENT_SHADER, fragSource);
        glAttachShader(programId, vs);
        glAttachShader(programId, fs);
        glLinkProgram(programId);
        glValidateProgram(programId);
        glDeleteShader(vs);
        glDeleteShader(fs);

        return new Program(programId);
    }

    /**
     * Compiles a shader from GLSL source code
     * @param type Type of shader
     * @param source Source code
     * @return Shader id
     *
     * @author Vincent Lahmann
     */

    private static int compileShader(int type, String source) {
        int shaderId = glCreateShader(type);
        glShaderSource(shaderId, source);
        glCompileShader(shaderId);
        int[] compileResult = new int[1];
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, compileResult);
        if(compileResult[0] == GL_FALSE) {
            String log = glGetShaderInfoLog(shaderId);
            System.err.println(log);
            return 0;
        }
        return shaderId;
    }

    /**
     * Reads a file and returns its content as string
     * @param file Path of file
     * @return Content of file
     *
     * @author Vincent Lahmann
     */
    private static String fileToString(String file){
        String lines = null;
        try {
            lines = Files.readString(Paths.get(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

}
