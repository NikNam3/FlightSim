package simobjects.aircraft.displays;

import graphics.Mesh;
import math.UnitConversion;
import math.Vec3;

/**
 * The Altimeter is a Display that shows the current Altitude of the Aircraft.
 * It extends the Display class and implements the Update method
 *
 * @see Display
 *
 * @version     a1.0
 * @since       a1.0
 * @author      Nikolas Kühnlein
 */
public class Altimeter extends Display {
    public Altimeter(Vec3 relPos, Vec3 relRot, Mesh backgroundMesh, Mesh longestPointerMesh, Mesh longPointerMesh, Mesh shortPointerMesh) {
        super(relPos, relRot);

        DisplayElement background = new DisplayElement(backgroundMesh);
        DisplayElement longestPointer = new DisplayElement(longestPointerMesh); // Shows the current altitude in 10000ft
        DisplayElement longPointer = new DisplayElement(longPointerMesh); // Shows the current altitude in 1000ft
        DisplayElement shortPointer = new DisplayElement(shortPointerMesh); // Shows the current altitude in 100ft


        addDisplayElement(background); // Adds DisplayElement background to the Display at the index 0
        addDisplayElement(longestPointer); // Adds DisplayElement pointer to the Display at the index 1
        addDisplayElement(longPointer); // Adds DisplayElement pointer to the Display at the index 2
        addDisplayElement(shortPointer); // Adds DisplayElement pointer to the Display at the index 3
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void Update() {
        getDisplayElement(1).setRotation(
                (UnitConversion.mtoFeet(aircraft.getAltitude()) / 10000) * 36
        ); // Sets the Rotation of the Pointer to the current Altitude in 10000ft (36° per 10000ft)
        getDisplayElement(2).setRotation(
                ((UnitConversion.mtoFeet(aircraft.getAltitude()) % 10000) / 1000) * 36 // TODO test
        ); // Sets the Rotation of the Pointer to the current Altitude in 1000ft (36° per 1000ft)
        getDisplayElement(3).setRotation(
                ((UnitConversion.mtoFeet(aircraft.getAltitude()) % 1000) / 100) * 36 // TODO test
        ); // Sets the Rotation of the Pointer to the current Altitude in 100ft (36° per 100ft)
    }
}
