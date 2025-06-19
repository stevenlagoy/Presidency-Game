package main.core.graphics.utils;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Consts {
    
    public static final float FOV = (float) Math.toRadians(60.0f);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 10000.0f;
    public static final float SPECULAR_POWER = 10f;

    public static final float CAMERA_MOVE_SPEED = 0.01f;
    public static final float MOUSE_SENSITIVITY = 0.2f;

    public static final int MAX_SPOT_LIGHTS = 5;
    public static final int MAX_POINT_LIGHTS = 5;

    public static final String TITLE = "Engine Test";

    public static final Vector3f AMBIENT_LIGHT = new Vector3f(0.3f, 0.3f, 0.3f);
    public static final Vector4f DEFAULT_COLOR = new Vector4f(1, 1, 1, 1);

}
