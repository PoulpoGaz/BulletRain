package fr.poulpogaz.jam.renderer.mesh;

import org.lwjgl.system.MemoryUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.GL_MAP_WRITE_BIT;
import static org.lwjgl.opengl.GL30.glMapBufferRange;

public class MultiIBO implements AutoCloseable {

    private final int CAPACITY_DIRTY = 1;
    private final int IBO_DIRTY = 2;

    private final List<IBO> ibos = new ArrayList<>();
    private final int usage;

    private ByteBuffer byteBuffer;
    private final int ibo;

    private int dirty = CAPACITY_DIRTY;

    public MultiIBO(int usage, int defaultSize) {
        this.usage = usage;
        byteBuffer = MemoryUtil.memAlloc(defaultSize * 4);

        ibo = glGenBuffers();
    }

    public void validate() {
        if ((dirty & CAPACITY_DIRTY) != 0) {
            int size = 0;
            if (ibos.size() > 0) {
                IBO last = ibos.get(ibos.size() - 1);

                size = (last.maxIndices() + last.offset) * 4;
            }

            byteBuffer.position(0);
            byteBuffer.limit(size);

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, byteBuffer, usage);
        } else if ((dirty & IBO_DIRTY) != 0) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);

            ByteBuffer buffer = glMapBufferRange(GL_ELEMENT_ARRAY_BUFFER, 0, byteBuffer.limit(), GL_MAP_WRITE_BIT);

            if (buffer != null) {
                buffer.put(byteBuffer);

                glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);
                buffer = null;
            }
        }

        dirty = 0;
    }

    protected void ensureCapacity(int minCapacity) {
        int cap = minCapacity * 4;

        if (byteBuffer.capacity() < cap) {
            byteBuffer = MemoryUtil.memRealloc(byteBuffer, cap);

            dirty = CAPACITY_DIRTY;
        }

        byteBuffer.limit(cap);
    }

    public IBO addIBO(int numIndices) {
        int capacity = numIndices;

        int offset = 0;
        if (ibos.size() > 0) {
            IBO last = ibos.get(ibos.size() - 1);

            offset = last.offset + last.maxIndices();
            capacity += offset;
        }

        ensureCapacity(capacity);
        byteBuffer.position(offset * 4);
        IntBuffer sub = MemoryUtil.memSlice(byteBuffer).asIntBuffer();
        byteBuffer.position(0);

        IBO ibo = new IBO(sub, offset);
        ibos.add(ibo);

        return ibo;
    }

    public void removeIBO(IBO ibo) {
        ibos.remove(ibo);
    }

    public IBO getIBO(int index) {
        return ibos.get(index);
    }

    public int numIBOs() {
        return ibos.size();
    }

    public void trimToSize() {

    }

    public void bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
    }

    public void unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public int currentMaxSize() {
        return byteBuffer.capacity() / 4;
    }

    @Override
    public void close() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(ibo);
        MemoryUtil.memFree(byteBuffer);
    }

    public class IBO {
        private final IntBuffer buffer;
        private final int offset;

        public IBO(IntBuffer buffer, int offset) {
            this.buffer = buffer;
            this.offset = offset;
        }

        public void setData(int[] data) {
            buffer.limit(data.length);
            buffer.put(0, data);
            buffer.position(0);

            setDirty();
        }

        public void getData(int[] out) {
            buffer.get(out);
            buffer.position(0);
        }

        public int nIndices() {
            return buffer.limit();
        }

        public int maxIndices() {
            return buffer.capacity();
        }

        public int offset() {
            return offset;
        }

        void setDirty() {
            MultiIBO.this.dirty |= IBO_DIRTY;
        }
    }

    public void write(String path) {
        try (BufferedWriter bw = Files.newBufferedWriter(Path.of(path))) {
            for (IBO ibo : ibos) {
                bw.write("<------IBO START----->\n");
                bw.write("OFFSET=%d, CAPACITY=%d, LIMIT=%d%n".formatted(ibo.offset, ibo.maxIndices(), ibo.nIndices()));

                IntBuffer b = ibo.buffer;

                int length = ibo.offset + ibo.nIndices();
                for (int i = ibo.offset; i < length; i += 3) {

                    for (int j = 0; j < 3; j++) {
                        bw.write("%5d".formatted(b.get()));

                        if (j + 1 < 3) {
                            bw.write(", ");
                        }
                    }

                    bw.newLine();
                }

                b.position(0);
                bw.write("<-------IBO END------>\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}