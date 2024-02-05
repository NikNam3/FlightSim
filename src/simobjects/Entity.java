package simobjects;

// import statements
import graphics.Mesh;
import math.Vec3;

import java.util.List;


/**
 * Entity is the Base class for all Objects in the Simulator
 *
 *
 * @author      Nikolas Kühnlein
 * @version     a1.0
 * @since       a1.0
 */
public class Entity {
    /**
     * ID of the Entity
     */
    private int id;
    /**
     * Position relative to the Parent Entity or to (0,0,0) if no Parent exists
     */
    private Vec3 relPos;
    /**
     * Rotation relative to the Parent Entity or to (0,0,0) if no Parent exists
     */
    private Vec3 relRot;
    /**
     * The Mesh of the Entity
     */
    private Mesh mesh;
    /**
     * All Child Entity's
     */
    private List<Entity> children;


    /**
     * Sets the Parent of this Entity to "entity"
     * @param entity is the new Parent of this Entity
     */
    public void setParent(Entity entity) {

    }
}
