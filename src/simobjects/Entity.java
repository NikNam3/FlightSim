package simobjects;

// import statements
import graphics.Mesh;
import math.Mat3;
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
    public Entity(Vec3 relPos, Mat3 relRot) {
        this.relPos = relPos;
        this.relRot = relRot;
    }
    /**
     * Position relative to the Parent Entity or to (0,0,0) if no Parent exists
     */
    private Vec3 relPos;
    /**
     * Rotation relative to the Parent Entity or to (0,0,0) if no Parent exists
     */
    private Mat3 relRot;

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
    public void setRelRot(Mat3 rotation) {
        this.relRot = rotation;
    }
    public void setRelPos(Vec3 position) {
        this.relPos = position;
    }

    public Mat3 getRelRotation() {
        return relRot;
    }
    public Vec3 getRelPosition() {
        return relPos;
    }
}
