package fr.poulpogaz.jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import fr.poulpogaz.jam.utils.Size;
import fr.poulpogaz.jam.utils.Utils;

import java.util.List;

public class MainMenuScreen extends AbstractScreen {

    private final Menu menu;
    private final Size s = new Size();

    public MainMenuScreen(Jam jam) {
        super(jam);

        menu = new Menu();
        menu.addLabel("Play");
        menu.addLabel("Exit");
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
        font.getData().setScale(1);
        menu.getPreferredSize(font, s);
        menu.draw(spriteBatch, font, (Constants.WIDTH - s.width) / 2f, (Constants.HEIGHT - s.height) / 2f + Utils.fontHeight(font), s);
        font.getData().setScale(0.5f);
        spriteBatch.end();
        spriteBatch.disableBlending();

        int s = menu.update();

        if (s == 0) {
            jam.setScreen(jam.getGameScreen());
        } else if (s == 1) {
            Gdx.app.exit();
        }
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
