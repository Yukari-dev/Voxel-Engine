package com.yukari.game.Shaders;

import static org.lwjgl.opengl.GL20.*;

import java.io.*;
import java.nio.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public class Shader {
    private int programID;

    public Shader(String vertex, String fragment) {
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(vertexShader, ExtractSource(vertex));

        glCompileShader(vertexShader);
        CompileCheck(vertexShader);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fragmentShader, ExtractSource(fragment));

        glCompileShader(fragmentShader);
        CompileCheck(fragmentShader);

        programID = glCreateProgram();
        glAttachShader(programID, vertexShader);
        glAttachShader(programID, fragmentShader);
        glLinkProgram(programID);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);

    }

    private String ExtractSource(String fileName) {
        try {
            // Load from classpath resources
            InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (stream == null) {
                throw new IOException("Shader file not found: " + fileName);
            }

            // Read the stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder source = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                source.append(line).append("\n");
            }

            reader.close();
            System.out.println("Loaded shader: " + fileName);
            return source.toString();

        } catch (IOException e) {
            System.err.println("Error reading shader file: " + e.getMessage());
            return "";
        }
    }

    private void CompileCheck(int shader) {
        int status = glGetShaderi(shader, GL_COMPILE_STATUS);
        if (status == GL_FALSE) {
            String infoLog = glGetShaderInfoLog(shader, 1024); // Max length of 1024 for info log
            System.err.println("Shader compilation failed: " + infoLog);
        }

    }

    public void SetMatrix(String name, Matrix4f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);
            matrix.get(buffer);
            glUniformMatrix4fv(location(name), false, buffer);
        }
    }

    public void SetVector(String name, Vector3f vector) {
        glUniform3f(location(name), vector.x, vector.y, vector.z);
    }

    private int location(String name) {
        int loc = glGetUniformLocation(programID, name);
        if (loc == -1)
            System.out.println("Uniform not found: " + name);
        return loc;
    }

    public void bind() {
        glUseProgram(programID);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        glDeleteProgram(programID);
    }

}
