package mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures;

import static org.lwjgl.opengl.GL41.*;

/**
 * Wrapper class for OpenGL shader programs
 */
public class Program extends GLStructure {
    public Program(int glApiId) {
        super(glApiId);
    }

    @Override
    public void makeActive() {
        glUseProgram(glApiId);
    }

    @Override
    public void clearFromDevice() {
        glDeleteProgram(glApiId);
    }

    /**
     * Uploads a uniform float for access on the device
     * @param name name of the uniform variable
     * @param val value of the uniform variable
     */
    public void setFloat(String name, float val){
        makeActive();
        int location = glGetUniformLocation(glApiId, name);
        if(location != -1)
            glUniform1f(location, val);
    }

    /**
     * Uploads a uniform vec3 for access on the device
     * @param name name of the uniform variable
     * @param val value of the uniform variable
     */
    public void setVec3(String name, float[] val){
        makeActive();
        int location = glGetUniformLocation(glApiId, name);
        if(location != -1)
            glUniform3f(location, val[0], val[1], val[2]);
    }

    /**
     * Uploads a uniform float for access on the device
     * @param name name of the uniform variable
     * @param val value of the uniform variable
     */
    public void setInt(String name, int val){
        makeActive();
        int location = glGetUniformLocation(glApiId, name);
        if(location != -1)
            glUniform1i(location, val);
    }

    /**
     * Uploads a uniform 4x4 matrix for access on the device
     * @param name name of the uniform variable
     * @param val value of the uniform variable
     */
    public void setMat4(String name, float[][] val){
        makeActive();
        // convert 2d array to 1d
        float[] matFlat = new float[16];
        for(int idx = 0; idx < 16; idx++){
            int i = idx / 4;
            int j = idx % 4;
            matFlat[idx] = val[i][j];
        }

        // upload 1d array to opengl
        int loc = glGetUniformLocation(glApiId, name);
        if(loc != -1){
            glUniformMatrix4fv(loc, true, matFlat);
        }
    }

}
