#version 410

layout(location = 0) in vec2 i_pos;
out vec2 v_uv;

void main(){
    v_uv = i_pos * 0.5 + 0.5;
    gl_Position = vec4(i_pos, 0, 1);
}