package main.core.graphics.scenes;

import javax.print.attribute.TextSyntax;

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
import main.core.graphics.ui.Button;
import main.core.graphics.ui.ClickableArea;
import main.core.graphics.ui.Container;
import main.core.graphics.utils.Consts;
import main.core.graphics.GFX;

public class TitleScreen implements ILogic {

    private final Window window;
    private boolean startGame;
    private Camera camera;
    private SceneManager scene;
    private RenderManager renderer;
    private DirectionalLight light;
    private Vector3f cameraInc;

    private final float screenWidth = 2560f, screenHeight = 1440f; // Not the actual expected values of width and height, just to use for positioning elements

    private Entity titleScreenBackground;
    private float[] titleScreenDimensions           = {0000f/screenWidth, 0000f/screenHeight, 2560f/screenWidth, 1440f/screenHeight};
    private float[] titleScreenWH = {Math.abs(titleScreenDimensions[0] - titleScreenDimensions[2]), Math.abs(titleScreenDimensions[1] - titleScreenDimensions[3])};
    private Container titleLogoContainer;
    private float[] titleLogoContainerDimensions    = {0137f/screenWidth, 0087f/screenHeight, 0853f/screenWidth, 0355f/screenHeight};
    private float[] titleLogoContainerWH = {Math.abs(titleLogoContainerDimensions[0] - titleLogoContainerDimensions[2]), Math.abs(titleLogoContainerDimensions[1] - titleLogoContainerDimensions[3])};
    private Entity titleLogo;
    private float[] titleLogoDimensions             = {0167f/screenWidth, 0117f/screenHeight, 0823f/screenWidth, 0325f/screenHeight};
    private float[] titleLogoWH = {Math.abs(titleLogoDimensions[0] - titleLogoDimensions[2]), Math.abs(titleLogoDimensions[1] - titleLogoDimensions[3])};
    private Container buttonsContainer;
    private float[] buttonsContainerDimensions      = {0137f/screenWidth, 0386f/screenHeight, 0853f/screenWidth, 0987f/screenHeight};
    private float[] buttonsContainerWH = {Math.abs(buttonsContainerDimensions[0] - buttonsContainerDimensions[2]), Math.abs(buttonsContainerDimensions[1] - buttonsContainerDimensions[3])};
    private Button newGameButton;
    private float[] newGameButtonDimensions         = {0197f/screenWidth, 0446f/screenHeight, 0793f/screenWidth, 0543f/screenHeight};
    private float[] newGameButtonWH = {Math.abs(newGameButtonDimensions[0] - newGameButtonDimensions[2]), Math.abs(newGameButtonDimensions[1] - newGameButtonDimensions[3])};
    private Button loadGameButton;
    private float[] loadGameButtonDimensions        = {0197f/screenWidth, 0574f/screenHeight, 0793f/screenWidth, 0671f/screenHeight};
    private float[] loadGameButtonWH = {Math.abs(loadGameButtonDimensions[0] - loadGameButtonDimensions[2]), Math.abs(loadGameButtonDimensions[1] - loadGameButtonDimensions[3])};
    private Button settingsButton;
    private float[] settingsButtonDimensions        = {0197f/screenWidth, 0720f/screenHeight, 0793f/screenWidth, 0799f/screenHeight};
    private float[] settingsButtonWH = {Math.abs(settingsButtonDimensions[0] - settingsButtonDimensions[2]), Math.abs(settingsButtonDimensions[1] - settingsButtonDimensions[3])};
    private Button nudgeButton;
    private float[] nudgeButtonDimensions           = {0197f/screenWidth, 0830f/screenHeight, 0793f/screenWidth, 0927f/screenHeight};
    private float[] nudgeButtonWH = {Math.abs(nudgeButtonDimensions[0] - nudgeButtonDimensions[2]), Math.abs(nudgeButtonDimensions[1] - nudgeButtonDimensions[3])};

