package com.yukari.game.Settings;

import org.joml.*;
import org.joml.Math;

public class EngineSettings {
    public static int width = 800;
    public static int height = 600;

    public static float aspectRatio = (float) width / (float) height;

    public static float fov = Math.toRadians(40.0f);

    public static float near_plane = 0.1f;
    public static float far_plane = 100.0f;

    public static Vector3f backgroundColor = new Vector3f(0.2f, 0.3f, 0.3f);

}
