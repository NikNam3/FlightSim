package mcg.in4.projekte_23_24.FlightSim.engine.components.physics;

/**
 * Component to store physics data for an entity
 *
 * @version 1.0
 * @since 1.0
 * @author Nikolas Kühnlein
 */

public class RigidBody {
    public float mass;
    public float[] velocity;
    public float[] angularVelocity;
    public float[] cgOffset;
    public float[][] momentOfInertia;


    /**
     * Default constructor
     * @author Nikolas Kühnlein
     */
    public RigidBody() {
        this.mass            = 1;
        this.cgOffset        = new float[3];
        this.velocity        = new float[3];
        this.angularVelocity = new float[3];
        this.momentOfInertia = new float[4][4];
    }
}
