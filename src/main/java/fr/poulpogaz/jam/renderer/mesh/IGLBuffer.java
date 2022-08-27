package fr.poulpogaz.jam.renderer.mesh;

import fr.poulpogaz.jam.renderer.utils.Disposable;

public interface IGLBuffer extends Disposable {

    boolean updateData();

    void markDirty();

    void bind();

    void unbind();

    int getUsage();

    @Override
    void dispose();
}
