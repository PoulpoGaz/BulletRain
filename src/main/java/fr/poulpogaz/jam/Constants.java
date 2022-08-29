package fr.poulpogaz.jam;

public class Constants {

    public static final boolean DEBUG = true;
    public static final boolean SHOW_HITBOX = DEBUG && true;

    // window
    public static final int HEIGHT = 500;
    public static final int WIDTH = HEIGHT * 3 / 4;

    public static final float HALF_WIDTH = WIDTH / 2f;
    public static final float HALF_HEIGHT = HEIGHT / 2f;
    public static final float Q_WIDTH = WIDTH / 4f;
    public static final float Q_HEIGHT = HEIGHT / 4f;
    public static final float Q3_WIDTH = 3 * WIDTH / 4f;
    public static final float Q3_HEIGHT = 3 * HEIGHT / 4f;

    public static final int OUTER_SCREEN_SIZE = 50;

    // speed
    public static final float MAP_SCROLL_SPEED = 0.75f; // added to every entity

    public static final int PLAYER_SPEED = 3;
    public static final int PLAYER_SPEED_SLOW_DOWN = 1;

    // damage
    public static final int PLAYER_BULLET_DAMAGE = 1;

    // score
    public static final int PLAYER_SURVIVE = 1; // added every tick
    public static final int PLAYER_PICK_SCORE_BLOCK = 100;
    public static final int PLAYER_KILL = 5000;
    public static final int PLAYER_KILL_BOSS = 100_000;
    public static final int PLAYER_PERFECT_STAGE = 100_000;
}
