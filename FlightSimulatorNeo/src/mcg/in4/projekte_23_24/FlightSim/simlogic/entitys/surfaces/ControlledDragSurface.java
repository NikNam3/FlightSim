package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.surfaces;

import mcg.in4.projekte_23_24.FlightSim.engine.Window;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class ControlledDragSurface extends DragSurface{
    float flapAngle; // in rad

    int increaseAngleKey;
    int decreaseAngleKey;

    final float FLAP_MULTIPLIER = 1.5f; // TODO magic number
    final float MAX_FLAP_ANGLE;
    final float MIN_FLAP_ANGLE;

    public ControlledDragSurface(float[] normal, float[] chord_line, float surface_area, float dragCoefficient,  float min_flap_angle, float max_flap_angle, int increaseAngleKey, int decreaseAngleKey) {
        super(normal, chord_line, surface_area, dragCoefficient);
        MAX_FLAP_ANGLE = max_flap_angle;
        MIN_FLAP_ANGLE = min_flap_angle;
        this.increaseAngleKey = increaseAngleKey;
        this.decreaseAngleKey = decreaseAngleKey;
    }

    @Override
    public float calculateDragCoefficient(float aoa){
        return DRAG_COEFFICIENT + (1 + FLAP_MULTIPLIER * -flapAngle);
    }
    @Override
    public void onUpdate(float deltaTime){
        long glfwWindow = Window.getGlfwWindowAddress();
        if (glfwGetKey(glfwWindow, increaseAngleKey) == 1) {
            flapAngle = MAX_FLAP_ANGLE;
        }

        else if (glfwGetKey(glfwWindow, decreaseAngleKey) == 1) {
            flapAngle = MIN_FLAP_ANGLE;
        }
        else {
            flapAngle = 0;
        }

    }

}
