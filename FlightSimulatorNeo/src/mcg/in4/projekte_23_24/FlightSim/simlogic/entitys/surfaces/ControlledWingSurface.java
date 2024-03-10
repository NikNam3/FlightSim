package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.surfaces;

import mcg.in4.projekte_23_24.FlightSim.engine.Window;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

/**
 * This class represents a controlled wing surface of an Aircraft. It extends the WingSurface class and adds the ability to control the flap angle of the wing surface.
 * The flap angle can be controlled by the user using the increaseAngleKey and decreaseAngleKey.
 * The flap angle is limited by the minAngle and maxAngle.
 * The flap angle is used to calculate the lift coefficient of the wing surface.
 *
 * @version 1.0
 * @since 1.0
 * @see WingSurface
 * @author Nikolas Kühnlein
 */


public class ControlledWingSurface extends WingSurface {
    private float flapAngle; // in rad

    private final int increaseAngleKey;
    private final int decreaseAngleKey;

    final float MAX_FLAP_ANGLE;
    final float MIN_FLAP_ANGLE;

    public final float FLAP_MULTIPLIER = (float) (10 * Math.PI / 180); // TODO magic number

    /**
     * Constructor for the ControlledWingSurface
     * @param normal The normal of the wing
     * @param surface_area The surface area of the wing
     * @param minAngle The minimum flap angle
     * @param maxAngle The maximum flap angle
     * @param increaseAngleKey The key to increase the flap angle
     * @param decreaseAngleKey The key to decrease the flap angle
     */
    public ControlledWingSurface(float[] normal, float surface_area, float minAngle, float maxAngle, int increaseAngleKey, int decreaseAngleKey) {
        super(normal, surface_area);

        this.increaseAngleKey = increaseAngleKey;
        this.decreaseAngleKey = decreaseAngleKey;

        MAX_FLAP_ANGLE = maxAngle;
        MIN_FLAP_ANGLE = minAngle;
    }

    /**
     * This method calculates the lift coefficient of the wing segment
     * The lift coefficient is calculated using the formula:
     * cl = 2 * pi * aspect_ratio * aoa / (aspect_ratio + 2) + 0.35
     * The flap angle is used to modify the effective aoa
     *
     * If the angle of attack is greater than 15 degrees, the wing is in a stall condition.
     * The angle of attack is then limited to 15 degrees and the effective angle of attack is modified to simulate the stall condition.
     *
     * If the effective angle of attack is less than -5 degrees, the wing is in a deep stall condition.
     * The effective angle of attack is then limited to -5 degrees.
     *
     *
     * @param aoa The angle of attack of the wing segment
     *            The angle of attack is in radians
     *            The angle of attack is the angle between the relative wind and the chord line of the wing segment
     *
     * @return The lift coefficient of the wing segment
     * @author Nikolas Kühnlein
     */
    @Override
    public float calculateLiftCoefficient(float aoa){

        if (aoa > 15*Math.PI/180) {
            // Stall
            aoa = (float) (15*Math.PI/180 - (aoa - 15*Math.PI/180));
            //System.out.println("Stall");
            if (aoa < -5*Math.PI/180) {
                aoa = (float) (-5*Math.PI/180);
            }
        }
        //System.out.println("Actual AOA: " + aoa);
        //System.out.println("Effective aoa: " + (aoa + -flapAngle * FLAP_MULTIPLIER));
        double liftCoefficient = 2*Math.PI * ASPECT_RATIO * (aoa + -flapAngle * FLAP_MULTIPLIER) / (ASPECT_RATIO + 2) + 0.35f;
        //System.out.println("Id: " + hostId + " Lift Coeff: " + liftCoefficient + " AOA: " + aoa + " Flap Angle: " + flapAngle);
        return (float) liftCoefficient;
    }

    /**
     * This method calculates the drag coefficient of the wing segment
     * The drag coefficient is calculated using the formula:
     * cd = cd_min + cl^2 / (pi * aspect_ratio * oswald_factor)
     *
     *
     * @param aoa The angle of attack of the wing segment
     * @return The drag coefficient of the wing segment
     * @author Nikolas Kühnlein
     */
    @Override
    public float calculateDragCoefficient(float aoa){
        double dragCoefficient = CD_MIN + calculateLiftCoefficient(aoa)*calculateLiftCoefficient(aoa) / (Math.PI * ASPECT_RATIO * OSWALD_FACTOR);
        return (float) dragCoefficient;
    }

    /**
     * This method updates the flap angle of the wing surface.
     * The flap angle is updated based on the user input.
     * The flap angle is limited by the minAngle and maxAngle.
     * The flap angle is used to calculate the lift coefficient of the wing surface.
     * If the user presses the increaseAngleKey, the flap angle is set to the maxAngle.
     * If the user presses the decreaseAngleKey, the flap angle is set to the minAngle.
     * If the user does not press any key, the flap angle is set to 0.
     *
     *
     * @param deltaTime The time since the last frame in seconds.
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
