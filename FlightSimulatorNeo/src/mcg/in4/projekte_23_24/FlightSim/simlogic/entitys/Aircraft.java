package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys;

import mcg.in4.projekte_23_24.FlightSim.engine.Math3d;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Transform;

/**
 * Aircraft
 * This class defines the behavior of an aircraft entity.
 * It is a subclass of EntityBehavior.
 *
 * @see EntityBehavior
 * @version 1.0
 * @since 1.0
 * @author Nikolas Kühnlein (17.02.2024)
 */

public class Aircraft extends EntityBehavior {


    /**
     * onUpdate
     * This method is called every frame to update the aircraft entity.
     * It handles the keyboard input and updates the aircraft's position.
     * @param deltaTime The time since the last frame in seconds.
     */
    @Override
    public void onUpdate(float deltaTime){
        //System.out.println("Aircraft position: \n" + Math3d.string(scene.getComponent(hostId, Transform.class).matrixOffset, false));
    }
}
