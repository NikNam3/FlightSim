package mcg.in4.projekte_23_24.FlightSim.engine.flightphysics;


/**
 * Class to store wing data
 * Wing is a type of surface
 * It adds values for:
 * - Aspect ratio
 * - Type (normal or symmetric)
 * - Flap angle
 * - Flap multiplier
 * - Oswald factor
 * - Minimum drag coefficient
 *
 * @version 1.0
 * @since 1.0
 * @author Nikolas Kühnlein
 */


public class Wing extends Surface{
    public final float aspectRatio;
    public final float type;

    public float flapAngle;

    public static final float FLAP_MULTIPLIER = (float) (10 * Math.PI / 180);

    public static final float OSWALD_FACTOR = 0.8f;
    public static final float CD_MIN        = 0.0270f;

    public static final int NORMAL_WING     = 0;
    public static final int SYMMETRIC_WING  = 1;


    /**
     * Constructor
     * This sets the offset, normal, area, aspect ratio and type
     * it also sets the flap angle to 0
     * @param offset Offset of the wing
     * @param normal Normal of the wing
     * @param area Area of the wing
     * @param aspectRatio Aspect ratio of the wing
     * @param type Type of the wing (normal or symmetric)
     *
     * @author Nikolas Kühnlein
     */
    public Wing(float[] offset, float[] normal, float area, float aspectRatio, float type) {
        super(offset, normal, area);
        this.aspectRatio = aspectRatio;
        this.type = type;
        this.flapAngle = 0;
    }

    /**
     * Method to get the coefficient of lift
     *
     * This method checks the type of the wing and calls the appropriate method
     * If the type is not normal or symmetric, it returns 0
     *
     * @param aoa Angle of attack
     * @return Coefficient of lift
     *
     * @author Nikolas Kühnlein
     */
    public float getCoefficientOfLift(float aoa) {
        if (type == NORMAL_WING) {
            return getNormalWingLiftCoefficient(aoa);
        } else if (type == SYMMETRIC_WING) {
            return getSymmetricalWingLiftCoefficient(aoa);
        } else {
            return 0;
        }
    }

    /**
     * Calculates the coefficient of lift for a normal wing
     * First it checks if the angle of attack is greater than 15 degrees
     * If it is, it changes the angle of attack to 15 degrees minus the difference to simulate a stall
     * - This is a greatly simplified model of a stall
     * Then it calculates the lift coefficient using the formula:
     *      2 * PI * aspectRatio * (aoa - flapAngle * FLAP_MULTIPLIER) / (aspectRatio + 2) + 0.35
     *      Where:
     *       - aoa is the angle of attack
     *       - aspectRatio is the aspect ratio of the wing
     *       - flapAngle is the angle of the flaps
     *       - FLAP_MULTIPLIER is a constant to simulate the lift reduction due to the flaps
     *       - The 0.35 is a constant to simulate the lift at 0 degrees angle of attack and is specific to the NACA 2412 airfoil
     *
     * @param aoa Angle of attack
     * @return Coefficient of lift
     *
     * @author Nikolas Kühnlein
     */
    private float getNormalWingLiftCoefficient(float aoa) {
        if (aoa > 15*Math.PI/180) {
            // Stall
            aoa = (float) (15*Math.PI/180 - (aoa - 15*Math.PI/180));
            //System.out.println("Stall");
            if (aoa < -5*Math.PI/180) {
                aoa = (float) (-5*Math.PI/180);
            }
        }
        double liftCoefficient = 2*Math.PI * aspectRatio * (aoa + -flapAngle * FLAP_MULTIPLIER) / (aspectRatio + 2) + 0.35f;
        return (float) liftCoefficient;
    }

    /**
     * Calculates the coefficient of lift for a symmetric wing
     * First it checks if the angle of attack is greater than 15 degrees or less than -15 degrees
     * If it is, it changes the angle of attack to simulate a stall
     * - This is a greatly simplified model of a stall
     * Then it calculates the lift coefficient using the formula:
     *     2 * PI * aspectRatio * (aoa - flapAngle * FLAP_MULTIPLIER) / (aspectRatio + 2)
     *     Where:
     *      - aoa is the angle of attack
     *      - aspectRatio is the aspect ratio of the wing
     *      - flapAngle is the angle of the flaps
     *      - FLAP_MULTIPLIER is a constant to simulate the lift reduction due to the flaps
     *     This is the same as the normal wing, but without the 0.35f meaning that at 0 degrees aoa the lift coefficient is 0
     * @param aoa Angle of attack
     * @return Coefficient of lift
     *
     * @author Nikolas Kühnlein
     */
    private float getSymmetricalWingLiftCoefficient(float aoa) {
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
        double liftCoefficient = 2*Math.PI * aspectRatio * (aoa + -flapAngle * FLAP_MULTIPLIER) / (aspectRatio + 2);
        return (float) liftCoefficient;
    }

    /**
     * Method to get the coefficient of drag
     * This method implements the abstract method from the Surface class
     * It calculates the drag coefficient using the formula:
     *     CD_MIN + CL^2 / (PI * aspectRatio * OSWALD_FACTOR)
     *     Where:
     *      - CL is the coefficient of lift
     *      - CD_MIN is the minimum drag coefficient
     *      - aspectRatio is the aspect ratio of the wing
     *      - OSWALD_FACTOR is the Oswald factor which is a constant approximately 0.8 for high wing aircraft
     * @param aoa Angle of attack
     * @return Coefficient of drag
     *
     * @author Nikolas Kühnlein
     */
    @Override
    public float getCoefficientOfDrag(float aoa) {
        double dragCoefficient = CD_MIN + getCoefficientOfLift(aoa)*getCoefficientOfLift(aoa) / (Math.PI * aspectRatio * OSWALD_FACTOR);
        return (float) dragCoefficient;
    }
}
