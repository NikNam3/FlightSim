#version 410

layout(location = 0)out vec4 o_color;

in vec3 v_normal;
in vec2 v_uv;

uniform sampler2D u_albedo_map;
uniform bool u_has_albedo_map;
uniform vec3 u_albedo;
uniform vec3 u_light_direction;
uniform vec3 u_light_color;
uniform vec3 u_ambient;

void main(){
    vec3 normal = normalize(v_normal);
    float shade = dot(normal, normalize(-u_light_direction));
    shade = max(shade, 0);
    vec3 albedo = u_albedo;
    if(u_has_albedo_map)
        albedo = texture(u_albedo_map, v_uv).xyz;

    vec3 diffuse = albedo * shade * u_light_color;
    diffuse += albedo * u_ambient;

    o_color = vec4(diffuse, 1);
    float gamma = 2.2;
    o_color.rgb = pow(o_color.rgb, vec3(1.0/gamma));

}