package fr.poulpogaz.jam;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import fr.poulpogaz.jam.utils.Utils;

import java.util.List;

public class WinScreen extends AbstractScreen {

    public WinScreen(Jam jam) {
        super(jam);
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void getDebugInfo(List<String> out) {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        spriteBatch.begin();
        spriteBatch.enableBlending();

        font.draw(spriteBatch, "YOU WIN!: SCORE= 0", 0, (Constants.HEIGHT - Utils.fontHeight(font)) / 2f,
                Constants.WIDTH, Align.center, false);

        spriteBatch.disableBlending();
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
