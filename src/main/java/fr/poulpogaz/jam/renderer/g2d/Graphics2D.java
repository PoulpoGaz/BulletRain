package fr.poulpogaz.jam.renderer.g2d;

import fr.poulpogaz.jam.renderer.Colors;
import fr.poulpogaz.jam.renderer.IColor;
import fr.poulpogaz.jam.renderer.ITexture;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

@SuppressWarnings("DuplicatedCode")
public class Graphics2D implements IGraphics2D {

    private static final float TWO_PI = (float) (Math.PI * 2);

    private final Renderer2D renderer;
    private final boolean ownsRenderer;

    private final Paint.TexturePaint myTexturePaint = new Paint.TexturePaint();
    private final Paint.ColorTexturePaint myColorTexturePaint = new Paint.ColorTexturePaint();

    private final Paint.ColorPaint color = new Paint.ColorPaint(Colors.WHITE); // never null, default paint
    private Paint paint = color; // never null
    private Paint newPaint;

    private final Matrix4f projection = new Matrix4f();
    private final Matrix4f transform = new Matrix4f();

    private final Matrix4f newProjection = new Matrix4f();
    private boolean projectionDirty = false;
    private final Matrix4f newTransform = new Matrix4f();
    private boolean transformDirty = false;

    private boolean dirty = false;

    private int primitive;

    public Graphics2D() {
        this(5000, 5000);
    }

    public Graphics2D(int numVertices, int numIndices) {
        this.renderer = new Renderer2D(numVertices, numIndices);
        ownsRenderer = true;

        paint.set(renderer);
    }

    public Graphics2D(Renderer2D renderer) {
        this.renderer = Objects.requireNonNull(renderer);
        ownsRenderer = false;

        paint.set(renderer);
    }

    @Override
    public void translate(float x, float y) {
        newTransform.translate(x, y, 0);
        transformDirty = true;
        dirty = true;
    }

    @Override
    public void scale(float sx, float sy) {
        newTransform.scale(sx, sy, 0);
        transformDirty = true;
        dirty = true;
    }

    @Override
    public void rotate(float theta) {
        newTransform.rotateZ(theta);
        transformDirty = true;
        dirty = true;
    }

    @Override
    public void drawSprite(ITexture texture, float dstX, float dstY, float dstWidth, float dstHeight, float srcX, float srcY, float srcWidth, float srcHeight) {
        Paint p = newPaint == null ? paint : newPaint;

        if (p instanceof Paint.ColorTexturePaint colorTexturePaint) {
            colorTexturePaint.setColor(Colors.WHITE);
        }

        if (p instanceof Paint.TexturePaint texturePaint) {
            dirty |= texturePaint.setTexture(texture);
            texturePaint.set(dstX, dstY, dstWidth, dstHeight, srcX, srcY, srcWidth, srcHeight);
        } else {
            myTexturePaint.setTexture(texture);
            myTexturePaint.set(dstX, dstY, dstWidth, dstHeight, srcX, srcY, srcWidth, srcHeight);
            setPaint(myTexturePaint);
        }

        fillRect(dstX, dstY, dstWidth, dstHeight);
    }

    @Override
    public void drawSprite(ITexture texture, IColor color, float dstX, float dstY, float dstWidth, float dstHeight, float srcX, float srcY, float srcWidth, float srcHeight) {
        Paint p = newPaint == null ? paint : newPaint;

        if (p instanceof Paint.ColorTexturePaint texturePaint) {
            dirty |= texturePaint.setTexture(texture);
            texturePaint.set(dstX, dstY, dstWidth, dstHeight, srcX, srcY, srcWidth, srcHeight);
            texturePaint.setColor(color);
        } else {
            myColorTexturePaint.setTexture(texture);
            myColorTexturePaint.set(dstX, dstY, dstWidth, dstHeight, srcX, srcY, srcWidth, srcHeight);
            myColorTexturePaint.setColor(color);
            setPaint(myColorTexturePaint);
        }

        fillRect(dstX, dstY, dstWidth, dstHeight);
    }

