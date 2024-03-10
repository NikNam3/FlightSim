package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.surfaces;

/**
 * This class represents a WingSurface of an Object in the Simulation. It contains the normal vector and the surface area of the WingSurface.
 * As this is a Surface it extends the Surface class.
 * @see Surface
 * @see WingSurface
 * @version 1.0
 * @since 1.0
 * @author Nikolas Kühnlein
 */

public class SymmetricalWing extends WingSurface{
    public SymmetricalWing(float[] normal, float surface_area) {
        super(normal, surface_area);
    }

    /**
     * This method calculates the lift coefficient of the WingSurface.
     * Cl = 2 * PI * ASPECT_RATIO * aoa / (ASPECT_RATIO + 2)
     * @param aoa The angle of attack of the WingSurface.
     * @return The lift coefficient of the WingSurface.
     */
    @Override
    public float calculateLiftCoefficient(float aoa){
        return (float) (2*Math.PI * ASPECT_RATIO * aoa / (ASPECT_RATIO + 2));
    }

    /**
     * This method calculates the drag coefficient of the WingSurface.
     * Cd = CD_MIN + Cl^2 / (PI * ASPECT_RATIO * OSWALD_FACTOR)
     * @param aoa The angle of attack of the WingSurface.
     * @return The drag coefficient of the WingSurface.
     */
    @Override
    public float calculateDragCoefficient(float aoa){
        return (float) (CD_MIN + calculateLiftCoefficient(aoa)*calculateLiftCoefficient(aoa) / (Math.PI * ASPECT_RATIO * OSWALD_FACTOR));
    }
}
