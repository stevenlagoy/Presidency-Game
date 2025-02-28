import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.nanovg.NanoSVG;

public class EngineManager {
    
    public static final long NANOSECOND = 1_000_000_000;
    public static final float FRAMERATE = 1000;

    private static int fps;
    private static float frametime = 1.0f / FRAMERATE;

    private boolean isRunning;

    private WindowManager window;
    private GLFWErrorCallback errorCallback;
    private MouseInput mouse;
    private ILogic gameLogic;

    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = Main.getWindow();
        gameLogic = Main.getGame();
        mouse = new MouseInput();
        window.init();
        gameLogic.init();
        mouse.init();
    }

    public void start() throws Exception {
        init();
        if(isRunning)
            return;
        run();
    }

    public void run() {
        this.isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while (isRunning) {
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) NANOSECOND;
            frameCounter += passedTime;

            input();

            while (unprocessedTime > frametime) {
                render = true;
                unprocessedTime -= frametime;

                if (window.windowShouldClose())
                    stop();
                
                if (frameCounter >= NANOSECOND) {
                    setFps(frames);
                    window.setTitle("ENGINE - FPS: " + getFps());
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if (render) {
                update(frametime);
                render();
                frames++;
            }
        }
        cleanup();
    }

    private void stop() {
        if(!isRunning)
            return;
        isRunning = false;
    }

    private void input() {
        gameLogic.input();
        mouse.input();
    }

    private void render() {
        gameLogic.render();
        window.update();
    }

    private void update(float interval) {
        gameLogic.update(interval, mouse);
    }

    private void cleanup() {
        window.cleanup();
        gameLogic.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    public static void setFps(int fps){
        EngineManager.fps = fps;
    }

    public static int getFps() {
        return fps;
    }

}
