package mcg.in4.projekte_23_24.FlightSim.engine.graphics.shader;

import static org.lwjgl.opengl.GL41.*;

public class ShaderProgram {
    public int glId;

    public ShaderProgram(int glId){
        this.glId = glId;
    }

    public void uploadMat4(String name, float[][] mat){
        glUseProgram(glId);
        float[] matFlat = new float[16];
        for(int idx = 0; idx < 16; idx++){
            int i = idx / 4;
            int j = idx % 4;
            matFlat[idx] = mat[i][j];
        }
        int loc = glGetUniformLocation(glId, name);
        if(loc != -1){
            glUniformMatrix4fv(loc, true, matFlat);
        }
    }

    public void uploadInt(String name, int i){
        glUseProgram(glId);
        int loc = glGetUniformLocation(glId, name);
        if(loc != -1){
            glUniform1i(loc, i);
        }
    }

    public void uploadFloat(String name, float i){
        glUseProgram(glId);
        int loc = glGetUniformLocation(glId, name);
        if(loc != -1){
            glUniform1f(loc, i);
        }
    }
}
