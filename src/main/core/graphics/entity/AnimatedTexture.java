package main.core.graphics.entity;

public class AnimatedTexture extends Texture {
    
    private final int frameCount;
    private final float frameTime;
    private float currentTime;
    private int currentFrame;
    private final int textureHeight;
    private final float frameUVHeight;

    public AnimatedTexture(int id, int frameCount, float frameTime, int textureHeight) {
        super(id);
        this.frameCount = frameCount;
        this.frameTime = frameTime;
        this.textureHeight = textureHeight;
        this.frameUVHeight = 1.0f / frameCount;
        this.currentFrame = 0;
        this.currentTime = 0;
    }

    public void update(float interval) {
        currentTime += interval;
        if (currentTime >= frameTime) {
            currentTime = 0;
            currentFrame = (currentFrame + 1) % frameCount;
        }
    }

    public float[] getTextureCoords() {
        float startV = 1.0f - (currentFrame * frameUVHeight);
        float endV = startV - frameUVHeight;

        return new float[] {
            0.0f, endV,   // Bottom left
            1.0f, endV,   // Bottom right
            1.0f, startV, // Top right
            0.0f, startV  // Top left
        };
    }

}
