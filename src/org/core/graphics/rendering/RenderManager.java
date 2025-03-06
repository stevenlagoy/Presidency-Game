package src.org.core.graphics.rendering;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import org.joml.Vector3f;
import src.org.core.graphics.Camera;
import src.org.core.graphics.ShaderManager;
import src.org.core.graphics.WindowManager;
import src.org.core.graphics.entity.Entity;
import src.org.core.graphics.entity.SceneManager;
import src.org.core.graphics.entity.terrain.Terrain;
import src.org.core.graphics.game.Launcher;
import src.org.core.graphics.lighting.DirectionalLight;
import src.org.core.graphics.lighting.PointLight;
import src.org.core.graphics.lighting.SpotLight;

public class RenderManager {
    
    private final WindowManager window;
    private EntityRenderer entityRenderer;
    private TerrainRenderer terrainRenderer;

    private static boolean isCulling = false;
    
        public RenderManager() {
            window = Launcher.getWindow();
        }
    
        public void init() throws Exception {
            entityRenderer = new EntityRenderer();
            terrainRenderer = new TerrainRenderer();
            entityRenderer.init();
            terrainRenderer.init();

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
    
        public static void renderLights(PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight, ShaderManager shader) {
            shader.setUniform("ambientLight", new Vector3f(0.6f, 0.6f, 0.6f));
            shader.setUniform("specularPower", 10f);
        
            int numLights;
            // Create Spot Lights
            numLights = spotLights != null ? spotLights.length : 0;
            for (int i = 0; i < numLights; i++) {
                shader.setUniform("spotLights", spotLights[i], i);
            }
            // Create Point Lights
            numLights = pointLights != null ? pointLights.length : 0;
            for (int i = 0; i < numLights; i++) {
                shader.setUniform("pointLights", pointLights[i], i);
            }
    
            shader.setUniform("directionalLight", directionalLight);
        }
    
        public void render(Camera camera, SceneManager scene) {
            clear();
    
            if(window.isResize()){
                GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
                window.setResize(false);
            }

            window.getProjectionMatrix().identity();
            window.updateProjectionMatrix();
    
            entityRenderer.render(camera, scene.getPointLights(), scene.getSpotLights(), scene.getDirectionalLight());
            terrainRenderer.render(camera, scene.getPointLights(), scene.getSpotLights(), scene.getDirectionalLight());
        }
    
    public static void enableCulling() {
        if(!isCulling) {
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glCullFace(GL11.GL_BACK);
            isCulling = true;
        }
    }

    public static void disableCulling() {
        if(isCulling) {
            GL11.glDisable(GL11.GL_CULL_FACE);
            isCulling = false;
        }
    }

    public void processEntity(Entity entity) {
        List<Entity> entityList = entityRenderer.getEntities().get(entity.getModel());
        if (entityList != null) {
            entityList.add(entity);
        }
        else {
            List<Entity> newEntityList = new ArrayList<>();
            newEntityList.add(entity);
            entityRenderer.getEntities().put(entity.getModel(), newEntityList);
        }
    }

    public void processTerrain(Terrain terrain) {
        if (terrain != null) terrainRenderer.getTerrain().add(terrain);
    }

    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        entityRenderer.cleanup();
        terrainRenderer.cleanup();
    }

}