    @Override
    public void drawRect(float x, float y, float width, float height) {
        check(GL_LINES, -1, 4, 8);

        int i = renderer.getNumVertices();
        renderer.index(i, i + 1,
                       i + 1, i + 2,
                       i + 2, i + 3,
                       i + 3, i);

        float x2 = x + width;
        float y2 = y + height;

        paint.paint(x, y, 0).pos(x, y);
        paint.paint(x2, y, 1).pos(x2, y);
        paint.paint(x2, y2, 2).pos(x2, y2);
        paint.paint(x, y2, 3).pos(x, y2);
    }

    @Override
    public void fillRect(float x, float y, float width, float height) {
        check(GL_TRIANGLES, -1, 4, 6);

        int i = renderer.getNumVertices();
        renderer.index(i, i + 1, i + 2,
                       i, i + 2, i + 3);

        float x2 = x + width;
        float y2 = y + height;

        paint.paint(x, y, 0).pos(x, y);
        paint.paint(x2, y, 1).pos(x2, y);
        paint.paint(x2, y2, 2).pos(x2, y2);
        paint.paint(x, y2, 3).pos(x, y2);
    }

    @Override
    public void fillRect(Vector2f p1, Vector2f p2, Vector2f p3, Vector2f p4) {
        check(GL_TRIANGLES, -1, 4, 6);

        int i = renderer.getNumVertices();
        renderer.index(i, i + 1, i + 2,
                i, i + 2, i + 3);

        paint.paint(p1.x, p1.y, 0).pos(p1.x, p1.y);
        paint.paint(p2.x, p2.y, 1).pos(p2.x, p2.y);
        paint.paint(p3.x, p3.y, 2).pos(p3.x, p3.y);
        paint.paint(p4.x, p4.y, 3).pos(p4.x, p4.y);
    }

    @Override
    public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        check(GL_LINES, 0, 3, 6);

        int i = renderer.getNumVertices();
        renderer.index(i, i + 1,
                       i + 1, i + 2,
                       i + 2, i);

