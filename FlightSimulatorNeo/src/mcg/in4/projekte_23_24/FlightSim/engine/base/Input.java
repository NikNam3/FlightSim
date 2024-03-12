package mcg.in4.projekte_23_24.FlightSim.engine.base;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Helper class for mouse and key input
 * @version 1.0
 * @since 1.0
 * @author Maximo, Vincent
 */
public class Input {

    // redefine all relevant key ids so that GLFW does not have to be imported by the users
    public static final int KEY_ID_A = GLFW_KEY_A;
    public static final int KEY_ID_B = GLFW_KEY_B;
    public static final int KEY_ID_C = GLFW_KEY_C;
    public static final int KEY_ID_D = GLFW_KEY_D;
    public static final int KEY_ID_E = GLFW_KEY_E;
    public static final int KEY_ID_F = GLFW_KEY_F;
    public static final int KEY_ID_G = GLFW_KEY_G;
    public static final int KEY_ID_H = GLFW_KEY_H;
    public static final int KEY_ID_I = GLFW_KEY_I;
    public static final int KEY_ID_J = GLFW_KEY_J;
    public static final int KEY_ID_K = GLFW_KEY_K;
    public static final int KEY_ID_L = GLFW_KEY_L;
    public static final int KEY_ID_M = GLFW_KEY_M;
    public static final int KEY_ID_N = GLFW_KEY_N;
    public static final int KEY_ID_O = GLFW_KEY_O;
    public static final int KEY_ID_P = GLFW_KEY_P;
    public static final int KEY_ID_Q = GLFW_KEY_Q;
    public static final int KEY_ID_R = GLFW_KEY_R;
    public static final int KEY_ID_S = GLFW_KEY_S;
    public static final int KEY_ID_T = GLFW_KEY_T;
    public static final int KEY_ID_U = GLFW_KEY_U;
    public static final int KEY_ID_V = GLFW_KEY_V;
    public static final int KEY_ID_W = GLFW_KEY_W;
    public static final int KEY_ID_X = GLFW_KEY_X;
    public static final int KEY_ID_Y = GLFW_KEY_Y;
    public static final int KEY_ID_Z = GLFW_KEY_Z;
    public static final int KEY_ID_SHIFT = GLFW_KEY_LEFT_SHIFT;
    public static final int KEY_ID_SPACE = GLFW_KEY_SPACE;
    public static final int KEY_ID_UP    = GLFW_KEY_UP;
    public static final int KEY_ID_DOWN  = GLFW_KEY_DOWN;
    public static final int KEY_ID_LEFT  = GLFW_KEY_LEFT;
    public static final int KEY_ID_RIGHT = GLFW_KEY_RIGHT;

    /**
     * address of glfw window
     *
     *
     */
    private static long glfwWindowPointer;

    /**
     * Setup with the GLFW window<br>Must be called once before all other methods
     * @param glfwWindowPointer Address of glfw window
     *
     * @author Vincent Lahmann
     */
    public static void init(long glfwWindowPointer){
        Input.glfwWindowPointer = glfwWindowPointer;
    }

    /**
     * Checks status of a keyboard key
     * @param keyId ID of the key
     * @return True if key is pressed
     *
     * @author Vincent
     */
    public static boolean isKeyDown(int keyId){
        return glfwGetKey(glfwWindowPointer, keyId) == 1;
    }
}
