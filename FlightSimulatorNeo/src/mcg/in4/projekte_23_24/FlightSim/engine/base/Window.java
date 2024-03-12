package mcg.in4.projekte_23_24.FlightSim.engine.base;

import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL41.*;

import static org.lwjgl.glfw.GLFW.*;

/**
 * TODO add description (remember to put a author for every method)
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

    public static void fitViewport(){
        int[] contentArea = getContentArea();
        glViewport(0, 0, contentArea[0], contentArea[1]);
    }

    public static int[] getContentArea(){
        return new int[]{width, height};
    }

    public static void newFrame(){
        glfwPollEvents();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void showFrame(){
        glfwSwapBuffers(glfwWindowAddress);
    }

    public static long getGlfwWindowAddress(){
        return glfwWindowAddress;
    }

    public static boolean exitRequested(){
        return glfwWindowShouldClose(glfwWindowAddress);
    }
}
