#version 330

in vec4 color;
in vec2 texture;

uniform sampler2D sampler;

out vec4 frag_color;

void main() {
    frag_color = color * texture2D(sampler, texture);
}