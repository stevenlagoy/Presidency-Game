package main.core.graphics.rendering;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import main.core.FilePaths;
import main.core.Engine;
import main.core.graphics.Camera;
import main.core.graphics.ShaderManager;
import main.core.graphics.Transformation;
import main.core.graphics.entity.Entity;
import main.core.graphics.entity.Material;
import main.core.graphics.entity.Model;
import main.core.graphics.entity.TextureRegion;
import main.core.graphics.lighting.DirectionalLight;
import main.core.graphics.lighting.PointLight;
import main.core.graphics.lighting.SpotLight;
import main.core.graphics.ui.Container;
import main.core.graphics.utils.Consts;
import main.core.graphics.utils.Utils;

public class EntityRenderer implements IRenderer<Object> {

    ShaderManager shader;
    private Map<Model, List<Entity>> entities;

    public EntityRenderer() throws Exception {
        entities = new HashMap<>();
        shader = new ShaderManager();
    }

    @Override
    public void init() throws Exception {
        shader.createVertexShader(Utils.loadResource(FilePaths.SHADERS_GFX_LOC.resolve("entity_vertex.vs")));
        shader.createFragmentShader(Utils.loadResource(FilePaths.SHADERS_GFX_LOC.resolve("entity_fragment.fs")));
        shader.link();
        shader.createUniform("projectionMatrix");
        shader.createUniform("textureSampler");
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
    public void render(Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight, Vector3f ambientLight, float specularPower) {

        shader.bind();
        shader.setUniform("projectionMatrix", Engine.getWindow().updateProjectionMatrix());
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
        RenderManager.renderLights(pointLights, spotLights, directionalLight, shader, ambientLight, specularPower);

        for (Model model : entities.keySet()) {
            List<Entity> entityList = entities.get(model);
            bind(model);
            
            for(Entity entity : entityList) {
                if (entity instanceof Container container) {
                    for (Container.DrawQuad quad : container.getDrawQuads()) {
                        // Center the quads on the container's position
                        float drawX = container.getPos().x - container.getWidth()/2f + quad.x + quad.w/2f;
                        float drawY = container.getPos().y - container.getHeight()/2f + quad.y + quad.h/2f;
                        renderQuad(drawX, drawY, quad.w, quad.h, quad.region);
                    }
                    continue;
                }

                prepare(entity, camera);

                // Validate OpenGL state before draw
                if (!GL30.glIsVertexArray(model.getId())) {
                    Engine.log("Invalid VAO ID: " + model.getId());
                    continue;
                }

                // Check if shader program is valid
                int program = shader.getProgramId();
                if (!GL20.glIsProgram(program)) {
                    Engine.log("Invalid shader program: " + program);
                    continue;
                }

                // Verify buffer bindings
                int vboId = model.getVertexBufferId();
                int iboId = model.getIndexBufferId();
                if (!GL15.glIsBuffer(vboId) || !GL15.glIsBuffer(iboId)) {
                    Engine.log("Invalid buffer objects - VBO: " + vboId + ", IBO: " + iboId);
                    continue;
                }

                // Check texture binding
                if (model.getTexture() != null) {
                    int texId = model.getTexture().getId();
                    if (!GL11.glIsTexture(texId)) {
                        Engine.log("Invalid texture ID: " + texId);
                        continue;
                    }
                }

                // Check texture binding
                if (model.getTexture() != null) {
                    int texId = model.getTexture().getId();
                    if (!GL11.glIsTexture(texId)) {
                        Engine.log("Invalid texture ID: " + texId);
                        continue;
                    }
                }

                int error = GL11.glGetError();
                if (error != GL11.GL_NO_ERROR) {
                    Engine.log("OpenGL Error before draw: " + error);
                }

                if (entity == null || entity.getModel() == null) {
                    Engine.log("Null entity or model");
                    continue;
                }

                int vertexCount = entity.getModel().getVertexCount();
                if (vertexCount <= 0) {
                    Engine.log("Invalid vertex count: " + vertexCount);
                    continue;
                }
                
                try {
                    GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);
                }
                catch (Exception e) {
                    Engine.log("Draw call failed");
                    Engine.log(e);
                }

                error = GL11.glGetError();
                if (error != GL11.GL_NO_ERROR) {
                    Engine.log("OpenGL Error after draw: " + error);
                }
            }
            unbind();
        }
        entities.clear();
        shader.unbind();
    }

    private void renderQuad(float x, float y, float w, float h, TextureRegion region) {

        shader.bind();

        // Prepare vertex data for a quad at (x, y) with size (w, h) and region UVs
        float u1 = region.getU1(), v1 = region.getV1();
        float u2 = region.getU2(), v2 = region.getV2();

        float[] vertices = {
            x,     y,   0f, u1, v1,
            x,     y+h, 0f, u1, v2,
            x+w,   y+h, 0f, u2, v2,
            x+w,   y,   0f, u2, v1
        };
        int[] indices = {0, 1, 2, 2, 3, 0};

        // Create and bind VAO/VBO/IBO (or use a static/shared one for all quads)
        int vao = GL30.glGenVertexArrays();
        int vbo = GL15.glGenBuffers();
        int ibo = GL15.glGenBuffers();

        GL30.glBindVertexArray(vao);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 5 * Float.BYTES, 0); // position (x, y, z)
        GL20.glEnableVertexAttribArray(0);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES); // UV
        GL20.glEnableVertexAttribArray(1);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

        // Bind the texture
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, region.getBaseTexture().getId());

        // Draw
        shader.setUniform("textureSampler", 0);
        shader.setUniform("transformationMatrix", new org.joml.Matrix4f().identity());
        GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0);

        // Cleanup
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
        GL15.glDeleteBuffers(vbo);
        GL15.glDeleteBuffers(ibo);
        GL30.glDeleteVertexArrays(vao);
    }

    @Override
    public void bind(Model model) {
        shader.bind();

        // Clear existing errors
        GL11.glGetError();

        // Bind VAO first
        GL30.glBindVertexArray(model.getId());

        // Enable vertex attributes in the correct order
        GL20.glEnableVertexAttribArray(0);  // positions
        GL20.glEnableVertexAttribArray(1);  // texture coords
        
        // Bind buffers explicitly
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, model.getVertexBufferId());
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, model.getIndexBufferId());

        // Set material properties
        Material material = model.getMaterial();
        if (material != null) {
            shader.setUniform("material", material);
            if (material.hasTransparency() || material.isDisableCulling()) {
                RenderManager.disableCulling();
            }
            else {
                //RenderManager.enableCulling();
            }
        }

        // Bind texture if present
        if (model.getTexture() != null) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());
        }

        // Validate shader is bound
        if (!GL20.glIsProgram(shader.getProgramId())) {
            shader.bind();
        }
    }

    @Override
    public void unbind() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void prepare(Object entity, Camera camera) {
        shader.setUniform("textureSampler", 0);
        shader.setUniform("transformationMatrix", Transformation.createTransformationMatrix((Entity) entity));
        shader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
    }

    @Override
    public void cleanup() {
        shader.cleanup();
    }
    
    public Map<Model, List<Entity>> getEntities() {
        return entities;
    }
}
