package mcg.in4.projekte_23_24.FlightSim.simlogic.components;

import mcg.in4.projekte_23_24.FlightSim.engine.Math3d;

public class Transform {
    public float[][] matrixOffset;
    public float[][] matrixRotate;
    public Transform(){
        matrixOffset = Math3d.mat4();
        matrixRotate = Math3d.mat4();
    }
    public Transform(float[] offset) {
        matrixOffset = Math3d.mat4();
        matrixRotate = Math3d.mat4();
        matrixOffset[0][3] = offset[0];
        matrixOffset[1][3] = offset[1];
        matrixOffset[2][3] = offset[2];
    }
}
