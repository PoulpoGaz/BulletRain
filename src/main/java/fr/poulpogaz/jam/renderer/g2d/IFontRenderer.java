package fr.poulpogaz.jam.renderer.g2d;

import fr.poulpogaz.jam.renderer.IColor;
import fr.poulpogaz.jam.renderer.shaders.Program;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public interface IFontRenderer {

    void translate(float x, float y);

    default void translate(Vector3f translation) {
        translate(translation.x, translation.y);
    }

    default void translate(Vector2f translation) {
        translate(translation.x, translation.y);
    }


    void scale(float sx, float sy);

    default void scale(Vector3f scale) {
        scale(scale.x, scale.y);
    }

    default void scale(Vector2f scale) {
        scale(scale.x, scale.y);
    }


    void rotate(float theta);


    void drawString(String str, int x, int y);

    void drawStringColored(String str, int x, int y);

    void flush();


    boolean isDrawing();


    void setFont(ImageFont font);

    ImageFont getFont();

    void setColor(IColor color);

    IColor getColor();

    void setShader(Program shader);

    Program getShader();

    void setProjection(Matrix4f projection);

    Matrix4f getProjection();

    void setTransform(Matrix4f transform);

    Matrix4f getTransform();
}
