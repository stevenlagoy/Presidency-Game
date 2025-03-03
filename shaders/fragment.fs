#version 400 core

in vec2 fragTextureCoord;
in vec3 fragNormal;
in vec3 fragPosition;

out vec4 fragColor;

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};

uniform sampler2D textureSampler;
uniform vec3 ambientLight;
uniform Material material;

vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

void setupColors(Material material, vec2 textureCoords) {
    if(material.hasTexture == 1) {
        ambientC = texture(textureSampler, textureCoords);
        diffuseC = ambientC;
        specularC = ambientC;
    }
    else {
        ambientC = material.ambient;
        diffuseC = material.diffuse;
        specularC = material.specular;
    }
}

void main() {

    if (material.hasTexture == 1) {
        ambientC = texture(textureSampler, fragTextureCoord);
    }
    else {
        ambientC = material.ambient + material.specular + material.diffuse + material.reflectance;
    }

    // setupColors(material, fragTextureCoord);

    fragColor = ambientC * vec4(ambientLight, 1.0);
}