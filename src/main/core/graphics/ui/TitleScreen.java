package main.core.graphics.ui;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import main.core.Engine;
import main.core.graphics.Camera;
import main.core.graphics.ILogic;
import main.core.graphics.MouseInput;
import main.core.graphics.Window;
import main.core.graphics.entity.Entity;
import main.core.graphics.entity.Model;
import main.core.graphics.entity.ModelManager;
import main.core.graphics.entity.SceneManager;
import main.core.graphics.entity.TextureManager;
import main.core.graphics.lighting.DirectionalLight;
import main.core.graphics.rendering.RenderManager;
import main.core.graphics.utils.Consts;

public class TitleScreen implements ILogic {

    private final Window window;
    private boolean startGame;
    private Camera camera;
    private SceneManager scene;
    private RenderManager renderer;
    private DirectionalLight light;
    private Vector3f cameraInc;

    private Entity titleBackground;
    private Entity titleText;
    private Entity startButtonBackground;
    private ClickableArea startButton;

    public TitleScreen() {
        window = Engine.getWindow();
        startGame = false;
        camera = new Camera();
        scene = new SceneManager();
        renderer = new RenderManager();
        cameraInc = new Vector3f(0);

        scene.setAmbientLight(new Vector3f(0.8f));
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

        int backgroundLayer = 10;
        int buttonLayer = 2;
        int textLayer = 1;

        renderer.init();

        // Load textures
        TextureManager.addTexture("title_bg", "title_background");
        TextureManager.addTexture("title_text", "title_text");
        TextureManager.addTexture("button_bg", "button_background");

        // Create quads for background and text
        Model bgModel = ModelManager.createQuad(Engine.calculateQuadDimensions(window.getFOV(), backgroundLayer));
        Model textModel = ModelManager.createQuad(800f/200f, 80f/200f);
        float buttonX1 = -0.5f, buttonY1 = -0.3f, buttonX2 = 0.5f, buttonY2 = -0.1f;
        float buttonWidth = Math.abs(buttonX2 - buttonX1), buttonHeight = Math.abs(buttonY2 - buttonY1);
        float buttonCenterX = (buttonX1 + buttonX2) / 2.0f;
        float buttonCenterY = (buttonY1 + buttonY2) / 2.0f;
        Model startButtonModel = ModelManager.createQuad(buttonWidth, buttonHeight);

        // Position quads
        titleBackground = new Entity(bgModel, new Vector3f(0, 0, -backgroundLayer), new Vector3f(0), 1.0f);
        titleText = new Entity(textModel, new Vector3f(0, 0, -textLayer), new Vector3f(0), 0.5f);
        startButtonBackground = new Entity(startButtonModel, new Vector3f(buttonCenterX, buttonCenterY, -buttonLayer), new Vector3f(0), 1.0f);

        // Set textures
        bgModel.setTexture("title_bg");
        textModel.setTexture("title_text");
        startButtonModel.setTexture("button_bg");

        startButton = new ClickableArea(buttonX1, buttonY1, buttonX2, buttonY2, () -> {startGame = true; System.out.println("Click!");}, null);

        // Add models to scene
        scene.addEntity(titleBackground);
        scene.addEntity(titleText);
        scene.addEntity(startButtonBackground);

        camera.setPosition(0);
        camera.setRotation(0);
    }

    @Override
    public void input() {
        cameraInc.set(0, 0, 0);
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
        if (window.isKeyPressed(GLFW.GLFW_KEY_LEFT_ALT))
            camera.setPosition(0);
        if(window.isKeyPressed(GLFW.GLFW_KEY_TAB))
            startGame = true;
    }

    @Override
    public void update(float interval, MouseInput mouse) {
        camera.movePosition(cameraInc.x * Consts.CAMERA_MOVE_SPEED, cameraInc.y * Consts.CAMERA_MOVE_SPEED, cameraInc.z * Consts.CAMERA_MOVE_SPEED);

        if (mouse.isRightButtonPress()) {
            Vector2f rotVec = mouse.getDisplVec();
            camera.moveRotation(rotVec.x * Consts.MOUSE_SENSITIVITY, rotVec.y * Consts.MOUSE_SENSITIVITY, 0);
        }

        for (Entity entity : scene.getEntities()) {
            renderer.processEntity(entity);
        }

        startButton.update(mouse);
    }

    @Override
    public void render() {
        renderer.render(camera, scene);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
    }

    public boolean shouldStartGame() {
        return startGame;
    }
}
