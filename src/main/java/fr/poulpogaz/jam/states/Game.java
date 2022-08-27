package fr.poulpogaz.jam.states;

import fr.poulpogaz.jam.Constants;
import fr.poulpogaz.jam.Jam;
import fr.poulpogaz.jam.entity.Bullet;
import fr.poulpogaz.jam.entity.Enemy;
import fr.poulpogaz.jam.entity.Player;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;

import java.util.ArrayList;
import java.util.List;

public class Game extends State {

    private final Player player;
    private final List<Enemy> enemies;
    private final List<Bullet> playerBullets;
    private final List<Bullet> enemiesBullets;

    public Game() {
        player = new Player(this, Jam.WIDTH_SCALED / 2, Jam.HEIGHT_SCALED / 2);
        enemies = new ArrayList<>();
        playerBullets = new ArrayList<>();
        enemiesBullets = new ArrayList<>();
    }

    @Override
    public void render(Graphics2D g2d, FontRenderer f2d) {
        player.render(g2d, f2d);

        for (Bullet p : playerBullets) {
            p.render(g2d, f2d);
        }

        if (Constants.DEBUG) {
            f2d.drawString("Player: (%d, %d)".formatted((int) player.getX(), (int) player.getY()),
                    0, f2d.getFont().getHeight());
        }
    }

    @Override
    public void update(float delta) {
        player.update(input, delta);

        for (Bullet p : playerBullets) {
            p.update(input, delta);
        }
    }

    @Override
    public void dispose() {

    }

    public void addBullet(Bullet bullet) {
        if (bullet.isPlayerBullet()) {
            playerBullets.add(bullet);
        } else {
            enemiesBullets.add(bullet);
        }
    }
}
