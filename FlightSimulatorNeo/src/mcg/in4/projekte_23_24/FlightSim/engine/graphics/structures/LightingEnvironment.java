package mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures;

import static mcg.in4.projekte_23_24.FlightSim.engine.base.Math3d.*;
import static mcg.in4.projekte_23_24.FlightSim.engine.base.Math3d.vec4;
/**
 * Class for holding the lighting of the world
 *
 * @version 1.0
 * @since 1.0
 * @author Vincent Lahmann
 */
public class LightingEnvironment {
    // 0      = midnight
    // 0.5     = noon
    // 1     = midnight

    private float timeOfDay;

    public LightingEnvironment(){
        timeOfDay = 0;
    }

    /**
     * Sets the time
     * @param val New time
     * @author Vincent Lahmann
     */
    public void setTimeOfDay(float val){
        timeOfDay = val - (int)val;
    }

    /**
     * Adds a value to the current time
     * @param amount The value by which the time of day is incremented
     * @author Vincent Lahmann
     */
    public void increaseTimeOfDay(float amount){
        setTimeOfDay(timeOfDay + amount);
    }

    /**
     * Calculates the direction if sunlight according to the time of day
     * @return Sunlight direction
     * @author Vincent Lahmann
     */
    public float[] getSunLightDirection(){
        float angle = (float)(timeOfDay * 2 * Math.PI);
        return vec3(mul(rotationX(angle), vec4(0, 1, 0)));
    }

    /**
     * Calculates the ambient light color according to the time of day
     * @return Ambient light color
     * @author Vincent Lahmann
     */
    public float[] getAmbientLighting(){
        float[] color   = vec3(0.4f, 0.4f, 0.4f);
        float intensity = -(float)Math.cos((float)(timeOfDay * 2 * Math.PI));
        intensity = Math.max(intensity, 0);

        return mul(color, intensity);
    }

    /**
     * Calculates to color of the sunlight at the time of day
     * @return Color of sunlight as a vector
     * @author Vincent Lahmann
     */
    public float[] getSunColor(){
        float[] color   = vec3(1.f, 1.f, 1.f);
        float intensity = -(float)Math.cos((float)(timeOfDay * 2 * Math.PI));
        intensity = Math.max(intensity, 0);
        return mul(color, intensity);
    }
}
