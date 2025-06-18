package main.core.graphics.entity;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import main.core.graphics.entity.terrain.Terrain;
import main.core.graphics.lighting.DirectionalLight;
import main.core.graphics.lighting.PointLight;
import main.core.graphics.lighting.SpotLight;
import main.core.graphics.utils.Consts;

public class SceneManager {
    
    private List<Entity> entities;
    private List<Terrain> terrains;
    private List<Model> models;

    private SpotLight[] spotLights;
    private PointLight[] pointLights;
    private DirectionalLight directionalLight;
    private Vector3f ambientLight;
    private float specularPower;
    private float lightAngle;
    private float spotAngle = 0;
    private float spotInc = 1;

    public SceneManager() {
        this(0.0f);
    }

    public SceneManager(float lightAngle) {
        entities = new ArrayList<>();
        terrains = new ArrayList<>();
        models = new ArrayList<>();
        ambientLight = Consts.AMBIENT_LIGHT;
        specularPower = Consts.SPECULAR_POWER;
        this.lightAngle = lightAngle;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public List<Terrain> getTerrains() {
        return terrains;
    }

    public void setTerrains(List<Terrain> terrains) {
        this.terrains = terrains;
    }

    public void addTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void removeTerrain(Terrain terrain) {
        terrains.remove(terrain);
    }

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    public void addModel(Model model) {
        models.add(model);
    }

    public void removeModel(Model model) {
        models.remove(model);
    }

    public Vector3f getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(Vector3f ambientLight) {
        this.ambientLight = ambientLight;
    }

    public void setAmbientLight(float x, float y, float z) {
        this.ambientLight = new Vector3f(x, y, z);
    }

    public float getSpecularPower() {
        return specularPower;
    }

    public void setSpecularPower(float specularPower) {
        this.specularPower = specularPower;
    }

    public float getLightAngle() {
        return lightAngle;
    }

    public void setLightAngle(float lightAngle) {
        this.lightAngle = lightAngle;
    }

    public void incLightAngle(float incrementAngle) {
        this.lightAngle += incrementAngle;
    }

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    public void setDirectionalLight(DirectionalLight directionalLight) {
        this.directionalLight = directionalLight;
    }

    public PointLight[] getPointLights() {
        return pointLights;
    }

    public void setPointLights(PointLight[] pointLights) {
        this.pointLights = pointLights;
    }

    public SpotLight[] getSpotLights() {
        return spotLights;
    }

    public void setSpotLights(SpotLight[] spotLights) {
        this.spotLights = spotLights;
    }

    public float getSpotAngle() {
        return spotAngle;
    }

    public void setSpotAngle(float spotAngle) {
        this.spotAngle = spotAngle;
    }

    public void incSpotAngle(float incrementAngle) {
        this.spotAngle *= incrementAngle;
    }

    public float getSpotInc() {
        return spotInc;
    }

    public void setSpotInc(float spotInc) {
        this.spotInc = spotInc;
    }

}
