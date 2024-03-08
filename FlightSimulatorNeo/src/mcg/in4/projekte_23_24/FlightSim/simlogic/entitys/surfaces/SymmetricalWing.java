package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.surfaces;

public class SymmetricalWing extends WingSurface{
    public SymmetricalWing(float[] normal, float[] chord_line, float surface_area) {
        super(normal, chord_line, surface_area);
    }

    @Override
    public float calculateLiftCoefficient(float aoa){
        return (float) (2*Math.PI * ASPECT_RATIO * aoa / (ASPECT_RATIO + 2));
    }
    @Override
    public float calculateDragCoefficient(float aoa){
        return (float) (CD_MIN + calculateLiftCoefficient(aoa)*calculateLiftCoefficient(aoa) / (Math.PI * ASPECT_RATIO * OSWALD_FACTOR));
    }
    @Override
    public void onUpdate(float deltaTime){
    }
}
