package aircraft;

import math.Vec3;
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
    private float velocity;


    public Aircraft(int id, Vec3 relPos, Vec3 relRot) {
        super(id, relPos, relRot);
    }

    /**
     * Updates visuals and Physics of the Aircraft
     * @param deltaTime is the time since the last Update call
     */
    public void update(float deltaTime) {

    }

    /**
     * Updates the Physics of the Aircraft
     * This method has a Fixed Update Interval
     * @param deltaTime is the time since the last call
     */
    abstract void updatePhysics(float deltaTime);

    /**
     * Reads the Inputs
     * This method gets called every Frame
     * @param deltaTime is the time since the last call
     */
    abstract void updateInputs(float deltaTime);

    /**
     * @return the Velocity of the Aircraft
     */
    public float getVelocity() {
        return velocity;
    }
}
