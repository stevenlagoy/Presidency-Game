import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class RenderManager {
    
    private final WindowManager window;
    private ShaderManager shader;

    public RenderManager() {
        window = Main.getWindow();
    }

    public void init() throws Exception {
        shader = new ShaderManager();
        shader.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
        shader.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
        shader.link();

        GL11.glEnable(GL11.GL_DEPTH_TEST);

        shader.createUniform("textureSampler");
        shader.createUniform("transformationMatrix");
        shader.createUniform("projectionMatrix");
        shader.createUniform("viewMatrix");
        shader.createUniform("ambientLight");
        shader.createMaterialUniform("material");
        shader.createUniform("specularPower");
        shader.createDirectionalLightUniform("directionalLight");
        shader.createPointLightUniform("pointLight");
        shader.createSpotLightUniform("spotLight");
    }

    public void render(Entity entity, Camera camera, DirectionalLight directionalLight, PointLight pointLight, SpotLight spotLight) {
        clear();

        System.out.println("Rendering entity with " + entity.getModel().getVertexCount() + " vertices");
        System.out.println("Camera position: " + camera.getPosition());
        System.out.println("Camera rotation: " + camera.getRotation());
        System.out.println("Entity position: " + entity.getPos());
        System.out.println("Light direction: " + directionalLight.getDirection());

        if (!window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shader.bind();

        shader.setUniform("textureSampler", 0);
        shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix(entity));
        shader.setUniform("projectionMatrix", window.updateProjectionMatrix());
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
        shader.setUniform("material", entity.getModel().getMaterial());
        shader.setUniform("ambientLight", Engine.AMBIENT_LIGHT);
        shader.setUniform("specularPower", Engine.SPECULAR_POWER);
        shader.setUniform("directionalLight", directionalLight);
        shader.setUniform("pointLight", pointLight);
        shader.setUniform("spotLight", spotLight);
        
        GL30.glBindVertexArray(entity.getModel().getId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL13.glActiveTexture(GL13.GL_TEXTURE0); // match value set in uniform
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getId());
        GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

        // Cleanup
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);

        shader.unbind();
    }

    public void clear() {
        GL11.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        if (shader != null)
            shader.cleanup();
    }

}
