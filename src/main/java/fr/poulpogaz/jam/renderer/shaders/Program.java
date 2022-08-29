package fr.poulpogaz.jam.renderer.shaders;

import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Program implements AutoCloseable {

    private final Shader vertex;
    private final Shader fragment;
    private final int program;

    private final HashMap<String, Integer> uniforms = new HashMap<>();

    public Program(Shader vertex, Shader fragment) {
        this.vertex = vertex;
        this.fragment = fragment;

        program = glCreateProgram();
        glAttachShader(program, vertex.getShader());
        glAttachShader(program, fragment.getShader());
    }

    public void bindAttribute(int location, String name) {
        glBindAttribLocation(program, location, name);
    }

    public void link() throws IOException {
        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
            throw new IOException("Shader program link error\n" + glGetProgramInfoLog(program));
        }

        glValidateProgram(program);
        if (glGetProgrami(program, GL_VALIDATE_STATUS) == GL_FALSE) {
            throw new IOException("Shader program validate error\n" + glGetProgramInfoLog(program));
        }
    }

    public void createUniform(String name) throws IOException {
        int location = glGetUniformLocation(program, name);

        if (location < 0) {
            throw new IOException("could not find uniform: " + name);
        }

        uniforms.put(name, location);
    }

    public void setUniform(String name, Vector2fc vec) {
        glUniform2f(uniforms.get(name), vec.x(), vec.y());
    }

    public void setUniform(String name, Vector3fc vec) {
        glUniform3f(uniforms.get(name), vec.x(), vec.y(), vec.z());
    }

    public void setUniform(String name, Vector4fc vec) {
        glUniform4f(uniforms.get(name), vec.x(), vec.y(), vec.z(), vec.w());
    }

    public void setUniform(String name, Matrix4fc mat) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniforms.get(name), false,
                    mat.get(stack.mallocFloat(16)));
        }
    }

    public void setUniformi(String name, int value) {
        glUniform1i(uniforms.get(name), value);
    }

    public void setUniformf(String name, float value) {
        glUniform1f(uniforms.get(name), value);
    }

    public void bind() {
        glUseProgram(program);
    }

    public static void unbind() {
        glUseProgram(0);
    }

    @Override
    public void close() {
        glDetachShader(program, vertex.getShader());
        glDetachShader(program, fragment.getShader());
        glDeleteProgram(program);
    }
}
