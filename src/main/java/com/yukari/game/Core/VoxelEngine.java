package com.yukari.game.Core;

import org.lwjgl.opengl.GL;

import com.yukari.game.Settings.EngineSettings;

import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class VoxelEngine {
    private long window;

    public void run() {
        init();
        loop();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = glfwCreateWindow(800, 600, "Voxel", NULL, NULL);
        if (window == NULL) {
            throw new IllegalStateException("Failed to create GLFW window.");
        }
        glfwMakeContextCurrent(window);

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            glViewport(0, 0, width, height);
            System.out.println("CHANGED");
        });

        GL.createCapabilities();

        glViewport(0, 0, 800, 600);
    }

    private void loop() {

        while (!glfwWindowShouldClose(window)) {
            Input.Update(window);
            glClearColor(EngineSettings.backgroundColor.x,
                    EngineSettings.backgroundColor.y,
                    EngineSettings.backgroundColor.z,
                    1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glfwSwapBuffers(window);
            glfwPollEvents();

        }
    }

    public long getWindow() {
        return window;
    }

}
