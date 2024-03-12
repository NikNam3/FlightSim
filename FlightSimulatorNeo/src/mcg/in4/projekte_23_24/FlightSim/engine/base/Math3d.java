package mcg.in4.projekte_23_24.FlightSim.engine.base;

import java.util.Arrays;

/**
 * Helper class for 3D math operations
 *
 * @version 1.0
 * @since 1.0
 * @see Math
 * @author Vincent Lahmann, Theo Kamp, Nikolas Kühnlein, Maximo Tewes
 */

public class Math3d {
    public static float[] vec3(float ... xyz){
        float x = 0, y = 0, z = 0;
        if(xyz.length > 0)
            x = xyz[0];
        if(xyz.length > 1)
            y = xyz[1];
        if(xyz.length > 2)
            z = xyz[2];
        return new float[]{x, y, z};
    }

    /**
     * Rotates a vector by a given matrix
     * @param vec vector to rotate
     * @param matrix rotation matrix
     * @return rotated vector
     *
     * @author Nikolas Kühnlein
     */

    public static float[] rotateVec3(float[] vec, float[][] matrix) {
        vec = new float[] {vec[0], vec[1], vec[2], 0};
        vec = mul(matrix, vec);
        return new float[] {vec[0], vec[1], vec[2]};
    }

    /**
     * Takes the cross product of two vectors
     * @param a first vector
     * @param b second vector
     * @return cross product
     *
     * @author Nikolas Kühnlein
     */
    public static float[] cross(float[] a, float[] b){
        return new float[]{
                a[1] * b[2] - a[2] * b[1],
                a[2] * b[0] - a[0] * b[2],
                a[0] * b[1] - a[1] * b[0]
        };
    }

    /**
     * Divides a vector by a scalar
     * @param a vector
     * @param s scalar
     * @return divided vector
     *
     * @author Nikolas Kühnlein
     */
    public static float[] div(float[] a, float s){
        float[] result = new float[a.length];
        for(int i = 0; i < a.length; i++){
            result[i] = a[i] / s;
        }
        return result;
    }

    /**
     * Creates a 4D vector
     * @param xyzw x, y, z and w values
     * @return 4D vector
     *
     * @author Vincent Lahmann
     */

    public static float[] vec4(float ... xyzw){
        float x = 0, y = 0, z = 0, w = 0;
        if(xyzw.length > 0)
            x = xyzw[0];
        if(xyzw.length > 1)
            y = xyzw[1];
        if(xyzw.length > 2)
            z = xyzw[2];
        if(xyzw.length > 3)
            w = xyzw[3];
        return new float[]{x, y, z, w};
    }

    /**
     * TODO: Add description
     * @param vec
     * @param supplement
     * @return
     */
    public static float[] vec4(float[] vec, float ... supplement){
        if(vec.length >= 4){
            return new float[]{vec[0], vec[1], vec[2], vec[3]};
        }
        int vecSize = vec.length;
        float[] result = new float[4];
        for(int i = 0; i < 4; i++){
            if(i < vecSize)
                result[i] = vec[i];
            else if(i - vecSize < supplement.length)
                result[i] = supplement[i - vecSize];
            else
                result[i] = 0.0f;
        }
        return result;
    }

    /**
     * Adds two vectors
     * @param a first vector
     * @param b second vector
     * @return sum of the two vectors
     *
     * @author Maximo Tewes
     */
    public static float[] add(float[] a, float[] b){
        float[] result = new float[a.length];
        for(int i = 0; i < a.length; i++){
            result[i] = a[i] + b[i];
        }
        return result;
    }

    /**
     * Multiplies a vector by a scalar
     * @param a vector
     * @param s scalar
     * @return multiplied vector
     *
     * @author Maximo Tewes
     */

    public static float[] mul(float[] a, float s){
        float[] result = new float[a.length];
        for(int i = 0; i < a.length; i++){
            result[i] = a[i] * s;
        }
        return result;
    }

    /**
     * Multiplies a scalar with a vector (Same as mul(float[] a, float s) but with different parameter order)
     * @param s scalar
     * @param a vector
     * @return multiplied vector
     *
     * @see #mul(float[], float)
     * @author Maximo Tewes
     */

    public static float[] mul(float s, float[] a){
        float[] result = new float[a.length];
        for(int i = 0; i < a.length; i++){
            result[i] = a[i] * s;
        }
        return result;
    }

