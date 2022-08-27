package fr.poulpogaz.jam.renderer.g2d;

import fr.poulpogaz.jam.renderer.IColor;
import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.Texture;
import org.joml.Vector2f;

public abstract class Paint {

    protected Renderer2D renderer;

    protected abstract Renderer2D paint(float x, float y, int index);

    protected abstract boolean compatible(Paint paint);

    protected abstract DrawMode drawMode();

    void set(Renderer2D renderer) {
        this.renderer = renderer;
    }

    void setRenderer(Renderer2D renderer) {
        this.renderer = renderer;
    }

    public static class ColorPaint extends Paint {

        private IColor color;

        public ColorPaint(IColor color) {
            this.color = color;
        }

        @Override
        protected Renderer2D paint(float x, float y, int index) {
            return renderer.color(color);
        }

        @Override
        protected boolean compatible(Paint paint) {
            return paint.drawMode() == drawMode();
        }

        @Override
        protected DrawMode drawMode() {
            return DrawMode.COLOR;
        }

        public IColor getColor() {
            return color;
        }

        public void setColor(IColor color) {
            this.color = color;
        }
    }

    public static class GradientPaint extends Paint {

        private IColor[] colors;
        private boolean loop = false;

        public GradientPaint(IColor... colors) {
            this.colors = colors;
        }

        public GradientPaint(boolean loop, IColor... colors) {
            this.colors = colors;
            this.loop = loop;
        }

        @Override
        protected Renderer2D paint(float x, float y, int index) {
            if (loop) {
                return renderer.color(colors[index % colors.length]);
            } else {
                return renderer.color(colors[index]);
            }
        }

        @Override
        protected boolean compatible(Paint paint) {
            return paint.drawMode() == drawMode();
        }

        @Override
        protected DrawMode drawMode() {
            return DrawMode.COLOR;
        }

        public IColor[] getColors() {
            return colors;
        }

        public void setColors(IColor[] colors) {
            this.colors = colors;
        }

        public boolean isLoop() {
            return loop;
        }

        public void setLoop(boolean loop) {
            this.loop = loop;
        }
    }

    public static abstract class AbstractTexturePaint extends Paint {

        protected ITexture texture;

        public AbstractTexturePaint(ITexture texture) {
            this.texture = texture;
        }

        @Override
        protected boolean compatible(Paint paint) {
            if (paint instanceof AbstractTexturePaint tex) {
                return tex.getTexture() == texture;
            }

            return false;
        }

        @Override
        protected DrawMode drawMode() {
            return DrawMode.TEXTURE;
        }

        public ITexture getTexture() {
            return texture;
        }
    }

    /**
     * u = srcX / IMAGE_WIDTH + (x - dstX) / dstWidth * srcWidth / IMAGE_WIDTH
     * v = srcY / IMAGE_HEIGHT + (y - dstY) / dstHeight * srcHeight / IMAGE_HEIGHT
     * <p>
     * let sx = 1f / dstWidth * srcWidth / IMAGE_WIDTH
     * let sy = 1f / dstHeight * srcHeight / IMAGE_HEIGHT
     * let tx = srcX / IMAGE_WIDTH - dstX * sx
     * let ty = srcY / IMAGE_HEIGHT - dstY * sy
     * <p>
     * so
     * u = tx + x * sx
     * v = ty + y * sy
     */
    public static class TexturePaint extends AbstractTexturePaint {

        private ITexture texture;

        private float tx;
        private float ty;

        private float sx;
        private float sy;

        TexturePaint() {
            super(null);
        }

        public TexturePaint(ITexture texture, float dstX, float dstY) {
            this(texture, dstX, dstY, texture.getWidth(), texture.getHeight(), 0, 0, texture.getWidth(), texture.getHeight());
        }

        public TexturePaint(ITexture texture, float dstX, float dstY, float dstWidth, float dstHeight) {
            this(texture, dstX, dstY, dstWidth, dstHeight, 0, 0, texture.getWidth(), texture.getHeight());
        }

        public TexturePaint(ITexture texture, float dstX, float dstY, float dstWidth, float dstHeight, float srcX, float srcY, float srcWidth, float srcHeight) {
            super(texture);
            set(dstX, dstY, dstWidth, dstHeight, srcX, srcY, srcWidth, srcHeight);
        }

        @Override
        protected Renderer2D paint(float x, float y, int index) {
            float u = tx + x * sx;
            float v = ty + y * sy;

            return renderer.texf(u, v);
        }

        public ITexture getTexture() {
            return texture;
        }

        public void setTexture(ITexture texture) {
            this.texture = texture;
        }

        public void set(float dstX, float dstY, float dstWidth, float dstHeight, float srcX, float srcY, float srcWidth, float srcHeight) {
            float invWidth = 1f / texture.getFullWidth();
            float invHeight = 1f / texture.getFullHeight();

            this.sx = 1 / dstWidth * srcWidth * invWidth;
            this.sy = 1 / dstHeight * srcHeight * invHeight;

            this.tx = (srcX + texture.getX()) * invWidth - dstX * sx;
            this.ty = (srcY + texture.getY()) * invHeight - dstY * sy;
        }
    }

    public static class IndexedTexturePaint extends AbstractTexturePaint {

        private Vector2f[] coords;

        public IndexedTexturePaint(Texture texture, Vector2f[] coords) {
            super(texture);
            this.coords = coords;
        }

        @Override
        protected Renderer2D paint(float x, float y, int index) {
            Vector2f coord = coords[index];

            return renderer.texf(coord.x, coord.y);
        }

        public void setTexture(Texture texture) {
            this.texture = texture;
        }

        public Vector2f[] getCoords() {
            return coords;
        }

        public void setCoords(Vector2f[] coords) {
            this.coords = coords;
        }
    }
}