package fr.poulpogaz.jam.renderer.g2d;

import fr.poulpogaz.jam.renderer.Colors;
import fr.poulpogaz.jam.renderer.IColor;
import fr.poulpogaz.jam.renderer.Texture;
import fr.poulpogaz.jam.renderer.shaders.Program;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class OldFontRenderer {

    private static final int VECTOR_2_SIZE_FLOAT = 2;
    private static final int VECTOR_3_SIZE_FLOAT = 3;
    private static final int VECTOR_4_SIZE_FLOAT = 4;
    private static final int INSTANCE_SIZE_FLOAT = VECTOR_2_SIZE_FLOAT + VECTOR_3_SIZE_FLOAT + VECTOR_4_SIZE_FLOAT;

    private static final int VECTOR_2_SIZE = VECTOR_2_SIZE_FLOAT * 4;
    private static final int VECTOR_3_SIZE = VECTOR_3_SIZE_FLOAT * 4;
    private static final int VECTOR_4_SIZE = VECTOR_4_SIZE_FLOAT * 4;
    private static final int INSTANCE_SIZE = INSTANCE_SIZE_FLOAT * 4;

    private static final int INDICES_COUNT = 6;

    private static Program shader;

    private static int vao;
    private static int vbo;
    private static int ibo;
    private static int glInstanceBuffer;

    private static FloatBuffer instanceBuffer;

    private static ImageFont defaultFont;

    public static void init() throws IOException {
        //String vertexData = Resource.readString("old/font_2D.vert");
        //String fragmentData = Resource.readString("old/font_2D.frag");

        //shader = new Shader(vertexData, fragmentData);
        //shader.bindAttribute(0, "vertex");
        //shader.bindAttribute(1, "model");
        //shader.bindAttribute(2, "glyph_info");
        //shader.bindAttribute(3, "color");
        //shader.link();

        //shader.createUniform("projection");
        //shader.createUniform("texture_size");
        //shader.createUniform("font_height");
        //shader.createUniform("height");
        //shader.createUniform("height_ratio");
        //shader.createUniform("sampler");

        FloatBuffer position = null;
        IntBuffer indices = null;
        try {
            position = MemoryUtil.memAllocFloat(8);
            position.put(0).put(0);
            position.put(1).put(0);
            position.put(1).put(1);
            position.put(0).put(1);
            position.flip();

            indices = MemoryUtil.memAllocInt(6);
            indices.put(0).put(1).put(2);
            indices.put(0).put(2).put(3);
            indices.flip();

            // create mesh
            vao = glGenVertexArrays();
            glBindVertexArray(vao);

            // vbo
            vbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, position, GL_STATIC_DRAW);
            glEnableVertexAttribArray(0);
            glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

            glInstanceBuffer = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, glInstanceBuffer);

            // translation
            int index = 1;
            int offset = 0;

            glVertexAttribPointer(index, 2, GL_FLOAT, false, INSTANCE_SIZE, offset);
            glVertexAttribDivisor(index, 1);
            glEnableVertexAttribArray(index);
            offset += VECTOR_2_SIZE;
            index++;

            // glyph info
            glVertexAttribPointer(index, 3, GL_FLOAT, false, INSTANCE_SIZE, offset);
            glVertexAttribDivisor(index, 1);
            glEnableVertexAttribArray(index);
            offset += VECTOR_3_SIZE;
            index++;

            // Color
            glVertexAttribPointer(index, 4, GL_FLOAT, false, INSTANCE_SIZE, offset);
            glVertexAttribDivisor(index, 1);
            glEnableVertexAttribArray(index);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            // ibo
            ibo = glGenBuffers();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

            glBindVertexArray(0);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        } finally {
            if (position != null) {
                MemoryUtil.memFree(position);
            }
            if (indices != null) {
                MemoryUtil.memFree(indices);
            }
        }

        instanceBuffer = MemoryUtil.memAllocFloat(INSTANCE_SIZE_FLOAT * 50); // 50 chars
    }

    public static void drawString(String text, Matrix4f projection, float x, float y) {
        drawString(null, text, projection, Colors.WHITE, x, y);
    }

    public static void drawString(ImageFont font, String text, Matrix4f projection, float x, float y) {
        font = getFont(font);

        if (font == null) {
            return;
        }

        drawString(font, text, projection, Colors.WHITE, x, y, font.getHeight());
    }

    public static void drawString(String text, Matrix4f projection, IColor color, float x, float y) {
        drawString(null, text, projection, color, x, y);
    }

    public static void drawString(ImageFont font, String text, Matrix4f projection, IColor color, float x, float y) {
        font = getFont(font);

        if (font == null) {
            return;
        }

        drawString(font, text, projection, color, x, y, font.getHeight());
    }

    public static void drawString(String text, Matrix4f projection, IColor color, float x, float y, float height) {
        ImageFont font = getFont(null);

        if (font == null) {
            return;
        }

        drawString(font, text, projection, color, x, y, height);
    }

    public static void drawString(ImageFont font, String text, Matrix4f projection, IColor color, float x, float y, float height) {
        font = getFont(font);

        if (font == null) {
            return;
        }

        // pre init
        instanceBuffer.clear();

        int size = INSTANCE_SIZE * text.length();
        if (instanceBuffer.capacity() <= size) {
            MemoryUtil.memRealloc(instanceBuffer, size);
        }

        float height_ratio = height / font.getHeight();

        fillBuffer(font, text, x, y, color, height_ratio, instanceBuffer);
        instanceBuffer.flip();

        shader.bind();
        shader.setUniform("projection", projection);
        shader.setUniformi("texture_size", font.getTextureSize());
        shader.setUniformi("font_height", font.getHeight());
        shader.setUniformf("height", height);
        shader.setUniformf("height_ratio", height_ratio);
        shader.setUniformi("sampler", 0);
        font.getTexture().bind();

        glBindVertexArray(vao);

        // draw
        glBindBuffer(GL_ARRAY_BUFFER, glInstanceBuffer);
        glBufferData(GL_ARRAY_BUFFER, instanceBuffer, GL_DYNAMIC_DRAW);

        glDrawElementsInstanced(GL_TRIANGLES, INDICES_COUNT, GL_UNSIGNED_INT, 0, text.length());

        // post init
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        Program.unbind();
        Texture.unbind();
    }

    private static void fillBuffer(ImageFont font, String text, float x, float y, IColor color, float height_ratio, FloatBuffer instanceBuffer) {
        for (char c : text.toCharArray()) {
            // translation
            instanceBuffer.put(x).put(y);

            // glyph info
            Glyph glyph = font.getGlyph(c);

            instanceBuffer.put(glyph.x());
            instanceBuffer.put(glyph.y());
            instanceBuffer.put(glyph.width());

            // color
            instanceBuffer.put(color.getRed())
                    .put(color.getGreen())
                    .put(color.getBlue())
                    .put(color.getAlpha());

            x += glyph.width() * height_ratio;
        }
    }

    public static void drawStringMultipleColors(String text, Matrix4f projection, float x, float y, float height) {
        ImageFont font = getFont(null);

        if (font == null) {
            return;
        }

        drawStringMultipleColors(font, text, projection, x, y, height);
    }

    /**
     * Multi color string: <c 1 1 1 1>Hello <c 1 0.5 0.25 1> world!
     */
    public static void drawStringMultipleColors(ImageFont font, String text, Matrix4f projection, float x, float y, float height) {
        font = getFont(font);

        if (font == null) {
            return;
        }

        // pre init
        instanceBuffer.clear();

        int size = INSTANCE_SIZE * text.length();
        if (instanceBuffer.capacity() <= size) {
            instanceBuffer = MemoryUtil.memRealloc(instanceBuffer, size);
        }

        float height_ratio = height / font.getHeight();

        int length = fillBufferMultipleColors(font, text, x, y, height_ratio, instanceBuffer);
        instanceBuffer.flip();

        shader.bind();
        shader.setUniform("projection", projection);
        shader.setUniformi("texture_size", font.getTextureSize());
        shader.setUniformi("font_height", font.getHeight());
        shader.setUniformf("height", height);
        shader.setUniformf("height_ratio", height_ratio);
        shader.setUniformi("sampler", 0);
        font.getTexture().bind();

        glBindVertexArray(vao);

        // draw
        glBindBuffer(GL_ARRAY_BUFFER, glInstanceBuffer);
        glBufferData(GL_ARRAY_BUFFER, instanceBuffer, GL_DYNAMIC_DRAW);

        glDrawElementsInstanced(GL_TRIANGLES, INDICES_COUNT, GL_UNSIGNED_INT, 0, length);

        // post init
        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        Program.unbind();
        Texture.unbind();
    }

    private static int fillBufferMultipleColors(ImageFont font, String text, float x, float y, float height_ratio, FloatBuffer instanceBuffer) {
        Vector4f color = new Vector4f(1, 1, 1, 1);

        int length = 0;

        StringCharacterIterator iterator = new StringCharacterIterator(text);

        char c = iterator.current();
        do {
            if (c == '<') {
                int index = iterator.getIndex();

                boolean result = tryReadColor(iterator, color);

                if (result) {
                    continue;
                }

                iterator.setIndex(index); // tryReadColor method changes position
            }

            // glyph info
            Glyph glyph = font.getGlyph(c);

            if (glyph == null) {
                glyph = Glyph.NULL;
            }

            // translation
            instanceBuffer.put(x).put(y);

            instanceBuffer.put(glyph.x());
            instanceBuffer.put(glyph.y());
            instanceBuffer.put(glyph.width());

            // color
            instanceBuffer.put(color.x()).put(color.y()).put(color.z()).put(color.w());

            x += glyph.width() * height_ratio;

            length++;
        } while ((c = iterator.next()) != CharacterIterator.DONE);

        return length;
    }

    private static boolean tryReadColor(CharacterIterator iterator, Vector4f out) {
        if (iterator.current() != '<') {
            return false;
        }
        if (iterator.next() != 'c') {
            return false;
        }
        while (iterator.next() == ' ');

        float[] components = new float[4];
        for (int i = 0; i < 4; i++) {
            StringBuilder floatBuilder = new StringBuilder();

            boolean point = false;

            for (char c = iterator.current(); c != ' ' && c != '>'; c = iterator.next()) {
                if (c == CharacterIterator.DONE) {
                    return false;
                }

                if (c == '.') {
                    if (point) { // number that contains two point
                        return false;
                    }

                    point = true;
                } else if (c < '0' || c > '9') {
                    return false;
                }

                floatBuilder.append(c);
            }

            try {
                float value = Float.parseFloat(floatBuilder.toString());
                components[i] = value;
            } catch (NumberFormatException e) {
                return false;
            }

            if (iterator.current() == ' ') {
                while (iterator.next() == ' ') ;
            }
        }

        if (iterator.current() != '>') {
            return false;
        }

        out.set(components);

        return true;
    }

    private static ImageFont getFont(ImageFont font) {
        if (font == null) {
            return defaultFont;
        }

        return font;
    }

    public static void setDefaultFont(ImageFont defaultFont) {
        OldFontRenderer.defaultFont = defaultFont;
    }

    public static ImageFont getDefaultFont() {
        return defaultFont;
    }

    public static void free() {
        if (shader != null) {
            shader.dispose();
        }

        if (instanceBuffer != null) {
            MemoryUtil.memFree(instanceBuffer);
        }

        glDeleteBuffers(vbo);
        glDeleteBuffers(ibo);
        glDeleteBuffers(glInstanceBuffer);
        glDeleteVertexArrays(vao);
    }
}