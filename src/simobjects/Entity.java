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
    public Entity(int id, Vec3 relPos, Vec3 relRot) {
        this.id = id;
        this.relPos = relPos;
        this.relRot = relRot;
    }
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

    private Mesh mesh;
    /**
     * All Child Entity's
     */
    private List<Entity> children;


    /**
     * Sets the Parent of this Entity to "entity"
     * This is done by adding this Entity to the children of "entity"
     * @param entity is the new Parent of this Entity
     */
    public void setParent(Entity entity) {

    }


    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
    public void setRelRot(Vec3 rotation) {
        this.relRot = rotation;
    }
}
