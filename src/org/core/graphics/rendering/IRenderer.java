package src.org.core.graphics.rendering;

import src.org.core.graphics.Camera;
import src.org.core.graphics.entity.Model;
import src.org.core.graphics.lighting.DirectionalLight;
import src.org.core.graphics.lighting.PointLight;
import src.org.core.graphics.lighting.SpotLight;

public interface IRenderer<T> {
    public void init() throws Exception;

    public void render(Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight);

    abstract void bind(Model model);

    public void unbind();
    
    public void prepare(T t, Camera camera);

    public void cleanup();
}
