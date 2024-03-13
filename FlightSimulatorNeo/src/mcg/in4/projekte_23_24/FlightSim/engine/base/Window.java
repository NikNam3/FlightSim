package mcg.in4.projekte_23_24.FlightSim.engine.base;

import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL41.*;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Static class for handling basic window operations
 *
 * @version 1.0
 * @since 1.0
 * @author Vincent Lahmann
 */

public class Window {
    private static long glfwWindowAddress = 0;
    private static int width;
    private static int height;

    public static void create(int width, int height, String title){
        glfwInit();
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        glfwWindowAddress = glfwCreateWindow(width, height, title, 0, 0);
        glfwMakeContextCurrent(glfwWindowAddress);
        glfwSwapInterval(1);
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 1.f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Window.width  = width;
        Window.height = height;

        glfwSetWindowSizeCallback(glfwWindowAddress, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                Window.width  = width;
                Window.height = height;
                fitViewport();
            }
        });
    }

    /**
     * Sets the OpenGL viewport to fill out the entire window
     * @author Vincent Lahmann
     */

    public static void fitViewport(){
        int[] contentArea = getContentArea();
        glViewport(0, 0, contentArea[0], contentArea[1]);
    }

    /**
     * Gets the current content area<br>[0] = pixel count x<br>[1] = pixel count y<br>
     * @return The current content area
     *
     * @author Vincent Lahmann
     */
    public static int[] getContentArea(){
        return new int[]{width, height};
    }

    /**
     * Clears screen buffers and polls all glfw events
     * @author Vincent Lahmann
     */
    public static void newFrame(){
        glfwPollEvents();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Displays everything that was draw to the screen buffer after newFrame()
     * @author Vincent Lahmann
     */
    public static void showFrame(){
        glfwSwapBuffers(glfwWindowAddress);
    }

    /**
     * @return The active glfw window address
     * @author Vincent Lahmann
     */
    public static long getGlfwWindowAddress(){
        return glfwWindowAddress;
    }

    /**
     * @return True if the user has pressed the X button
     * @author Vincent Lahmann
     */
    public static boolean exitRequested(){
        return glfwWindowShouldClose(glfwWindowAddress);
    }
}
