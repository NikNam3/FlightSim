package mcg.in4.projekte_23_24.FlightSim.engine.flightphysics;

/**
 * Base class for all flight surfaces
 *
 *
 * @version 1.0
 * @since 1.0
 * @author Nikolas Kühnlein
 */
public abstract class Surface {
    public final float[] normal;
    public final float[] offset;
    public final float area;

    /**
     * Constructor
     * @param offset Offset of the surface
     * @param normal Normal of the surface
     * @param area Area of the surface
     *
     * @author Nikolas Kühnlein
     */
    public Surface(float[] offset, float[] normal, float area){
        this.offset = offset;
        this.normal = normal;
        this.area = area;
    }

    /**
     * Method to get the coefficient of drag
     * @param angleOfAttack Angle of attack
     * @return Coefficient of drag
     *
     * @author Nikolas Kühnlein
     */
    public abstract float getCoefficientOfDrag(float angleOfAttack);
}
