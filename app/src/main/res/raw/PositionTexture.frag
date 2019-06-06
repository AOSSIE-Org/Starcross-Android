precision lowp float;

varying vec4 fragmentColor;
varying vec2 fragmentTextureCoordinates;

uniform sampler2D texture0;

void main() {
    gl_FragColor = fragmentColor * texture2D(texture0, fragmentTextureCoordinates);
}