    public TitleScreen() {
        window = Engine.getWindow();
        startGame = false;
        camera = new Camera();
        scene = new SceneManager();
        renderer = new RenderManager();
        cameraInc = new Vector3f(0);

        try {
            Engine.loadTextures();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

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
        float[] backgroundLayerDimensions = Engine.calculateQuadDimensions(backgroundLayer);
        int containerLayer = 9;
        float[] containerLayerDimensions = Engine.calculateQuadDimensions(containerLayer);
        int logoLayer = 8;
        float[] logoLayerDimensions = Engine.calculateQuadDimensions(logoLayer);
        int buttonLayer = 2;
        float[] buttonLayerDimensions = Engine.calculateQuadDimensions(buttonLayer);
        int textLayer = 1;
        float[] textLayerDimensions = Engine.calculateQuadDimensions(textLayer);

        renderer.init();

        // REMEMBER TO PUT TEXTURES IN GFX.java FILE

        // Create quads for background and text
        Model titleScreenBackgroundModel = ModelManager.createQuad(
            titleScreenWH[0] * backgroundLayerDimensions[0], // Why is this one different?
            titleScreenWH[1] * backgroundLayerDimensions[1]
        );
        titleScreenBackgroundModel.setTexture("title_bg");
        Model titleLogoContainerModel = ModelManager.createQuad(
            titleLogoContainerWH[1] * containerLayerDimensions[1],
            titleLogoContainerWH[0] * containerLayerDimensions[0]
        );
        Model titleLogoModel = ModelManager.createQuad(
            titleLogoWH[1] * logoLayerDimensions[1],
            titleLogoWH[0] * logoLayerDimensions[0]
        );
        titleLogoModel.setTexture("title_logo");
        Model buttonsContainerModel = ModelManager.createQuad(
            buttonsContainerWH[1] * containerLayerDimensions[1],
            buttonsContainerWH[0] * containerLayerDimensions[0]
        );
        Model newGameButtonModel = ModelManager.createQuad(
            newGameButtonWH[1] * buttonLayerDimensions[1],
            newGameButtonWH[0] * buttonLayerDimensions[0]
        );
        Model loadGameButtonModel = ModelManager.createQuad(
            loadGameButtonWH[1] * buttonLayerDimensions[1],
            loadGameButtonWH[0] * buttonLayerDimensions[0]
        );
        Model settingsButtonModel = ModelManager.createQuad(
            settingsButtonWH[1] * buttonLayerDimensions[1],
            settingsButtonWH[0] * buttonLayerDimensions[0]
        );
        Model nudgeButtonModel = ModelManager.createQuad(
            nudgeButtonWH[1] * buttonLayerDimensions[1],
            nudgeButtonWH[0] * buttonLayerDimensions[0]
        );

        // Create entities
        titleScreenBackground = new Entity(
            titleScreenBackgroundModel,
            new Vector3f(
                (titleScreenDimensions[0] * 0.5f) * backgroundLayerDimensions[0], // why is this one different?
                -(titleScreenDimensions[1] * 1.0f) * backgroundLayerDimensions[1],
                -backgroundLayer
            )
        );
        titleLogoContainer = new Container(
            new Entity(
                titleLogoContainerModel,
                new Vector3f(
                    (titleLogoContainerDimensions[0] - 0.5f) * containerLayerDimensions[1] + (titleLogoContainerDimensions[0] * 0.5f * containerLayerDimensions[1]),
                    -(titleLogoContainerDimensions[1] * 1.0f) * containerLayerDimensions[0],
                    -containerLayer
                )
            ),
            "container_base"
        );
        titleLogo = new Entity(
            titleLogoModel,
            new Vector3f(
                (titleLogoDimensions[0] - 0.5f) * logoLayerDimensions[1] + (titleLogoDimensions[0] * 0.5f * logoLayerDimensions[1]),
                -(titleLogoDimensions[1] * 1.0f) * logoLayerDimensions[0],
                -logoLayer
            )
        );
        buttonsContainer = new Container(
            new Entity(
                buttonsContainerModel,
                new Vector3f(
                    (buttonsContainerDimensions[0] - 0.5f) * containerLayerDimensions[1] + (buttonsContainerDimensions[0] * 0.5f * containerLayerDimensions[1]),
                    -(buttonsContainerDimensions[1] * 1.0f) * containerLayerDimensions[0],
                    -containerLayer
                )
            ),
            "container_base"
        );
        newGameButton = new Button(
            new Entity(
                newGameButtonModel,
                new Vector3f(
                    (newGameButtonDimensions[0] - 0.5f) * buttonLayerDimensions[1] + (newGameButtonDimensions[0] * 0.5f * buttonLayerDimensions[1]),
                    -(newGameButtonDimensions[1] * 1.0f) * buttonLayerDimensions[0],
                    -buttonLayer
                )
            ),
            "new_game_button_bg",
            "new_game_button_hover_bg",
            () -> {
                System.out.println("Hover");
            },
            "new_game_button_click_bg",
            () -> {
                System.out.println("Left Click");
            },
            "new_game_button_click_bg",
            () -> {
                System.out.println("Right Click");
            }
        );
        loadGameButton = new Button(
            new Entity(
                loadGameButtonModel,
                new Vector3f(
                    (loadGameButtonDimensions[0] - 0.5f) * buttonLayerDimensions[1] + (loadGameButtonDimensions[0] * 0.5f * buttonLayerDimensions[1]),
                    -(loadGameButtonDimensions[1] * 1.0f) * buttonLayerDimensions[0],
                    -buttonLayer
                )
            ),
            "load_game_button_bg",
            "load_game_button_hover_bg",
            () -> {
                System.out.println("Hover");
            },
            "load_game_button_click_bg",
            () -> {
                System.out.println("Left Click");
            },
            "load_game_button_click_bg",
            () -> {
                System.out.println("Right Click");
            }
        );
        settingsButton = new Button(
            new Entity(
                settingsButtonModel,
                new Vector3f(
                    (settingsButtonDimensions[0] - 0.5f) * buttonLayerDimensions[1] + (settingsButtonDimensions[0] * 0.5f * buttonLayerDimensions[1]),
                    -(settingsButtonDimensions[1] * 1.0f) * buttonLayerDimensions[0],
                    -buttonLayer
                )
            ),
            "title_settings_button_bg",
            "title_settings_button_hover_bg",
            () -> {
                System.out.println("Hover");
            },
            "title_settings_button_click_bg",
            () -> {
                System.out.println("Left Click");
            },
            "title_settings_button_click_bg",
            () -> {
                System.out.println("Right Click");
            }
        );
        nudgeButton = new Button(
            new Entity(
                nudgeButtonModel,
                new Vector3f(
                    (nudgeButtonDimensions[0] - 0.5f) * buttonLayerDimensions[1] + (nudgeButtonDimensions[0] * 0.5f * buttonLayerDimensions[1]),
                    -(nudgeButtonDimensions[1] * 1.0f) * buttonLayerDimensions[0],
                    -buttonLayer
                )
            ),
            "title_nudge_button_bg",
            "title_nudge_button_hover_bg",
            () -> {
                System.out.println("Hover");
            },
            "title_nudge_button_click_bg",
            () -> {
                System.out.println("Left Click");
            },
            "title_nudge_button_click_bg",
            () -> {
                System.out.println("Right Click");
            }
        );

        // Add models to scene
        scene.addEntity(titleScreenBackground);
        scene.addEntity(titleLogoContainer);
        scene.addEntity(titleLogo);
        scene.addEntity(buttonsContainer);
        scene.addEntity(newGameButton);
        scene.addEntity(loadGameButton);
        scene.addEntity(settingsButton);
        scene.addEntity(nudgeButton);

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
        if (window.isKeyPressed(GLFW.GLFW_KEY_T))
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

        newGameButton.update(mouse, camera);
        loadGameButton.update(mouse, camera);
        settingsButton.update(mouse, camera);
        nudgeButton.update(mouse, camera);
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

    @Override
    public Camera getCamera() {
        return camera;
    }
}
