package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.engine.AABB;
import fr.poulpogaz.jam.engine.HitBox;
import fr.poulpogaz.jam.particles.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Entity {

    protected final GameScreen game;
    protected final EntityRenderer renderer;
    protected Vector2 pos;

    protected HitBox detailedHitBox;
    protected AABB aabbHitBox = new AABB();
    protected boolean hitBoxDirty = true;
    protected boolean aabbDirty = true;

    protected List<Particle> particles = new ArrayList<>();

    public Entity(GameScreen game) {
        this.game = Objects.requireNonNull(game);
        this.renderer = Objects.requireNonNull(createRenderer());
    }

    public Entity(GameScreen game, EntityRenderer renderer) {
        this.game = Objects.requireNonNull(game);
        this.renderer = Objects.requireNonNull(renderer);
    }

    protected EntityRenderer createRenderer() {
        return null;
    }

    public void update(float delta) {
        updateParticles(delta);
    }

    protected void updateParticles(float delta) {
        int i = 0;
        while (i < particles.size()) {
            Particle p = particles.get(i);
            p.update(delta);

            if (p.isDead()) {
                particles.remove(i);
                continue;
            }
            i++;
        }
    }


    public void render(SpriteBatch batch, BitmapFont font) {
        renderer.render(batch, font, game, this);

        for (Particle p : particles) {
            p.render(batch, font);
        }
    }


    protected void markDirty() {
        hitBoxDirty = true;
        aabbDirty = true;
    }

    protected abstract HitBox getDetailedHitBoxImpl();

    protected void getAABBImpl() {
        getDetailedHitBoxImpl().getAABB(aabbHitBox);
    }

    public HitBox getDetailedHitBox() {
        if (hitBoxDirty) {
            detailedHitBox = getDetailedHitBoxImpl();
            hitBoxDirty = false;
        }

        return detailedHitBox;
    }

    public AABB getAABB() {
        if (aabbDirty) {
            getAABBImpl();
            aabbDirty = false;
        }

        return aabbHitBox;
    }



    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }

    public Vector2 getPos() {
        return pos;
    }

    public EntityRenderer getRenderer() {
        return renderer;
    }

    public boolean hasParticles() {
        return !particles.isEmpty();
    }
}
