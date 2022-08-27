package fr.poulpogaz.jam.renderer.g2d;

public record Glyph(char c, int x, int y, int width) {

    public static final Glyph NULL = new Glyph((char) 0, 0, 0, 0);

    @Override
    public String toString() {
        return "Glyph{" +
                "c=" + c +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                '}';
    }

    static class GlyphBuilder {

        private char c;
        private int x;
        private int y;
        private int width;

        public GlyphBuilder() {

        }

        public Glyph build() {
            return new Glyph(c, x, y, width);
        }

        public char getChar() {
            return c;
        }

        public void setChar(char c) {
            this.c = c;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
    }
}