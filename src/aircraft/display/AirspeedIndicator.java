package aircraft.display;


import graphics.Mesh;
import math.UnitConversion;
import math.Vec3;


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
                        aircraft.getVelocity()) * 1.9565f);
    }

}
