package main.core.graphics.entity;

public class QuadModel extends Model {
    private final float width, height;

    public QuadModel(int id, int vertexCount, Texture texture, float[] positions, int[] indices, float width, float height) {
        super(id, vertexCount, texture, positions, indices);
        this.width = width;
        this.height = height;
    }

    public float getWidth() { return width; }
    
    public float getHeight() { return height; }
}