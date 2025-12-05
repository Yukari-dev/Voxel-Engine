package com.yukari.game.Core;

import org.lwjgl.opengl.GL;

import com.yukari.game.Render.Mesh;
import com.yukari.game.Settings.EngineSettings;
import com.yukari.game.Shaders.Shader;

import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.joml.*;
import org.joml.Math;

public class VoxelEngine {
    private long window;
    Matrix4f projection = new Matrix4f();

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
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);

        window = glfwCreateWindow(EngineSettings.width, EngineSettings.height, "Voxel", NULL, NULL);
        if (window == NULL) {
            throw new IllegalStateException("Failed to create GLFW window.");
        }
        glfwMakeContextCurrent(window);
        projection = new Matrix4f().perspective(EngineSettings.fov, EngineSettings.aspectRatio,
                EngineSettings.near_plane,
                EngineSettings.far_plane);

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            EngineSettings.width = width;
            EngineSettings.height = height;
            EngineSettings.aspectRatio = (float) width / (float) height;
            projection = new Matrix4f().perspective(EngineSettings.fov, EngineSettings.aspectRatio,
                    EngineSettings.near_plane,
                    EngineSettings.far_plane);

            glViewport(0, 0, width, height);
        });

        GL.createCapabilities();
        glDisable(GL_CULL_FACE);
        projection.identity();

        glEnable(GL_DEPTH_TEST);
        glViewport(0, 0, EngineSettings.width, EngineSettings.height);
    }

    private void loop() {
        Shader shader = new Shader("src/main/java/com/yukari/game/Shaders/vertexShader.glsl",
                "src/main/java/com/yukari/game/Shaders/fragmentShader.glsl");

        Mesh mesh = new Mesh();

        while (!glfwWindowShouldClose(window)) {
            Input.Update(window);
            glClearColor(EngineSettings.backgroundColor.x,
                    EngineSettings.backgroundColor.y,
                    EngineSettings.backgroundColor.z,
                    1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            shader.bind();
            Matrix4f view = new Matrix4f().lookAt(
                    0f, 0f, 5f,
                    0f, 0f, 0f,
                    0f, 1f, 0f);
            Matrix4f model = new Matrix4f().identity();

            shader.SetMatrix("projection", projection);
            shader.SetMatrix("view", view);
            shader.SetMatrix("model", model);
            mesh.Render();

            int err = glGetError();
            if (err != GL_NO_ERROR) {
                System.out.println("GL ERROR: " + err);
            }

            glfwSwapBuffers(window);
            glfwPollEvents();

        }
    }

    public long getWindow() {
        return window;
    }

}
