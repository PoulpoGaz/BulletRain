package fr.poulpogaz.jam.renderer;

public interface IMesh extends AutoCloseable {

    void render();

    void renderList();
}