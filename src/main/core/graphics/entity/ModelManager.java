package main.core.graphics.entity;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import main.core.FilePaths;
import main.core.IOUtil;
import main.core.graphics.ObjectLoader;

public class ModelManager {
    
    public static class ModelInfo {
        public String modelName;
        public String modelFile;
        public float scale;
        public Vector3f defaultRotation;
        public Model model;

        public ModelInfo(String modelName, String modelFile, float scale, Vector3f defaultRotation, Model model) {
            this.modelName = modelName;
            this.modelFile = modelFile;
            this.scale = scale;
            this.defaultRotation = defaultRotation;
            this.model = model;
        }

        public ModelInfo(String modelName, String modelFile, float scale, Vector3f defaultRotation) {
            this.modelName = modelName;
            this.modelFile = modelFile;
            this.scale = scale;
            this.defaultRotation = defaultRotation;
            this.model = null;
        }

        public float getScale() {
            return scale;
        }

        public Vector3f getRotation() {
            return defaultRotation;
        }
    }

    private static final List<ModelInfo> models = new ArrayList<ModelInfo>() {{
        add(new ModelInfo("cube", "cube", 50.0f, new Vector3f(0, 0, 0)));
        add(new ModelInfo("bunny", "bunny", 500.0f, new Vector3f(0, 0, 0)));
        add(new ModelInfo("tree", "tree", 1.0f, new Vector3f(-90, 0, 0)));
        add(new ModelInfo("teapot", "teapot", 20.0f, new Vector3f(0, 0, 0)));
        add(new ModelInfo("cow", "cow", 20.0f, new Vector3f(0, 0, 0)));
        add(new ModelInfo("pumpkin", "pumpkin", 1.0f, new Vector3f(-90, 0, 0)));
        add(new ModelInfo("teddyBear", "teddy_bear", 2.0f, new Vector3f(0, 0, 0)));
    }};

    public static void loadModelFiles() {
        for (ModelInfo modelInfo : models) {
            modelInfo.model = ObjectLoader.loadOBJModel(FilePaths.MODELS_GFX_LOC.resolve(modelInfo.modelFile + IOUtil.Extension.OBJ.extension));
        }
    }

    public static List<ModelInfo> getModelFilesList() {
        return models;
    }

    public static void addModel(String modelName, String modelFile) {
        models.add(new ModelInfo(modelName, modelFile, 1.0f, new Vector3f(0, 0, 0), null));
        loadModelFiles();
    }

    public static void addModel(String modelName, String modelFile, float scale, Vector3f defaultRotation) {
        models.add(new ModelInfo(modelName, modelFile, scale, defaultRotation, null));
        loadModelFiles();
    }

    public static Model getModel(String modelName) {
        for (ModelInfo modelInfo : models) {
            if (modelInfo.modelName.equals(modelName)) {
                return modelInfo.model;
            }
        }
        return null;
    }

    public static ModelInfo getModelInfo(String modelName) {
        for (ModelInfo modelInfo : models) {
            if (modelInfo.modelName.equals(modelName)) {
                return modelInfo;
            }
        }
        return null;
    }

    public static ModelInfo getModelInfo(Model model) {
        for (ModelInfo modelInfo : models) {
            if (modelInfo.model != null && modelInfo.model.equals(model)) {
                return modelInfo;
            }
        }
        return null;
    }

    public static float getModelScale(String modelName) {
        for (ModelInfo modelInfo : models) {
            if (modelInfo.modelName.equals(modelName)) {
                return modelInfo.scale;
            }
        }
        return 1.0f;
    }

    public static Vector3f getModelDefaultRotation(String modelName) {
        for (ModelInfo modelInfo : models) {
            if (modelInfo.modelName.equals(modelName)) {
                return modelInfo.defaultRotation;
            }
        }
        return new Vector3f(0, 0, 0);
    }

    public static QuadModel createQuad(float size) {
        return createQuad(size, size);
    }

    public static QuadModel createQuad(float[] dimensions) {
        return createQuad(dimensions[1], dimensions[0]);
    }

    public static QuadModel createQuad(float width, float height) {
        float[] positions = new float[] {
            -width/2,  height/2, 0.0f, // top left
            -width/2, -height/2, 0.0f, // bottom left
            width/2, -height/2, 0.0f, // bottom right
            width/2,  height/2, 0.0f  // top right
        };

        float[] textureCoords = new float[] {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
        };

        int[] indices = new int[] {
            0, 1, 2,
            2, 3, 0
        };

        // Create VAO and get id
        int vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        // Create VBO for vertices
        int vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positions, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        // Create VBO for texture coordinates
        int textureVboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureVboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureCoords, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);

        // Create IBO
        int iboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

        // Unbind
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        // Create and return QuadModel
        QuadModel quad = new QuadModel(vaoId, indices.length, null, positions, indices, width, height);
        quad.setVertexBufferId(vboId);
        quad.setIndexBufferId(iboId);
        return quad;
    }

    public static QuadModel createTiledQuad(float width, float height, float repeatX, float repeatY) {
        float[] positions = new float[] {
            -width/2,  height/2, 0.0f, // top left
            -width/2, -height/2, 0.0f, // bottom left
            width/2, -height/2, 0.0f, // bottom right
            width/2,  height/2, 0.0f  // top right
        };

        float[] textureCoords = new float[] {
            0.0f,      0.0f,
            0.0f,      repeatY,
            repeatX,   repeatY,
            repeatX,   0.0f
        };

        int[] indices = new int[] {
            0, 1, 2,
            2, 3, 0
        };

        // Create VAO and get id
        int vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        // Create VBO for vertices
        int vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positions, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);

        // Create VBO for texture coordinates
        int textureVboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, textureVboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureCoords, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);

        // Create IBO
        int iboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

        // Unbind
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);

        // Create and return QuadModel
        QuadModel quad = new QuadModel(vaoId, indices.length, null, positions, indices, width, height);
        quad.setVertexBufferId(vboId);
        quad.setIndexBufferId(iboId);
        return quad;
    }
}