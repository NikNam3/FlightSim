package mcg.in4.projekte_23_24.FlightSim.simlogic.components;

import mcg.in4.projekte_23_24.FlightSim.engine.Math3d;

public class Transform {
    public float[][] matrixOffset;
    public float[][] matrixRotate;
    public Transform(){
        matrixOffset = Math3d.mat4();
        matrixRotate = Math3d.mat4();
    }
}
