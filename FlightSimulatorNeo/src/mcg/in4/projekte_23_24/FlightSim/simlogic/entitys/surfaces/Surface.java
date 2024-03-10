package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.surfaces;

import mcg.in4.projekte_23_24.FlightSim.engine.Math3d;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.EntityBehavior;

/**
 * This class represents a Surface of an Object in the Simulation. It contains the normal vector and the surface area of the Surface.
 * As this is an Entity it extends the EntityBehavior class.
 * This class is abstract and should be extended by specific Surface classes.
 * @see EntityBehavior
 * @version 1.0
 * @since 1.0
 * @author Nikolas Kühnlein
 */

public abstract class Surface {
    public final float[] NORMAL; // Normalized vector
    public final float SURFACE_AREA; // TODO magic number

    public Surface(float[] normal, float surface_area) {
        super();
        NORMAL = Math3d.normalize(normal);
        SURFACE_AREA = surface_area;
    }

    public abstract float calculateLiftCoefficient(float aoa);

    public abstract float calculateDragCoefficient(float aoa);
}
