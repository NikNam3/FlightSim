#version 410

#define ATMOSPHERE_SCALE 10000.0

in vec2 v_uv;
out vec4 o_color;

uniform sampler2D u_color_buffer;
uniform sampler2D u_depth_buffer;
uniform mat4 u_camera;
uniform vec3 u_from_sun;
uniform vec3 u_camera_pos;
uniform float u_aspect_ratio;
uniform float u_near;
uniform float u_far;
uniform float u_fov;

float linearize_depth(float d);

struct Sphere{
    float r;
    vec3 p;
};

vec2 ray_sphere(Sphere sphere, vec3 ro, vec3 rd)
{
    vec3 offset = ro - sphere.p;
    float a = 1;
    float b = 2 * dot(offset, rd);
    float c = dot(offset, offset) - sphere.r * sphere.r;
    float d = b * b - 4 * a * c;
    if(d > 0){
        float s = sqrt(d);
        float near = max(0, (-b - s) / (2 * a));
        float far  = (-b + s) / (2 * a);
        if(far > 0)
            return vec2(near, far - near);
    }
    return vec2(1e9, 0);
}

float getDensity(Sphere atmosphere, vec3 point){
    float factor = 3;
    float dist = length(atmosphere.p - point) / atmosphere.r;
    return (1 - dist) * exp(-dist * factor);
}

float viewDepth(Sphere atmosphere, vec3 ro, vec3 rd, float rl){
    int nSamplePoints = 10;
    vec3 samplePoint = ro;
    float stepSize = rl / float(nSamplePoints - 1);
    float result = 0;
    for(int i = 0; i < nSamplePoints; i++){
        float density = getDensity(atmosphere, samplePoint);
        result += density * stepSize;
        samplePoint += rd * stepSize;
    }
    return result;
}

vec3 getAtmosphereColor(Sphere atmosphere, vec3 viewPosition, vec3 viewRay, float viewRayLength){
    int samplePoints = 10;

    Sphere planet = Sphere(637.1, atmosphere.p);

    float scatterFactor = 100;
    vec3 lightCoefficients = vec3(
        pow(400.0 / 700.0, 4) * scatterFactor,
        pow(400.0 / 530.0, 4) * scatterFactor,
        pow(400.0 / 440.0, 4) * scatterFactor
    );

    vec3 toSun = -u_from_sun;

    vec2 atmosphereHit = ray_sphere(atmosphere, viewPosition, viewRay);
    vec2 planetHit     = ray_sphere(planet, viewPosition, viewRay);
    float distTo      = atmosphereHit.x;
    float distThrough = atmosphereHit.y;
    distThrough = min(distThrough, planetHit.x - distTo);

    vec3 currentPoint = (viewRay * distTo) + viewPosition;
    float stepSize    = float(distThrough) / float(samplePoints - 1);
    vec3 light = vec3(0);
    for(int i = 0; i < samplePoints; i++){
        float lSunRay = ray_sphere(atmosphere, currentPoint, toSun).y;
        float sunViewDepth = viewDepth(atmosphere, currentPoint, toSun, lSunRay);
        float camViewDepth = viewDepth(atmosphere, currentPoint, -viewRay, stepSize * i);
        float density = getDensity(atmosphere, currentPoint);
        vec3 lightPass = exp(-(sunViewDepth + camViewDepth) * lightCoefficients);

        light += stepSize * density * lightPass * lightCoefficients;
        currentPoint += viewRay * stepSize;
    }
    return light;
}

void main(){
    vec3 color  = texture(u_color_buffer, v_uv).xyz;
    float depth = linearize_depth(texture(u_depth_buffer, v_uv).r);
    vec3 ray = vec3((v_uv * 2 - 1) * sin(u_fov / 2), -1);
    ray *= depth;
    ray.x *= u_aspect_ratio;
    ray = (u_camera * vec4(ray, 0)).xyz;
    float rayLength = length(ray);
    ray    = normalize(ray);

    float nDotS = max(0, dot(vec3(0, 1, 0), -u_from_sun));
    float vDotS = max(0, dot(ray, -u_from_sun));

    Sphere atmosphere = Sphere(650.0, vec3(0, -637.1, 0));
    vec3 skyColor   = getAtmosphereColor(atmosphere, u_camera_pos / ATMOSPHERE_SCALE, ray, rayLength / ATMOSPHERE_SCALE);
    vec3 sunColor   = vec3(pow(vDotS, 1000) * 20) * skyColor;
    vec3 finalColor = sunColor + skyColor;
    if(rayLength < u_far - 10)
        o_color = vec4(color, 1);
    else
        o_color = vec4(finalColor, 1);
}

float linearize_depth(float d)
{
    float z_n = 2.0 * d - 1.0;
    return 2.0 * u_near * u_far / (u_far + u_near - z_n * (u_far - u_near));
}