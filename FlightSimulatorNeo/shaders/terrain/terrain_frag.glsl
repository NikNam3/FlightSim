#version 410

out vec4 o_color;

in vec3 v_normal;
in vec2 v_uv;


uniform sampler2D u_albedo_map;
uniform vec3 u_light_direction;
uniform vec3 u_light_color;
uniform bool u_has_albedo_map;
uniform int u_layer_idx;
uniform vec3 u_ambient;


uniform sampler2D u_height_map;
uniform bool u_has_height_map;

void main(){
    vec3 normal = normalize(v_normal);
    if(u_has_height_map){
        float refHeight = texture(u_height_map, v_uv).r;
        float sampleDist = 0.1;
        float dy1 = refHeight - texture(u_height_map, v_uv + vec2(0, sampleDist / 2000)).r;
        float dy2 = refHeight - texture(u_height_map, v_uv + vec2(sampleDist / 2000, 0)).r;
        normal.x =  -dy1 / sampleDist;
        normal.z =  dy2 / sampleDist;
        normal.y = 1;
        normal = normalize(normal);
    }


    float shade = dot(normal, normalize(-u_light_direction));
    shade = max(shade, 0);
    vec3 albedo = vec3(0.8, 0.8, 0.8);
    if(u_has_albedo_map)
        albedo = texture(u_albedo_map, v_uv).xyz;

    vec3 diffuse = albedo * shade * u_light_color;
    diffuse += albedo * u_ambient;

    o_color = vec4(diffuse, 1);

    float gamma = 2.2;
    o_color.rgb = pow(o_color.rgb, vec3(1.0/gamma));

//    o_color.rgb = normal * 0.5 + 0.5;
}