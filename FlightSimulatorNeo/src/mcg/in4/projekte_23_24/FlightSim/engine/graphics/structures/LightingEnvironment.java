package mcg.in4.projekte_23_24.FlightSim.engine.graphics.structures;

import static mcg.in4.projekte_23_24.FlightSim.engine.base.Math3d.*;
import static mcg.in4.projekte_23_24.FlightSim.engine.base.Math3d.vec4;

public class LightingEnvironment {
    // 0      = midnight
    // 0.5     = noon
    // 1     = midnight

    private float timeOfDay;

    public LightingEnvironment(){
        timeOfDay = 0;
    }

    public void setTimeOfDay(float val){
        timeOfDay = val - (int)val;
    }

    public void increaseTimeOfDay(float amount){
        setTimeOfDay(timeOfDay + amount);
    }

    public float[] getSunLightDirection(){
        float angle = (float)(timeOfDay * 2 * Math.PI);
        return vec3(mul(rotationX(angle), vec4(0, 1, 0)));
    }

    public float[] getAmbientLighting(){
        float[] color   = vec3(0.4f, 0.4f, 0.4f);
        float intensity = -(float)Math.cos((float)(timeOfDay * 2 * Math.PI));
        intensity = Math.max(intensity, 0);

        return mul(color, intensity);
    }

    public float[] getSunColor(){
        float[] color   = vec3(1.f, 1.f, 1.f);
        float intensity = -(float)Math.cos((float)(timeOfDay * 2 * Math.PI));
        intensity = Math.max(intensity, 0);
        return mul(color, intensity);
    }
}
