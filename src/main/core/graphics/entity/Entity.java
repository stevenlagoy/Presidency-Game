package main.core.graphics.entity;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import main.core.graphics.entity.ModelManager.ModelInfo;

public class Entity {
    
    private Model model;
    private Vector3f pos, rotation;
    private float scale;

    public Entity(Model model, Vector3f pos, Vector3f rotation, float scale) {
        this.model = model;
        this.pos = pos;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Entity(Model model, Vector3f pos) {
        ModelInfo modelInfo = ModelManager.getModelInfo(model);
        this.model = model;
        this.pos = pos;
        this.rotation = modelInfo != null ? modelInfo.getRotation() : new Vector3f(0);
        this.scale = modelInfo != null ? modelInfo.getScale() : 1.0f;
    }

    public void incPos(float x, float y, float z) {
        this.pos.x += x;
        this.pos.y += y;
        this.pos.z += z;
    }

    public void setPos(float x, float y, float z) {
        this.pos.x = x;
        this.pos.y = y;
        this.pos.z = z;
    }

    public void incRotation(float x, float y, float z) {
        this.rotation.x += x;
        this.rotation.y += y;
        this.rotation.z += z;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Model getModel() {
        return model;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public boolean containsPoint(float x, float y, float z) {
        float localX = x - pos.x;
        float localY = y - pos.y;
        float localZ = z - pos.z;

        if (rotation.x != 0 || rotation.y != 0 || rotation.z != 0) {
            float rx = (float) Math.toRadians(-rotation.x);
            float ry = (float) Math.toRadians(-rotation.y);
            float rz = (float) Math.toRadians(-rotation.z);

            float tempX, tempY, tempZ;

            tempX = localX;
            tempY = localY;
            localX = (float) (tempX * Math.cos(rz) - tempY * Math.sin(rz));
            localY = (float) (tempX * Math.sin(rz) + tempY * Math.cos(rz));
            
            tempX = localX;
            tempZ = localZ;
            localX = (float) (tempX * Math.cos(ry) + tempZ * Math.sin(ry));
            localZ = (float) (-tempX * Math.sin(ry) + tempZ * Math.cos(ry));

            tempY = localY;
            tempZ = localZ;
            localY = (float) (tempY * Math.cos(rx) - tempZ * Math.sin(rx));
            localZ = (float) (tempY * Math.sin(rx) + tempZ * Math.cos(rx));
        }

        localX /= scale;
        localY /= scale;
        localZ /= scale;

        // check if point within model bounds
        float halfWidth = 0.5f;
        float halfHeight = 0.5f;
        float halfDepth = 0.5f;
        
        return localX >= -halfWidth && localX <= halfWidth &&
            localY >= -halfHeight && localY <= halfHeight &&
            localZ >= -halfDepth && localZ <= halfDepth;
    }

    public boolean intersects(Vector3f rayOrigin, Vector3f rayDirection) {
        Matrix4f transform = new Matrix4f().translate(pos).rotateXYZ(
            (float) Math.toRadians(rotation.x),
            (float) Math.toRadians(rotation.y),
            (float) Math.toRadians(rotation.z)
        ).scale(scale);

        Matrix4f inverse = new Matrix4f(transform).invert();

        Vector3f localOrigin = new Vector3f();
        Vector3f localDirection = new Vector3f();
        inverse.transformPosition(rayOrigin, localOrigin);
        inverse.transformDirection(rayDirection, localDirection);

        // Axis-aligned box in local space: min (-0.5, -0.5, -0.5), max (0.5, 0.5, 0.5)
        float tMin = -Float.MAX_VALUE;
        float tMax = Float.MAX_VALUE;
        float[] min = {-0.5f, -0.5f, -0.5f};
        float[] max = { 0.5f,  0.5f,  0.5f};
        float[] origin = {localOrigin.x, localOrigin.y, localOrigin.z};
        float[] dir = {localDirection.x, localDirection.y, localDirection.z};

        for (int i = 0; i < 3; i++) {
            if (Math.abs(dir[i]) < 1e-6) {
                // Ray is parallel to slab. No hit if origin not within slab
                if (origin[i] < min[i] || origin[i] > max[i]) return false;
            }
            else {
                float t1 = (min[i] - origin[i]) / dir[i];
                float t2 = (max[i] - origin[i]) / dir[i];
                float tNear = Math.min(t1, t2);
                float tFar = Math.max(t1, t2);
                tMin = Math.max(tMin, tNear);
                tMax = Math.min(tMax, tFar);
                if (tMin > tMax) return false;
            }
        }
        return tMax > 0;
    }

}