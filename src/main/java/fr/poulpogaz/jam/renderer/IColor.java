package fr.poulpogaz.jam.renderer;

import org.joml.Vector4f;

public interface IColor {

    float getRed();

    float getGreen();

    float getBlue();

    float getAlpha();


    Color add(float val, Color dest);

    Color add(float r, float g, float b, Color dest);

    Color add(float r, float g, float b, float a, Color dest);

    Color add(IColor color, Color dest);


    Color sub(float val, Color dest);

    Color sub(float r, float g, float b, Color dest);

    Color sub(float r, float g, float b, float a, Color dest);

    Color sub(IColor color, Color dest);


    Color mul(float val, Color dest);

    Color mul(float r, float g, float b, Color dest);

    Color mul(float r, float g, float b, float a, Color dest);

    Color mul(IColor color, Color dest);


    Color div(float val, Color dest);

    Color div(float r, float g, float b, Color dest);

    Color div(float r, float g, float b, float a, Color dest);

    Color div(IColor color, Color dest);


    Color lerp(IColor color, float t, Color dest);

    Color lerp(float r, float g, float b, float a, float t, Color dest);


    Color copy();

    ImmutableColor immutableCopy();

    default Vector4f asVector() {
        return new Vector4f(getRed(), getGreen(), getBlue(), getAlpha());
    }
}
