package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.poulpogaz.jam.GameScreen;

public interface EntityRenderer {

    void loadTextures();

    void render(SpriteBatch batch, BitmapFont font, GameScreen game, Entity entity);
}
