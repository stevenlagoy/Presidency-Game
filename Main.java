import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import javafx.stage.Window;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class Main
{

    private static WindowManager window;
    private static EngineManager engine;

    public static void main(String[] args){
        window = new WindowManager("ENGINE", 0, 0, false);
        engine = new EngineManager();
        
        try {
            engine.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Engine.init();
        // Engine.language = Engine.Language.EN;
        
        // boolean active = true;
        // while (active) {
        //     active = Engine.tick();
        // }

        // Engine.done();
        // System.out.print("Main Done\n");
    }

    public static WindowManager getWindow() {
        return window;
    }
}
