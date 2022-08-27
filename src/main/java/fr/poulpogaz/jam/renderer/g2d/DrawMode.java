package fr.poulpogaz.jam.renderer.g2d;

import fr.poulpogaz.jam.renderer.mesh.VertexAttribute;
import fr.poulpogaz.jam.renderer.mesh.VertexAttributes;
import fr.poulpogaz.jam.renderer.shaders.Program;
import fr.poulpogaz.jam.renderer.shaders.Shaders;

public enum DrawMode {

    COLOR(Shaders.COLOR, 6, 2, 0, false,
          VertexAttribute.position2D(0), VertexAttribute.color(1)),

    TEXTURE(Shaders.TEXTURE, 4, 0, 2, true,
            VertexAttribute.position2D(0), VertexAttribute.texture(1)),

    COLOR_TEXTURE(Shaders.COLOR_TEXTURE, 8, 2, 6, true,
                  VertexAttribute.position2D(0), VertexAttribute.color(1), VertexAttribute.texture(2));

    private final Program shader;
    private final int vertexSize;
    private final int colorOffset;
    private final int textureOffset;
    private final boolean texture;
    private final VertexAttributes attributes;

    DrawMode(Program shader, int vertexSize, int colorOffset, int textureOffset, boolean texture, VertexAttribute... attributes) {
        this.shader = shader;
        this.vertexSize = vertexSize;
        this.colorOffset = colorOffset;
        this.textureOffset = textureOffset;
        this.texture = texture;
        this.attributes = new VertexAttributes(attributes);
    }

    public Program getShader() {
        return shader;
    }

    public int getVertexSize() {
        return vertexSize;
    }

    public int getColorOffset() {
        return colorOffset;
    }

    public int getTextureOffset() {
        return textureOffset;
    }

    public boolean isTexture() {
        return texture;
    }

    public VertexAttributes getAttributes() {
        return attributes;
    }
}