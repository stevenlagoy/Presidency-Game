package main.core.graphics.rendering;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import main.core.graphics.Camera;
import main.core.graphics.ShaderManager;
import main.core.graphics.Transformation;
import main.core.graphics.entity.Material;
import main.core.graphics.entity.Model;
import main.core.graphics.entity.terrain.Terrain;
import main.core.graphics.game.Launcher;
import main.core.graphics.lighting.DirectionalLight;
import main.core.graphics.lighting.PointLight;
import main.core.graphics.lighting.SpotLight;
import main.core.graphics.utils.Consts;
import main.core.graphics.utils.Utils;

public class TerrainRenderer implements IRenderer<Object> {

    ShaderManager shader;
    private List<Terrain> terrains;

    public TerrainRenderer() throws Exception {
        terrains = new ArrayList<>();
        shader = new ShaderManager();
    }

    @Override
    public void init() throws Exception {
        shader.createVertexShader(Utils.loadResource("/shaders/terrain_vertex.vs"));
        shader.createFragmentShader(Utils.loadResource("/shaders/terrain_fragment.fs"));
        shader.link();
        shader.createUniform("projectionMatrix");
        shader.createUniform("backgroundTexture");
        shader.createUniform("redTexture");
        shader.createUniform("greenTexture");
        shader.createUniform("blueTexture");
        shader.createUniform("blendMap");
        shader.createUniform("transformationMatrix");
        shader.createUniform("viewMatrix");
        shader.createUniform("ambientLight");
        shader.createMaterialUniform("material");
        shader.createUniform("specularPower");
        shader.createDirectionalLightUniform("directionalLight");
        shader.createPointLightListUniform("pointLights", Consts.MAX_POINT_LIGHTS);
        shader.createSpotLightListUniform("spotLights", Consts.MAX_SPOT_LIGHTS);
    }

    @Override
    public void render(Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight) {
        shader.bind();
        shader.setUniform("projectionMatrix", Launcher.getWindow().updateProjectionMatrix());
        RenderManager.renderLights(pointLights, spotLights, directionalLight, shader);

        terrains.sort((t1, t2) -> {
            float dist1 = camera.getPosition().distance(t1.getPosition());
            float dist2 = camera.getPosition().distance(t2.getPosition());
            return Float.compare(dist2, dist1);
        });

        for (Terrain terrain : terrains) {
            bind(terrain.getModel());
            prepare(terrain, camera);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbind();
        }
        terrains.clear();
        shader.unbind();
    }

    @Override
    public void bind(Model model) {
        GL30.glBindVertexArray(model.getId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        Material material = model.getMaterial();
        if (material.hasTransparency()) {
            RenderManager.disableCulling();
        }
        else {
            RenderManager.enableCulling();
        }

        shader.setUniform("backgroundTexture", 0);
        shader.setUniform("redTexture", 1);
        shader.setUniform("greenTexture", 2);
        shader.setUniform("blueTexture", 3);
        shader.setUniform("blendMap", 4);
        
        shader.setUniform("material", model.getMaterial());
    }

    @Override
    public void unbind() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void prepare(Object terrain, Camera camera) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0); // match value set in uniform
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, ((Terrain) terrain).getBlendMapTerrain().getBackground().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1); // match value set in uniform
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, ((Terrain) terrain).getBlendMapTerrain().getRedTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE2); // match value set in uniform
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, ((Terrain) terrain).getBlendMapTerrain().getGreenTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE3); // match value set in uniform
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, ((Terrain) terrain).getBlendMapTerrain().getBlueTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE4); // match value set in uniform
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, ((Terrain) terrain).getBlendMap().getTextureID());

        shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix((Terrain) terrain));
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
    }

    @Override
    public void cleanup() {
        shader.cleanup();
    }
    
    public List<Terrain> getTerrain() {
        return terrains;
    }
}
