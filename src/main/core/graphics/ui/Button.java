package main.core.graphics.ui;

import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;

import main.core.Engine;
import main.core.graphics.Camera;
import main.core.graphics.MouseInput;
import main.core.graphics.Transformation;
import main.core.graphics.entity.Entity;
import main.core.graphics.entity.Texture;
import main.core.graphics.entity.TextureManager;

public class Button extends Entity {

    private Entity entity;

    private Texture normalTexture;
    private Texture hoverTexture;
    private Texture leftClickTexture;
    private Texture rightClickTexture;

    private Runnable onHover;
    private Runnable onLeftClick;
    private Runnable onRightClick;

    private boolean isLeftClick;
    private boolean isRightClick;
    private boolean isHover;

    public Button(Entity entity, Texture normalTexture, Texture hoverTexture, Runnable onHover, Texture leftClickTexture, Runnable onLeftClick, Texture rightClickTexture, Runnable onRightClick) {
        super(entity.getModel(), entity.getPos(), entity.getRotation(), entity.getScale());
        this.entity = entity;
        this.normalTexture = normalTexture;
        this.hoverTexture = hoverTexture;
        this.onHover = onHover;
        this.leftClickTexture = leftClickTexture;
        this.onLeftClick = onLeftClick;
        this.rightClickTexture = rightClickTexture;
        this.onRightClick = onRightClick;

        this.isLeftClick = false;
        this.isRightClick = false;
        this.isHover = false;
    }

    public Button(Entity entity, String normalTextureName, String hoverTextureName, Runnable onHover, String leftClickTextureName, Runnable onLeftClick, String rightClickTextureName, Runnable onRightClick) {
        this(
            entity,
            TextureManager.getTexture(normalTextureName),
            TextureManager.getTexture(hoverTextureName),
            onHover,
            TextureManager.getTexture(leftClickTextureName),
            onLeftClick,
            TextureManager.getTexture(rightClickTextureName),
            onRightClick
        );
    }

    public void update(MouseInput mouse, Camera camera) {
        Vector2d mousePos = mouse.getCurrentPosition();

        float ndcX = (float) ((2.0 * mousePos.x) / Engine.getWindow().getWidth() - 1.0);
        float ndcY = (float) (1.0 - (2.0 * mousePos.y) / Engine.getWindow().getHeight());
        float ndcZ = -1.0f; // near plane

        Matrix4f viewMatrix = Transformation.getViewMatrix(camera);

        Vector3f rayOrigin = new Vector3f(camera.getPosition());
        Vector3f rayDirection = Engine.getWindow().calculateRayDirection(ndcX, ndcY, viewMatrix);

        isHover = entity.intersects(rayOrigin, rayDirection);

        isLeftClick = isHover && mouse.isLeftButtonPress();
        isRightClick = isHover && mouse.isRightButtonPress();

        if (isLeftClick) {
            // if (onLeftClick != null) onLeftClick.run();
            if (leftClickTexture != null) entity.getModel().setTexture(leftClickTexture);
        }
        else if (isRightClick) {
            // if (onRightClick != null) onRightClick.run();
            if (rightClickTexture != null) entity.getModel().setTexture(rightClickTexture);
        }
        else if (isHover) {
            // if (onHover != null) onHover.run();
            if (hoverTexture != null) entity.getModel().setTexture(hoverTexture);
        }
        else {
            entity.getModel().setTexture(normalTexture);
        }
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isLeftClicked() {
        return isLeftClick;
    }

    public boolean isRightClicked() {
        return isRightClick;
    }

    public boolean isHover() {
        return isHover;
    }

}
