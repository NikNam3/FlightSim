package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys;

import mcg.in4.projekte_23_24.FlightSim.engine.Window;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

public class Engine extends EntityBehavior{
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

    @Override
    public void onUpdate(float deltaTime){
        long glfwWindow = Window.getGlfwWindowAddress();
        if (glfwGetKey(glfwWindow, increaseKey) == 1)
            setRPM += 8f;
        if (glfwGetKey(glfwWindow, decreaseKey) == 1)
            setRPM -= 8f;

        if (setRPM > MAX_RPM)
            setRPM = MAX_RPM;
        if (setRPM < 0)
            setRPM = 0;
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
}
