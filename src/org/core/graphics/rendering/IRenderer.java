package org.core.graphics.rendering;

import org.core.graphics.Camera;
import org.core.graphics.entity.Model;
import org.core.graphics.lighting.DirectionalLight;
import org.core.graphics.lighting.PointLight;
import org.core.graphics.lighting.SpotLight;

public interface IRenderer<T> {
    public void init() throws Exception;

    public void render(Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight);

    abstract void bind(Model model);

    public void unbind();
    
    public void prepare(T t, Camera camera);

    public void cleanup();
}
