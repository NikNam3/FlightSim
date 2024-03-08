package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.surfaces;

import mcg.in4.projekte_23_24.FlightSim.engine.Window;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class ControlledWingSurface extends WingSurface {
    private float flapAngle; // in rad

    private final int increaseAngleKey;
    private final int decreaseAngleKey;

    final float MAX_FLAP_ANGLE;
    final float MIN_FLAP_ANGLE;

    public final float FLAP_MULTIPLIER = (float) (10 * Math.PI / 180); // TODO magic number

    public ControlledWingSurface(float[] normal, float[] chord_line, float surface_area, float minAngle, float maxAngle, int increaseAngleKey, int decreaseAngleKey) {
        super(normal, chord_line, surface_area);

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
            if (aoa < -5*Math.PI/180) {
                aoa = (float) (-5*Math.PI/180);
            }
        }
        //System.out.println("Actual AOA: " + aoa);
        //System.out.println("Effective aoa: " + (aoa + -flapAngle * FLAP_MULTIPLIER));
        double liftCoefficient = 2*Math.PI * ASPECT_RATIO * (aoa + -flapAngle * FLAP_MULTIPLIER) / (ASPECT_RATIO + 2) +0.35;
        //System.out.println("Id: " + hostId + " Lift Coeff: " + liftCoefficient + " AOA: " + aoa + " Flap Angle: " + flapAngle);
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
