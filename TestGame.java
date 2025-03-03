import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;

public class TestGame implements ILogic{

    private static final float CAMERA_MOVE_SPEED = 0.01f;
    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;

    private List<Entity> entities;
    private Camera camera;

    Vector3f cameraInc;

    private float lightAngle;
    private DirectionalLight directionalLight;
    private PointLight[] pointLights;
    private SpotLight[] spotLights;

    public TestGame() {
        renderer = new RenderManager();
        window = Main.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
        lightAngle = -90;
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        Model model = loader.loadOBJModel("/models/cube.obj");
        model.setTexture(new Texture(loader.loadTexture("textures/blue.png")), 1f);

        entities = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 200; i++) {
            float x = rand.nextFloat() * 100 - 50;
            float y = rand.nextFloat() * 100 - 50;
            float z = rand.nextFloat() * -300;
            entities.add(
                new Entity(model, new Vector3f(x, y, z),
                new Vector3f(rand.nextFloat() * 180, rand.nextFloat() * 180, 0),
                1)
            );
        }
        entities.add(new Entity(model, new Vector3f(0, 0, -2f), new Vector3f(0, 0, 0), 1));

        float lightIntensity;
        Vector3f lightPosition, lightColor;

        // Point Lights
        lightIntensity = 1.0f;
        lightPosition = new Vector3f(-0.5f, -0.5f, -3.2f);
        lightColor = new Vector3f(1, 1, 1);
        PointLight pointLight = new PointLight(lightColor, lightPosition, lightIntensity, 0, 0, 1);

        // Spot Lights
        Vector3f coneDirection = new Vector3f(0, 0, -1);
        float cutoff = (float) Math.cos(Math.toRadians(140));
        SpotLight spotLight0 = new SpotLight(new PointLight(lightColor, new Vector3f(0, 0, -4.6f), lightIntensity, 0, 0, 1), coneDirection, cutoff);
        SpotLight spotLight1 = new SpotLight(new PointLight(lightColor, lightPosition, lightIntensity, 0, 0, 1), coneDirection, cutoff);
        spotLight1.getPointLight().setPosition(new Vector3f(0.5f, 0.5f, -3.6f));

        // Directional Light
        lightIntensity = 0.0f;
        lightPosition = new Vector3f(-1, -10, 0);
        lightColor = new Vector3f(1, 1, 1);
        directionalLight = new DirectionalLight(lightColor, lightPosition, lightIntensity);

        pointLights = new PointLight[] {pointLight};
        spotLights = new SpotLight[] {spotLight0, spotLight1};
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
        if(window.isKeyPressed(GLFW.GLFW_KEY_Z))
            cameraInc.y = -10;
        if(window.isKeyPressed(GLFW.GLFW_KEY_X))
            cameraInc.y = 10;
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
        if(window.isKeyPressed(GLFW.GLFW_KEY_O))
            pointLights[0].getPosition().x += 0.1f;
        if(window.isKeyPressed(GLFW.GLFW_KEY_P))
            pointLights[0].getPosition().x -= 0.1f;
        float lightPosition = spotLights[0].getPointLight().getPosition().z;
        if(window.isKeyPressed(GLFW.GLFW_KEY_N))
            spotLights[0].getPointLight().getPosition().z = lightPosition + 0.1f;
        if(window.isKeyPressed(GLFW.GLFW_KEY_M))
            spotLights[0].getPointLight().getPosition().z = lightPosition - 0.1f;
    }

    @Override
    public void update(float interval, MouseInput mouse) {
        camera.movePosition(cameraInc.x * CAMERA_MOVE_SPEED, cameraInc.y * CAMERA_MOVE_SPEED, cameraInc.z * CAMERA_MOVE_SPEED);

        if (mouse.isRightButtonPress()) {
            Vector2f rotVec = mouse.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

        //entity.incRotation(0f, 0.25f, 0f);

        lightAngle += 0.1f;
        if(lightAngle > 90) {
            directionalLight.setIntensity(0);
            if (lightAngle >= 360)
                lightAngle = -90;
        }
        else if (lightAngle <= -80 || lightAngle >= 80) {
            float factor = 1 - (Math.abs(lightAngle) - 80) / 10.0f;
            directionalLight.setIntensity(factor);
            directionalLight.getColor().y = Math.max(factor, 0.9f);
            directionalLight.getColor().z = Math.max(factor, 0.5f);
        }
        else {
            directionalLight.setIntensity(1);
            directionalLight.getColor().x = 1;
            directionalLight.getColor().y = 1;
            directionalLight.getColor().z = 1;        
        }
        double angRad = Math.toRadians(lightAngle);
        directionalLight.getDirection().x = (float)Math.sin(angRad);
        directionalLight.getDirection().y = (float)Math.cos(angRad);

        for (Entity entity : entities) {
            renderer.processEntity(entity);
        }
    }

    @Override
    public void render() {
        renderer.render(camera, directionalLight, pointLights, spotLights);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
    

    
}
