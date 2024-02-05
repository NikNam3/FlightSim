package aircraft.display;

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
    public DisplayElement(int id, Vec3 relPos, Mesh mesh, int z) {
        super(id, relPos, Vec3.zero());
        this.setMesh(mesh);
        this.z = z;
    }
    /**
     * How far in front the Element is
     * The higher the Z value the further in front the Element is
     * z = 0 is the background
     */
    private final int z;

    /**
     * sets the Rotation of the Background
     * @param rotation is the new Rotation of the Background in degrees (0°-360°)
     */
    protected void setRotation(float rotation) {
         setRelRot(new Vec3(0,0, rotation));
    }


}
