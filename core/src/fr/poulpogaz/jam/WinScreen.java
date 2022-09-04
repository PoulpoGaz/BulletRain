package fr.poulpogaz.jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.List;

public class WinScreen extends AbstractScreen {

    private Score score;
    private final GlyphLayout layout = new GlyphLayout();
    private final Color color = new Color(1, 1, 1, 1);

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

        String text = "YOU WIN!!\n" +
                "Total score = " + score.getScore(true) + "\n" +
                "Kill: " + score.getnKill() + "\n" +
                "Item: " + score.getnItem() + "\n" +
                "Total time: " + score.getTimeSurvived() + "\n" +
                "Number of continue: " + score.getnContinue() +
                (score.isPerfect() ? "\nPERFECT GAME!" : "");

        layout.setText(font, text, color, Constants.WIDTH, Align.center, false);

        Gdx.app.debug("DEBUG", "w=" + layout.width + ", h=" + layout.height);

        font.draw(spriteBatch, layout, 0, Constants.HEIGHT / 2f);

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

    public void setScore(Score score) {
        this.score = score;
    }
}
