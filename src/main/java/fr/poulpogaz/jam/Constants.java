package fr.poulpogaz.jam;

public class Constants {

    public static final boolean DEBUG = true;
    public static final boolean SHOW_HITBOX = DEBUG && false;

    // speed
    public static final int MAP_SPEED = 1;

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
