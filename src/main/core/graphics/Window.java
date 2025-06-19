package main.core.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import main.core.graphics.utils.Consts;

public class Window {
    
    private final String title;

    private int width, height;
    private long window;

    private float FOV;

    private boolean resize, vSync;

    private Matrix4f projectionMatrix;

    public Window(String title, int width, int height, float FOV, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.FOV = FOV;
        this.vSync = vSync;
        projectionMatrix = new Matrix4f();
    }

    public void init() {

        boolean maximized = false;
        if (width == 0 || height == 0) {
            width = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()).width();
            height = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()).height();
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
            maximized = true;
        }

        window = GLFW.glfwCreateWindow(width, height, title, resize ? GLFW.glfwGetPrimaryMonitor() : 0, 0);
        if (window == MemoryUtil.NULL)
            throw new RuntimeException("Failed to create GLFW window");

        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResize(true);
        });

        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            // System.out.println("Key pressed: " + key + ", action: " + action);
            if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
                GLFW.glfwSetWindowShouldClose(window, true);
        });

        if(maximized) {
            GLFW.glfwMaximizeWindow(window);
        }
        else {
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
        }

        GLFW.glfwMakeContextCurrent(window);

        if(isvSync())
            GLFW.glfwSwapInterval(1);

        updateProjectionMatrix();

        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        GL11.glClearColor(0.1f, 0.1f, 0.1f, 0.1f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
    }

    public void update() {
        if(resize) {
            GL11.glViewport(0, 0, width, height);
            resize = false;
        }
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    public void cleanup() {
        GLFW.glfwMakeContextCurrent(window);
        
        if (window != MemoryUtil.NULL) {
            GL.setCapabilities(null);
            GLFW.glfwHideWindow(window);
            GLFW.glfwDestroyWindow(window);
            window = MemoryUtil.NULL;
        }
    }

    public void setClearColor(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
    }

    public boolean isKeyPressed(int keycode) {
        return GLFW.glfwGetKey(window, keycode) == GLFW.GLFW_PRESS;
    }

    public boolean isMouseButtonPressed(int button) {
        return GLFW.glfwGetMouseButton(window, button) == GLFW.GLFW_PRESS;
    }

    public boolean windowShouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(window, title);
    }

    public boolean isResize() {
        return resize;
    }
    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public boolean isvSync() {
        return vSync;
    }
    public void setvSync(boolean vSync){
        this.vSync = vSync;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getFOV() {
        return FOV;
    }

    public float getAspectRatio() {
        return (float) width / height;
    }

    public long getWindow() {
        return window;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f updateProjectionMatrix() {
        return projectionMatrix.identity().perspective(
            FOV,
            getAspectRatio(),
            Consts.Z_NEAR,
            Consts.Z_FAR
        );
    }

    public Matrix4f updateProjectionMatrix(Matrix4f matrix, int width, int height){
        return matrix.identity().perspective(
            FOV,
            getAspectRatio(),
            Consts.Z_NEAR,
            Consts.Z_FAR
        );
    }

    public Vector3f calculateRayDirection(float ndcX, float ndcY) {
        Vector4f clipCoords = new Vector4f(ndcX, ndcY, -1.0f, 1.0f);
        Matrix4f invProj = new Matrix4f(getProjectionMatrix()).invert();
        Vector4f eyeCoords = invProj.transform(clipCoords);
        eyeCoords.z = -1.0f;
        eyeCoords.w = 0.0f;

        // Camera looks down -Z, so direction is (eyeCoords.x, eyeCoords.y, -1)
        // If you have a view matrix (for a moving/rotating camera), apply its inverse here:
        // Matrix4f invView = new Matrix4f(camera.getViewMatrix()).invert();
        // invView.transformDirection(eyeCoords);

        // Normalize to get direction
        Vector3f rayDir = new Vector3f(eyeCoords.x, eyeCoords.y, eyeCoords.z).normalize();
        return rayDir;
    }

    public Vector3f calculateRayDirection(float ndcX, float ndcY, Matrix4f viewMatrix) {
        Vector4f clipCoords = new Vector4f(ndcX, ndcY, -1.0f, 1.0f);
        Matrix4f invProj = new Matrix4f(getProjectionMatrix()).invert();
        Vector4f eyeCoords = invProj.transform(clipCoords);
        eyeCoords.z = -1.0f;
        eyeCoords.w = 0.0f;
        Matrix4f invView = new Matrix4f(viewMatrix).invert();
        Vector4f worldCoords = invView.transform(eyeCoords);
        return new Vector3f(worldCoords.x, worldCoords.y, worldCoords.z).normalize();
    }
}
