package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.surfaces;

import mcg.in4.projekte_23_24.FlightSim.engine.Math3d;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.EntityBehavior;

public class Surface extends EntityBehavior {
    public final float[] NORMAL; // Normalized vector
    public final float SURFACE_AREA; // TODO magic number
    public final float[] CHORD_LINE;

    public Surface(float[] normal, float[] chord_line, float surface_area) {
        super();
        NORMAL = Math3d.normalize(normal);
        SURFACE_AREA = surface_area;
        CHORD_LINE = chord_line;

    }

    public float calculateLiftCoefficient(float aoa){
        return 0;
    }
    public float calculateDragCoefficient(float aoa){
        return 0;
    }
}
