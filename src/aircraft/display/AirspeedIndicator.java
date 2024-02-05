package aircraft.display;


import graphics.Mesh;
import math.UnitConversion;

public class AirspeedIndicator extends Display {
    /**
     * Creates a new AirspeedIndicator
     */
    public AirspeedIndicator() {
        super(null);
        addDisplayElement(
                new DisplayElement(
                        new Mesh(

                        ), 0));
        addDisplayElement(
                new DisplayElement(
                        new Mesh(

                        ), 1));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void Update() {
        getDisplayElement(1).setRotation(
                UnitConversion.mpsToKnots(
                        aircraft.getVelocity()));
    }

}
