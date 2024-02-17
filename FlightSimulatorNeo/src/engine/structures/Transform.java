package engine.structures;

import engine.Math3d;

public class Transform {
    public float[][] matrixOffset;
    public float[][] matrixRotate;
    public Transform(){
        matrixOffset = Math3d.mat4();
        matrixRotate = Math3d.mat4();
    }
}
