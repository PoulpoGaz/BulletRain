package fr.poulpogaz.jam.renderer.mesh;

import fr.poulpogaz.jam.renderer.utils.ArrayIterator;

import java.util.Iterator;

public class VertexAttributes implements Iterable<VertexAttribute> {

    private final VertexAttribute[] attributes;
    private final int vertexSize;
    private final int vertexSizeBytes;

    public VertexAttributes(VertexAttribute... attributes) {
        this.attributes = attributes;

        vertexSize = computeVertexSize();
        vertexSizeBytes = vertexSize * 4;
    }

    protected int computeVertexSize() {
        int size = 0;

        for (VertexAttribute attribute : attributes) {
            attribute.offset = size * 4;
            size += attribute.size();
        }

        return size;
    }

    public int getOffset(int index) {
        return attributes[index].offset;
    }

    public int[] getOffsets() {
        int[] offsets = new int[numAttributes()];
        int offset = 0;

        for (int i = 0; i < offsets.length; i++) {
            offsets[i] = offset;
            offset += attributes[i].sizeInBytes();
        }

        return offsets;
    }

    public VertexAttribute get(int index) {
        return attributes[index];
    }

    public VertexAttribute[] getAttributes() {
        return attributes;
    }

    public int numAttributes() {
        return attributes.length;
    }

    public int vertexSizeInBytes() {
        return vertexSizeBytes;
    }

    public int vertexSize() {
        return vertexSize;
    }

    @Override
    public Iterator<VertexAttribute> iterator() {
        return new ArrayIterator<>(attributes);
    }
}