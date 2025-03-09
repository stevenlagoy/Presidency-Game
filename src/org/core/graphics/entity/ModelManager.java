package org.core.graphics.entity;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import org.core.graphics.ObjectLoader;

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

    public static final String modelsLoc = "/models/";
    public static final String objExtension = ".obj";
    public static ObjectLoader loader = new ObjectLoader();

    private static final List<ModelInfo> models = new ArrayList<ModelInfo>(){{
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
            modelInfo.model = loader.loadOBJModel(modelsLoc + modelInfo.modelFile + objExtension);
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
            if (modelInfo.model.equals(model)) {
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
}