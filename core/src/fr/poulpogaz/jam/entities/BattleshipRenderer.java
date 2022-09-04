package fr.poulpogaz.jam.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.GameScreen;
import fr.poulpogaz.jam.Jam;

public class BattleshipRenderer implements EntityRenderer {

    private static final float TURRET_CX = 6.5f;
    private static final float TURRET_CY = 6.5f;

    private TextureRegion battleship;
    private TextureRegion turret;

    private final Vector2 temp = new Vector2();

    @Override
    public void loadTextures() {
        Jam.getOrLoadTexture("tileset.png");
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font, GameScreen game, Entity entity) {
        setTextures();

        if (entity instanceof LivingEntity) {
            LivingEntity l = (LivingEntity) entity;

            if (l.isDying()) {
                batch.setColor(1, 1, 1, 1 - l.percentToDeath());
                render(batch, game, (Battleship) entity);
                batch.setColor(1, 1, 1, 1);
                return;
            }
        }

        render(batch, game, (Battleship) entity);
    }

    private void render(SpriteBatch batch, GameScreen game, Battleship me) {
        float angle = me.getAngleDeg();

        batch.draw(battleship,
                me.getX() - battleship.getRegionWidth() / 2f, me.getY() - battleship.getRegionHeight() / 2f,
                battleship.getRegionWidth() / 2f, battleship.getRegionHeight() / 2f, battleship.getRegionWidth(), battleship.getRegionHeight(),
                1, 1,
                angle);

        // turret 1
        Vector2 player = game.getPlayer().getPos();

        drawTurret(batch, player, me.getTurret1Pos());
        drawTurret(batch, player, me.getTurret2Pos());
    }

    private void drawTurret(SpriteBatch batch, Vector2 player, Vector2 pos) {
        float angle = temp.set(player).sub(pos).angleDeg();
        batch.draw(turret,
                pos.x - TURRET_CX, pos.y - TURRET_CY,
                TURRET_CX, TURRET_CY, turret.getRegionWidth(), turret.getRegionHeight(),
                1, 1,
                angle);
    }

    private void setTextures() {
        if (battleship == null && turret == null) {
            Texture tileset = Jam.getTexture("tileset.png");

            battleship = new TextureRegion(tileset, 128, 48, 87, 18);
            turret = new TextureRegion(tileset, 128, 32, 24, 13);
        }
    }
}
