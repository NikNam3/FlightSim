package math;

/**
 * A 3D Vector
 * This is used to store the Position and Rotation of the Display and its Elements
 * Where x is the right, y is the up and z is the forward direction
 * And x is the pitch, y is the roll and z is the yaw // TODO update if changes
 */

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

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vec3 normalized() {
        float length = length();
        return new Vec3(x / length, y / length, z / length);
    }
    public Vec3 dot(Vec3 other) {
        return new Vec3(x * other.x, y * other.y, z * other.z);
    }
    public Vec3 scalar(float scalar) {
        return new Vec3(x * scalar, y * scalar, z * scalar);
    }
    public Vec3 sub(Vec3 other) {
        return new Vec3(x - other.x, y - other.y, z - other.z);
    }
}
