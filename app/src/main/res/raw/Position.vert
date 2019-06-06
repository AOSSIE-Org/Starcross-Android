precision lowp float;

attribute vec4 position;

uniform mat4 matrix;
uniform vec4 diffuseColor;

varying lowp vec4 fragmentColor;

void main() {
    gl_Position   = matrix * position;
    fragmentColor = diffuseColor;
}
