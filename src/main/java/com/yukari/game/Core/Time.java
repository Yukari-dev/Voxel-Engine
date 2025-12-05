package com.yukari.game.Core;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Time {
    private static float lastFrame = 0.0f;
    public static float deltaTime = 0.0f;

    public static int fps = 0;
    private static int frameCount = 0;
    private static float fpsTimer = 0.0f;

    public static void Update() {
        float currentTime = (float) glfwGetTime();
        deltaTime = currentTime - lastFrame;

        lastFrame = currentTime;
        frameCount++;
        fpsTimer += deltaTime;
        if (fpsTimer >= 1) {
            fps = frameCount;
            frameCount = 0;
            fpsTimer--;
        }
    }
}
