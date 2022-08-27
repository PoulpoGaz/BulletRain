#version 330

layout (location = 0) in vec2 vertex;
layout (location = 1) in vec4 color_;

out vec4 color;

uniform mat4 projection;
uniform mat4 modelView;

void main() {
    color = color_;

    gl_Position = projection * modelView * vec4(vertex, 0, 1);
}