package com.yukari.game.Render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.lwjgl.BufferUtils;

public class Texture {
    private int textureID;
    private int width;
    private int height;

    public Texture(String path) {
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
            if (stream == null) {
                throw new Exception("Texture file not found: " + path);
            }

            // Read stream into ByteBuffer
            ReadableByteChannel channel = Channels.newChannel(stream);
            ByteBuffer imageBuffer = BufferUtils.createByteBuffer(1024 * 1024); // 1MB initial
            while (channel.read(imageBuffer) != -1) {
                if (imageBuffer.remaining() == 0) {
                    ByteBuffer newBuffer = BufferUtils.createByteBuffer(imageBuffer.capacity() * 2);
                    imageBuffer.flip();
                    newBuffer.put(imageBuffer);
                    imageBuffer = newBuffer;
                }
            }
            imageBuffer.flip();

            IntBuffer widthBuf = BufferUtils.createIntBuffer(1);
            IntBuffer heightBuf = BufferUtils.createIntBuffer(1);
            IntBuffer channelsBuf = BufferUtils.createIntBuffer(1);

            stbi_set_flip_vertically_on_load(true);
            ByteBuffer image = stbi_load_from_memory(imageBuffer, widthBuf, heightBuf, channelsBuf, 4);

            if (image != null) {
                width = widthBuf.get(0);
                height = heightBuf.get(0);

                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
                glGenerateMipmap(GL_TEXTURE_2D);

                stbi_image_free(image);
                System.out.println("Loaded texture: " + path + " (" + width + "x" + height + ")");
            } else {
                System.err.println("Failed to load texture: " + path);
            }

        } catch (Exception e) {
            System.err.println("Error loading texture: " + e.getMessage());
        }

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanup() {
        glDeleteTextures(textureID);
    }

    public int getID() {
        return textureID;
    }
}
