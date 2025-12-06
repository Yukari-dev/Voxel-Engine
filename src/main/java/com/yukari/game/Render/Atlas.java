package com.yukari.game.Render;

public class Atlas {
    private Texture texture;
    private int tilesX;
    private int tilesY;
    private float tileWidth;
    private float tileHeight;

    public Atlas(String texturePath, int tilesX, int tilesY) {
        this.texture = new Texture(texturePath);
        this.tilesX = tilesX;
        this.tilesY = tilesY;
        this.tileWidth = 1.0f / tilesX;
        this.tileHeight = 1.0f / tilesY;
    }

    public float[] getUVs(int tileX, int tileY) {
        float x = tileX * tileWidth;
        float y = tileY * tileHeight;

        return new float[] {
                x, y, // Bottom-left
                x + tileWidth, y, // Bottom-right
                x + tileWidth, y + tileHeight, // Top-right
                x, y + tileHeight // Top-left
        };
    }

    public float[][] getBlockUVs(String name) {
        switch (name) {
            case "Dirt":
                return new float[][] {
                        getUVs(1, 0),
                        getUVs(1, 0),
                        getUVs(1, 0),
                        getUVs(1, 0),
                        getUVs(1, 0),
                        getUVs(1, 0),
                };
            default:
                return new float[][] {
                        getUVs(0, 0),
                        getUVs(0, 0),
                        getUVs(0, 0),
                        getUVs(0, 0),
                        getUVs(0, 0),
                        getUVs(0, 0),
                };
        }
    }

    public void bind() {
        texture.bind();
    }

    public void unbind() {
        texture.unbind();
    }

    public void cleanup() {
        texture.cleanup();
    }
}
