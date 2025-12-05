package com.yukari.game.Input;

import java.nio.DoubleBuffer;
import org.lwjgl.BufferUtils;

import com.yukari.game.Core.Camera;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL14.*;

public class Input {

    public static void Update(long window, Camera camera) {
        if (isKeyPressed(window, GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(window, true);
        }
        ProcessMouse(window, camera);
    }

    public static boolean isKeyPressed(long window, int key) {
        return glfwGetKey(window, key) == GLFW_PRESS;
    }

    private static double lastMouseX = 400;
    private static double lastMouseY = 300;
    private static boolean firstMouse = true;

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
