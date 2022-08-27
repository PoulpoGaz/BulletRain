package fr.poulpogaz.jam.entity;

import fr.poulpogaz.jam.patterns.MovePattern;
import fr.poulpogaz.jam.renderer.io.Input;
import fr.poulpogaz.jam.states.Game;
import org.joml.Vector2f;

public abstract class AbstractBullet extends Entity {

    protected final boolean playerBullet;
    protected final MovePattern movePattern;

    protected int t = 0;
    protected Vector2f dir;

    protected float angle;

    public AbstractBullet(Game game, boolean playerBullet, MovePattern movePattern, Vector2f pos) {
        super(game);
        this.pos = pos;
        this.playerBullet = playerBullet;
        this.movePattern = movePattern;

        dir = movePattern.dir(0);
        angle =  computeAngle();
    }

    @Override
    public void update(Input in, float delta) {
        dir = movePattern.dir(t);
        pos.add(dir);

        angle = computeAngle();

        t++;
    }

    private float computeAngle() {
        return (float) (Math.atan2(dir.y, dir.x));
    }

    public boolean isPlayerBullet() {
        return playerBullet;
    }
}
