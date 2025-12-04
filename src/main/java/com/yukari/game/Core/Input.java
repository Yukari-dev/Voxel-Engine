package com.yukari.game.Core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL14.*;

public class Input {

    public static void Update(long window) {
        if (isKeyPressed(window, GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(window, true);
        }
    }

    public static boolean isKeyPressed(long window, int key) {
        return glfwGetKey(window, key) == GLFW_PRESS;
    }

}
