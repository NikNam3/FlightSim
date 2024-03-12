package mcg.in4.projekte_23_24.FlightSim.engine.flightphysics;

/**
 * Class to store engine data
 * @version 1.0
 * @since 1.0
 * @author Nikolas Kühnlein
 */
public class Engine {
    public final float MAX_RPM;
    public final float RPM_CHANGE_RATE;
    public final float DIAMETER;
    public final float PITCH;

    public float currentRPM = 0;
    public float setRPM = 0;


    /**
     * Constructor with parameters
     * @param max_rpm Maximum RPM
     * @param rpm_change_rate RPM change rate
     * @param diameter Diameter of the engine
     * @param pitch Pitch of the engine
     * @author Nikolas Kühnlein
     */
    public Engine(float max_rpm, float rpm_change_rate, float diameter, float pitch) {
        MAX_RPM = max_rpm;
        RPM_CHANGE_RATE = rpm_change_rate;
        DIAMETER = diameter;
        PITCH = pitch;
    }
}
