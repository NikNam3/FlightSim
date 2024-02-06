package simobjects.weather;


import math.Vec3;

/**
 * Wind is a Class to store the Wind Speed and Direction
 * It is used to simulate the Wind in the Simulator
 *
 *
 * @author      Nikolas Kühnlein
 * @version     a1.0
 * @since       a1.0
 */
public class Wind {
    private float speed;
    private Vec3 direction;

    public Wind(float speed, Vec3 direction) {
        this.speed = speed;
        this.direction = direction;
    }

    public float getSpeed() {
        return speed;
    }

    public Vec3 getDirection() {
        return direction;
    }

    public void setDirection(Vec3 direction) {
        this.direction = direction;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
