package mcg.in4.projekte_23_24.FlightSim.simlogic.components;

import java.util.List;

/**
 * This class is a Component of an Entity. It contains the RigidBody of the Entity.
 * The RigidBody contains the mass, velocity, relCenterOfGravity, angularVelocity, inertiaTensor and forces of the Aircraft.
 * @author Nikolas Kühnlein
 * @version 1.0
 * @since 1.0
 */

public class RigidBody {
    public float mass;
    public float[] velocity;
    public float[] relCenterOfGravity;
    public float[] angularVelocity; // Yaw, Pitch, Roll in rad/s
    public float[][] inertiaTensor; // Ixx, Iyy, Izz

    public List<float[][]> forces; // 0: force, 1: relposition
}
