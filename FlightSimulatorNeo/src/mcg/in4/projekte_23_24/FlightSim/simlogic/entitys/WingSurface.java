package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys;

public class WingSurface extends Surface {
    final float ASPECT_RATIO = 7.40f; // TODO magic number
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
