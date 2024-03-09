package mcg.in4.projekte_23_24.FlightSim.simlogic.util_scripts;

import mcg.in4.projekte_23_24.FlightSim.engine.Math3d;
import mcg.in4.projekte_23_24.FlightSim.engine.Window;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Transform;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.EntityBehavior;

import static org.lwjgl.glfw.GLFW.*;
import static mcg.in4.projekte_23_24.FlightSim.engine.Math3d.*;

// NUR ZUM TEST ... API WIRD SICH ÄNDERN!!!

public class FreeCameraScript2 extends EntityBehavior {
    @Override
    public void onUpdate(float deltaTime){
        long glfwWindow = Window.getGlfwWindowAddress();

        if(!hasComponent(Transform.class))
            addComponent(new Transform(), Transform.class);

        Transform transform = getComponent(Transform.class);

        float moveSpeed = 5;
        float rotSpeed  = 0.8f;

        if(glfwGetKey(glfwWindow, GLFW_KEY_LEFT_SHIFT) == 1){
            moveSpeed *= 50;
            rotSpeed  *= 2f;
        }




        if(glfwGetKey(glfwWindow, GLFW_KEY_LEFT) == 1)
            transform.matrixRotate = mul(rotationY(rotSpeed * deltaTime), transform.matrixRotate);
        if(glfwGetKey(glfwWindow, GLFW_KEY_RIGHT) == 1)
            transform.matrixRotate = mul(rotationY(rotSpeed * -deltaTime), transform.matrixRotate);
        if(glfwGetKey(glfwWindow, GLFW_KEY_UP) == 1)
            transform.matrixRotate = mul(transform.matrixRotate, rotationX(rotSpeed * -deltaTime));
        if(glfwGetKey(glfwWindow, GLFW_KEY_DOWN) == 1)
            transform.matrixRotate = mul(transform.matrixRotate, rotationX(rotSpeed * deltaTime));

        transform.matrixOffset = Math3d.add(scene.getComponent(2, Transform.class).matrixOffset, new float[][]
                {
                        {0,0,0,0f},
                        {0,0,0,0},
                        {0,0,0,20f},
                        {0,0,0,0}
                });
    }
}
