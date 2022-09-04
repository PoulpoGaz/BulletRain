package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.Jam;

import java.util.List;

public class ThunderBulletRenderer implements EntityRenderer {
    @Override
    public void loadTextures() {

    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font, GameScreen game, Entity entity) {
        if (entity instanceof ThunderBullet) {
            ThunderBullet b = (ThunderBullet) entity;

            List<Vector2> pos = b.getHits();

            if (pos.isEmpty()) {
                return;
            }

            batch.end();
            ShapeRenderer sr = Jam.INSTANCE.getShape();
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(1, 1, 153 / 255f, 1);

            Vector2 start = entity.getPos();
            for (int i = 0; i < pos.size(); i++) {
                Vector2 p = pos.get(i);

                sr.line(start.x, start.y, p.x, p.y);

                start = p;
            }

            sr.end();
            batch.begin();
        }
    }
}
