package org.core.graphics.entity;

import org.joml.Vector4f;

import org.core.graphics.utils.Consts;

public class Material {
    
    private Vector4f ambientColor, diffuseColor, specularColor;
    private float reflectance;
    private Texture texture;
    private boolean disableCulling;
    private boolean hasTransparency;

    public Material(Vector4f ambientColor, Vector4f diffuseColor, Vector4f specularColor, float reflectance, Texture texture, boolean hasTransparency) {
        this.ambientColor = ambientColor;
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.reflectance = reflectance;
        this.texture = texture;
        this.hasTransparency = hasTransparency;
    }

    public Material() {
        this.ambientColor = Consts.DEFAULT_COLOR;
        this.diffuseColor = Consts.DEFAULT_COLOR;
        this.specularColor = Consts.DEFAULT_COLOR;
        this.reflectance = 0;
        this.texture = null;
        this.disableCulling = false;
        this.hasTransparency = false;
    }

    public Material(Vector4f color, float reflectance) {
        this(color, color, color, reflectance, null, false);
    }

    public Material(Vector4f color, float reflectance, boolean hasTransparency) {
        this(color, color, color, reflectance, null, hasTransparency);
    }

    public Material(Vector4f color, float reflectance, Texture texture, boolean hasTransparency) {
        this(color, color, color, reflectance, texture, hasTransparency);
    }

    public Material(Vector4f color, float reflectance, Texture texture) {
        this(color, color, color, reflectance, texture, false);
    }

    public Material(Texture texture) {
        this(Consts.DEFAULT_COLOR, Consts.DEFAULT_COLOR, Consts.DEFAULT_COLOR, 0, texture, false);
    }

    public Material(Texture texture, boolean hasTransparency) {
        this(Consts.DEFAULT_COLOR, Consts.DEFAULT_COLOR, Consts.DEFAULT_COLOR, 0, texture, hasTransparency);
    }

    public Material(Texture texture, float reflectance) {
        this(Consts.DEFAULT_COLOR, Consts.DEFAULT_COLOR, Consts.DEFAULT_COLOR, reflectance, texture, false);
    }

    public Vector4f getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(Vector4f ambientColor) {
        this.ambientColor = ambientColor;
    }

    public Vector4f getDiffuseColor() {
        return diffuseColor;
    }

    public void setDiffuseColor(Vector4f diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    public Vector4f getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(Vector4f specularColor) {
        this.specularColor = specularColor;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean hasTexture() {
        return texture != null;
    }

    public boolean isDisableCulling() {
        return disableCulling;
    }

    public void setDisableCulling(boolean disableCulling) {
        this.disableCulling = disableCulling;
    }

    public boolean hasTransparency() {
        return hasTransparency;
    }

    public void setTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }
}
