package main.core.graphics.ui;

import main.core.Engine;
import main.core.graphics.Camera;
import main.core.graphics.ILogic;
import main.core.graphics.MouseInput;
import main.core.graphics.Window;
import main.core.graphics.entity.Entity;
import main.core.graphics.entity.SceneManager;
import main.core.graphics.rendering.RenderManager;
import main.core.graphics.utils.Consts;

public class BlankScene implements ILogic {

    private final Window window;
    private Camera camera;
    private SceneManager scene;
    private RenderManager renderer;

    public BlankScene() {
        window = Engine.getWindow();
        camera = new Camera();
        scene = new SceneManager();
        renderer = new RenderManager();

        scene.setAmbientLight(Consts.AMBIENT_LIGHT);
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        // Load Textures

        // Load Models

        // Set Textures

        // Create Entities

        // Add Entities to Scene
    }

    @Override
    public void input() {

    }

    @Override
    public void update(float interval, MouseInput mouse) {
        for (Entity entity : scene.getEntities()) {
            renderer.processEntity(entity);
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
    
}
