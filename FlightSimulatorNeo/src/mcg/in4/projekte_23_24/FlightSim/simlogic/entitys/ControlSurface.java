package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys;

import mcg.in4.projekte_23_24.FlightSim.engine.Window;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class ControlSurface extends WingSurface {
    float flapAngle; // in rad

    int increaseAngleKey;
    int decreaseAngleKey;

    final float FLAP_MULTIPLIER = 0.5f; // TODO magic number
    final float MAX_FLAP_ANGLE;
    final float MIN_FLAP_ANGLE;

    public ControlSurface(float[] normal, float surface_area, float minAngle, float maxAngle, int increaseAngleKey, int decreaseAngleKey) {
        super(normal, surface_area);

        this.increaseAngleKey = increaseAngleKey;
        this.decreaseAngleKey = decreaseAngleKey;

        MAX_FLAP_ANGLE = maxAngle;
        MIN_FLAP_ANGLE = minAngle;
    }

    @Override
    public float calculateLiftCoefficient(float aoa){


        if (aoa > 15*Math.PI/180) {
            // Stall
            aoa = (float) (15*Math.PI/180 - (aoa - 15*Math.PI/180));
            System.out.println("Stall");
        }
        double liftCoefficient = 2*Math.PI * ASPECT_RATIO * aoa / (ASPECT_RATIO + 2) + 0.35 + -flapAngle * FLAP_MULTIPLIER; // WELP THIS IS HOW THE FLAPS WORK NOW I GUESS;
        return (float) liftCoefficient;
    }
    @Override
    public float calculateDragCoefficient(float aoa){
        double dragCoefficient = CD_MIN + calculateLiftCoefficient(aoa)*calculateLiftCoefficient(aoa) / (Math.PI * ASPECT_RATIO * OSWALD_FACTOR);
        return (float) dragCoefficient;
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
