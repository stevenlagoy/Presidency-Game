package main.core.graphics.entity;

/**
 * Represents a rectangular region of a texture (for use with atlases or 9-slice UI).
 * UV coordinates are in the range [0, 1].
 */
public class TextureRegion extends Texture {
    private final Texture baseTexture;
    private final float u1, v1, u2, v2; // (u1,v1): top-left, (u2,v2): bottom-right

    /**
     * @param baseTexture The full texture this region is part of.
     * @param u1 Left U coordinate (0.0 = left edge)
     * @param v1 Top V coordinate (0.0 = top edge)
     * @param u2 Right U coordinate (1.0 = right edge)
     * @param v2 Bottom V coordinate (1.0 = bottom edge)
     */
    public TextureRegion(Texture baseTexture, float u1, float v1, float u2, float v2) {
        super(baseTexture.getId());
        this.baseTexture = baseTexture;
        this.u1 = u1;
        this.v1 = v1;
        this.u2 = u2;
        this.v2 = v2;
    }

    public Texture getBaseTexture() {
        return baseTexture;
    }

    public float getU1() {
        return u1;
    }

    public float getV1() {
        return v1;
    }

    public float getU2() {
        return u2;
    }

    public float getV2() {
        return v2;
    }
}