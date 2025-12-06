package com.yukari.game.Core;

import static org.joml.Math.*;
import static org.lwjgl.glfw.GLFW.*;

import org.joml.*;

import com.yukari.game.Input.Input;
import com.yukari.game.Settings.EngineSettings;

public class Camera {

    private Vector3f position;
    private Vector3f front;
    private Vector3f up;
    private Vector3f right;
    private Vector3f worldUp;

    private float yaw;
    private float pitch;

    private Matrix4f viewMatrix;

    public Camera(Vector3f position, float yaw, float pitch) {
        this.position = position;
        this.front = new Vector3f(0, 0, -1f);
        this.worldUp = new Vector3f(0, 1, 0);
        this.right = new Vector3f();
        this.up = new Vector3f();
        this.yaw = yaw;
        this.pitch = pitch;
        UpdateCamera();
    }

    public void Update(long window) {
        Move(window);
    }

    private void Move(long window) {
        float velocity = EngineSettings.cameraNormalSpeed * Time.deltaTime;
        Vector3f movement = new Vector3f();
        Vector3f direction = new Vector3f(Input.getMovementInput(window));

        movement.add(new Vector3f(front).mul(direction.y));

        movement.add(new Vector3f(right).mul(direction.x));
        movement.add(new Vector3f(worldUp).mul(direction.z));

        position.add(movement.mul(velocity));
    }

    public void ProcessMouseMovement(float xOffset, float yOffset) {
        xOffset *= EngineSettings.cameraSensitivity;
        yOffset *= EngineSettings.cameraSensitivity;

        yaw += xOffset;
        pitch += yOffset;

        if (pitch > 89.0f)
            pitch = 89.0f;
        if (pitch < -89.0f)
            pitch = -89.0f;

        UpdateCamera();
    }

    private void UpdateCamera() {
        Vector3f forward = new Vector3f();
        forward.x = cos(toRadians(yaw)) * cos(toRadians(pitch));
        forward.y = sin(toRadians(pitch));
        forward.z = sin(toRadians(yaw)) * cos(toRadians(pitch));
        front = forward.normalize();

        right = worldUp.cross(front, new Vector3f()).normalize(); // worldUp × front
        up = front.cross(right, new Vector3f()).normalize(); // front × right
    }

    public Matrix4f getViewMatrix() {
        Matrix4f view = new Matrix4f().lookAt(position, new Vector3f(position).add(front), up);
        return view;
    }

}
