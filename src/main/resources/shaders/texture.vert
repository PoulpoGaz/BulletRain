#version 330

layout (location = 0) in vec2 vertex;
layout (location = 1) in vec2 texture_;

out vec2 texture;

uniform mat4 projection;
uniform mat4 modelView;

void main() {
    texture = texture_;

    gl_Position = projection * modelView * vec4(vertex, 0, 1);
}