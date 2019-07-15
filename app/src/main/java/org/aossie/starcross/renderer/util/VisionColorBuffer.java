package org.aossie.starcross.renderer.util;

import javax.microedition.khronos.opengles.GL10;

public class VisionColorBuffer {
    private ColorBuffer normalBuffer;

    public VisionColorBuffer(boolean useVBO) {
        normalBuffer = new ColorBuffer(useVBO);
    }

    public void reset(int numVertices) {
        normalBuffer.reset(numVertices);
    }

    public void reload() {
        normalBuffer.reload();
    }

    public void set(GL10 gl) {
        normalBuffer.set(gl);
    }

    public void addColor() {
        normalBuffer.addColor();
    }

    public void addColor(int i) {
        normalBuffer.addColor(i, i);
    }
}