package mcg.in4.projekte_23_24.FlightSim.engine.flightphysics;

/**
 * Class to store surface data
 * A drag surface is a surface that only creates drag and no lift
 *
 * Note: This class is not implemented in the current version of the game
 * @version 1.0
 *
 * @author Nikolas Kühnlein
 */
public class DragSurface extends Surface{
    public DragSurface(float[] offset, float[] normal, float area) {
        super(offset, normal, area);
    }

    @Override
    public float getCoefficientOfDrag(float angleOfAttack) {
        return 0;
    }
}
