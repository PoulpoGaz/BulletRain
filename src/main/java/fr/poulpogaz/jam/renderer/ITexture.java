package fr.poulpogaz.jam.renderer;

public interface ITexture extends AutoCloseable {

    void bind();

    void bind(int index);

    void unbind();

    int getX();

    int getY();

    int getWidth();

    int getHeight();

    default int getFullWidth() {
        return getWidth();
    }

    default int getFullHeight() {
        return getHeight();
    }
}
