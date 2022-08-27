#version 330

layout (location = 0) in vec2 vertex;
layout (location = 1) in vec2 texture_;
layout (location = 2) in mat4 modelView;

out vec2 texture;

uniform mat4 projection;

void main() {
    texture = texture_;

    gl_Position = projection * modelView * vec4(vertex, 0, 1);
}