package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys;

public class Engine{
    private final float MAX_RPM;
    private float currentRPM = 0;
    private float setRPM = 0;
    private final int increaseKey;
    private final int decreaseKey;
    private final float RPMChangeRate;
    private final float DIAMETER;
    private final float PITCH;

    public Engine(float diameter, float pitch, float max_rpm, float rpmChangeRate, int increaseKey, int decreaseKey) {
        MAX_RPM = max_rpm;
        this.increaseKey = increaseKey;
        this.decreaseKey = decreaseKey;
        RPMChangeRate = rpmChangeRate;
        DIAMETER = diameter;
        PITCH = pitch;
    }

    public float getCurrentRPM() {
        return currentRPM;
    }
    public float getSetRPM() {
        return setRPM;
    }
    public void setCurrentRPM(float currentRPM) {
        this.currentRPM = currentRPM;
    }


    public float getRPMChangeRate() {
        return RPMChangeRate;
    }

    public float getDIAMETER() {
        return DIAMETER;
    }
    public float getPitch() {
        return PITCH;
    }

    public int getIncreaseKey() {
        return increaseKey;
    }

    public int getDecreaseKey() {
        return decreaseKey;
    }

    public void setSetRPM(float v) {
        setRPM = v;
    }

    public float getMaxRPM() {
        return MAX_RPM;
    }
}
