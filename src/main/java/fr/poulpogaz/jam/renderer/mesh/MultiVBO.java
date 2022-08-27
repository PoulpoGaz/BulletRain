package fr.poulpogaz.jam.renderer.mesh;

import fr.poulpogaz.jam.renderer.utils.Disposable;
import org.lwjgl.system.MemoryUtil;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.GL_MAP_WRITE_BIT;
import static org.lwjgl.opengl.GL30.glMapBufferRange;

public class MultiVBO implements Disposable {

    private final int CAPACITY_DIRTY = 1;
    private final int VBO_DIRTY = 2;

    private final List<VBO> vbos = new ArrayList<>();
    private final int usage;

    private ByteBuffer byteBuffer;
    private final int vbo;

    private int dirty = CAPACITY_DIRTY;

    public MultiVBO(int usage, int defaultSize) {
        this.usage = usage;
        byteBuffer = MemoryUtil.memAlloc(defaultSize * 4);

        vbo = glGenBuffers();
    }

    public void validate() {
        if ((dirty & CAPACITY_DIRTY) != 0) {
            int size = 0;
            if (vbos.size() > 0) {
                VBO last = vbos.get(vbos.size() - 1);

                size = (last.capacity() + last.offset) * 4;
            }

            byteBuffer.position(0);
            byteBuffer.limit(size);

            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, byteBuffer, usage);
        } else if ((dirty & VBO_DIRTY) != 0) {
            glBindBuffer(GL_ARRAY_BUFFER, vbo);

            ByteBuffer buffer = glMapBufferRange(GL_ARRAY_BUFFER, 0, byteBuffer.limit(), GL_MAP_WRITE_BIT);

            if (buffer != null) {
                buffer.put(byteBuffer);

                glUnmapBuffer(GL_ARRAY_BUFFER);
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

    public VBO addVBO(VertexAttributes attributes, int numVertices) {
        int capacity = numVertices * attributes.vertexSize();

        int offset = 0;
        if (vbos.size() > 0) {
            VBO last = vbos.get(vbos.size() - 1);

            offset = last.offset + last.capacity();
            capacity += offset;
        }

        ensureCapacity(capacity);
        byteBuffer.position(offset * 4);
        FloatBuffer sub = MemoryUtil.memSlice(byteBuffer).asFloatBuffer();
        byteBuffer.position(0);

        VBO vbo = new VBO(attributes, sub, offset);
        vbos.add(vbo);

        return vbo;
    }

    public void removeVBO(VBO vbo) {
        vbos.remove(vbo);
    }

    public VBO getVBO(int index) {
        return vbos.get(index);
    }

    public int numVBOs() {
        return vbos.size();
    }

    public void trimToSize() {

    }

    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
    }

    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public int currentMaxSize() {
        return byteBuffer.capacity() / 4;
    }

    @Override
    public void dispose() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vbo);
        MemoryUtil.memFree(byteBuffer);
    }

    public class VBO {
        private final VertexAttributes attributes;
        private final FloatBuffer buffer;

        private final int offset;

        public VBO(VertexAttributes attributes, FloatBuffer buffer, int offset) {
            this.attributes = attributes;
            this.buffer = buffer;
            this.offset = offset;
        }

        public void enable(boolean forceInstanced) {
            bind();

            for (VertexAttribute attribute : attributes) {
                attribute.enable(attributes.vertexSizeInBytes(), forceInstanced, offset * 4);
            }
        }

        public void disable() {
            for (VertexAttribute attribute : attributes) {
                attribute.disable();
            }
        }

        public void setData(float[] data) {
            buffer.limit(data.length);
            buffer.put(0, data);
            buffer.position(0);

            setDirty();
        }

        public void getData(float[] out) {
            buffer.get(out);
            buffer.position(0);
        }

        public int nVertices() {
            return buffer.limit() / attributes.vertexSize();
        }

        public int maxVertices() {
            return buffer.capacity() / attributes.vertexSize();
        }

        public int verticesOffset() {
            return offset / attributes.vertexSize();
        }

        public int limit() {
            return buffer.limit();
        }

        public int capacity() {
            return buffer.capacity();
        }

        public int offset() {
            return offset;
        }

        void setDirty() {
            MultiVBO.this.dirty |= VBO_DIRTY;
        }
    }

    public void write(String path) {
        try (BufferedWriter bw = Files.newBufferedWriter(Path.of(path))) {
            for (VBO vbo : vbos) {
                bw.write("<------VBO START----->\n");
                bw.write("OFFSET=%d, CAPACITY=%d, LIMIT=%d%n".formatted(vbo.offset, vbo.capacity(), vbo.limit()));

                VertexAttributes attributes = vbo.attributes;
                FloatBuffer b = vbo.buffer;
                int length = vbo.offset + vbo.limit();

                int add = attributes.vertexSizeInBytes() / 4;
                for (int i = vbo.offset; i < length; i += add) {

                    for (int j = 0; j < add; j++) {
                        bw.write("%8.2f".formatted(b.get()));

                        if (j + 1 < attributes.numAttributes()) {
                            bw.write(", ");
                        }
                    }

                    bw.newLine();
                }

                b.position(0);
                bw.write("<-------VBO END------>\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}