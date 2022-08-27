package fr.poulpogaz.jam.renderer;

public class Color implements IColor {

    float r;
    float g;
    float b;
    float a;

    public Color() {
        r = g = b = a = 1;
    }

    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1;
        clamp();
    }

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        clamp();
    }

    public Color(IColor color) {
        r = color.getRed();
        g = color.getGreen();
        b = color.getBlue();
        a = color.getAlpha();
    }

    @Override
    public float getRed() {
        return r;
    }

    @Override
    public float getGreen() {
        return g;
    }

    @Override
    public float getBlue() {
        return b;
    }

    @Override
    public float getAlpha() {
        return a;
    }

    public Color add(float val) {
        return add(val, this);
    }

    @Override
    public Color add(float val, Color dest) {
        dest.r = r + val;
        dest.g = g + val;
        dest.b = b + val;
        dest.a = a + val;
        dest.clamp();
        return dest;
    }

    public Color add(float r, float g, float b) {
        return add(r, g, b, this);
    }

    @Override
    public Color add(float r, float g, float b, Color dest) {
        dest.r = this.r + r;
        dest.g = this.g + g;
        dest.b = this.b + b;
        dest.a = a;
        dest.clamp();
        return dest;
    }

    public Color add(float r, float g, float b, float a) {
        return add(r, g, b, a, this);
    }

    @Override
    public Color add(float r, float g, float b, float a, Color dest) {
        dest.r = this.r + r;
        dest.g = this.g + g;
        dest.b = this.b + b;
        dest.a = this.a + a;
        dest.clamp();
        return dest;
    }

    public Color add(IColor color) {
        return add(color, this);
    }

    @Override
    public Color add(IColor color, Color dest) {
        dest.r = r + color.getRed();
        dest.g = g + color.getGreen();
        dest.b = b + color.getBlue();
        dest.a = a + color.getAlpha();
        dest.clamp();
        return dest;
    }

    public Color sub(float val) {
        return sub(val, this);
    }

    @Override
    public Color sub(float val, Color dest) {
        dest.r = r - val;
        dest.g = g - val;
        dest.b = b - val;
        dest.a = a - val;
        dest.clamp();
        return dest;
    }

    public Color sub(float r, float g, float b) {
        return sub(r, g, b, this);
    }

    @Override
    public Color sub(float r, float g, float b, Color dest) {
        dest.r = this.r - r;
        dest.g = this.g - g;
        dest.b = this.b - b;
        dest.a = a;
        dest.clamp();
        return dest;
    }

    public Color sub(float r, float g, float b, float a) {
        return sub(r, g, b, a, this);
    }

    @Override
    public Color sub(float r, float g, float b, float a, Color dest) {
        dest.r = this.r - r;
        dest.g = this.g - g;
        dest.b = this.b - b;
        dest.a = this.a - a;
        dest.clamp();
        return dest;
    }

    public Color sub(IColor color) {
        return sub(color, this);
    }

    @Override
    public Color sub(IColor color, Color dest) {
        dest.r = r - color.getRed();
        dest.g = g - color.getGreen();
        dest.b = b - color.getBlue();
        dest.a = a - color.getAlpha();
        dest.clamp();
        return dest;
    }

    public Color mul(float val) {
        return mul(val, this);
    }

    @Override
    public Color mul(float val, Color dest) {
        dest.r = r * val;
        dest.g = g * val;
        dest.b = b * val;
        dest.a = a * val;
        dest.clamp();
        return dest;
    }

    public Color mul(float r, float g, float b) {
        return mul(r, g, b, this);
    }

    @Override
    public Color mul(float r, float g, float b, Color dest) {
        dest.r = this.r * r;
        dest.g = this.g * g;
        dest.b = this.b * b;
        dest.a = a;
        dest.clamp();
        return dest;
    }

    public Color mul(float r, float g, float b, float a) {
        return mul(r, g, b, a, this);
    }

    @Override
    public Color mul(float r, float g, float b, float a, Color dest) {
        dest.r = this.r * r;
        dest.g = this.g * g;
        dest.b = this.b * b;
        dest.a = this.a * a;
        dest.clamp();
        return dest;
    }

    public Color mul(IColor color) {
        return mul(color, this);
    }

    @Override
    public Color mul(IColor color, Color dest) {
        dest.r = r * color.getRed();
        dest.g = g * color.getGreen();
        dest.b = b * color.getBlue();
        dest.a = a * color.getAlpha();
        dest.clamp();
        return dest;
    }

    public Color div(float val) {
        return div(val, this);
    }

    @Override
    public Color div(float val, Color dest) {
        dest.r = r / val;
        dest.g = g / val;
        dest.b = b / val;
        dest.a = a / val;
        dest.clamp();
        return dest;
    }

    public Color div(float r, float g, float b) {
        return div(r, g, b, this);
    }

    @Override
    public Color div(float r, float g, float b, Color dest) {
        dest.r = this.r / r;
        dest.g = this.g / g;
        dest.b = this.b / b;
        dest.a = a;
        dest.clamp();
        return dest;
    }

    public Color div(float r, float g, float b, float a) {
        return div(r, g, b, a, this);
    }

    @Override
    public Color div(float r, float g, float b, float a, Color dest) {
        dest.r = this.r / r;
        dest.g = this.g / g;
        dest.b = this.b / b;
        dest.a = this.a / a;
        dest.clamp();
        return dest;
    }

    public Color div(IColor color) {
        return div(color, this);
    }

    @Override
    public Color div(IColor color, Color dest) {
        dest.r = r / color.getRed();
        dest.g = g / color.getGreen();
        dest.b = b / color.getBlue();
        dest.a = a / color.getAlpha();
        dest.clamp();
        return dest;
    }

    public Color lerp(IColor color, float t) {
        return lerp(color, t, this);
    }

    @Override
    public Color lerp(IColor color, float t, Color dest) {
        dest.r = r + t * (color.getRed() - r);
        dest.g = g + t * (color.getGreen() - g);
        dest.b = b + t * (color.getBlue() - b);
        dest.a = a + t * (color.getAlpha() - a);
        dest.clamp();
        return dest;
    }

    public Color lerp(float r, float g, float b, float a, float t) {
        return lerp(r, g, b, a, t, this);
    }

    @Override
    public Color lerp(float r, float g, float b, float a, float t, Color dest) {
        dest.r = this.r + t * (r - this.r);
        dest.g = this.g + t * (g - this.g);
        dest.b = this.b + t * (b - this.b);
        dest.a = this.a + t * (a - this.a);
        dest.clamp();
        return dest;
    }

    public Color fromHSV(float hue, float saturation, float brightness) {
        if (saturation == 0) {
            r = g = b = brightness;
        } else {
            float h = (hue - (int) hue) * 6.0f;
            int i = (int) h;
            float f = h - i;
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));

            switch (i) {
                case 0 -> {
                    r = brightness;
                    g = t;
                    b = p;
                }
                case 1 -> {
                    r = q;
                    g = brightness;
                    b = p;
                }
                case 2 -> {
                    r = p;
                    g = brightness;
                    b = t;
                }
                case 3 -> {
                    r = p;
                    g = q;
                    b = brightness;
                }
                case 4 -> {
                    r = t;
                    g = p;
                    b = brightness;
                }
                case 5 -> {
                    r = brightness;
                    g = p;
                    b = q;
                }
            }
        }

        return this;
    }

    @Override
    public Color copy() {
        return new Color(this);
    }

    @Override
    public ImmutableColor immutableCopy() {
        return new ImmutableColor(this);
    }

    protected Color clamp() {
        if (r < 0) {
            r = 0;
        } else if (r > 1) {
            r = 1;
        }

        if (g < 0) {
            g = 0;
        } else if (g > 1) {
            g = 1;
        }

        if (b < 0) {
            b = 0;
        } else if (b > 1) {
            b = 1;
        }

        if (a < 0) {
            a = 0;
        } else if (a > 1) {
            a = 1;
        }

        return this;
    }
}