package org.aossie.starcross.renderer.util;

import javax.microedition.khronos.opengles.GL10;

public class NightVisionColorBuffer {
    private ColorBuffer normalBuffer;
    private ColorBuffer mRedBuffer;

    public NightVisionColorBuffer(boolean useVBO) {
        normalBuffer = new ColorBuffer(useVBO);
        mRedBuffer = new ColorBuffer(useVBO);
    }

    public int size() {
        return normalBuffer.size();
    }

    public void reset(int numVertices) {
        normalBuffer.reset(numVertices);
        mRedBuffer.reset(numVertices);
    }

    public void reload() {
        normalBuffer.reload();
        mRedBuffer.reload();
    }

    public void set(GL10 gl, boolean nightVisionMode) {
        if (nightVisionMode) {
            mRedBuffer.set(gl);
        } else {
            normalBuffer.set(gl);
        }
    }

    public void addColor(int a, int r, int g, int b) {
        normalBuffer.addColor(a, r, g, b);
        // I tried luminance here first, but many objects we care a lot about weren't very noticable because they were
        // bluish.  An average gets a better result.
        int avg = (r + g + b) / 3;
        mRedBuffer.addColor(a, avg, 0, 0);
    }

    public void addColor(int abgr, boolean iswhite) {
        addColor(255, 255, 255, 255);
    }

    public void addColor(int abgr, boolean iswhite, boolean isopag) {
        addColor(40, 255, 255, 255);
    }
}