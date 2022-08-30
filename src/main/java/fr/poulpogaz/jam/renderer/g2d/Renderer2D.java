package fr.poulpogaz.jam.renderer.g2d;

import fr.poulpogaz.jam.renderer.IColor;
import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.mesh.Mesh;
import fr.poulpogaz.jam.renderer.mesh.VertexBufferObject;
import fr.poulpogaz.jam.renderer.shaders.Program;
import org.joml.Matrix4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;

public class Renderer2D implements AutoCloseable {

    private final Mesh[] meshes;

    private DrawMode drawMode;
    private Program custom;
    private int primitiveType;

    private ITexture tex;
    private float invTexWidth;
    private float invTexHeight;

    private FloatBuffer vbo;
    private IntBuffer ibo;

    private int vertexIndex;

    private int colOffset;
    private int texOffset;
    private int vertexSize;

    private boolean isDrawing = false;
    private boolean autoBind = true;

    public Renderer2D(int numVertices, int numIndices) {
        DrawMode[] modes = DrawMode.values();
        meshes = new Mesh[modes.length];

        for (int i = 0; i < modes.length; i++) {
            DrawMode mode = modes[i];

            meshes[i] = new Mesh(mode.getAttributes(), numVertices, numIndices, GL_DYNAMIC_DRAW);
        }
    }

    public void begin(int primitiveType, DrawMode drawMode) {
        if (isDrawing) {
            throw new IllegalStateException("Already drawing");
        }

        this.primitiveType = primitiveType;
        this.drawMode = drawMode;

        vertexSize = drawMode.getVertexSize();
        colOffset = drawMode.getColorOffset();
        texOffset = drawMode.getTextureOffset();

        vertexIndex = 0;

        Mesh m = meshes[drawMode.ordinal()];
        vbo = m.getVerticesBuffer();
        vbo.limit(vbo.capacity());
        vbo.position(0);

        ibo = m.getIndicesBuffer();
        ibo.limit(ibo.capacity());
        ibo.position(0);

        isDrawing = true;
    }

    public Renderer2D index(int i) {
        ibo.put(i);
        return this;
    }

    public Renderer2D index(int i, int i2) {
        ibo.put(i).put(i2);
        return this;
    }

    public Renderer2D index(int i, int i2, int i3) {
        ibo.put(i).put(i2).put(i3);
        return this;
    }

    public Renderer2D index(int i, int i2, int i3, int i4) {
        ibo.put(i).put(i2).put(i3).put(i4);
        return this;
    }

    public Renderer2D index(int i, int i2, int i3, int i4, int i5) {
        ibo.put(i).put(i2).put(i3).put(i4).put(i5);
        return this;
    }

    public Renderer2D index(int i, int i2, int i3, int i4, int i5, int i6) {
        ibo.put(i).put(i2).put(i3).put(i4).put(i5).put(i6);
        return this;
    }

    public Renderer2D index(int... index) {
        ibo.put(index);

        return this;
    }

    public Renderer2D pos(float x, float y) {
        vbo.put(vertexIndex, x);
        vbo.put(vertexIndex + 1, y);

        vertexIndex += vertexSize;

        return this;
    }

    public Renderer2D color(float r, float g, float b) {
        return color(r, g, b, 1);
    }

    public Renderer2D color(IColor color) {
        return color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public Renderer2D color(float r, float g, float b, float a) {
        int i = vertexIndex + colOffset;

        vbo.put(i, r);
        vbo.put(i + 1, g);
        vbo.put(i + 2, b);
        vbo.put(i + 3, a);

        return this;
    }

    public Renderer2D texf(float u, float v) {
        int i = vertexIndex + texOffset;

        vbo.put(i, u);
        vbo.put(i + 1, v);

        return this;
    }

    public Renderer2D texfNotNormalized(float u, float v) {
        int i = vertexIndex + texOffset;

        vbo.put(i, (u + tex.getX()) * invTexWidth);
        vbo.put(i + 1, (v + tex.getY()) * invTexHeight);

        return this;
    }

    public Renderer2D texi(int u, int v) {
        int i = vertexIndex + texOffset;

        vbo.put(i, (u + tex.getX()) * invTexWidth);
        vbo.put(i + 1, (v + tex.getY()) * invTexHeight);

        return this;
    }

    public void end(Matrix4f projection, Matrix4f modelView) {
        if (!isDrawing) {
            throw new IllegalStateException("Isn't drawing");
        }
        if (vertexIndex == 0) {
            isDrawing = false;
            return;
        }

        Program shader = custom == null ? drawMode.getShader() : custom;

        Mesh mesh = meshes[drawMode.ordinal()];

        // flip vbo
        vbo.limit(vertexIndex);
        vbo.position(0);
        mesh.getVBO().markDirty();

        ibo.flip();
        mesh.getIBO().markDirty();

        if (drawMode.isTexture()) {
            tex.bind();
        }

        if (autoBind) {
            shader.bind();
            shader.setUniform("projection", projection);
            shader.setUniform("modelView", modelView);
        }

        mesh.render(primitiveType);

        if (autoBind) {
            Program.unbind();
        }

        if (drawMode.isTexture()) {
            tex.unbind();
        }

        isDrawing = false;
    }

    public ITexture getTexture() {
        return tex;
    }

    public void setTexture(ITexture tex) {
        this.tex = tex;

        if (tex != null) {
            invTexWidth = 1f / tex.getFullWidth();
            invTexHeight = 1f / tex.getFullHeight();
        }
    }

    public Program getCustom() {
        return custom;
    }

    public void setCustomShader(Program custom) {
        this.custom = custom;
    }

    public boolean isAutoBind() {
        return autoBind;
    }

    public void setAutoBind(boolean autoBind) {
        this.autoBind = autoBind;
    }

    public boolean isDrawing() {
        return isDrawing;
    }

    public void setDrawing(boolean drawing) {
        isDrawing = drawing;
    }

    public int getMaxVertices() {
        return meshes[0].getMaxVertices();
    }

    public int getMaxIndices() {
        return meshes[0].getMaxIndices();
    }

    public int getNumVertices() {
        return vertexIndex / vertexSize;
    }

    public int getNumIndices() {
        return ibo.position();
    }

    @Override
    public void close() {
        for (Mesh mesh : meshes) {
            mesh.close();
        }
    }
}