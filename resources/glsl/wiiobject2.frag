@version 4.2
@include mapstruct.ggsl

layout(location = 0) out vec4 fcolor;

layout(location = 0) in vec2 uv0;
layout(location = 1) in vec3 pos;
layout(location = 2) in vec3 varying_normal;
layout(location = 3) in vec2 lightmapCoord;
layout(location = 4) in vec2 normalCoord;
layout(location = 5) in vec2 specularCoord;
layout(location = 6) in vec4 fs_layer0_color;
layout(location = 7) in vec4 varying_lightDirSet;
layout(location = 8) in vec3 varying_tangent;
layout(location = 9) in vec3 varying_bitangent;

layout(set=7, binding=0) unwrap uniform HDRBlock{
    float exposure;
    float gamma;
};

layout(binding = 0, set = 0) unwrap uniform VP{
    mat4 view;
    mat4 perspective;
    vec3 camera;
};

layout(binding = 0, set = 1) unwrap uniform Model{
    mat4 model;
};

uniform layout(set = 1, binding = 1) sampler2D Kd;

vec4 surfaceNormal;
vec3 normal;
float fs_prelitSpecularFactor = 0;
float ldotn0 = 0;
float ldotn1 = 0;
float ldotn2 = 0;
vec3 diffuseLight = vec3(0);
vec3 specularLight = vec3(0);
vec3 incident;

void main() {
}
