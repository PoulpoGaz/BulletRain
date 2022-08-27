#version 330

layout (location = 0) in vec2 vertex;
layout (location = 1) in vec3 glyph_info;  // x, y, width in pixel. eg: 50, 50, 20
layout (location = 2) in vec4 color_;
layout (location = 3) in mat4 model;

uniform mat4 projection;
uniform int texture_size;  // size of the texture
uniform int font_height;

out vec2 texture;
out vec4 color;

void main() {
    vec2 pos = vertex * vec2(glyph_info.z, font_height);

    // compute texture coordinates, values between 0 and texture_size
    // map texture coordinates between 0 and 1
    texture = (glyph_info.xy + pos) / texture_size;

    color = color_;

    gl_Position = projection * model * vec4(pos, 0, 1);
}