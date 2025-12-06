package com.yukari.game.Core;

import org.lwjgl.opengl.GL;
import com.yukari.game.Input.Input;
import com.yukari.game.Render.Mesh;
import com.yukari.game.Settings.EngineSettings;
import com.yukari.game.Shaders.Shader;
import org.lwjgl.glfw.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import org.joml.*;

public class VoxelEngine {
    private long window;
    private Matrix4f projection = new Matrix4f();

    private Shader shader;
    private Mesh mesh;
    private Camera camera;

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        initGLFW();
        createWindow();
        setupCallbacks();
        initOpenGL();
        initProjection();
    }

    private void initGLFW() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
    }

    private void createWindow() {
        window = glfwCreateWindow(EngineSettings.width, EngineSettings.height, "Voxel", NULL, NULL);
        if (window == NULL) {
            throw new IllegalStateException("Failed to create GLFW window.");
        }
        glfwMakeContextCurrent(window);
        glfwSwapInterval(0);
    }

    private void setupCallbacks() {
        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            EngineSettings.width = width;
            EngineSettings.height = height;
            EngineSettings.aspectRatio = (float) width / (float) height;
            initProjection();
            glViewport(0, 0, width, height);
        });
    }

    private void initOpenGL() {
        GL.createCapabilities();
        glDisable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glViewport(0, 0, EngineSettings.width, EngineSettings.height);
    }

    private void initProjection() {
        projection = new Matrix4f().perspective(
                EngineSettings.fov,
                EngineSettings.aspectRatio,
                EngineSettings.near_plane,
                EngineSettings.far_plane);
    }

    private void loop() {
        loadResources();

        while (!glfwWindowShouldClose(window)) {
            update();
            render();
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void loadResources() {
        shader = new Shader("Shaders/vertexShader.glsl", "Shaders/fragmentShader.glsl");
        mesh = new Mesh();
        camera = new Camera(new Vector3f(0f, 0f, 3f), -90, 0);
        Input.HideCursor(window);
    }

    private void update() {
        Time.Update();
        Input.Update(window, camera);
        camera.Update(window);
    }

    private void render() {
        glClearColor(
                EngineSettings.backgroundColor.x,
                EngineSettings.backgroundColor.y,
                EngineSettings.backgroundColor.z,
                1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shader.bind();
        shader.SetMatrix("projection", projection);
        shader.SetMatrix("view", camera.getViewMatrix());
        shader.SetMatrix("model", new Matrix4f().identity());

        mesh.Render();
    }

    private void cleanup() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public long getWindow() {
        return window;
    }
}
