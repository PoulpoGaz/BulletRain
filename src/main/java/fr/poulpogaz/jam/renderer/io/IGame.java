package fr.poulpogaz.jam.renderer.io;

public interface IGame {

    void init(Window window, GameEngine engine) throws Exception;

    void render();

    void update(float delta);

    void terminate();
}