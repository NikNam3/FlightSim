package mcg.in4.projekte_23_24.FlightSim.engine.graphics.shader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDeleteShader;

public class ShaderProgramLoader {

    public static void load(String vertexFile, String fragFile, ShaderProgram result){
        String vertSource = fileToString(vertexFile);
        String fragSource = fileToString(fragFile);

        int programId = glCreateProgram();
        int vs = compileShader(GL_VERTEX_SHADER, vertSource);
        int fs = compileShader(GL_FRAGMENT_SHADER, fragSource);
        glAttachShader(programId, vs);
        glAttachShader(programId, fs);
        glLinkProgram(programId);
        glValidateProgram(programId);
        glDeleteShader(vs);
        glDeleteShader(fs);

        result.glId = programId;
    }

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
