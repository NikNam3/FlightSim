package mcg.in4.projekte_23_24.FlightSim.simlogic.util_scripts;


import mcg.in4.projekte_23_24.FlightSim.engine.Window;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Transform;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.EntityBehavior;

import static org.lwjgl.glfw.GLFW.*;
import static mcg.in4.projekte_23_24.FlightSim.engine.Math3d.*;

// NUR ZUM TEST ... API WIRD SICH ÄNDERN!!!

public class FreeCameraScript extends EntityBehavior {
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

        var deltaPositionVector = vec3();
        if(glfwGetKey(glfwWindow, GLFW_KEY_W) == 1)
            deltaPositionVector = add(deltaPositionVector, vec3(0, 0, -moveSpeed * deltaTime));
        if(glfwGetKey(glfwWindow, GLFW_KEY_S) == 1)
            deltaPositionVector = add(deltaPositionVector, vec3(0, 0,  moveSpeed * deltaTime));
        if(glfwGetKey(glfwWindow, GLFW_KEY_A) == 1)
            deltaPositionVector = add(deltaPositionVector, vec3(-moveSpeed * deltaTime));
        if(glfwGetKey(glfwWindow, GLFW_KEY_D) == 1)
            deltaPositionVector = add(deltaPositionVector, vec3(moveSpeed * deltaTime));
        if(glfwGetKey(glfwWindow, GLFW_KEY_SPACE) == 1)
            deltaPositionVector = add(deltaPositionVector, vec3(0, moveSpeed * deltaTime));
        if(glfwGetKey(glfwWindow, GLFW_KEY_C) == 1)
            deltaPositionVector = add(deltaPositionVector, vec3(0,-moveSpeed * deltaTime));


        if(glfwGetKey(glfwWindow, GLFW_KEY_Q) == 1)
            transform.matrixRotate = mul(rotationY(rotSpeed * deltaTime), transform.matrixRotate);
        if(glfwGetKey(glfwWindow, GLFW_KEY_E) == 1)
            transform.matrixRotate = mul(rotationY(rotSpeed * -deltaTime), transform.matrixRotate);
        if(glfwGetKey(glfwWindow, GLFW_KEY_R) == 1)
            transform.matrixRotate = mul(transform.matrixRotate, rotationX(rotSpeed * -deltaTime));
        if(glfwGetKey(glfwWindow, GLFW_KEY_F) == 1)
            transform.matrixRotate = mul(transform.matrixRotate, rotationX(rotSpeed * deltaTime));

        var dpv4 = vec4(deltaPositionVector, 0);
        deltaPositionVector = vec3(mul(transform.matrixRotate, dpv4));
        transform.matrixOffset = mul(translation(deltaPositionVector), transform.matrixOffset);
    }
}
