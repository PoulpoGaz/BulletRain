package fr.poulpogaz.jam.renderer.mesh;

import fr.poulpogaz.jam.renderer.utils.Disposable;

public interface IMesh extends Disposable {

    void render(int primitive);

    VertexAttributes getVertexAttributes();

    int getUsage();

    @Override
    void dispose();
}
