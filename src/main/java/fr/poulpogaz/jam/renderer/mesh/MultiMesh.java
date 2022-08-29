package fr.poulpogaz.jam.renderer.mesh;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL30.*;

public class MultiMesh implements IMesh {

    private static final List<MultiMesh> MESHES = new ArrayList<>();
    private static final MultiVBO MULTI_VBO = new MultiVBO(GL_STATIC_DRAW, 65_536);
    private static final MultiIBO MULTI_IBO = new MultiIBO(GL_STATIC_DRAW, 65_536);

    private final VertexAttributes attributes;
    private final int usage;

    private final int vao;
    private final MultiVBO.VBO vbo;
    private final MultiIBO.IBO ibo;

    private boolean vaoDirty = true;

    public MultiMesh(VertexAttributes attributes, int nVertices, int numIndices, int usage) {
        this.attributes = attributes;
        this.usage = usage;

        vbo = MULTI_VBO.addVBO(attributes, nVertices);
        ibo = MULTI_IBO.addIBO(numIndices);

        vao = glGenVertexArrays();
        MESHES.add(this);
    }

    protected void setupVAO() {
        vbo.enable(false);
        MULTI_IBO.bind();
        vaoDirty = false;
    }

    public void render(int primitiveType) {
        glBindVertexArray(vao);
        MULTI_VBO.validate();
        MULTI_IBO.validate();

        boolean v = vaoDirty;
        if (vaoDirty) {
            setupVAO();
        }

        glDrawElements(primitiveType, ibo.nIndices(), GL_UNSIGNED_INT, ibo.offset() * 4L);

        glBindVertexArray(0);
        if (v) {
            MULTI_IBO.unbind();
        }
    }

    @Override
    public VertexAttributes getVertexAttributes() {
        return attributes;
    }

    @Override
    public int getUsage() {
        return usage;
    }

    public void setVertices(float[] vertices) {
        vbo.setData(vertices);
    }

    public void setIndices(int[] indices) {
        ibo.setData(indices);
    }

    @Override
    public void close() {
        glBindVertexArray(0);
        glDeleteVertexArrays(vao);

        MULTI_IBO.removeIBO(ibo);
        MULTI_VBO.removeVBO(vbo);
    }

    public static void write(String vbo, String ibo) {
        Locale d = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);

        MULTI_VBO.write(vbo);
        MULTI_IBO.write(ibo);
        writeOBJ();

        Locale.setDefault(d);
    }

    private static void writeOBJ() {
        String path = "obj_%d.obj";

        for (int i = 0; i < MULTI_VBO.numVBOs(); i++) {
            MultiVBO.VBO vbo = MULTI_VBO.getVBO(i);
            MultiIBO.IBO ibo = MULTI_IBO.getIBO(i);

            float[] vertices = new float[vbo.limit()];
            vbo.getData(vertices);
            int[] indices = new int[ibo.nIndices()];
            ibo.getData(indices);

            try (BufferedWriter bw = Files.newBufferedWriter(Path.of(path.formatted(i)))) {
                for (int j = 0; j < vertices.length; j += 8) {
                    bw.write("v %f %f %f%n".formatted(vertices[j], vertices[j + 1], vertices[j + 2]));
                }
                bw.newLine();

                for (int j = 0; j < indices.length; j += 3) {
                    bw.write("f %d %d %d%n".formatted(indices[j] + 1, indices[j + 1] + 1, indices[j + 2] + 1));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void disposeAll() {
        for (MultiMesh mesh : MESHES) {
            mesh.close();
        }

        MULTI_IBO.close();
        MULTI_VBO.close();
    }
}