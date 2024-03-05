package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys;

public class Surface extends EntityBehavior{
    public final float[] NORMAL; // Normalized vector
    public final float SURFACE_AREA; // TODO magic number

    public Surface(float[] normal, float surface_area) {
        super();
        NORMAL = normal;
        SURFACE_AREA = surface_area;
    }

    public float calculateLiftCoefficient(float aoa){
        return 0;
    }
    public float calculateDragCoefficient(float aoa){
        return 0;
    }
}
