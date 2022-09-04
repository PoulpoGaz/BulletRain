package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.Jam;
import fr.poulpogaz.jam.patterns.Boss2Pattern3;

public class Boss2Renderer implements EntityRenderer {

    private final Vector2 temp = new Vector2();

    private TextureRegion phase1;
    private TextureRegion phase2;
    private TextureRegion phase3;

    @Override
    public void loadTextures() {
        Jam.getOrLoadTexture("tileset.png");
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font, GameScreen game, Entity entity) {
        setTextures();

        Boss boss = (Boss) entity;
        int i = boss.getCurrentPhase();

        TextureRegion curr;
        if (i == 0) {
            curr = phase1;
        } else if (i == 1) {
            curr = phase2;
        } else {
            curr = phase3;
        }

        batch.draw(curr, entity.getX() - curr.getRegionWidth() / 2f, entity.getY() - curr.getRegionHeight() / 2f);

        if (i == 2) {
            Boss2Pattern3 p = (Boss2Pattern3) ((Boss) entity).getDescriptor().phases().get(i).getMovePattern(0).pattern();

            if (p.getT() <= 1000) {
                batch.end();

                ShapeRenderer sr = Jam.INSTANCE.getShape();
                sr.begin(ShapeRenderer.ShapeType.Line);
                sr.setColor(1, 0, 0, 1);

                float thetaAdd = 10;
                float theta = p.getOffset();

                while (theta <= 360f + p.getOffset()) {
                    temp.set(1, 0).rotateDeg(theta).scl(Constants.MAP_WIDTH + Constants.MAP_HEIGHT).add(entity.getPos());

                    sr.line(entity.getPos(), temp);

                    theta += thetaAdd;
                }

                sr.end();
                batch.begin();
            }
        }
    }

    private void setTextures() {
        if (phase1 == null) {
            Texture tileset = Jam.getTexture("tileset.png");

            phase1 = new TextureRegion(tileset, 48, 80, 23, 25);
            phase2 = new TextureRegion(tileset, 71, 80, 23, 25);
            phase3 = new TextureRegion(tileset, 94, 80, 23, 25);
        }
    }
}
