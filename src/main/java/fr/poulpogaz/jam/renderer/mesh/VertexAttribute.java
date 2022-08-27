package fr.poulpogaz.jam.renderer.mesh;

import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class VertexAttribute {

    public static VertexAttribute position2D(int index) {
        return new VertexAttribute(index, DataType.FLOAT_VEC_2);
    }

    public static VertexAttribute color(int index) {
        return new VertexAttribute(index, DataType.FLOAT_VEC_4);
    }

    public static VertexAttribute texture(int index) {
        return new VertexAttribute(index, DataType.FLOAT_VEC_2);
    }

    public static VertexAttribute mat4(int index) {
        return new VertexAttribute(index, DataType.MAT_4);
    }

    protected final int index;
    protected final DataType dataType;
    protected final boolean normalized;
    protected final int divisor;

    // internal use
    public int offset;

    public VertexAttribute(int index, DataType dataType) {
        this(index, dataType, false, 0);
    }

    public VertexAttribute(int index, DataType dataType, boolean normalized) {
        this(index, dataType, normalized, 0);
    }

    public VertexAttribute(int index, DataType dataType, int divisor) {
        this(index, dataType, false, divisor);
    }

    public VertexAttribute(int index, DataType dataType, boolean normalized, int divisor) {
        this.index = index;
        this.dataType = dataType;
        this.normalized = normalized;
        this.divisor = divisor;
    }

    public void enable(int vertexSize) {
        enable(vertexSize, divisor > 0, 0);
    }

    public void enable(int vertexSize, boolean instanced) {
        enable(vertexSize, instanced, 0);
    }

    public void enable(int vertexSize, boolean instanced, int offset2) {
        int length = index + dataType.numAttributes();

        int offset = this.offset;
        for (int i = index; i < length; i++) {
            glEnableVertexAttribArray(i);

            glVertexAttribPointer(i, dataType.sizePerAttribute(), dataType.type(), normalized, vertexSize, offset2 + offset);

            if (instanced) {
                glVertexAttribDivisor(i, divisor);
            }

            offset += dataType.sizePerAttribute() * 4;
        }
    }

    public void disable() {
        int length = index + dataType.numAttributes();
        for (int i = index; i < length; i++) {
            glEnableVertexAttribArray(i);
        }
    }

    public int size() {
        return dataType.size();
    }

    public int sizeInBytes() {
        return dataType.size() * 4;
    }

    public int getIndex() {
        return index;
    }

    public boolean isNormalized() {
        return normalized;
    }

    public int getOffset() {
        return offset;
    }

    public int getDivisor() {
        return divisor;
    }
}