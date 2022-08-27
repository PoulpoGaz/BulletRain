package fr.poulpogaz.jam.renderer;

import java.util.Objects;

public class SubTexture implements ITexture {

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    private final Texture texture;

    public SubTexture(int x, int y, int width, int height, Texture texture) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = Objects.requireNonNull(texture);

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Negative width or height");
        }

        if (x < texture.getX() || y < texture.getY() || x + width > texture.getWidth() || y + height > texture.getHeight()) {
            throw new IllegalArgumentException("Illegal x/y/width/height values");
        }
    }

    @Override
    public void bind() {
        texture.bind();
    }

    @Override
    public void bind(int index) {
        texture.bind(index);
    }

    @Override
    public void unbind() {
        texture.unbind();
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getFullWidth() {
        return texture.getWidth();
    }

    @Override
    public int getFullHeight() {
        return texture.getHeight();
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
