package fr.poulpogaz.jam.renderer.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;

public class Mesh implements IMesh {

    private final VertexAttributes attributes;
    private final int usage;

    private final int vao;
    private final VertexBufferObject vbo;
    private final IndexBufferObject ibo;
    private InstanceBuffer instanceBuffer;

    private boolean vaoDirty = true;

    public Mesh(VertexAttributes attributes, int nVertices, int numIndices, int usage) {
        this.attributes = attributes;
        this.usage = usage;

        vbo = new VertexBufferObject(attributes, nVertices, usage);
        ibo = new IndexBufferObject(numIndices, usage);

        vao = glGenVertexArrays();
    }

    public Mesh(int nVertices, int numIndices, int usage, VertexAttribute... attributes) {
        this(new VertexAttributes(attributes), nVertices, numIndices, usage);
    }

    protected void setupVAO() {
        glBindVertexArray(vao);

        // VBO
        vbo.bind();
        for (VertexAttribute attribute : attributes) {
            attribute.enable(attributes.vertexSizeInBytes(), false);
        }

        if (isInstanceRenderingEnable()) {
            instanceBuffer.enableAttribs();
        }

        vaoDirty = false;
    }

    public void render(int primitiveType) {
        if (vaoDirty) {
            setupVAO();
        } else {
            glBindVertexArray(vao);
        }

        vbo.updateData();
        if (instanceBuffer != null && instanceBuffer.getNumInstances() > 0) {
            instanceBuffer.updateData();

            if (ibo.getNumIndices() > 0) {
                ibo.updateData();
                glDrawElementsInstanced(primitiveType, ibo.getNumIndices(), GL_UNSIGNED_INT, 0, instanceBuffer.getNumInstances());
            } else {
                glDrawArraysInstanced(primitiveType, 0, vbo.getNumVertices(), instanceBuffer.getNumInstances());
            }
        } else {
            if (ibo.getNumIndices() > 0) {
                ibo.updateData();
                glDrawElements(primitiveType, ibo.getNumIndices(), GL_UNSIGNED_INT, 0);
            } else {
                glDrawArrays(primitiveType, 0, vbo.getNumVertices());
            }
        }

        glBindVertexArray(0);
    }

    public void enableInstanceRendering(int numInstances, int usage, VertexAttribute... attributes) {
        enableInstanceRendering(new VertexAttributes(attributes), numInstances, usage);
    }

    public void enableInstanceRendering(VertexAttributes attributes, int numInstances, int usage) {
        if (instanceBuffer == null) {
            instanceBuffer = new InstanceBuffer(attributes, numInstances, usage);
            vaoDirty = true;
        }
    }

    public void disableInstanceRendering() {
        if (instanceBuffer != null) {
            instanceBuffer.dispose();
            instanceBuffer = null;
            vaoDirty = true;
        }
    }

    public boolean isInstanceRenderingEnable() {
        return instanceBuffer != null;
    }

    public void setVertices(FloatBuffer buffer) {
        vbo.setVertices(buffer);
    }

    public void setVertices(float[] array) {
        vbo.setVertices(array, 0, array.length, 0);
    }

    public void setVertices(float[] array, int offset) {
        vbo.setVertices(array, offset, array.length, 0);
    }

    public void setVertices(float[] array, int offset, int length) {
        vbo.setVertices(array, offset, length, 0);
    }

    public void setVertices(float[] array, int offset, int length, int offsetInDst) {
        vbo.setVertices(array, offset, length, offsetInDst);
    }

    public float[] getVertices(float[] array) {
        return vbo.getVertices(array);
    }

    public float[] getVertices(float[] array, int offset, int length) {
        return vbo.getVertices(array, offset, length);
    }

    public float[] getVertices(float[] array, int offset, int length, int offsetInDst) {
        return vbo.getVertices(array, offset, length, offsetInDst);
    }

    public FloatBuffer getVerticesBuffer() {
        return vbo.getBuffer();
    }

    public int getMaxVertices() {
        return vbo.getMaxVertices();
    }

    public int getNumVertices() {
        return vbo.getNumVertices();
    }

    public void setIndices(IntBuffer buffer) {
        ibo.setIndices(buffer);
    }

    public void setIndices(int[] array) {
        ibo.setIndices(array, 0, array.length, 0);
    }

    public void setIndices(int[] array, int offset) {
        ibo.setIndices(array, offset, array.length, 0);
    }

    public void setIndices(int[] array, int offset, int length) {
        ibo.setIndices(array, offset, length, 0);
    }

    public void setIndices(int[] array, int offset, int length, int offsetInDst) {
        ibo.setIndices(array, offset, length, offsetInDst);
    }

    public int[] getIndices(int[] array) {
        return ibo.getIndices(array);
    }

    public int[] getIndices(int[] array, int offset, int length) {
        return ibo.getIndices(array, offset, length);
    }

    public int[] getIndices(int[] array, int offset, int length, int offsetInDst) {
        return ibo.getIndices(array, offset, length, offsetInDst);
    }

    public IntBuffer getIndicesBuffer() {
        return ibo.getBuffer();
    }

    public int getMaxIndices() {
        return ibo.getMaxIndices();
    }

    public int getNumIndices() {
        return ibo.getNumIndices();
    }

    public void setInstanceData(FloatBuffer buffer) {
        instanceBuffer.setData(buffer);
    }

    public void setInstanceData(float[] array) {
        instanceBuffer.setData(array, 0, array.length, 0);
    }

    public void setInstanceData(float[] array, int offset) {
        instanceBuffer.setData(array, offset, array.length, 0);
    }

    public void setInstanceData(float[] array, int offset, int length) {
        instanceBuffer.setData(array, offset, length, 0);
    }

    public void setInstanceData(float[] array, int offset, int length, int offsetInDst) {
        instanceBuffer.setData(array, offset, length, offsetInDst);
    }

    public float[] getInstanceData(float[] array) {
        return instanceBuffer.getData(array);
    }

    public float[] getInstanceData(float[] array, int offset, int length) {
        return instanceBuffer.getData(array, offset, length);
    }

    public float[] getInstanceData(float[] array, int offset, int length, int offsetInDst) {
        return instanceBuffer.getData(array, offset, length, offsetInDst);
    }

    public FloatBuffer getInstanceData() {
        return instanceBuffer.getBuffer();
    }

    public int getMaxInstances() {
        return instanceBuffer.getMaxInstances();
    }

    public int getNumInstances() {
        return instanceBuffer.getNumInstances();
    }

    public VertexAttributes getVertexAttributes() {
        return attributes;
    }

    public VertexBufferObject getVBO() {
        return vbo;
    }

    public IndexBufferObject getIBO() {
        return ibo;
    }

    public InstanceBuffer getInstanceBuffer() {
        return instanceBuffer;
    }

    public int getUsage() {
        return usage;
    }

    @Override
    public void dispose() {
        vbo.dispose();
        ibo.dispose();

        if (instanceBuffer != null) {
            instanceBuffer.dispose();
        }

        glBindVertexArray(0);
        glDeleteVertexArrays(vao);
    }
}