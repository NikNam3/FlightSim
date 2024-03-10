package mcg.in4.projekte_23_24.FlightSim.simlogic.entitys;

import mcg.in4.projekte_23_24.FlightSim.engine.Math3d;
import mcg.in4.projekte_23_24.FlightSim.engine.Window;
import mcg.in4.projekte_23_24.FlightSim.engine.structures.Scene;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Engines;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Surfaces;
import mcg.in4.projekte_23_24.FlightSim.simlogic.components.Transform;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.surfaces.ControlledSymmetricalWing;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.surfaces.ControlledWingSurface;
import mcg.in4.projekte_23_24.FlightSim.simlogic.entitys.surfaces.Surface;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

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
     * It handles the keyboard input
     * @param deltaTime The time since the last frame in seconds.
     */
    @Override
    public void onUpdate(float deltaTime){
        long glfwWindow = Window.getGlfwWindowAddress();
        // Use the surfaces to update the flap angles
        int[] surfaceIds = scene.getComponent(hostId, Surfaces.class).wingSurfaceIds; // Only Wing surfaces can be controlled
        int[] engineIds = scene.getComponent(hostId, Engines.class).engineIds; // Engines can be controlled
        for (int surfaceId : surfaceIds) {
            Surface surface = scene.getComponent(surfaceId, Surface.class);
            if (surface instanceof ControlledWingSurface wingSurface) {
                if (glfwGetKey(glfwWindow, wingSurface.getDecreaseAngleKey()) == 1) {
                    wingSurface.setFlapAngle(wingSurface.getMIN_FLAP_ANGLE());
                }
                else if (glfwGetKey(glfwWindow, wingSurface.getIncreaseAngleKey()) == 1) {
                    wingSurface.setFlapAngle(wingSurface.getMAX_FLAP_ANGLE());
                }
                else {
                    wingSurface.setFlapAngle(0);
                }
            }
            else if (surface instanceof ControlledSymmetricalWing symmetricalWing) {
                if (glfwGetKey(glfwWindow, symmetricalWing.getDecreaseAngleKey()) == 1) {
                    symmetricalWing.setFlapAngle(symmetricalWing.getMIN_FLAP_ANGLE());
                }
                else if (glfwGetKey(glfwWindow, symmetricalWing.getIncreaseAngleKey()) == 1) {
                    symmetricalWing.setFlapAngle(symmetricalWing.getMAX_FLAP_ANGLE());
                }
                else {
                    symmetricalWing.setFlapAngle(0);
                }
            }

        }
        for (int engineId : engineIds) {
            Engine engine = scene.getComponent(engineId, Engine.class);
            if (glfwGetKey(glfwWindow, engine.getIncreaseKey()) == 1)
                engine.setSetRPM(engine.getSetRPM() + 8f);
            else if (glfwGetKey(glfwWindow, engine.getDecreaseKey()) == 1)
                engine.setSetRPM(engine.getSetRPM() - 8f);

            if (engine.getSetRPM() > engine.getMaxRPM())
                engine.setSetRPM(engine.getMaxRPM());
            else if (engine.getSetRPM() < 0)
                engine.setSetRPM(0);
        }


    }
}
