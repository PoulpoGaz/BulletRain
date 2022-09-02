package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.engine.HitBox;
import fr.poulpogaz.jam.utils.Mathf;

import static fr.poulpogaz.jam.Constants.ITEM_MAX_SPEED;
import static fr.poulpogaz.jam.Constants.MAP_SCROLL_SPEED;

public class Item extends Entity implements Pool.Poolable {

    public static final ItemRenderer renderer = new ItemRenderer();

    public static final int SCORE = 0;
    public static final int POWER = 1;

    private int type;
    private float value;
    private final Vector2 speed;

    private boolean picked;

    private boolean attracted = false;

    public Item(GameScreen game) {
        super(game, renderer);

        pos = new Vector2();
        speed = new Vector2();
    }

    public void set(float x, float y, int type, float value) {
        this.pos.set(x, y);
        this.speed.set(Mathf.RANDOM.nextFloat() * 2 - 1, Mathf.RANDOM.nextFloat() * 2 - 1)
                .nor().scl(ITEM_MAX_SPEED);

        this.type = type;
        this.value = value;
    }

    @Override
    public void update(float delta) {
        Player p = game.getPlayer();

        Vector2 dir = p.getPos().cpy().sub(pos);
        float dist = dir.len();

        if ((attracted && dist <= 2 * p.getAttractionPower()) || (dist <= p.getAttractionPower())) {
            Gdx.app.debug("DEBUG", "dist= " + dist);

            float s = Math.min(dist, Math.min(15f, p.getAttractionPower() / 10f));

            speed.set(0, 0);
            pos.add(dir.nor().scl(s));

            attracted = true;
        } else {
            speed.add(0, -MAP_SCROLL_SPEED);

            if (speed.len2() >= Constants.ITEM_MAX_SPEED_2) {
                speed.scl(ITEM_MAX_SPEED / speed.len());
            }

            pos.add(speed);
            attracted = false;
        }

        markDirty();
        if (getDetailedHitBox().collide(p.getDetailedHitBox())) {
            p.pick(this);
        }
    }

    @Override
    protected HitBox getDetailedHitBoxImpl() {
        aabbHitBox.set(pos.x - 8, pos.y - 8, 16, 16);
        return aabbHitBox;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    public boolean isPicked() {
        return picked;
    }

    public float getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    @Override
    public void reset() {
        value = 0;
        type = 0;
        pos.x = 0;
        pos.y = 0;
        picked = false;
        attracted = false;
        markDirty();
    }

    public static class ItemRenderer implements EntityRenderer {

        private static final TextureEntityRenderer SCORE = new TextureEntityRenderer("tileset.png", 16, 144, 16, 16);
        private static final TextureEntityRenderer POWER = new TextureEntityRenderer("tileset.png", 0, 144, 16, 16);

        @Override
        public void loadTextures() {
            SCORE.loadTextures();
            POWER.loadTextures();
        }

        @Override
        public void render(SpriteBatch batch, BitmapFont font, GameScreen game, Entity entity) {
            if (entity instanceof Item) {
                Item i = (Item) entity;

                if (i.getType() == Item.POWER) {
                    POWER.render(batch, font, game, entity);
                } else if (i.getType() == Item.SCORE) {
                    SCORE.render(batch, font, game, entity);
                } else {
                    Gdx.app.error("ERROR", "Unknown item type: " + i.getType());
                }

            } else {
                throw new IllegalStateException();
            }
        }
    }
}
