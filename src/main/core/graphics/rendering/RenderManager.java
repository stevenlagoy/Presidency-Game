package main.core.graphics.rendering;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import main.core.Engine;
import main.core.graphics.Camera;
import main.core.graphics.ShaderManager;
import main.core.graphics.Window;
import main.core.graphics.entity.Entity;
import main.core.graphics.entity.SceneManager;
import main.core.graphics.entity.terrain.Terrain;
import main.core.graphics.lighting.DirectionalLight;
import main.core.graphics.lighting.PointLight;
import main.core.graphics.lighting.SpotLight;

public class RenderManager {
    
    private Window window;
    private EntityRenderer entityRenderer;
    private TerrainRenderer terrainRenderer;

    private static boolean isCulling = false;
    
    public RenderManager() {
        window = Engine.getWindow();
    }

    public void init() throws Exception {
        entityRenderer = new EntityRenderer();
        terrainRenderer = new TerrainRenderer();
        entityRenderer.init();
        terrainRenderer.init();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void renderLights(PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight, ShaderManager shader, Vector3f ambientLight, float specularPower) {

        shader.setUniform("ambientLight", ambientLight);
        shader.setUniform("specularPower", specularPower);
    
        // Directional Light
        if (directionalLight != null) {
            shader.setUniform("directionalLight", directionalLight);
        }
        
        // Point Lights
        int numPointLights = pointLights != null ? pointLights.length : 0;
        for (int i = 0; i < numPointLights; i++) {
            shader.setUniform("pointLights", pointLights[i], i);
        }

        // Spot Lights
        int numSpotLights = spotLights != null ? spotLights.length : 0;
        for (int i = 0; i < numSpotLights; i++) {
            shader.setUniform("spotLights", spotLights[i], i);
        }
    }

    public void render(Camera camera, SceneManager scene) {
        window = Engine.getWindow();
        GL11.glClearColor(0, 0, 0.1f, 1);
        clear();

        if(window.isResize()){
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(false);
        }

        window.getProjectionMatrix().identity();
        window.updateProjectionMatrix();

        entityRenderer.render(camera, scene.getPointLights(), scene.getSpotLights(), scene.getDirectionalLight(), scene.getAmbientLight(), scene.getSpecularPower());
        terrainRenderer.render(camera, scene.getPointLights(), scene.getSpotLights(), scene.getDirectionalLight(), scene.getAmbientLight(), scene.getSpecularPower());
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
