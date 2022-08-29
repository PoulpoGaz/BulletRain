package fr.poulpogaz.jam.renderer.mesh;

import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

public class IndexBufferObject implements IGLBuffer {

    private final IntBuffer buffer;
    private final ByteBuffer byteBuffer;

    private final int ibo;
    private final int usage;

    private boolean dirty = true;

    public IndexBufferObject(int numIndices, int usage) {
        byteBuffer = MemoryUtil.memAlloc(numIndices * 4);
        buffer = byteBuffer.asIntBuffer();

        byteBuffer.flip();
        buffer.flip();

        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, byteBuffer.capacity(), usage);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        this.usage = usage;
    }

    public int getNumIndices() {
        return buffer.limit();
    }

    public int getMaxIndices() {
        return buffer.capacity();
    }

    public void setIndices(IntBuffer b) {
        int old = b.position();

        buffer.clear();
        buffer.put(b);

        b.position(old);

        buffer.flip();
    }

    public void setIndices(int[] indices) {
        buffer.clear();
        buffer.put(indices);
        buffer.flip();
    }

    public void setIndices(int[] array, int offset) {
        setIndices(array, offset, array.length, 0);
    }

    public void setIndices(int[] array, int offset, int length) {
        setIndices(array, offset, length, 0);
    }

    public void setIndices(int[] array, int offset, int length, int offsetInDst) {
        dirty = true;

        buffer.limit(offsetInDst + length);
        buffer.position(offsetInDst);
        buffer.put(array, offset, length);
        buffer.position(0);
    }

    public int[] getIndices(int[] array) {
        if (array == null) {
            array = new int[buffer.limit()];
        }

        return getIndices(array, 0, array.length, 0);
    }

    public int[] getIndices(int[] array, int offset, int length) {
        if (array == null) {
            array = new int[length];
        }

        return getIndices(array, offset, length, 0);
    }

    public int[] getIndices(int[] array, int offset, int length, int offsetInDst) {
        buffer.get(offset, array, offsetInDst, length);

        return array;
    }

    public IntBuffer getBuffer() {
        dirty = true;
        return buffer;
    }

    @Override
    public boolean updateData() {
        if (dirty) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            byteBuffer.limit(buffer.limit() * 4);
            byteBuffer.position(0);
            glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, byteBuffer);

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
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        }
    }

    @Override
    public void unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    @Override
    public int getUsage() {
        return usage;
    }

    @Override
    public void close() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(ibo);

        MemoryUtil.memFree(byteBuffer);
    }
}