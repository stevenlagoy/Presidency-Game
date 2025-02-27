import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class LWJGLExample {
    private long window;

    public void run() {
        init();
        loop();

        // Cleanup
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private void init() {
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        window = glfwCreateWindow(800, 600, "LWJGL Window", NULL, NULL);
        if (window == NULL) throw new RuntimeException("Failed to create window");

        glfwMakeContextCurrent(window);
        glfwShowWindow(window);
    }

    private void loop() {
        GL.createCapabilities();
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new LWJGLExample().run();
    }
}
