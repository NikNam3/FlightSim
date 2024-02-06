package simobjects.aircraft.displays;

import graphics.Mesh;
import math.Mat3;
import math.Vec3;

/**
 * The VerticalSpeed class handles the Logic behind the VerticalSpeed Display
 * It extends the Display class and implements the Update method
 * The VerticalSpeed class is used to display the current VerticalSpeed of the Aircraft
 *
 * @see Display
 *
 * @author      Nikolas Kühnlein
 * @version     a1.0
 * @since       a1.0
 */

public class VerticalSpeedIndicator extends Display {
    public VerticalSpeedIndicator(Vec3 relPos, Mat3 relRot, Mesh backgroundMesh, Mesh pointerMesh) {
        super(relPos, relRot);

        DisplayElement background = new DisplayElement(backgroundMesh);
        DisplayElement pointer = new DisplayElement(pointerMesh);

        addDisplayElement(background); // Adds DisplayElement background to the Display at the index 0
        addDisplayElement(pointer); // Adds DisplayElement pointer to the Display at the index 1
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void Update() {
        getDisplayElement(1).setRotation( // Sets the Rotation of the Pointer to the current Vertical Speed
                aircraft.getVerticalSpeed() * 0.36f);
    }
}
