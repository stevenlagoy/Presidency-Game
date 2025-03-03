#version 400 core

in vec3 position;
in vec2 textureCoord;
in vec3 normal;

out vec2 fragTextureCoord;
out vec3 fragNormal;
out vec3 fragPosition;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main() {
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    fragNormal = normalize(worldPosition).xyz;
    fragPosition = worldPosition.xyz;
    fragTextureCoord = textureCoord;
}