        paint.paint(x1, y1, 0).pos(x1, y1);
        paint.paint(x2, y2, 1).pos(x2, y2);
        paint.paint(x3, y3, 2).pos(x3, y3);
    }

    @Override
    public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        check(GL_TRIANGLES, -1, 3, 3);

        int i = renderer.getNumVertices();
        renderer.index(i, i + 1, i + 2);

        paint.paint(x1, y1, 0).pos(x1, y1);
        paint.paint(x2, y2, 1).pos(x2, y2);
        paint.paint(x3, y3, 2).pos(x3, y3);
    }

    @Override
    public void line(float x1, float y1, float x2, float y2) {
        check(GL_LINES, -1, 2, 2);

        int i = renderer.getNumVertices();
        renderer.index(i, i + 1);
        paint.paint(x1, y1, 0).pos(x1, y1);
        paint.paint(x2, y2, 1).pos(x2, y2);
    }

    @Override
    public void polyline(float[] x, float[] y, int nPoints) {
        if (nPoints <= 1) {
            return;
        }

        check(GL_LINES, -1, nPoints, (nPoints - 1) * 2);

        int j = renderer.getNumVertices();

        for (int i = 0; i < nPoints; i++) {
            if (i < nPoints - 1) {
                renderer.index(j, j + 1);
            }
            paint.paint(x[i], y[i], i).pos(x[i], y[i]);

            j++;
        }
    }

    @Override
    public void drawPolygon(float[] x, float[] y, int nPoints) {
        check(GL_LINES, -1, nPoints, nPoints * 2);

        int j = renderer.getNumVertices();
        int k = j;
        for (int i = 0; i < nPoints; i++) {
            if (i == 0 || i < nPoints - 1) {
                renderer.index(j, j + 1);
                j++;
            }

            paint.paint(x[i], y[i], i).pos(x[i], y[i]);
        }

        renderer.index(k, j);
    }

    @Override
    public void drawCircle(float x, float y, float radius, int nPoints) {
        if (nPoints <= 1) {
            return;
        }

        check(GL_LINES, -1, nPoints, nPoints * 2);

        float theta = TWO_PI / nPoints;
        float sin = (float) Math.sin(theta);
        float cos = (float) Math.cos(theta);

        float x2 = radius;
        float y2 = 0;

        int j = renderer.getNumVertices();
        int k = j;

        for (int i = 0; i < nPoints; i++) {
            if (i < nPoints - 1) {
                renderer.index(j, j + 1);
                j++;
            }

            float x3 = x + x2;
            float y3 = y - y2;

            paint.paint(x3, y3, i).pos(x3, y3);

            float temp = x2;
            x2 = x2 * cos - y2 * sin;
            y2 = temp * sin + y2 * cos;
        }

        renderer.index(k, j);
    }

    /**
     * be aware of the central point
     */
    @Override
    public void fillCircle(float x, float y, float radius, int nPoints) {
        if (nPoints <= 1) {
            return;
        }

        check(GL_TRIANGLES, -1, nPoints + 1, nPoints * 3);

        float theta = TWO_PI / nPoints;
        float sin = (float) Math.sin(theta);
        float cos = (float) Math.cos(theta);

        float x2 = radius;
        float y2 = 0;

        int j = renderer.getNumVertices();

        paint.paint(x, y, 0).pos(x, y);

        for (int i = 0; i < nPoints; i++) {
            float x3 = x + x2;
            float y3 = y - y2;

            paint.paint(x3, y3, i + 1).pos(x3, y3);
            if (i < nPoints - 1) {
                renderer.index(j, j + i + 1, j + i + 2);
            }

            float tempX = x2;
            x2 = x2 * cos - y2 * sin;
            y2 = tempX * sin + y2 * cos;
        }

        renderer.index(j, j + 1, j + nPoints);
    }

    @Override
    public void drawEllipse(float x, float y, float width, float height, int nPoints) {
        if (nPoints <= 1) {
            return;
        }

        check(GL_LINES, -1, nPoints, nPoints * 2);

        float halfWidth = width / 2f;
        float halfHeight = height / 2f;

        float cx = x + halfWidth;
        float cy = y + halfHeight;

        float theta = TWO_PI / nPoints;
        float sin = (float) Math.sin(theta);
        float cos = (float) Math.cos(theta);

        float x2 = 1;
        float y2 = 0;

        int j = renderer.getNumVertices();
        int k = j;

        for (int i = 0; i < nPoints; i++) {
            if (i == 0 || i < nPoints - 1) {
                renderer.index(j, j + 1);
                j++;
            }

            float x3 = cx + x2 * halfWidth;
            float y3 = cy - y2 * halfHeight;

            paint.paint(x3, y3, i).pos(x3, y3);

            float temp = x2;
            x2 = x2 * cos - y2 * sin;
            y2 = temp * sin + y2 * cos;
        }

        renderer.index(k, j);
    }

    @Override
    public void fillEllipse(float x, float y, float width, float height, int nPoints) {
        if (nPoints <= 1) {
            return;
        }

        check(GL_TRIANGLES, -1, nPoints + 1, nPoints * 3);

        float halfWidth = width / 2f;
        float halfHeight = height / 2f;

        float cx = x + halfWidth;
        float cy = y + halfHeight;

        float theta = TWO_PI / nPoints;
        float sin = (float) Math.sin(theta);
        float cos = (float) Math.cos(theta);

        float x2 = 1;
        float y2 = 0;

        int j = renderer.getNumVertices();

        paint.paint(cx, cy, 0).pos(cx, cy);

        for (int i = 0; i < nPoints; i++) {
            float x3 = cx + x2 * halfWidth;
            float y3 = cy - y2 * halfHeight;

            paint.paint(x3, y3, i + 1).pos(x3, y3);
            if (i < nPoints - 1) {
                renderer.index(j, j + i + 1, j + i + 2);
            }

            float tempX = x2;
            x2 = x2 * cos - y2 * sin;
            y2 = tempX * sin + y2 * cos;
        }

        renderer.index(j, j + 1, j + nPoints);
    }

    @Override
    public void drawArc(float x, float y, float width, float height, float arcStart, float arcLen, int nPoints, int arcType) {
        if (nPoints <= 1) {
            return;
        }

        int nVertices;
        int nIndices;
        if (arcType == OPEN) {
            nIndices = (nPoints - 1) * 2;
            nVertices = nPoints;
        } else if (arcType == CHORD) {
            nIndices = nPoints * 2;
            nVertices = nPoints;
        } else if (arcType == PIE) {
            nVertices = nPoints + 1;
            nIndices = nVertices * 2;
        } else {
            return;
        }

        check(GL_LINES, -1, nVertices, nIndices);

        float halfWidth = width / 2f;
        float halfHeight = height / 2f;

        float cx = x + halfWidth;
        float cy = y + halfHeight;

        float theta = arcLen / (nPoints - 1);
        float sin = (float) Math.sin(theta);
        float cos = (float) Math.cos(theta);

        float x2 = (float) Math.cos(arcStart); // 1 * cos - 0 * sin
        float y2 = (float) Math.sin(arcStart); // 1 * sin + 0 * cos

        int j = renderer.getNumVertices();

        int k = 0;
        if (arcType == CHORD) {
            renderer.index(j, j + nPoints - 1);
        } else if (arcType == PIE) {
            paint.paint(cx, cy, 0).pos(cx, cy);
            renderer.index(j, j + 1);
            renderer.index(j, j + nPoints);
            k++;
            j++;
        }

        for (int i = 0; i < nPoints; i++, k++, j++) {
            if (i < nPoints - 1) {
                renderer.index(j, j + 1);
            }

            float x3 = cx + x2 * halfWidth;
            float y3 = cy - y2 * halfHeight;

            paint.paint(x3, y3, k).pos(x3, y3);

            float tempX = x2;
            x2 = x2 * cos - y2 * sin;
            y2 = tempX * sin + y2 * cos;
        }
    }

    @Override
    public void fillArc(float x, float y, float width, float height, float arcStart, float arcLen, int nPoints, int arcType) {
        if (nPoints <= 1) {
            return;
        }

        switch (arcType) {
            case OPEN, CHORD -> {
                check(GL_TRIANGLES, -1, nPoints, (nPoints - 2) * 3);
                fillArcChord(x, y, width, height, arcStart, arcLen, nPoints);
            }
            case PIE -> {
                fillArcPie(x, y, width, height, arcStart, arcLen, nPoints);
            }
        }
    }

    protected void fillArcChord(float x, float y, float width, float height, float arcStart, float arcLen, int nPoints) {
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;

        float cx = x + halfWidth;
        float cy = y + halfHeight;

        float theta = arcLen / (nPoints - 1);
        float sin = (float) Math.sin(theta);
        float cos = (float) Math.cos(theta);

        float x2 = (float) Math.cos(arcStart); // 1 * cos - 0 * sin
        float y2 = (float) Math.sin(arcStart); // 1 * sin + 0 * cos

        int j = renderer.getNumVertices();
        int k = j + 1;

        for (int i = 0; i < nPoints; i++, k++) {
            if (i < nPoints - 2) {
                renderer.index(j, k, k + 1);
            }

            float x3 = cx + x2 * halfWidth;
            float y3 = cy - y2 * halfHeight;

            paint.paint(x3, y3, i).pos(x3, y3);

            float tempX = x2;
            x2 = x2 * cos - y2 * sin;
            y2 = tempX * sin + y2 * cos;
        }
    }

    protected void fillArcPie(float x, float y, float width, float height, float arcStart, float arcLen, int nPoints) {
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;

        float cx = x + halfWidth;
        float cy = y + halfHeight;

        float theta = arcLen / (nPoints - 1);
        float sin = (float) Math.sin(theta);
        float cos = (float) Math.cos(theta);

        float x2 = (float) Math.cos(arcStart); // 1 * cos - 0 * sin
        float y2 = (float) Math.sin(arcStart); // 1 * sin + 0 * cos

        int j = renderer.getNumVertices();
        int k = j + 1;

        paint.paint(cx, cy, 0).pos(cx, cy);

        for (int i = 0; i < nPoints; i++, k++) {
            if (i < nPoints - 1) {
                renderer.index(j, k, k + 1);
            }

            float x3 = cx + x2 * halfWidth;
            float y3 = cy - y2 * halfHeight;

            paint.paint(x3, y3, i + 1).pos(x3, y3);

            float tempX = x2;
            x2 = x2 * cos - y2 * sin;
            y2 = tempX * sin + y2 * cos;
        }
    }

    private void check(int prefPrimitive, int alternative, int nVertices, int nIndices) {
        if (renderer.isDrawing()) {
            boolean primitiveOk = primitive == prefPrimitive || primitive == alternative;

            if (!primitiveOk ||
                    dirty ||
                    renderer.getNumVertices() + nVertices > renderer.getMaxVertices() ||
                    renderer.getNumIndices() + nIndices > renderer.getMaxIndices()) {
                primitive = prefPrimitive;

                flush();
            }
        } else {
            primitive = prefPrimitive;
            clean();
            begin();
        }
    }

    private void begin() {
        renderer.begin(primitive, paint.drawMode());
    }

    public void flush() {
        end();
        clean();
        begin();
    }

    public void end() {
        if (!renderer.isDrawing()) {
            return;
        }

        renderer.end(projection, transform);
    }

    private void clean() {
        if (!dirty) {
            return;
        }

        if (newPaint != null) {
            paint = newPaint;
            newPaint = null;

            if (paint instanceof Paint.AbstractTexturePaint texturePaint) {
                renderer.setTexture(texturePaint.getTexture());
                texturePaint.clean();
            }
        } else if (paint instanceof Paint.AbstractTexturePaint texturePaint && texturePaint.hasTextureChanged()) {
            renderer.setTexture(texturePaint.getTexture());
            texturePaint.clean();
        }

        if (projectionDirty) {
            projection.set(newProjection);
            projectionDirty = false;
        }
        if (transformDirty) {
            transform.set(newTransform);
            transformDirty = false;
        }
        dirty = false;
    }


    @Override
    public void setPaint(Paint paint) {
        if (paint != this.paint) {
            Paint newPaint = paint == null ? color : paint;

            if (this.paint.compatible(newPaint)) {
                this.paint = newPaint;
            } else {
                this.newPaint = newPaint;
                dirty = true;
            }

            newPaint.set(renderer);
        } else if (paint != null && newPaint != null) {
            newPaint = null;
        }
    }

    @Override
    public Paint getPaint() {
        return paint;
    }

    @Override
    public void setColor(IColor color) {
        this.color.setColor(color);
        setPaint(this.color);
    }

    @Override
    public IColor getColor() {
        return color.getColor();
    }

    @Override
    public void setLineWidth(float width) {

    }

    @Override
    public float getLineWidth() {
        return 0;
    }

    @Override
    public void setProjection(Matrix4f projection) {
        newProjection.set(projection);
        dirty = true;
        projectionDirty = true;
    }

    @Override
    public Matrix4f getProjection() {
        dirty = true;
        projectionDirty = true;
        return newProjection;
    }

    @Override
    public void setTransform(Matrix4f transform) {
        newTransform.set(transform);
        transformDirty = true;
        dirty = true;
    }

    @Override
    public Matrix4f getTransform() {
        dirty = true;
        transformDirty = true;
        return newTransform;
    }

    @Override
    public void close() {
        if (ownsRenderer) {
            renderer.close();
        }
    }
}