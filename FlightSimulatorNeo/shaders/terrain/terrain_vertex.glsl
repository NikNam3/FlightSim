#version 410

layout(location = 0) in vec3 i_position;
layout(location = 1) in vec3 i_normal;
layout(location = 2) in vec2 i_uv;

out float v_camera_dist;
out vec3 v_normal;
out vec2 v_uv;

uniform mat4 u_matrix_perspective;
uniform mat4 u_matrix_view;
uniform mat4 u_matrix_model;

uniform sampler2D u_height_map;
uniform bool u_has_height_map;
uniform float u_sinking_threshold;

void main(){
    float height = 0.0f;
    if(u_has_height_map)
        height = texture(u_height_map, vec2(i_uv.x, i_uv.y)).r;

    vec3 cameraPositionXZ = vec3(inverse(u_matrix_view)[3][0], 0, inverse(u_matrix_view)[3][2]);
    float distFromCameraFoot = distance(cameraPositionXZ, (u_matrix_model * vec4(i_position, 1)).xyz);
    if(distFromCameraFoot < u_sinking_threshold)
        height -= 500;

    vec3 truePos = i_position + vec3(0, height, 0);
    gl_Position = u_matrix_perspective * u_matrix_view * u_matrix_model * vec4(truePos, 1);
    v_normal   = (u_matrix_model * vec4(i_normal, 0)).xyz;
    v_uv = i_uv;
}