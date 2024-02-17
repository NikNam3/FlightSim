package engine;

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

    public static float[] add(float[] a, float[] b){
        float[] result = new float[a.length];
        for(int i = 0; i < a.length; i++){
            result[i] = a[i] + b[i];
        }
        return result;
    }

    public static float[] sub(float[] a, float[] b){
        float[] result = new float[a.length];
        for(int i = 0; i < a.length; i++){
            result[i] = a[i] - b[i];
        }
        return result;
    }

    public static float[] mul(float[] a, float[] b){
        float[] result = new float[a.length];
        for(int i = 0; i < a.length; i++){
            result[i] = a[i] * b[i];
        }
        return result;
    }

    public static float[] mul(float[] a, float s){
        float[] result = new float[a.length];
        for(int i = 0; i < a.length; i++){
            result[i] = a[i] * s;
        }
        return result;
    }

    public static float[] mul(float s, float[] a){
        float[] result = new float[a.length];
        for(int i = 0; i < a.length; i++){
            result[i] = a[i] * s;
        }
        return result;
    }

    public static float length2(float[] vec){
        float sum = 0.0f;
        for(float e : vec)
            sum += e * e;
        return sum;
    }

    public static float length(float[] vec){
        return (float)Math.sqrt(length2(vec));
    }

    public static float[][] mat4(){
        return new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
    }

    public static float[][] rotationX(float a){
        return new float[][]{
                {1, 0, 0, 0},
                {0, cosf(a), sinf(a), 0},
                {0,-sinf(a), cosf(a), 0},
                {0, 0, 0, 1}
        };
    }

    public static float[][] rotationY(float a){
        return new float[][]{
                { cosf(a), 0, sinf(a), 0},
                {0, 1, 0, 0},
                {-sinf(a), 0, cosf(a), 0},
                {0, 0, 0, 1}
        };
    }

    public static float[][] rotationZ(float a){
        return new float[][]{
                {cosf(a),-sinf(a), 0, 0},
                {sinf(a), cosf(a), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
    }

    public static float[][] perspective(float fov, float zNear, float zFar, float a){
        return new float[][] {
                {1f / (a * tanf(fov / 2)), 0, 0, 0},
                {0, 1 / (tanf(fov / 2)), 0, 0},
                {0, 0, -(zFar + zNear) / (zFar - zNear), -(2 * zFar * zNear) / (zFar - zNear)},
                {0, 0, -1, 0}
        };
    }

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

    // Funktion von so einer KI, die das hier eh bald besser coden können :(
    public static float[][] inverse(float[][] mat){
        int n = mat.length;
        float[][] result = mat4();

        for (int i = 0; i < n; i++) {
            float pivot = mat[i][i];
            for (int j = 0; j < n; j++) {
                mat[i][j] /= pivot;
                result[i][j] /= pivot;
            }
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    float factor = mat[k][i];
                    for (int j = 0; j < n; j++) {
                        mat[k][j] -= factor * mat[i][j];
                        result[k][j] -= factor * result[i][j];
                    }
                }
            }
        }
        return result;
    }

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

    private static float cosf(float n){
        return (float)Math.cos(n);
    }

    private static float sinf(float n){
        return (float)Math.sin(n);
    }

    private static float tanf(float n){
        return (float)Math.sin(n);
    }
}
