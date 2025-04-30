package main.core.graphics.rendering;

import main.core.graphics.Camera;
import main.core.graphics.entity.Model;
import main.core.graphics.lighting.DirectionalLight;
import main.core.graphics.lighting.PointLight;
import main.core.graphics.lighting.SpotLight;

public interface IRenderer<T> {
    public void init() throws Exception;

    public void render(Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight);

    abstract void bind(Model model);

    public void unbind();
    
    public void prepare(T t, Camera camera);

    public void cleanup();
}
