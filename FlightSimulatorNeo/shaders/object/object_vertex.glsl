#version 410

layout(location = 0) in vec3 i_position;
layout(location = 1) in vec3 i_normal;
layout(location = 2) in vec2 i_uv;

out vec3 v_normal;
out vec2 v_uv;

uniform mat4 u_matrix_perspective;
uniform mat4 u_matrix_view;
uniform mat4 u_matrix_model;

void main(){
    gl_Position = u_matrix_perspective * u_matrix_view * u_matrix_model * vec4(i_position, 1);
    v_normal = (u_matrix_model * vec4(i_normal, 0)).xyz;
    v_uv = i_uv;
}