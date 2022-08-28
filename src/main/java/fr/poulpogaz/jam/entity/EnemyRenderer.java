package fr.poulpogaz.jam.entity;

import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.states.Game;

public interface EnemyRenderer {

    void loadTextures() throws Exception;

    void render(Graphics2D g2d, FontRenderer f2d, Game game, Enemy enemy);
}
