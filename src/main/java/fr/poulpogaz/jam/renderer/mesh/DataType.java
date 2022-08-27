package fr.poulpogaz.jam.renderer.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_BOOL;

public enum DataType {

    // SCALARS

    BOOL  (1, 1, GL_BOOL),
    UINT  (1, 1, GL_UNSIGNED_INT),
    INT   (1, 1, GL_INT),
    FLOAT (1, 1, GL_FLOAT),
    DOUBLE(1, 1, GL_DOUBLE),

    // VECTORS 2

    BOOL_VEC_2  (2, 1, GL_BOOL),
    UINT_VEC_2  (2, 1, GL_UNSIGNED_INT),
    INT_VEC_2   (2, 1, GL_INT),
    FLOAT_VEC_2 (2, 1, GL_FLOAT),
    DOUBLE_VEC_2(2, 1, GL_DOUBLE),

    // VECTORS 3

    BOOL_VEC_3  (3, 1, GL_BOOL),
    UINT_VEC_3  (3, 1, GL_UNSIGNED_INT),
    INT_VEC_3   (3, 1, GL_INT),
    FLOAT_VEC_3 (3, 1, GL_FLOAT),
    DOUBLE_VEC_3(3, 1, GL_DOUBLE),

    // VECTORS 4

    BOOL_VEC_4  (4, 1, GL_BOOL),
    UINT_VEC_4  (4, 1, GL_UNSIGNED_INT),
    INT_VEC_4   (4, 1, GL_INT),
    FLOAT_VEC_4 (4, 1, GL_FLOAT),
    DOUBLE_VEC_4(4, 1, GL_DOUBLE),

    // MAT 2

    MAT_2  (2, 2, GL_FLOAT),
    MAT_2x3(2, 3, GL_FLOAT),
    MAT_2x4(2, 4, GL_FLOAT),

    // MAT 3

    MAT_3x2(3, 2, GL_FLOAT),
    MAT_3  (3, 3, GL_FLOAT),
    MAT_3x4(3, 4, GL_FLOAT),

    // MAT 4

    MAT_4x2(4, 2, GL_FLOAT),
    MAT_4x3(4, 3, GL_FLOAT),
    MAT_4  (4, 4, GL_FLOAT);

    private final int size;
    private final int sizePerAttribute;
    private final int numAttributes;
    private final int type;

    DataType(int sizePerAttribute, int numAttributes, int type) {
        this.size = sizePerAttribute * numAttributes;
        this.sizePerAttribute = sizePerAttribute;
        this.numAttributes = numAttributes;
        this.type = type;
    }

    public int size() {
        return size;
    }

    public int sizePerAttribute() {
        return sizePerAttribute;
    }

    public int numAttributes() {
        return numAttributes;
    }

    public int type() {
        return type;
    }
}
