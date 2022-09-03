package fr.poulpogaz.jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import fr.poulpogaz.jam.utils.Mathf;

import static fr.poulpogaz.jam.Constants.*;

public class RainEffect implements Effect {

    private static final float MIN_SPEED = 5;
    private static final float MAX_SPEED = 15;

    private static final float DOUBLE_ALIGN_LEN = OUTER_SCREEN_SIZE * 2 + MAP_WIDTH + MAP_HEIGHT;
    private static final float W_PERCENT = (OUTER_SCREEN_SIZE + MAP_WIDTH) / DOUBLE_ALIGN_LEN;
    private static final float H_PERCENT = (OUTER_SCREEN_SIZE + MAP_HEIGHT) / DOUBLE_ALIGN_LEN;

    private static final Color RAIN_COLOR = new Color().set(217 / 255f, 249 / 255f, 255 / 255f, 0.75f);

    private static final Color BOT = new Color(1, 1, 1, 1f);
    private static final Color TOP = new Color(71 / 255f, 119 / 255f, 214 / 255f, 1f);

    private final Array<Drop> drops = new Array<>();
    private final Vector2 rainDir = new Vector2();

    // percentage of rain that comes from the top/bottom
    private float hPercent;
    /**
     * @see Align
     */
    private int emitFrom;

    public RainEffect() {

    }

    @Override
    public void loadTextures() {

    }

    @Override
    public void reset() {
        drops.clear();

        rainDir.set(Mathf.random(-1, 1), Mathf.random(-1, 1)).nor();

        hPercent = Math.abs(rainDir.y) * H_PERCENT + (1 - Math.abs(rainDir.x)) * W_PERCENT;

        emitFrom = 0;
        if (rainDir.x < 0) {
            emitFrom |= Align.left;
        } else if (rainDir.x > 0) {
            emitFrom |= Align.right;
        }

        if (rainDir.y < 0) {
            emitFrom |= Align.top;
        } else {
            emitFrom |= Align.bottom;
        }

        Gdx.app.debug("DEBUG", "Rain dir: " + rainDir + ". emit from: " + Align.toString(emitFrom) + " percent: " + hPercent);
    }

    @Override
    public void renderEffect(SpriteBatch batch, ShapeRenderer sr) {
        batch.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        int i = 0;
        while (i < drops.size) {
            Drop d = drops.get(i);

            d.update();

            if (d.isOutside()) {
                drops.removeIndex(i);
                continue;
            }

            d.draw(sr);
            i++;
        }

        int n = Mathf.RANDOM.nextInt(40) + 10;
        for (i = 0; i < n; i++) {
            Vector2 pos = emit(emitFrom);
            float speed = Mathf.random(MIN_SPEED, MAX_SPEED);

            drops.add(new Drop(pos, rainDir.cpy().scl(speed)));
        }

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
    }

    private Vector2 emit(int align) {
        if (align == Align.left) {
            return emitLeft();
        } else if (align == Align.right) {
            return emitRight();
        } else if (align == Align.top) {
            return emitTop();
        } else if (align == Align.bottom) {
            return emitBot();
        } else if (align == Align.topLeft) {
            return emitTopLeft();
        } else if (align == Align.topRight) {
            return emitTopRight();
        } else if (align == Align.bottomLeft) {
            return emitBotLeft();
        } else if (align == Align.bottomRight) {
            return emitBotRight();
        } else {
            throw new IllegalStateException();
        }
    }

    private Vector2 emitTopLeft() {
        boolean left = Mathf.RANDOM.nextFloat() >= hPercent;

        if (left) {
            return emitLeft();
        } else {
            return emitTop();
        }
    }

    private Vector2 emitTopRight() {
        boolean right = Mathf.RANDOM.nextFloat() >= hPercent;

        if (right) {
            return emitRight();
        } else {
            return emitTop();
        }
    }

    private Vector2 emitBotLeft() {
        boolean left = Mathf.RANDOM.nextFloat() >= hPercent;

        if (left) {
            return emitLeft();
        } else {
            return emitBot();
        }
    }

    private Vector2 emitBotRight() {
        boolean right = Mathf.RANDOM.nextFloat() >= hPercent;

        if (right) {
            return emitRight();
        } else {
            return emitBot();
        }
    }

    private Vector2 emitTop() {
        float x = Mathf.random(-OUTER_SCREEN_SIZE, MAP_WIDTH + OUTER_SCREEN_SIZE);

        return new Vector2(x, MAP_HEIGHT + 15);
    }

    private Vector2 emitLeft() {
        float y = Mathf.random(-OUTER_SCREEN_SIZE, MAP_HEIGHT + OUTER_SCREEN_SIZE);

        return new Vector2(MAP_WIDTH + 15, y);
    }

    private Vector2 emitRight() {
        float y = Mathf.random(-OUTER_SCREEN_SIZE, MAP_HEIGHT + OUTER_SCREEN_SIZE);

        return new Vector2(-15, y);
    }

    private Vector2 emitBot() {
        float x = Mathf.random(-OUTER_SCREEN_SIZE, MAP_WIDTH + OUTER_SCREEN_SIZE);

        return new Vector2(x, -15);
    }

    @Override
    public void dispose() {

    }


    private static class Drop {

        private final Vector2 pos;
        private final Vector2 speed;

        public Drop(Vector2 pos, Vector2 speed) {
            this.pos = pos;
            this.speed = speed;
        }

        private void update() {
            pos.add(speed);
        }

        private void draw(ShapeRenderer sr) {
            sr.setColor(RAIN_COLOR);
            sr.line(pos.x, pos.y, pos.x + speed.x, pos.y + speed.y);
        }

        public boolean isOutside() {
            return pos.x < -OUTER_SCREEN_SIZE || pos.x > MAP_WIDTH + OUTER_SCREEN_SIZE ||
                    pos.y < -OUTER_SCREEN_SIZE || pos.y > MAP_HEIGHT + OUTER_SCREEN_SIZE;
        }

        public boolean isOutsideScreen() {
            return pos.x < 0 || pos.y < 0 || pos.x > MAP_WIDTH || pos.y > MAP_HEIGHT;
        }
    }
}
