package com.yukari.game.Render;

import static org.lwjgl.opengl.GL30.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

public class Mesh {
    private int vaoID;
    private int posVboID;
    private int uvVboID;
    private int eboID;
    private int vertexCount;
    private int indexCount;

    public Mesh(float[][] UVs) {
        float[] vertices = {
                // Front face
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,

                // Back face
                0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,

                // Left face
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,

                // Right face
                0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, 0.5f,

                // Top face
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,

                // Bottom face
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f
        };

        // BUILD UVs from the parameter, not hardcoded!
        float[] uvs = new float[48]; // 6 faces × 8 floats
        for (int i = 0; i < 6; i++) {
            System.arraycopy(UVs[i], 0, uvs, i * 8, 8);
        }

        int[] indices = {
                // Front
                0, 1, 2, 2, 3, 0,
                // Back
                4, 5, 6, 6, 7, 4,
                // Left
                8, 9, 10, 10, 11, 8,
                // Right
                12, 13, 14, 14, 15, 12,
                // Top
                16, 17, 18, 18, 19, 16,
                // Bottom
                20, 21, 22, 22, 23, 20
        };

        this.vertexCount = vertices.length / 3;
        this.indexCount = indices.length;

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Position VBO
        posVboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, posVboID);
        FloatBuffer posBuffer = BufferUtils.createFloatBuffer(vertices.length);
        posBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        // UV VBO
        uvVboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, uvVboID);
        FloatBuffer uvBuffer = BufferUtils.createFloatBuffer(uvs.length);
        uvBuffer.put(uvs).flip();
        glBufferData(GL_ARRAY_BUFFER, uvBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);

        // EBO
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        IntBuffer eboBuf = BufferUtils.createIntBuffer(indices.length);
        eboBuf.put(indices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, eboBuf, GL_STATIC_DRAW);

        glBindVertexArray(0);
    }

    public void Render() {
        glBindVertexArray(vaoID);
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void Cleanup() {
        glDeleteBuffers(posVboID);
        glDeleteBuffers(uvVboID);
        glDeleteBuffers(eboID);
        glDeleteVertexArrays(vaoID);
    }
}
