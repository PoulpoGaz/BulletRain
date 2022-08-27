#version 330

in vec2 texture;

uniform sampler2D sampler;

out vec4 frag_color;

void main() {
    frag_color = texture2D(sampler, texture);
}