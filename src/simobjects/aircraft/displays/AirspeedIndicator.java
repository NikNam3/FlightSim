package simobjects.aircraft.displays;


import graphics.Mesh;
import math.UnitConversion;
import math.Vec3;
import simobjects.aircraft.Aircraft;

/**
 * The AirspeedIndicator class handles the Logic behind the AirspeedIndicator Display
 * It extends the Display class and implements the Update method
 * The AirspeedIndicator class is used to display the current Airspeed of the Aircraft
 *
 * @see Display
 *
 * @author      Nikolas Kühnlein
 * @version     a1.0
 * @since       a1.0
 */
public class AirspeedIndicator extends Display {
    public AirspeedIndicator(Vec3 relPos, Vec3 relRot, Mesh backgroundMesh, Mesh pointerMesh) {
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
        getDisplayElement(1).setRotation( // Sets the Rotation of the Pointer to the current Airspeed
                UnitConversion.mpsToKnots(
                        aircraft.getAirSpeed()) * 1.9565f);
    }

}
