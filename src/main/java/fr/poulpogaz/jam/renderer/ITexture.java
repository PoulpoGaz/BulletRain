package fr.poulpogaz.jam.renderer;

import fr.poulpogaz.jam.renderer.utils.Disposable;

public interface ITexture extends Disposable {

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
