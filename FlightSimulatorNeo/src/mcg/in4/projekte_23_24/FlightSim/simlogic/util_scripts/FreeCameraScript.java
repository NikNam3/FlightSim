package mcg.in4.projekte_23_24.FlightSim.simlogic.util_scripts;


import mcg.in4.projekte_23_24.FlightSim.engine.Math3d;
import mcg.in4.projekte_23_24.FlightSim.engine.Window;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Transform;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.EntityBehavior;

import static org.lwjgl.glfw.GLFW.*;
import static mcg.in4.projekte_23_24.FlightSim.engine.Math3d.*;

// NUR ZUM TEST ... API WIRD SICH ÄNDERN!!!

public class FreeCameraScript extends EntityBehavior {
    @Override
    public void onUpdate(float deltaTime){


        Transform transform = getComponent(Transform.class);


        transform.matrixOffset = Math3d.add(scene.getComponent(2, Transform.class).matrixOffset, new float[][]
                {
                        {0,0,0,0},
                        {0,0,0,-5},
                        {0,0,0,20},
                        {0,0,0,0}
                });
        transform.matrixRotate = scene.getComponent(2, Transform.class).matrixRotate;
    }
}
