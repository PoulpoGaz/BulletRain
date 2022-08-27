package fr.poulpogaz.jam.renderer;

import org.joml.Math;

public class ImmutableColor implements IColor {

    private final float r;
    private final float g;
    private final float b;
    private final float a;

    public ImmutableColor(float r, float g, float b, float a) {
        this.r = Math.clamp(0, 1, r);
        this.g = Math.clamp(0, 1, g);
        this.b = Math.clamp(0, 1, b);
        this.a = Math.clamp(0, 1, a);
    }

    public ImmutableColor(float r, float g, float b) {
        this.r = Math.clamp(0, 1, r);
        this.g = Math.clamp(0, 1, g);
        this.b = Math.clamp(0, 1, b);
        this.a = 1;
    }

    public ImmutableColor(IColor color) {
        this.r = color.getRed();
        this.g = color.getGreen();
        this.b = color.getBlue();
        this.a = color.getAlpha();
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

    @Override
    public Color add(float val, Color dest) {
        dest.r = r + val;
        dest.g = g + val;
        dest.b = b + val;
        dest.a = a + val;
        dest.clamp();
        return dest;
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

    @Override
    public Color add(float r, float g, float b, float a, Color dest) {
        dest.r = this.r + r;
        dest.g = this.g + g;
        dest.b = this.b + b;
        dest.a = this.a + a;
        dest.clamp();
        return dest;
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

    @Override
    public Color sub(float val, Color dest) {
        dest.r = r - val;
        dest.g = g - val;
        dest.b = b - val;
        dest.a = a - val;
        dest.clamp();
        return dest;
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

    @Override
    public Color sub(float r, float g, float b, float a, Color dest) {
        dest.r = this.r - r;
        dest.g = this.g - g;
        dest.b = this.b - b;
        dest.a = this.a - a;
        dest.clamp();
        return dest;
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

    @Override
    public Color mul(float val, Color dest) {
        dest.r = r * val;
        dest.g = g * val;
        dest.b = b * val;
        dest.a = a * val;
        dest.clamp();
        return dest;
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

    @Override
    public Color mul(float r, float g, float b, float a, Color dest) {
        dest.r = this.r * r;
        dest.g = this.g * g;
        dest.b = this.b * b;
        dest.a = this.a * a;
        dest.clamp();
        return dest;
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

    @Override
    public Color div(float val, Color dest) {
        dest.r = r / val;
        dest.g = g / val;
        dest.b = b / val;
        dest.a = a / val;
        dest.clamp();
        return dest;
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

    @Override
    public Color div(float r, float g, float b, float a, Color dest) {
        dest.r = this.r / r;
        dest.g = this.g / g;
        dest.b = this.b / b;
        dest.a = this.a / a;
        dest.clamp();
        return dest;
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

    @Override
    public Color lerp(IColor color, float t, Color dest) {
        dest.r = r + t * (color.getRed() - r);
        dest.g = g + t * (color.getGreen() - g);
        dest.b = b + t * (color.getBlue() - b);
        dest.a = a + t * (color.getAlpha() - a);
        dest.clamp();
        return dest;
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


    @Override
    public Color copy() {
        return new Color(this);
    }

    @Override
    public ImmutableColor immutableCopy() {
        return new ImmutableColor(this);
    }
}