package simobjects.aircraft.displays;

import graphics.Mesh;
import math.Vec3;
import simobjects.Entity;

/**
 * DisplayBackground is the Background of a Display
 *
 *
 * @author      Nikolas Kühnlein
 * @version     a1.0
 * @since       a1.0
 */
public class DisplayElement extends Entity {
    public DisplayElement(Mesh mesh) {
        super(Vec3.zero(), Vec3.zero()); // Sets the rel- Position and Rotation to (0,0,0)
        this.setMesh(mesh);
    }
    /**
     * sets the Rotation of the Background
     * @param rotation is the new Rotation of the Background in degrees (0°-360°)
     */
    protected void setRotation(float rotation) {
         setRelRot(new Vec3(0,0, rotation)); // TODO look into this
    }


}
