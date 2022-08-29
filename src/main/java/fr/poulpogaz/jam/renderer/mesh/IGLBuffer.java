package fr.poulpogaz.jam.renderer.mesh;

public interface IGLBuffer extends AutoCloseable {

    boolean updateData();

    void markDirty();

    void bind();

    void unbind();

    int getUsage();

    @Override
    void close();
}
