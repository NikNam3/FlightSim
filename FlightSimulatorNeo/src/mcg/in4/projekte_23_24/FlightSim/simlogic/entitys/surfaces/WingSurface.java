package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.surfaces;

/**
 * This class represents a WingSurface of an Object in the Simulation. It contains the normal vector and the surface area of the WingSurface.
 * Further it contains the aspect ratio, the minimum drag coefficient and the Oswald factor of the WingSurface.
 * 
 * As this is a Surface it extends the Surface class.
 * @see Surface
 * @version 1.0
 * @since 1.0
 * @author Nikolas Kühnlein
 */
public class WingSurface extends Surface {
    final float ASPECT_RATIO = 7.37f; // TODO magic number
    final float CD_MIN = 0.0270f; // TODO magic number
    final float OSWALD_FACTOR = 0.80f; // TODO magic number

    public WingSurface(float[] normal, float surface_area) {
        super(normal, surface_area);
    }
    @Override
    public float calculateLiftCoefficient(float aoa){
        double liftCoefficient = 2*Math.PI * ASPECT_RATIO * aoa / (ASPECT_RATIO + 2);
        return (float) liftCoefficient;
    }
    @Override
    public float calculateDragCoefficient(float aoa){
        double dragCoefficient = CD_MIN + calculateLiftCoefficient(aoa)*calculateLiftCoefficient(aoa) / (Math.PI * ASPECT_RATIO * OSWALD_FACTOR);
        return (float) dragCoefficient;
    }
}
