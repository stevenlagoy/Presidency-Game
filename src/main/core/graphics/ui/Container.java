package main.core.graphics.ui;

import java.util.ArrayList;
import java.util.List;

import main.core.graphics.entity.Entity;
import main.core.graphics.entity.Texture;
import main.core.graphics.entity.TextureManager;
import main.core.graphics.entity.TextureRegion;

public class Container extends Entity {

    private float width;
    private float height;
    private Entity entity;
    private Texture topLeftCorner;
    private Texture topEdge;
    private Texture topRightCorner;
    private Texture leftEdge;
    private Texture center;
    private Texture rightEdge;
    private Texture bottomLeftCorner;
    private Texture bottomEdge;
    private Texture bottomRightCorner;
    private Texture containerTexture;
    private List<DrawQuad> drawQuads;

    /*
     * Edge Texture occupies outer thirds of the texture
     * Center Texture occupies center ninth of the texture
     * TLTETR
     * LECCRE
     * BLBEBR
     */

    public Container(Entity entity, Texture containerTexture) {
        super(entity.getModel(), entity.getPos(), entity.getRotation(), entity.getScale());
        makeDrawQuads();
    }

    public Container(Entity entity, String containerTextureName) {
        this(entity, TextureManager.getTexture(containerTextureName));
    }

    public Entity getEntity() {
        return entity;
    }

    private void extractTextures() {
        if (containerTexture == null) return;

        /*
         * 0,0  0,1  0,2
         * 1,0  1,1  1,2
         * 2,0  2,1  2,2
         */
        topLeftCorner     = new TextureRegion(containerTexture, 0.0f/3.0f, 0.0f/3.0f, 1.0f/3.0f, 1.0f/3.0f);
        topEdge           = new TextureRegion(containerTexture, 0.0f/3.0f, 1.0f/3.0f, 1.0f/3.0f, 2.0f/3.0f);
        topRightCorner    = new TextureRegion(containerTexture, 0.0f/3.0f, 2.0f/3.0f, 1.0f/3.0f, 3.0f/3.0f);
        leftEdge          = new TextureRegion(containerTexture, 1.0f/3.0f, 0.0f/3.0f, 2.0f/3.0f, 1.0f/3.0f);
        center            = new TextureRegion(containerTexture, 1.0f/3.0f, 1.0f/3.0f, 2.0f/3.0f, 2.0f/3.0f);
        rightEdge         = new TextureRegion(containerTexture, 1.0f/3.0f, 2.0f/3.0f, 2.0f/3.0f, 3.0f/3.0f);
        bottomLeftCorner  = new TextureRegion(containerTexture, 2.0f/3.0f, 0.0f/3.0f, 3.0f/3.0f, 1.0f/3.0f);
        bottomEdge        = new TextureRegion(containerTexture, 2.0f/3.0f, 1.0f/3.0f, 3.0f/3.0f, 2.0f/3.0f);
        bottomRightCorner = new TextureRegion(containerTexture, 2.0f/3.0f, 2.0f/3.0f, 3.0f/3.0f, 3.0f/3.0f);
    }

    public static class DrawQuad {
        public float x, y, w, h;
        public TextureRegion region;

        public DrawQuad(float x, float y, float w, float h, TextureRegion region) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.region = region;
        }
    }

    public void makeDrawQuads() {
        extractTextures(); // Ensure regions are set

        List<DrawQuad> quads = new ArrayList<>();

        // Assume the corner/edge sizes are 1/3 of the original texture size
        float texW = 1.0f;
        float texH = 1.0f;

        float cornerW = width / 3f;
        float cornerH = height / 3f;
        float edgeW = width - 2 * cornerW;
        float edgeH = height - 2 * cornerH;

        // Top row
        quads.add(new DrawQuad(0,           0,           cornerW, cornerH, (TextureRegion)topLeftCorner));
        quads.add(new DrawQuad(cornerW,     0,           edgeW,   cornerH, (TextureRegion)topEdge));
        quads.add(new DrawQuad(cornerW+edgeW,0,          cornerW, cornerH, (TextureRegion)topRightCorner));
        // Middle row
        quads.add(new DrawQuad(0,           cornerH,     cornerW, edgeH,   (TextureRegion)leftEdge));
        quads.add(new DrawQuad(cornerW,     cornerH,     edgeW,   edgeH,   (TextureRegion)center));
        quads.add(new DrawQuad(cornerW+edgeW,cornerH,    cornerW, edgeH,   (TextureRegion)rightEdge));
        // Bottom row
        quads.add(new DrawQuad(0,           cornerH+edgeH,cornerW, cornerH, (TextureRegion)bottomLeftCorner));
        quads.add(new DrawQuad(cornerW,     cornerH+edgeH,edgeW,   cornerH, (TextureRegion)bottomEdge));
        quads.add(new DrawQuad(cornerW+edgeW,cornerH+edgeH,cornerW, cornerH, (TextureRegion)bottomRightCorner));

        this.drawQuads = quads;
    }

    public List<DrawQuad> getDrawQuads() {
        if (drawQuads == null) makeDrawQuads();
        return drawQuads;
    }

}
