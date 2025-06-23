package main.core.graphics.scenes;

import java.util.Arrays;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import main.core.Engine;
import main.core.graphics.Camera;
import main.core.graphics.ILogic;
import main.core.graphics.MouseInput;
import main.core.graphics.Window;
import main.core.graphics.entity.Entity;
import main.core.graphics.entity.SceneManager;
import main.core.graphics.entity.Entity.EntityType;
import main.core.graphics.entity.Model;
import main.core.graphics.entity.ModelManager;
import main.core.graphics.entity.QuadModel;
import main.core.graphics.lighting.DirectionalLight;
import main.core.graphics.rendering.RenderManager;
import main.core.graphics.ui.Button;
import main.core.graphics.ui.Container;
import main.core.graphics.ui.Quad;
import main.core.graphics.utils.Consts;

public class BlankScene implements ILogic {

    private final Window window;
    private Camera camera;
    private SceneManager scene;
    private RenderManager renderer;
    private DirectionalLight light;
    private Vector3f cameraInc;

    // Layers

    // Entities

    private final float screenWidth = 2560f, screenHeight = 1440f; // Not the actual expected values of width and height, just to use for positioning elements

    // REMEMBER TO PUT TEXTURES IN GFX.java FILE
    private Entity[] entities        = {};
    private EntityType[] entityTypes = {};
    private float[][] XYArrays       = {};
    private int[] layers             = {};
    private Model[] models           = {};
    private String[][] textureNames  = {};

    private Runnable[][] runnables = new Runnable[entities.length][];
    private float[][] layersDimensions = new float[layers.length][];
    private float[][] WHArrays = new float[XYArrays.length][];

    public BlankScene() {
        window = Engine.getWindow();
        camera = new Camera();
        scene = new SceneManager();
        renderer = new RenderManager();
        cameraInc = new Vector3f(0);

        // Set runnables

        try {
            Engine.loadTextures();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        scene.setAmbientLight(Consts.AMBIENT_LIGHT);
        scene.setSpecularPower(Consts.SPECULAR_POWER);

        // Initialize directional light
        Vector3f lightColor = new Vector3f(1.0f, 1.0f, 1.0f);
        Vector3f lightDirection = new Vector3f(0.0f, 1.0f, -1.0f).normalize();
        float lightIntensity = 1.5f;
        light = new DirectionalLight(lightColor, lightDirection, lightIntensity);
        scene.setDirectionalLight(light);
    }

    @Override
    public void init() throws Exception {
        
        if (entities.length != XYArrays.length || entities.length != layers.length || entities.length != models.length) {
            throw new IndexOutOfBoundsException("The entities array, XY/WH arrays, and layer array must have the same length");
        }

        int max = -1;
        for (int i : layers) max = i > max ? i : max;
        layersDimensions = new float[max+1][];
        for (int i = 0; i <= max; i++) {
            layersDimensions[i] = Engine.calculateQuadDimensions(i);
        }

        for (int i = 0; i < XYArrays.length; i++) {
            XYArrays[i] = new float[] {XYArrays[i][0] / screenWidth, XYArrays[i][1] / screenHeight, XYArrays[i][2] / screenWidth, XYArrays[i][3] / screenHeight};
            WHArrays[i] = new float[] {Math.abs(XYArrays[i][0] - XYArrays[i][2]), Math.abs(XYArrays[i][1] - XYArrays[i][3])};
        }

        renderer.init();

        // Create quads for background and text

        for (int i = 0; i < WHArrays.length; i++) {
            models[i] = ModelManager.createQuad(
                WHArrays[i][0] * layersDimensions[layers[i]][0],
                WHArrays[i][1] * layersDimensions[layers[i]][1]
            );
        }

        // Create entities

        for (int i = 0; i < entities.length; i++) {

            float left = Math.min(XYArrays[i][0], XYArrays[i][2]);
            float right = Math.max(XYArrays[i][0], XYArrays[i][2]);
            float top = Math.min(XYArrays[i][1], XYArrays[i][3]);
            float bottom = Math.max(XYArrays[i][1], XYArrays[i][3]);

            float centerX = (left + right) / 2.0f;
            float centerY = (top + bottom) / 2.0f;

            float glX = (centerX - 0.5f) * layersDimensions[layers[i]][0];
            float glY = (0.5f - centerY) * layersDimensions[layers[i]][1];

            Vector3f position = new Vector3f(glX, glY, -layers[i]);

            switch (entityTypes[i]) {
                case QUAD:
                    entities[i] = new Quad((QuadModel) models[i], position);
                    entities[i].getModel().setTexture(textureNames[i][0]);
                    break;
                case BUTTON:
                    entities[i] = new Button(
                        new Entity(models[i], position),
                        textureNames[i][0], textureNames[i][1], runnables[i][0], textureNames[i][2], runnables[i][1], textureNames[i][3], runnables[i][2]
                    );
                    break;
                case CONTAINER:
                    entities[i] = new Container( new Entity(models[i], position), textureNames[i][0]);
                    break;
                case ENTITY:
                    entities[i] = new Entity(models[i], position);
                    entities[i].getModel().setTexture(textureNames[i][0]);
                    break;
            }
        }

        // Change draw order to ensure back-to-front
        Arrays.sort(entities, (a, b) -> Float.compare(a.getPos().z, b.getPos().z));

        // Add entities to scene
        for (Entity entity : entities) {
            scene.addEntity(entity);
        }

        camera.setPosition(0);
        camera.setRotation(0);
    }

    @Override
    public void input() {
        cameraInc.set(0, 0, 0);
        if (Engine.DEBUG_MODE) {
            if(window.isKeyPressed(GLFW.GLFW_KEY_W))
                cameraInc.z = -10;
            if(window.isKeyPressed(GLFW.GLFW_KEY_S))
                cameraInc.z = 10;
                if(window.isKeyPressed(GLFW.GLFW_KEY_A))
                cameraInc.x = -10;
            if(window.isKeyPressed(GLFW.GLFW_KEY_D))
                cameraInc.x = 10;
            if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
                cameraInc.y = -10;
            if(window.isKeyPressed(GLFW.GLFW_KEY_SPACE))
                cameraInc.y = 10;
            if(window.isKeyPressed(GLFW.GLFW_KEY_Q))
                camera.moveRotation(0.0f, 0.0f, -0.5f);
            if(window.isKeyPressed(GLFW.GLFW_KEY_E))
                camera.moveRotation(0.0f, 0.0f, 0.5f);
            if(window.isKeyPressed(GLFW.GLFW_KEY_R))
                camera.setRotation(0);
            if (window.isKeyPressed(GLFW.GLFW_KEY_T))
                camera.setPosition(0);
        }
    }

    @Override
    public void update(float interval, MouseInput mouse) {
        camera.movePosition(cameraInc.x * Consts.CAMERA_MOVE_SPEED, cameraInc.y * Consts.CAMERA_MOVE_SPEED, cameraInc.z * Consts.CAMERA_MOVE_SPEED);

        if (Engine.DEBUG_MODE) {
            if (mouse.isRightButtonPress()) {
                Vector2f rotVec = mouse.getDisplVec();
                camera.moveRotation(rotVec.x * Consts.MOUSE_SENSITIVITY, rotVec.y * Consts.MOUSE_SENSITIVITY, 0);
            }
        }

        for (Entity entity : scene.getEntities()) {
            renderer.processEntity(entity);

            if (entity instanceof Button button) {
                button.update(mouse, camera);
            }
        }
    }

    @Override
    public void render() {
        renderer.render(camera, scene);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
    }

    @Override
    public Camera getCamera() {
        return camera;
    }
    
}
