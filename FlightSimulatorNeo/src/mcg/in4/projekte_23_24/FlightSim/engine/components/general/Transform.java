package mcg.in4.projekte_23_24.FlightSim.engine.components.general;

import mcg.in4.projekte_23_24.FlightSim.engine.base.Math3d;

/**
 *  Component to store position and rotation data for an entity
 * @version 1.0
 * @since 1.0
 *
 * @author Maximo Tewes
 */

public class Transform {
    /**
     *  4x4 Matrix to store rotation
     *
     */
    public float[][] matrixRotation;

    /**
     *  4x4 Matrix to store position
     */
    public float[][] matrixPosition;

    /**
     * Default constructor
     * @author Maximo Tewes
     */
    public Transform(){
        this(Math3d.mat4(), Math3d.mat4());
    }

    /**
     * Constructor with parameters
     * @param matrixRotation 4x4 Matrix to store rotation
     * @param matrixPosition 4x4 Matrix to store position
     * @author Maximo Tewes
     */
    public Transform(float[][] matrixRotation, float[][] matrixPosition) {
        this.matrixRotation = matrixRotation;
        this.matrixPosition = matrixPosition;
    }
}
