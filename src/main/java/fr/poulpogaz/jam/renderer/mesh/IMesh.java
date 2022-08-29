package fr.poulpogaz.jam.renderer.mesh;

public interface IMesh extends AutoCloseable {

    void render(int primitive);

    VertexAttributes getVertexAttributes();

    int getUsage();

    @Override
    void close();
}
