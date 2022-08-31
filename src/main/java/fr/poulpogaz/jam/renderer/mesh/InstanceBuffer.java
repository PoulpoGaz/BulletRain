package fr.poulpogaz.jam.renderer.mesh;

import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;

public class InstanceBuffer implements IGLBuffer {

    private final VertexAttributes attributes;

    private final ByteBuffer byteBuffer;
    private final FloatBuffer buffer;

    private final int glBuffer;
    private final int usage;

    private boolean dirty = true;

    public InstanceBuffer(VertexAttributes attributes, int numInstances, int usage) {
        this.attributes = attributes;
        byteBuffer = MemoryUtil.memAlloc(numInstances * attributes.vertexSizeInBytes());
        buffer = byteBuffer.asFloatBuffer();

        byteBuffer.flip();
        buffer.flip();

        this.usage = usage;

        glBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, glBuffer);
        glBufferData(GL_ARRAY_BUFFER, byteBuffer.capacity(), usage);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void enableAttribs() {
        bind();

        for (VertexAttribute attribute : attributes) {
            attribute.enable(attributes.vertexSizeInBytes(), true);
        }
    }

    public void setData(FloatBuffer buffer) {
        dirty = true;
        this.buffer.clear();

        buffer.position(0);

        this.buffer.put(buffer);
        this.buffer.limit(buffer.limit());
    }

    public void setData(float[] array) {
        setData(array, 0, array.length, 0);
    }

    public void setData(float[] array, int offset) {
        setData(array, offset, array.length, 0);
    }

    public void setData(float[] array, int offset, int length) {
        setData(array, offset, length, 0);
    }

    public void setData(float[] array, int offset, int length, int offsetInDst) {
        dirty = true;
        buffer.limit(offsetInDst + length);
        buffer.position(offsetInDst);
        buffer.put(array, offset, length);
        buffer.position(0);
    }

    public float[] getData(float[] array) {
        if (array == null) {
            array = new float[buffer.limit()];
        }

        return getData(array, 0, array.length, 0);
    }

    public float[] getData(float[] array, int offset, int length) {
        if (array == null) {
            array = new float[length];
        }

        return getData(array, offset, length, 0);
    }

    public float[] getData(float[] array, int offset, int length, int offsetInDst) {
        buffer.get(offset, array, offsetInDst, length);

        return array;
    }

    public int getNumInstances() {
        return buffer.limit() * 4 / attributes.vertexSizeInBytes();
    }

    public int getMaxInstances() {
        return byteBuffer.capacity() / attributes.vertexSizeInBytes();
    }

    public FloatBuffer getBuffer() {
        dirty = true;
        return buffer;
    }

    @Override
    public boolean updateData() {
        if (dirty) {
            glBindBuffer(GL_ARRAY_BUFFER, glBuffer);

            byteBuffer.limit(buffer.limit() * 4);
            byteBuffer.position(0);

            glBufferSubData(GL_ARRAY_BUFFER, 0, byteBuffer);
            dirty = false;

            return true;
        }

        return false;
    }

    @Override
    public void markDirty() {
        dirty = true;
    }

    @Override
    public void bind() {
        if (!updateData()) {
            glBindBuffer(GL_ARRAY_BUFFER, glBuffer);
        }
    }

    @Override
    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    @Override
    public int getUsage() {
        return usage;
    }

    @Override
    public void close() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(glBuffer);
        MemoryUtil.memFree(byteBuffer);
    }

    public VertexAttributes getAttributes() {
        return attributes;
    }
}