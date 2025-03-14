#version 400 core

const int MAX_POINT_LIGHTS = 5;
const int MAX_SPOT_LIGHTS = 5;

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

struct DirectionalLight {
    vec3 direction;
    vec3 color;
    float intensity;
};

struct Attenuation {
    float constant;
    float linear;
    float exponent;
};

struct PointLight {
    vec3 color;
    vec3 position;
    float intensity;
    Attenuation att;
};

struct SpotLight {
    PointLight pl;
    vec3 coneDirection;
    float cutoff;
};

uniform sampler2D backgroundTexture;
uniform sampler2D redTexture;
uniform sampler2D greenTexture;
uniform sampler2D blueTexture;
uniform sampler2D blendMap;

uniform vec3 ambientLight;
uniform Material material;
uniform float specularPower;
uniform DirectionalLight directionalLight;
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];

vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

void setupColors(Material material, vec2 textureCoords) {
    if(material.hasTexture == 0) {

        vec4 blendMapColor = texture(blendMap, textureCoords);
        float backgroundTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
        vec2 tiledCoordinates = textureCoords / 2.5f;
        
        vec4 backgroundTextureColor = texture(backgroundTexture, tiledCoordinates) * backgroundTextureAmount;
        vec4 redTextureColor = texture(redTexture, tiledCoordinates) * blendMapColor.r;
        vec4 greenTextureColor = texture(greenTexture, tiledCoordinates) * blendMapColor.g;
        vec4 blueTextureColor = texture(blueTexture, tiledCoordinates) * blendMapColor.b;

        ambientC = backgroundTextureColor + redTextureColor + greenTextureColor + blueTextureColor;
        ambientC.a = material.ambient.a;
        diffuseC = ambientC;
        specularC = ambientC;
    }
    else {
        ambientC = material.ambient;
        diffuseC = material.diffuse;
        specularC = material.specular;
    }
}

vec4 calcLightColor(vec3 light_color, float light_intensity, vec3 position, vec3 to_light_dir, vec3 normal) {
    vec4 diffuseColor = vec4(0, 0, 0, 0);
    vec4 specularColor = vec4(0, 0, 0, 0);
    
    // Diffuse Light
    float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
    diffuseColor = diffuseC * vec4(light_color, 1.0) * light_intensity * diffuseFactor;

    // Specular Light
    vec3 camera_direction = normalize(-position);
    vec3 from_light_dir = -to_light_dir;
    vec3 reflectedLight = normalize(reflect(from_light_dir, normal));
    float specularFactor = max(dot(camera_direction, reflectedLight), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specularColor = specularC * light_intensity * specularFactor * material.reflectance * vec4(light_color, 1.0);

    return (diffuseColor + specularColor);
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal) {
    vec3 light_dir = light.position - position;
    vec3 to_light_dir = normalize(light_dir);
    vec4 light_color = calcLightColor(light.color, light.intensity, position, to_light_dir, normal);

    // Attenuation
    float distance = length(light_dir);
    float attenuation_inv = light.att.constant + light.att.linear * distance + light.att.exponent * distance * distance;

    return light_color / attenuation_inv;
}

vec4 calcSpotLight(SpotLight light, vec3 position, vec3 normal) {
    vec3 light_dir = light.pl.position - position;
    vec3 to_light_dir = normalize(light_dir);
    vec3 from_light_dir = -to_light_dir;
    float spot_alfa = dot(from_light_dir, normalize(light.coneDirection));
    vec4 color = vec4(0, 0, 0, 0);

    if (spot_alfa > light.cutoff) {
        color = calcPointLight(light.pl, position, normal);
        color *= (1.0 - (1.0 - spot_alfa) / (1.0 - light.cutoff));
    }

    return color;
}

vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal) {
    return calcLightColor(light.color, light.intensity, position, normalize(light.direction), normal);
}

void main() {

    setupColors(material, fragTextureCoord);

    vec4 diffuseSpecularComp = calcDirectionalLight(directionalLight, fragPosition, fragNormal);
    
    for (int i = 0; i < MAX_POINT_LIGHTS; i++) {
        if (pointLights[i].intensity > 0) {
            diffuseSpecularComp += calcPointLight(pointLights[i], fragPosition, fragNormal);
        }
    }
    for (int i = 0; i < MAX_SPOT_LIGHTS; i++) {
        if (spotLights[i].pl.intensity > 0) {
            diffuseSpecularComp += calcSpotLight(spotLights[i], fragPosition, fragNormal);
        }
    }

    fragColor = ambientC * vec4(ambientLight, 1.0) + diffuseSpecularComp;
}