package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.surfaces;

import mcg.in4.projekte_23_24.FlightSim.engine.Window;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

/**
 * This class represents a SymmetricalWing with the ability to control the flap angle.
 * The flap angle can be controlled by the user using the increaseAngleKey and decreaseAngleKey.
 * The flap angle is limited by the minAngle and maxAngle.
 * The flap angle is used to calculate the lift coefficient of the wing surface.
 *
 * @see SymmetricalWing
 * @author Nikolas Kühnlein
 * @version 1.0
 * @since 1.0
 */


public class ControlledSymmetricalWing extends SymmetricalWing {
    private float flapAngle; // in rad

    private final int increaseAngleKey;
    private final int decreaseAngleKey;

    final float MAX_FLAP_ANGLE;
    final float MIN_FLAP_ANGLE;

    public final float FLAP_MULTIPLIER = (float) (10 * Math.PI / 180); // TODO magic number


    /**
     * Constructor for the ControlledSymmetricalWing
     * @param normal The normal of the wing
     * @param surface_area The surface area of the wing
     * @param minAngle The minimum flap angle
     * @param maxAngle The maximum flap angle
     * @param increaseAngleKey The key to increase the flap angle
     * @param decreaseAngleKey The key to decrease the flap angle
     *
     * @author Nikolas Kühnlein
     */
    public ControlledSymmetricalWing(float[] normal, float surface_area, float minAngle, float maxAngle, int increaseAngleKey, int decreaseAngleKey) {
        super(normal, surface_area);
        this.increaseAngleKey = increaseAngleKey;
        this.decreaseAngleKey = decreaseAngleKey;

        MAX_FLAP_ANGLE = maxAngle;
        MIN_FLAP_ANGLE = minAngle;
    }

    /**
     * This method calculates the lift coefficient of the wing segment
     * The lift coefficient is calculated using the formula:
     * cl = 2 * pi * aspect_ratio * aoa / (aspect_ratio + 2)
     * If the angle of attack is greater than 15 degrees or smaller than -15 degrees, the lift coefficient will be reduced because of the stall effect
     * The Stall behavior is greatly simplified and does not represent the real behavior of a wing
     * @param aoa The angle of attack
     * @return The lift coefficient
     * @author Nikolas Kühnlein
     */

    @Override
    public float calculateLiftCoefficient(float aoa){
        // Stall+
        if (aoa > 15*Math.PI/180) {

            aoa = (float) (15*Math.PI/180 - (aoa - 15*Math.PI/180));
            if (aoa < 0) {
                aoa = 0;
            }
        } else if (aoa < -15*Math.PI/180) { // Stall-

            aoa = (float) (-15*Math.PI/180 - (aoa + 15*Math.PI/180));
            if (aoa > 0) {
                aoa = 0;
            }
        }

        // Cl = 2 * pi * aspect_ratio * effective_aoa / (aspect_ratio + 2)
        double liftCoefficient = 2*Math.PI * ASPECT_RATIO * (aoa + -flapAngle * FLAP_MULTIPLIER) / (ASPECT_RATIO + 2);
        return (float) liftCoefficient;
    }


    /**
     * This method updates the flap angle of the wing segment
     * The flap angle is updated using the keys defined in the constructor
     * If the increaseAngleKey is pressed, the flap angle will be set to the maximum flap angle
     * If the decreaseAngleKey is pressed, the flap angle will be set to the minimum flap angle
     * If no key is pressed, the flap angle will be set to 0
     * @param deltaTime The time since the last frame
     *                  This parameter is not used in this method
     *                  It is only there to override the onUpdate method of the Surface class
     *
     * @author Nikolas Kühnlein
     */

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
