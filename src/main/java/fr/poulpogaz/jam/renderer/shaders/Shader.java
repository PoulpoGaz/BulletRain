package fr.poulpogaz.jam.renderer.shaders;

import fr.poulpogaz.jam.renderer.utils.Disposable;
import fr.poulpogaz.jam.renderer.utils.Resource;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader implements Disposable {

    public static Shader newShader(ShaderType type, String shaderName) throws IOException {
        return switch (type) {
            case VERTEX -> new Shader(type, Resource.readString("shaders/%s.vert".formatted(shaderName)));
            case FRAGMENT -> new Shader(type, Resource.readString("shaders/%s.frag".formatted(shaderName)));
        };
    }

    private final ShaderType type;
    private final int shader;

    public Shader(ShaderType type, String source) throws IOException {
        this.type = type;

        shader = glCreateShader(type.getType());
        glShaderSource(shader, source);
        glCompileShader(shader);

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new IOException("Failed to compile %s (%s): %s\n".formatted(source, type, glGetShaderInfoLog(shader)));
        }
    }

    public ShaderType getType() {
        return type;
    }

    public int getShader() {
        return shader;
    }

    @Override
    public void dispose() {
        glDeleteShader(shader);
    }
}
