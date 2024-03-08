package mcg.in4.projekte_23_24.FlightSim.simlogic.components;

import java.util.List;

public class RigidBody {
    public float mass;
    public float[] velocity;
    public float[] relCenterOfGravity;
    public float[] angularVelocity; // Yaw, Pitch, Roll in rad/s
    public float[][] inertiaTensor; // Ixx, Iyy, Izz

    public List<float[][]> forces; // 0: force, 1: relposition
}
