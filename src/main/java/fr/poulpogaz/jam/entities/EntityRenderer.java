package fr.poulpogaz.jam.entities;

import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.states.Game;

public interface EntityRenderer {

    void loadTextures() throws Exception;

    void render(Graphics2D g2d, FontRenderer f2d, Game game, Entity entity);
}
