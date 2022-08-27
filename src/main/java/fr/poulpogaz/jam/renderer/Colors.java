package fr.poulpogaz.jam.renderer;

public class Colors {

    public static final IColor WHITE = new ImmutableColor(1, 1, 1, 1);

    public static final IColor LIGHT_GRAY = new ImmutableColor(0.75f, 0.75f, 0.75f, 1);

    public static final IColor GRAY = new ImmutableColor(0.5f, 0.5f, 0.5f, 1);

    public static final IColor DARK_GRAY = new ImmutableColor(0.25f, 0.25f, 0.25f, 1);

    public static final IColor DARK = new ImmutableColor(0, 0, 0, 1);

    public static final IColor RED = new ImmutableColor(1, 0, 0, 1);

    public static final IColor PINK = new ImmutableColor(1, 25f / 40f, 25f / 40f, 1);

    public static final IColor ORANGE = new ImmutableColor(1, 40f / 51f, 0, 1);

    public static final IColor YELLOW = new ImmutableColor(1, 1, 0, 1);

    public static final IColor GREEN = new ImmutableColor(0, 1, 0, 1);

    public static final IColor MAGENTA = new ImmutableColor(1, 0, 1, 1);

    public static final IColor CYAN = new ImmutableColor(0, 1, 1, 1);

    public static final IColor BLUE = new ImmutableColor(0, 0, 1, 1);

    public static Color fromHSV(float hue, float saturation, float brightness) {
        return new Color().fromHSV(hue, saturation, brightness);
    }
}