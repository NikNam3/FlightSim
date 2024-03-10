package mcg.in4.projekte_23_24.FlightSim.simlogic.components;

import mcg.in4.projekte_23_24.FlightSim.engine.Math3d;

/**
 * This class is a Component of an Entity. It contains the Transform of the Entity.
 * The Transform contains the matrixOffset and the matrixRotate of the Entity.
 * The matrixOffset is the translation matrix of the Entity.
 * The matrixRotate is the rotation matrix of the Entity.
 * @author Vincent Lahmann
 * @version 1.0
 * @since 1.0
 */


public class Transform {
    public float[][] matrixOffset;
    public float[][] matrixRotate;

    /**
     * This constructor creates a Transform with the default values.
     * @author Vincent Lahmann
     */
    public Transform(){
        matrixOffset = Math3d.mat4();
        matrixRotate = Math3d.mat4();
    }


    /**
     * This constructor creates a Transform with the given offset.
     * @param offset is the offset for the world 0 of the Transform
     * @author Vincent Lahmann
     */
    public Transform(float[] offset) {
        matrixOffset = Math3d.mat4();
        matrixRotate = Math3d.mat4();
        matrixOffset[0][3] = offset[0];
        matrixOffset[1][3] = offset[1];
        matrixOffset[2][3] = offset[2];
    }
}
