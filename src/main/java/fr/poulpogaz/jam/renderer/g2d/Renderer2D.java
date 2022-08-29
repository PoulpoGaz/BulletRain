package fr.poulpogaz.jam.renderer.g2d;

import fr.poulpogaz.jam.renderer.IColor;
import fr.poulpogaz.jam.renderer.ITexture;
import fr.poulpogaz.jam.renderer.mesh.Mesh;
import fr.poulpogaz.jam.renderer.shaders.Program;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;

public class Renderer2D implements AutoCloseable {

    private final Mesh[] meshes;

    private DrawMode drawMode;
    private Program custom;
    private int primitiveType;

    private ITexture tex;
    private float invTexWidth;
    private float invTexHeight;

    private final float[] vertices;
    private final int[] indices;

    private int indicesPos;
    private int vertexIndex;

    private int colOffset;
    private int texOffset;
    private int vertexSize;

    private boolean isDrawing = false;
    private boolean autoBind = true;

    public Renderer2D(int numVertices, int numIndices) {
        DrawMode[] modes = DrawMode.values();
        meshes = new Mesh[modes.length];

        int max = 0;
        for (int i = 0; i < modes.length; i++) {
            DrawMode mode = modes[i];

            meshes[i] = new Mesh(mode.getAttributes(), numVertices, numIndices, GL_DYNAMIC_DRAW);
            max = Math.max(mode.getAttributes().vertexSizeInBytes(), max);
        }

        vertices = new float[numVertices * max];
        indices = new int[numIndices];
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
        indicesPos = 0;

        isDrawing = true;
    }

    public Renderer2D index(int... index) {
        System.arraycopy(index, 0, indices, indicesPos, index.length);
        indicesPos += index.length;

        return this;
    }

    public Renderer2D pos(float x, float y) {
        vertices[vertexIndex] = x;
        vertices[vertexIndex + 1] = y;

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

        vertices[i] = r;
        vertices[i + 1] = g;
        vertices[i + 2] = b;
        vertices[i + 3] = a;

        return this;
    }

    public Renderer2D texf(float u, float v) {
        int i = vertexIndex + texOffset;

        vertices[i] = u;
        vertices[i + 1] = v;

        return this;
    }

    public Renderer2D texfNotNormalized(float u, float v) {
        int i = vertexIndex + texOffset;

        vertices[i] = (u + tex.getX()) * invTexWidth;
        vertices[i + 1] = (v + tex.getY()) * invTexHeight;

        return this;
    }

    public Renderer2D texi(int u, int v) {
        int i = vertexIndex + texOffset;

        vertices[i] = (u + tex.getX()) * invTexWidth;
        vertices[i + 1] = (v + tex.getY()) * invTexHeight;

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
        mesh.setVertices(vertices, 0, vertexIndex);

        if (mesh.getNumIndices() != indicesPos) {
            mesh.setIndices(indices, 0, indicesPos);
        }

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
        return indicesPos;
    }

    @Override
    public void close() {
        for (Mesh mesh : meshes) {
            mesh.close();
        }
    }
}