package engine;

import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL41;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private static long glfwWindowAddress = 0;
    private static int width;
    private static int height;

    public static void initWindow(int width, int height, String title){
        glfwInit();
        glfwWindowAddress = glfwCreateWindow(width, height, title, 0, 0);
        glfwMakeContextCurrent(glfwWindowAddress);
        glfwSwapInterval(1);
        GL.createCapabilities();
        GL41.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        GL41.glEnable(GL41.GL_DEPTH_TEST);

        Window.width  = width;
        Window.height = height;

        glfwSetWindowSizeCallback(glfwWindowAddress, new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                Window.width  = width;
                Window.height = height;
                GL41.glViewport(0, 0, width, height);
            }
        });
    }

    public static int getWidth(){
        return width;
    }

    public static int getHeight(){
        return height;
    }

    public static void newFrame(){
        glfwPollEvents();
        GL41.glClear(GL41.GL_COLOR_BUFFER_BIT | GL41.GL_DEPTH_BUFFER_BIT);
    }

    public static void refresh(){
        glfwSwapBuffers(glfwWindowAddress);
    }

    public static long getGlfwWindowAddress(){
        return glfwWindowAddress;
    }

    public static boolean exitRequested(){
        return glfwWindowShouldClose(glfwWindowAddress);
    }
}
