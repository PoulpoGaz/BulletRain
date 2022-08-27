package fr.poulpogaz.jam.states;

import fr.poulpogaz.jam.entity.Player;
import fr.poulpogaz.jam.renderer.g2d.FontRenderer;
import fr.poulpogaz.jam.renderer.g2d.Graphics2D;

public class Game extends State {

    private Player player;

    public Game() {
        player = new Player();
    }

    @Override
    public void render(Graphics2D g2d, FontRenderer f2d) {
        player.render(g2d, f2d);
    }

    @Override
    public void update(float delta) {
        player.update(input, delta);
    }

    @Override
    public void dispose() {

    }
}
