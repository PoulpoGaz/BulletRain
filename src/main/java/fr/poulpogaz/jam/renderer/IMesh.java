package fr.poulpogaz.jam.renderer;

import fr.poulpogaz.jam.renderer.utils.Disposable;

public interface IMesh extends Disposable {

    void render();

    void renderList();
}