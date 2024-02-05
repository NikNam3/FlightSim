package math;

public class Vec3 {
    public float x;
    public float y;
    public float z;
    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public static Vec3 zero() {
        return new Vec3(0, 0, 0);
    }
}
