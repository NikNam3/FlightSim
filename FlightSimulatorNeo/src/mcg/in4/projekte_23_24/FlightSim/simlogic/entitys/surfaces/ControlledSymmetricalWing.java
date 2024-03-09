package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.surfaces;

import mcg.in4.projekte_23_24.FlightSim.engine.Window;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class ControlledSymmetricalWing extends SymmetricalWing {
    private float flapAngle; // in rad

    private final int increaseAngleKey;
    private final int decreaseAngleKey;

    final float MAX_FLAP_ANGLE;
    final float MIN_FLAP_ANGLE;

    public final float FLAP_MULTIPLIER = (float) (10 * Math.PI / 180); // TODO magic number

    public ControlledSymmetricalWing(float[] normal, float[] chord_line, float surface_area, float minAngle, float maxAngle, int increaseAngleKey, int decreaseAngleKey) {
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
            //System.out.println("SymStall");
            if (aoa < 0) {
                aoa = 0;
            }
        } else if (aoa < -15*Math.PI/180) {
            // Stall
            aoa = (float) (-15*Math.PI/180 - (aoa + 15*Math.PI/180));
            //System.out.println("SymStall");
            if (aoa > 0) {
                aoa = 0;
            }
        }

        double liftCoefficient = 2*Math.PI * ASPECT_RATIO * (aoa + -flapAngle * FLAP_MULTIPLIER) / (ASPECT_RATIO + 2);
        return (float) liftCoefficient;
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
