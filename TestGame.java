import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class TestGame implements ILogic{

    private static final float CAMERA_MOVE_SPEED = 0.01f;
    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;

    private Entity entity;
    private Camera camera;

    Vector3f cameraInc;

    public TestGame() {
        renderer = new RenderManager();
        window = Main.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        Model model = loader.loadOBJModel("/models/bunny.obj");
        model.setTexture(new Texture(loader.loadTexture("textures/blue.png")), 1f);
        entity = new Entity(model, new Vector3f(0, 0, -5), new Vector3f(0, 0, 0), 1);
    }

    @Override
    public void input() {
        cameraInc.set(0, 0, 0);
        if(window.isKeyPressed(GLFW.GLFW_KEY_W))
            cameraInc.z = -1;
        if(window.isKeyPressed(GLFW.GLFW_KEY_S))
            cameraInc.z = 1;
            if(window.isKeyPressed(GLFW.GLFW_KEY_A))
            cameraInc.x = -1;
        if(window.isKeyPressed(GLFW.GLFW_KEY_D))
            cameraInc.x = 1;
        if(window.isKeyPressed(GLFW.GLFW_KEY_Z))
            cameraInc.y = -1;
        if(window.isKeyPressed(GLFW.GLFW_KEY_X))
            cameraInc.y = 1;
        if(window.isKeyPressed(GLFW.GLFW_KEY_E))
            camera.moveRotation(0.0f, 0.5f, 0.0f);
        if(window.isKeyPressed(GLFW.GLFW_KEY_Q))
            camera.moveRotation(0.0f, -0.5f, 0.0f);
        if(window.isKeyPressed(GLFW.GLFW_KEY_R))
            camera.moveRotation(-0.5f, 0.0f, 0.0f);
        if(window.isKeyPressed(GLFW.GLFW_KEY_F))
            camera.moveRotation(0.5f, 0.0f, 0.0f);
        if(window.isKeyPressed(GLFW.GLFW_KEY_K))
            camera.moveRotation(0.0f, 0.0f, -0.5f);
        if(window.isKeyPressed(GLFW.GLFW_KEY_L))
            camera.moveRotation(0.0f, 0.0f, 0.5f);
    }

    @Override
    public void update(float interval, MouseInput mouse) {
        camera.movePosition(cameraInc.x * CAMERA_MOVE_SPEED, cameraInc.y * CAMERA_MOVE_SPEED, cameraInc.z * CAMERA_MOVE_SPEED);

        if (mouse.isRightButtonPress()) {
            Vector2f rotVec = mouse.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        entity.incRotation(0f, 0.25f, 0f);
    }

    @Override
    public void render() {
        renderer.render(entity, camera);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
    

    
}
