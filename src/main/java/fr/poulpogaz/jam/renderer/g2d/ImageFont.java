package fr.poulpogaz.jam.renderer.g2d;

import fr.poulpogaz.jam.Cache;
import fr.poulpogaz.json.*;
import fr.poulpogaz.jam.renderer.Texture;
import fr.poulpogaz.jam.renderer.utils.Disposable;
import fr.poulpogaz.jam.renderer.utils.ImageLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFWImage;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ImageFont implements Disposable {

    private static final Logger LOGGER = LogManager.getLogger(ImageFont.class);

    private static final Map<String, ImageFont> FONTS = new HashMap<>();

    public static ImageFont getOrCreate(String name, Font font, String charset) {
        return getOrCreate(name, font, Charset.forName(charset));
    }

    public static ImageFont getOrCreate(String name, Font font, Charset charset) {
        ImageFont f = tryLoad(name, font);

        if (f == null) {
            f = new ImageFont(font, charset);

            cacheFont(name, f);
        }

        return f;
    }

    public static ImageFont getOrCreate(String name, Font font, char[] chars) {
        ImageFont f = tryLoad(name, font);

        if (f == null) {
            f = new ImageFont(font, chars);

            cacheFont(name, f);
        }

        return f;
    }

    private static ImageFont tryLoad(String name, Font font) {
        ImageFont f = FONTS.get(name);

        if (f == null) {
            Path image = Cache.of("fonts/" + name + ".png");
            Path glyph = Cache.of("fonts/" + name + ".json");

            if (Files.exists(image) && Files.exists(glyph)) {
                LOGGER.info("Loading font from disk: {}", name);
                f = loadFontFromCache(image, glyph, font);

                FONTS.put(name, f);
            }
        }

        return f;
    }


    private static ImageFont loadFontFromCache(Path image, Path glyph, Font font) {
        try {
            // get image
            GLFWImage img = ImageLoader.loadImage(image.toAbsolutePath().toString(), false);
            Texture texture = new Texture(img);

            // get glyphs
            Map<Character, Glyph> glyphs = new HashMap<>();
            IJsonReader jr = new JsonReader(Files.newBufferedReader(glyph, StandardCharsets.UTF_8));

            jr.beginObject();
            while (!jr.isObjectEnd()) {
                char c = jr.nextKey().charAt(0);

                jr.beginArray();
                int x = jr.nextInt();
                int y = jr.nextInt();
                int width = jr.nextInt();
                jr.endArray();

                glyphs.put(c, new Glyph(c, x, y, width));
            }
            jr.endObject();
            jr.close();

            return new ImageFont(font, glyphs, texture);
        } catch (IOException | JsonException e) {
            LOGGER.warn("Failed to load font image:{} glyph:{}", image, glyph, e);
        }

        return null;
    }

    private static void cacheFont(String name, ImageFont font) {
        try {
            LOGGER.info("Caching font {}", name);
            Path image = Cache.of("fonts/" + name + ".png");
            Files.createDirectories(image.getParent());

            ImageIO.write(font.createImage(font.getTextureSize()), "png", image.toFile());

            Path json = Cache.of("fonts/" + name + ".json");
            IJsonWriter jw = new JsonWriter(Files.newBufferedWriter(json, StandardCharsets.UTF_8));
            jw.beginObject();

            for (Glyph glyph : font.charMap.values()) {
                jw.key(glyph.c() + "")
                        .beginArray()
                        .value(glyph.x())
                        .value(glyph.y())
                        .value(glyph.width())
                        .endArray();
            }

            jw.endObject();
            jw.close();

        } catch (IOException | JsonException e) {
            LOGGER.warn("Failed to cache font {}", name, e);
        }

        FONTS.put(name, font);
    }






    private static final int PADDING = 2;
    private static final BufferedImage EMPTY = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    private static final Graphics2D g2dStatic = EMPTY.createGraphics();

    static {
        g2dStatic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2dStatic.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    private final Font font;
    private final Map<Character, Glyph> charMap;
    private FontMetrics fm;

    private Texture texture;

    protected ImageFont(Font font,
                        Map<Character, Glyph> charMap,
                        Texture texture) {
        this.font = font;
        this.charMap = charMap;
        this.texture = texture;

        fm = g2dStatic.getFontMetrics(font);
    }

    public ImageFont(Font font, Charset charset) {
        this.font = font;

        char[] chars = availableCharsForCharset(charset);

        charMap = new HashMap<>(chars.length);

        int size = computeTextureDimension(chars);
        makeTexture(size);
    }

    public ImageFont(Font font, String charset) {
        this(font, Charset.forName(charset));
    }

    public ImageFont(Font font, char[] chars) {
        this.font = font;

        charMap = new HashMap<>(chars.length);

        int size = computeTextureDimension(chars);
        makeTexture(size);
    }

    private char[] availableCharsForCharset(Charset charset) {
        CharsetEncoder encoder = charset.newEncoder();

        StringBuilder chars = new StringBuilder((int) Character.MAX_VALUE);
        for (char i = 0; i < Character.MAX_VALUE; i++) {
            if (encoder.canEncode(i)) {
                chars.append(i);
            }
        }

        return chars.toString().toCharArray();
    }

    private int computeTextureDimension(char[] chars) {
        fm = g2dStatic.getFontMetrics(font);

        int sqrt = (int) Math.sqrt(chars.length);
        int preferredSize = fm.getHeight() * sqrt;
        int exponent = (int) (Math.log(preferredSize) / Math.log(2)) + 1;
        preferredSize = (int) Math.pow(2, exponent);

        HashMap<Character, Glyph.GlyphBuilder> builders = new HashMap<>(chars.length);

        boolean revalidate = false;
        int x = 0;
        int y = 0;

        // try to put all chars in a texture of size preferredSize, which is a power of two
        int height = 0;
        for (char c : chars) {
            int charWidth = fm.charWidth(c);

            int x2 = x + charWidth + PADDING;

            if (x2 >= preferredSize) {
                x = 0;
                y += getHeight();

                // we can't put all chars
                if (y >= preferredSize) {
                    revalidate = true;

                    // store height
                    height = y + getHeight();
                }
            }

            Glyph.GlyphBuilder builder = new Glyph.GlyphBuilder();

            builder.setChar(c);
            builder.setX(x);
            builder.setY(y);
            builder.setWidth(charWidth);

            builders.put(c, builder);

            x += builder.getWidth() + PADDING;
        }

        int size;
        if (revalidate) {
            size = height * preferredSize; // preferredSize = width
            int old = preferredSize * preferredSize;

            float ratio = (float) size / old;
            int power = (int) (Math.round(Math.log(ratio) / Math.log(2)));

            size = (int) Math.pow(power * exponent, 2);

            x = 0;
            y = 0;
            for (char c : chars) {
                Glyph.GlyphBuilder builder = builders.get(c);

                int x2 = x + builder.getWidth() + PADDING;

                if (x2 >= preferredSize) {
                    x = 0;
                    y += getHeight();
                }

                builder.setX(x);
                builder.setY(y);

                x += builder.getWidth();

                builders.remove(c, builder);
                charMap.put(c, builder.build());
            }
        } else {
            size = preferredSize;

            for (Map.Entry<Character, Glyph.GlyphBuilder> entry : builders.entrySet()) {
                charMap.put(entry.getKey(), entry.getValue().build());
            }
        }

        return size;
    }

    private void makeTexture(int textureSize) {
        BufferedImage image = createImage(textureSize);

        int[] ints = ((DataBufferInt) image.getData().getDataBuffer()).getData();
        ByteBuffer buffer = ByteBuffer.allocateDirect(ints.length * 4);

        for (int i = 0; i < textureSize * textureSize; i++) {
            int color = ints[i];

            // convert argb to rgba
            color = color << 8 | (color >> 24) & 0xFF;

            buffer.putInt(color);
        }

        buffer.flip();

        texture = new Texture(GLFWImage.create().set(textureSize, textureSize, buffer));
    }

    private BufferedImage createImage(int textureSize) {
        BufferedImage image = new BufferedImage(textureSize, textureSize, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.setFont(font);

        for (Glyph glyph : charMap.values()) {
            g2d.drawString("" + glyph.c(), glyph.x(), glyph.y() + getAscent());
        }

        g2d.dispose();

        return image;
    }

    public int stringWidth(String str) {
        int width = 0;

        for (int i = 0; i < str.length(); i++) {
            width += glyphWidth(str.charAt(i));
        }

        return width;
    }

    public int glyphWidth(char c) {
        return charMap.getOrDefault(c, Glyph.NULL).width();
    }

    public Glyph getGlyph(char c) {
        return charMap.get(c);
    }

    public Font getFont() {
        return font;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getHeight() {
        return fm.getHeight();
    }

    public int getAscent() {
        return fm.getAscent();
    }

    public int getDescent() {
        return fm.getDescent();
    }

    public int getLeading() {
        return fm.getLeading();
    }

    public int getTextureSize() {
        return texture.getWidth();
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}