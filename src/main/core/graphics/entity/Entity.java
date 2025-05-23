package main.core.graphics.entity;

import org.joml.Vector3f;

import main.core.graphics.entity.ModelManager.ModelInfo;

public class Entity {
    
    private Model model;
    private Vector3f pos, rotation;
    private float scale;

    public Entity(Model model, Vector3f pos, Vector3f rotation, float scale) {
        this.model = model;
        this.pos = pos;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Entity(Model model, Vector3f pos) {
        ModelInfo modelInfo = ModelManager.getModelInfo(model);
        this.model = model;
        this.pos = pos;
        this.rotation = modelInfo.getRotation();
        this.scale = modelInfo.getScale();
    }

    public void incPos(float x, float y, float z) {
        this.pos.x += x;
        this.pos.y += y;
        this.pos.z += z;
    }

    public void setPos(float x, float y, float z) {
        this.pos.x = x;
        this.pos.y = y;
        this.pos.z = z;
    }

    public void incRotation(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Model getModel() {
        return model;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

}