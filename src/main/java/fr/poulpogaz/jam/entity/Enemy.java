package fr.poulpogaz.jam.entity;

import fr.poulpogaz.jam.engine.polygons.AABB;
import fr.poulpogaz.jam.engine.polygons.Polygon;
import fr.poulpogaz.jam.patterns.BulletPattern;
import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;
import fr.poulpogaz.jam.renderer.io.Input;
import fr.poulpogaz.jam.states.Game;
import org.joml.Vector2f;

public class Enemy extends Entity {

    private final int maxLife;
    private final MovePattern movePattern;
    private final BulletPattern bulletPattern;

    private int life;

    private int t;
    private Vector2f dir;

    public Enemy(Game game,
                 int maxLife,
                 MovePattern movePattern,
                 BulletPattern bulletPattern,
                 Vector2f pos) {
        super(game);
        this.maxLife = maxLife;
        this.movePattern = movePattern;
        this.bulletPattern = bulletPattern;
        this.pos = pos;

        t = 0;
        dir = movePattern.dir(t);
        life = maxLife;
    }

    @Override
    public void update(Input in, float delta) {
        dir = movePattern.dir(t);
        pos.add(dir);
        t++;

        bulletPattern.addBullets(game, this, false);
    }

    @Override
    public void render(Graphics2D g2d, FontRenderer f2d) {

    }

    @Override
    public Polygon getDetailedHitBox() {
        return null;
    }

    @Override
    public AABB aabb() {
        return null;
    }

    public int getMaxLife() {
        return maxLife;
    }

    public int getLife() {
        return life;
    }

    public boolean isDied() {
        return life <= 0;
    }
}
