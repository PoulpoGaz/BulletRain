package fr.poulpogaz.jam;

public class Constants {

    public static boolean DEBUG = false;
    public static boolean SHOW_HITBOX = false;

    // window
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    // the size of the map
    public static final int MAP_WIDTH = 420;
    public static final int MAP_HEIGHT = HEIGHT;

    public static final int SCALE_FACTOR = 2;

    public static final int WINDOW_WIDTH = Constants.WIDTH * SCALE_FACTOR;
    public static final int WINDOW_HEIGHT = Constants.HEIGHT * SCALE_FACTOR;

    public static final float HALF_WIDTH = WIDTH / 2f;
    public static final float HALF_HEIGHT = HEIGHT / 2f;
    public static final float Q_WIDTH = WIDTH / 4f;
    public static final float Q_HEIGHT = HEIGHT / 4f;
    public static final float Q3_WIDTH = 3 * WIDTH / 4f;
    public static final float Q3_HEIGHT = 3 * HEIGHT / 4f;

    public static final float M_HALF_WIDTH = MAP_WIDTH / 2f;
    public static final float M_HALF_HEIGHT = MAP_HEIGHT / 2f;
    public static final float M_Q_WIDTH = MAP_WIDTH / 4f;
    public static final float M_Q_HEIGHT = MAP_HEIGHT / 4f;
    public static final float M_Q3_WIDTH = 3 * MAP_WIDTH / 4f;
    public static final float M_Q3_HEIGHT = 3 * MAP_HEIGHT / 4f;

    public static final int OUTER_SCREEN_SIZE = 50;

    public static final float PLAYER_MIN_POWER = 1f;
    public static final float PLAYER_MAX_POWER = 4f;
    public static final float POWER_BLOCK_VALUE = 0.05f;

    // speed
    public static final float MAP_SCROLL_SPEED = 0.75f; // added to every entity

    public static final float PLAYER_SPEED = 4f;
    public static final float PLAYER_SPEED_SLOW_DOWN = 1.5f;

    public static final float PLAYER_HITBOX_RAD = 3;

    // bullets
    public static final float PLAYER_BULLET_SPEED = 5f;
    public static final String PLAYER_BULLET_NAME = "player_bullet";

    // score
    public static final int PLAYER_SURVIVE = 1; // added every tick
    public static final int PLAYER_PICK_SCORE_BLOCK = 100;
    public static final int PLAYER_KILL = 5000;
    public static final int PLAYER_KILL_BOSS = 50_000;
    public static final int PLAYER_PERFECT_GAME = 100_000;

    // all items at a distance of 30 are attracted
    public static final float PLAYER_MIN_ATTRACTION = 50f;

    public static final float ITEM_MAX_SPEED = 3f;
    public static final float ITEM_MAX_SPEED_2 = ITEM_MAX_SPEED * ITEM_MAX_SPEED;
}