    /**
     * Takes the dot product of two vectors
     * @param a first vector
     * @param b second vector
     * @return dot product
     *
     * @author Theo Kamp
     */

    public static float dot(float[] a, float[] b){
        float result = 0.f;
        for(int idx = 0; idx < a.length; idx++)
            result += a[idx] * b[idx];
        return result;
    }

    /**
     * Normalizes a vector
     * Be aware that this method does not check for division by zero
     * @param v vector to normalize
     * @return normalized vector
     *
     * @author Theo Kamp
     */
    public static float[] normalize(float[] v){
        float l = length(v);
        float[] result = new float[v.length];
        for(int i = 0; i < result.length; i++){
            result[i] = v[i] / l;
        }
        return result;
    }

    /**
     * Returns the squared length of a vector
     * @param vec vector
     * @return squared length
     *
     * @author Maximo Tewes
     */

    public static float length2(float[] vec){
        float sum = 0.0f;
        for(float e : vec)
            sum += e * e;
        return sum;
    }

    /**
     * Returns the length of a vector
     * @param vec vector
     * @return length
     *
     * @see #length2(float[])
     * @author Maximo Tewes
     */
    public static float length(float[] vec){
        return (float)Math.sqrt(length2(vec));
    }

    /**
     * Returns the default 4x4 matrix
     * @return 4x4 matrix
     *
     * @author Maximo Tewes
     */

    public static float[][] mat4(){
        return new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
    }

    /**
     * Rotates a 4x4 matrix around the x-axis
     * @param a angle in radians
     * @return rotated 4x4 matrix
     *
     * @see #cosf(float)
     * @see #sinf(float)
     *
     * @author Theo Kamp
     */

    public static float[][] rotationX(float a){
        return new float[][]{
                {1, 0, 0, 0},
                {0, cosf(a), sinf(a), 0},
                {0,-sinf(a), cosf(a), 0},
                {0, 0, 0, 1}
        };
    }
    /**
     * Rotates a 4x4 matrix around the y-axis
     * @param a angle in radians
     * @return rotated 4x4 matrix
     *
     * @see #cosf(float)
     * @see #sinf(float)
     *
     * @author Theo Kamp
     */

    public static float[][] rotationY(float a){
        return new float[][]{
                { cosf(a), 0, sinf(a), 0},
                {0, 1, 0, 0},
                {-sinf(a), 0, cosf(a), 0},
                {0, 0, 0, 1}
        };
    }
    /**
     * Rotates a 4x4 matrix around the z-axis
     * @param a angle in radians
     * @return rotated 4x4 matrix
     *
     * @see #cosf(float)
     * @see #sinf(float)
     *
     * @author Theo Kamp
     */
    public static float[][] rotationZ(float a){
        return new float[][]{
                {cosf(a),-sinf(a), 0, 0},
                {sinf(a), cosf(a), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
    }

    /**
     * TODO: add description
     * @param fov
     * @param zNear
     * @param zFar
     * @param a
     * @return
     * @author Vincent Lahmann
     */
    public static float[][] perspective(float fov, float zNear, float zFar, float a){
        return new float[][] {
                {1f / (a * tanf(fov / 2)), 0, 0, 0},
                {0, 1 / (tanf(fov / 2)), 0, 0},
                {0, 0, -(zFar + zNear) / (zFar - zNear), -(2 * zFar * zNear) / (zFar - zNear)},
                {0, 0, -1, 0}
        };
    }

    /**
     * TODO: add description
     * @param xyz
     * @return
     *
     * @author Vincent Lahmann
     */
    public static float[][] translation(float ... xyz){
        float[] offset3 = new float[3];
        for(int i = 0; i < 3; i++){
            if(i < xyz.length)
                offset3[i] = xyz[i];
            else
                offset3[i] = 0.0f;
        }
        return new float[][]{
                {1, 0, 0, offset3[0]},
                {0, 1, 0, offset3[1]},
                {0, 0, 1, offset3[2]},
                {0, 0, 0, 1}
        };
    }

    /**
     * Returns the rotation matrix for given pitch, yaw and roll angles in radians
     * @param x pitch in radians
     * @param y yaw in radians
     * @param z roll in radians
     * @return rotation matrix
     *
     * @see #rotationX(float)
     * @see #rotationY(float)
     * @see #rotationZ(float)
     *
     * @author Nikolas Kühnlein
     */

    public static float[][] rotationMatrix(float x, float y, float z) { // Input in radians
        float[][] rx = rotationX(x);
        float[][] ry = rotationY(y);
        float[][] rz = rotationZ(z);
        return mul(mul(rz, ry), rx);
    }

    /**
     * Returns the inverse of a matrix
     * @param matrix input matrix
     * @return inverse matrix
     *
     * @author Nikolas Kühnlein
     */

    public static float[][] inverse(float[][] matrix) {
        int n = matrix.length;

        // Create a copy of the input matrix
        float[][] originalMatrix = new float[n][n];
        for (int i = 0; i < n; i++) {
            originalMatrix[i] = Arrays.copyOf(matrix[i], n);
        }

        // Create an identity matrix of the same size as the input matrix
        float[][] identityMatrix = new float[n][n];
        for (int i = 0; i < n; i++) {
            identityMatrix[i][i] = 1;
        }

        // Apply Gauss-Jordan elimination
        for (int i = 0; i < n; i++) {
            float pivot = originalMatrix[i][i];
            if (pivot == 0) {
                // Matrix is singular, inverse does not exist
                return null;
            }

            // Scale the row to make the pivot 1
            for (int j = 0; j < n; j++) {
                originalMatrix[i][j] /= pivot;
                identityMatrix[i][j] /= pivot;
            }

            // Subtract multiples of the row from other rows to make all other entries in the column zero
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    float factor = originalMatrix[k][i];
                    for (int j = 0; j < n; j++) {
                        originalMatrix[k][j] -= factor * originalMatrix[i][j];
                        identityMatrix[k][j] -= factor * identityMatrix[i][j];
                    }
                }
            }
        }

        return identityMatrix;
    }

