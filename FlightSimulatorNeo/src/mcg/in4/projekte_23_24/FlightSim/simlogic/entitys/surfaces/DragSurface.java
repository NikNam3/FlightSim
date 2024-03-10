package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.surfaces;



public class DragSurface extends Surface {
    final float DRAG_COEFFICIENT; // TODO magic number

    public DragSurface(float[] normal, float surface_area, float dragCoefficient) {
        super(normal, surface_area);
        DRAG_COEFFICIENT = dragCoefficient;
    }
    @Override
    public float calculateLiftCoefficient(float aoa){
        return 0;
    }
    @Override
    public float calculateDragCoefficient(float aoa){
        return DRAG_COEFFICIENT;
    }

}
