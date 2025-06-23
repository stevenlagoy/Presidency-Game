package main.core.graphics.entity;

public class TiledTexture extends Texture {
    
    private final float repeatX, repeatY;

    public TiledTexture(int id, float repeatX, float repeatY) {
        super(id);
        this.repeatX = repeatX;
        this.repeatY = repeatY;
    }

    public float getRepeatX() { return repeatX; }
    public float getRepeatY() { return repeatY; }

}
