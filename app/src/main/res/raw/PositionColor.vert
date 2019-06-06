precision lowp float;

attribute vec4 position;
attribute vec4 color;

uniform mat4 matrix;
uniform vec4 diffuseColor;

varying lowp vec4 fragmentColor;

void main() {
    gl_Position   = matrix * position;
    fragmentColor = color * diffuseColor;
}
