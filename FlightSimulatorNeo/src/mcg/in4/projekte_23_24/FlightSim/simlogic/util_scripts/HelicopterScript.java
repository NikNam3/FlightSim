package mcg.in4.projekte_23_24.FlightSim.simlogic.util_scripts;

import mcg.in4.projekte_23_24.FlightSim.engine.Window;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Transform;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.EntityBehavior;

import static mcg.in4.projekte_23_24.FlightSim.engine.Math3d.*;
import static org.lwjgl.glfw.GLFW.*;

public class HelicopterScript extends EntityBehavior {

    @Override
    public void onUpdate(float dt){
        long glfwWindow = Window.getGlfwWindowAddress();

        if(!hasComponent(Transform.class))
            addComponent(new Transform(), Transform.class);

        Transform transform = getComponent(Transform.class);

        var deltaPositionVector = vec3();
        if(glfwGetKey(glfwWindow, GLFW_KEY_UP) == 1)
            deltaPositionVector = add(deltaPositionVector, vec3(0, dt, 0));

        var dpv4 = vec4(deltaPositionVector, 0);
        deltaPositionVector = vec3(mul(transform.matrixRotate, dpv4));
        transform.matrixOffset = mul(translation(deltaPositionVector), transform.matrixOffset);
    }
}
