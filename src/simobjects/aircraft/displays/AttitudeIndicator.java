package simobjects.aircraft.displays;

import graphics.Mesh;
import math.Mat3;
import math.Math3D;
import math.Vec3;

/**
 * The AttitudeIndicator class handles the Logic behind the AttitudeIndicator Display
 * It extends the Display class and implements the Update method
 * The AttitudeIndicator class is used to display the current Attitude of the Aircraft
 *
 * @see Display
 *
 * @author      Nikolas Kühnlein
 * @version     a1.0
 * @since       a1.0
 */

public class AttitudeIndicator extends Display {
    public AttitudeIndicator(Vec3 relPos, Mat3 relRot, Mesh backgroundMesh, Mesh pointerMesh) {
        super(relPos, relRot);

        DisplayElement background = new DisplayElement(backgroundMesh);
        background.setRelPos(new Vec3(0, -1, 0)); // TODO set the correct position of the Gyro Ball

        DisplayElement pointer = new DisplayElement(pointerMesh);

        addDisplayElement(background); // Adds DisplayElement moving background to the Display at the index 0
        addDisplayElement(pointer); // Adds not moving DisplayElement pointer to the Display at the index 1
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void Update() {
        assert Math3D.rotationMatrixToEulerAngles(aircraft.getRelRotation()) != null; // Make sure Math3D funciton returns a value

        getDisplayElement(0).setRelRot(
                Math3D.eulerAnglesToRotationMatrix(new Vec3(
                -Math3D.rotationMatrixToEulerAngles(aircraft.getRelRotation()).x,
                -Math3D.rotationMatrixToEulerAngles(aircraft.getRelRotation()).y,
                -Math3D.rotationMatrixToEulerAngles(aircraft.getRelRotation()).z
                )

        )); // TODO add the correct rotation

        // Sets the Rotation of the Gyro to zero
    }
}
