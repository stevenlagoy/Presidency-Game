#version 400

in vec3 position;
in vec2 textureCoord;
in vec3 vertexNormal;

out vec2 fragTextureCoord;
out vec3 fragNormal;
out vec3 fragPosition;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat3 normalMatrix;

void main() {
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;

    fragNormal = normalize(normalMatrix * vertexNormal);
    fragPosition = worldPosition.xyz;
    fragTextureCoord = textureCoord;
}