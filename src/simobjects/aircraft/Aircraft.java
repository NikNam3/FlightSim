package simobjects.aircraft;

import gamecontrol.Session;
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
    private Vec3 velocity;


    public Aircraft(Vec3 relPos, Vec3 relRot) {
        super(relPos, relRot);
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
     * By accounting for the Wind and the movement of the Aircraft the correct Airspeed gets calculated
     * This Value is used in the "AirspeedIndicator" as well as used to calculate the Lift, Drag and other physics properties
     * @return the AirSpeed of the Aircraft
     */
    public float getAirSpeed() {
        return velocity.sub(Session.wind.getDirection().dot(velocity.normalized()).scalar(Session.wind.getSpeed())).length(); // TODO needs to be tested thoroughly
        // Gets the projection of the wind on the Aircraft (normalized wind direction dot normalized aircraft movement)
        // This gets multiplied by the wind magnitude (Wind direction * wind speed)
        // Finally this gets subtracted from the Velocity of the Aircraft
        // The length of the resulting vector is the measured Airspeed
    }
    public float getAltitude() {
        return getRelPosition().y;
    }
    public float getVerticalSpeed() {
        return velocity.y;
    }
}
