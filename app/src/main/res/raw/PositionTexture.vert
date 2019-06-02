precision lowp float;

attribute vec4 position;
attribute vec2 texureCoordinates;

uniform mat4 matrix;
uniform vec4 diffuseColor;

varying lowp vec4 fragmentColor;
varying lowp vec2 fragmentTextureCoordinates;

void main() {
    gl_Position = matrix * position;
    fragmentColor = diffuseColor;
    fragmentTextureCoordinates = texureCoordinates;
}
