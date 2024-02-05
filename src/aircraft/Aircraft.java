package aircraft;

import simobjects.Entity;

/**
 * Aircraft is the Abstract base Class for All Aircraft Models. It handles the Update physics and inputs
 *
 *
 * @author      Nikolas Kühnlein
 * @version     a1.0
 * @since       a1.0
 */

public abstract class Aircraft extends Entity {
    /**
     * Updates visuals and Physics of the Aircraft
     * @param deltaTime is the time since the last Update call
     */
    public void update(float deltaTime) {

    }

    /**
     * Updates the Physics of the Aircraft
     * @param deltaTime is the time since the last call
     */
    abstract void updatePhysics(float deltaTime);

    /**
     * Reads the Inputs
     * @param deltaTime is the time since the last call
     */
    abstract void updateInputs(float deltaTime);
}
