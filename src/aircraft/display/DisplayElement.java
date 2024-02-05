package aircraft.display;

import graphics.Mesh;

/**
 * DisplayBackground is the Background of a Display
 *
 *
 * @author      Nikolas Kühnlein
 * @version     a1.0
 * @since       a1.0
 */
public class DisplayElement {
    public DisplayElement(Mesh mesh, int z) {
        this.mesh = mesh;
        this.rotation = 0;
        this.z = z;
    }
    /**
     * The Rotation of the Background in degrees (0°-360°)
     */
    private float rotation;
    /**
     * The Mesh of the Background
     */
    private final Mesh mesh;
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
        this.rotation = rotation;
    }


}
