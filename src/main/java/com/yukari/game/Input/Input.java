package com.yukari.game.Input;

import java.nio.DoubleBuffer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import com.yukari.game.Core.Camera;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL14.*;

public class Input {

    private static double lastMouseX = 400;
    private static double lastMouseY = 300;
    private static boolean firstMouse = true;

    public static void Update(long window, Camera camera) {
        if (isKeyPressed(window, GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(window, true);
        }

        ProcessMouse(window, camera);
    }

    public static boolean isKeyPressed(long window, int key) {
        return glfwGetKey(window, key) == GLFW_PRESS;
    }

    public static Vector3f getMovementInput(long window) {
        int forward = isKeyPressed(window, GLFW_KEY_W) ? 1 : 0;
        int backward = isKeyPressed(window, GLFW_KEY_S) ? 1 : 0;
        int right = isKeyPressed(window, GLFW_KEY_D) ? 1 : 0;
        int left = isKeyPressed(window, GLFW_KEY_A) ? 1 : 0;
        int up = isKeyPressed(window, GLFW_KEY_E) ? 1 : 0;
        int down = isKeyPressed(window, GLFW_KEY_Q) ? 1 : 0;

        Vector3f movement = new Vector3f(left - right, forward - backward, up - down);
        if (movement.lengthSquared() > 0) {
            movement.normalize();
        }

        return movement;
    }

    public static void ProcessMouse(long window, Camera camera) {
        DoubleBuffer xBuf = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yBuf = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(window, xBuf, yBuf);
        double currentX = xBuf.get(0);
        double currentY = yBuf.get(0);

        if (firstMouse) {
            lastMouseX = currentX;
            lastMouseY = currentY;
            firstMouse = false;
        }

        float xOffset = (float) (currentX - lastMouseX);
        float yOffset = (float) (lastMouseY - currentY);

        lastMouseX = currentX;
        lastMouseY = currentY;

        camera.ProcessMouseMovement(xOffset, yOffset);
    }

    public static void ShowCursor(long window) {
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    public static void HideCursor(long window) {
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

}
