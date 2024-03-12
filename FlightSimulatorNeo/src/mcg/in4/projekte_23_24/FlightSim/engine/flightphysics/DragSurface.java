package mcg.in4.projekte_23_24.FlightSim.engine.flightphysics;

public class DragSurface extends Surface{
    public DragSurface(float[] offset, float[] normal, float area) {
        super(offset, normal, area);
    }

    @Override
    public float getCoefficientOfDrag(float angleOfAttack) {
        return 0;
    }
}
