package main.core.graphics.entity;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import main.core.graphics.entity.ModelManager.ModelInfo;

public class Entity {

    public enum EntityType { ENTITY, CONTAINER, BUTTON, QUAD }
    
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
        // Transform ray to local space as before
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

        float[] positions = model.getPositions();
        int[] indices = model.getIndices();

        for (int i = 0; i < indices.length; i += 3) {
            Vector3f v0 = new Vector3f(
                positions[indices[i] * 3],
                positions[indices[i] * 3 + 1],
                positions[indices[i] * 3 + 2]
            );
            Vector3f v1 = new Vector3f(
                positions[indices[i + 1] * 3],
                positions[indices[i + 1] * 3 + 1],
                positions[indices[i + 1] * 3 + 2]
            );
            Vector3f v2 = new Vector3f(
                positions[indices[i + 2] * 3],
                positions[indices[i + 2] * 3 + 1],
                positions[indices[i + 2] * 3 + 2]
            );
            if (rayIntersectsTriangle(localOrigin, localDirection, v0, v1, v2)) {
                return true;
            }
        }
        return false;
    }

    public static boolean rayIntersectsTriangle(Vector3f rayOrigin, Vector3f rayDir, Vector3f v0, Vector3f v1, Vector3f v2) {
        final float EPSILON = 1e-6f;
        Vector3f edge1 = new Vector3f();
        Vector3f edge2 = new Vector3f();
        v1.sub(v0, edge1);
        v2.sub(v0, edge2);

        Vector3f h = new Vector3f();
        rayDir.cross(edge2, h);
        float a = edge1.dot(h);
        if (a > -EPSILON && a < EPSILON) return false; // Ray is parallel to triangle

        float f = 1.0f / a;
        Vector3f s = new Vector3f();
        rayOrigin.sub(v0, s);
        float u = f * s.dot(h);
        if (u < 0.0f || u > 1.0f) return false;

        Vector3f q = new Vector3f();
        s.cross(edge1, q);
        float v = f * rayDir.dot(q);
        if (v < 0.0f || u + v > 1.0f) return false;

        float t = f * edge2.dot(q);
        return t > EPSILON;
    }

}