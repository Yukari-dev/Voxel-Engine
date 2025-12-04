package com.yukari.game.Render;

import static org.lwjgl.opengl.GL30.*;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

public class Mesh {

    private int vaoID;
    private int vboID;
    private int vertexCount;

    public Mesh(float[] vertices) {
        this.vertexCount = vertices.length / 3;

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
        glBindVertexArray(0);

    }

    public void Render() {
        glBindVertexArray(vaoID);

        glDrawArrays(GL_TRIANGLES, 0, vertexCount);

        glBindVertexArray(0);

    }

    public void Cleanup() {

    }

}