    /**
     * Multiplies a 4x4 matrix with a 4x1 vector
     * @param mat 4x4 matrix
     * @param vec 4x1 vector
     * @return 4x1 vector
     *
     * @author Theo Kamp
     */
    public static float[] mul(float[][] mat, float[] vec){
        int n = vec.length;
        float[] result = new float[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i] += mat[i][j] * vec[j];
            }
        }
        return result;
    }

    /**
     * Multiplies two matrices
     * @param matA first matrix
     * @param matB second matrix
     * @return result matrix
     *
     * @author Theo Kamp
     */

    public static float[][] mul(float[][] matA, float[][] matB){
        int n = matA.length;
        float[][] result = new float[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                float sum = 0.0f;
                for(int k = 0; k < n; k++)
                    sum += matA[i][k] * matB[k][j];
                result[i][j] = sum;
            }
        }
        return result;
    }

    /**
     * Returns a string representation of a vector with optional rounding
     * @param vec input vector
     * @param round if true, the vector will be rounded to one decimal place
     * @return string representation of the vector
     *
     * @author Vincent Lahmann
     */

    public static String string(float[] vec, boolean round){
        StringBuilder result = new StringBuilder("[");
        for(int i = 0; i < vec.length; i++){
            float e = vec[i];
            if(round)
                e = Math.round(e * 10) / 10.0f;
            if(i > 0) {
                result.append(",");
                if(e >= 0.0f) {
                    result.append(" ");
                }
            }
            result.append(e);
        }
        result.append("]");
        return result.toString();
    }

    /**
     * Returns a string representation of a matrix with optional rounding
     * @param mat input matrix
     * @param round if true, the matrix will be rounded to one decimal place
     * @return string representation of the matrix
     *
     * @author Vincent Lahmann
     */
    public static String string(float[][] mat, boolean round){
        StringBuilder result = new StringBuilder();
        for (float[] row : mat) {
            result.append("[");
            for (int c = 0; c < row.length; c++) {
                float e = row[c];
                if(round)
                    e = Math.round(e * 10) / 10.0f;
                if (c > 0) {
                    result.append(",");
                    if(e >= 0.0f)
                        result.append(" ");
                }
                result.append(e);
            }
            result.append("]\n");
        }
        return result.toString();
    }

    /**
     * Cosine function
     * @param n angle in radians
     * @return cosine of the angle
     *
     * @author Maximo Tewes
     */
    private static float cosf(float n){
        return (float)Math.cos(n);
    }
    /**
     * Sine function
     * @param n angle in radians
     * @return Sine of the angle
     *
     * @author Maximo Tewes
     */
    private static float sinf(float n){
        return (float)Math.sin(n);
    }
    /**
     * Tangent function
     * @param n angle in radians
     * @return tangent of the angle
     *
     * @author Maximo Tewes
     */

    private static float tanf(float n){
        return (float)Math.sin(n);
    }
}
