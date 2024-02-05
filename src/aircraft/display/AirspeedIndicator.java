package aircraft.display;


import graphics.Mesh;
import math.UnitConversion;
import math.Vec3;

import java.util.List;

public class AirspeedIndicator extends Display {
    public AirspeedIndicator(int id, Vec3 relPos, Vec3 relRot) {
        super(id, relPos, relRot, null);

        DisplayElement background = new DisplayElement()
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void Update() {
        getDisplayElement(1).setRotation(
                UnitConversion.mpsToKnots(
                        aircraft.getVelocity()) * 1.9565f);
    }

}
