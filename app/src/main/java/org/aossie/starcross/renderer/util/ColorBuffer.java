package org.aossie.starcross.renderer.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class ColorBuffer {
    private boolean useVBO;
    private int numVertices;
    private IntBuffer colorBuffer = null;
    private GLBuffer GLBuffer = new GLBuffer(GL11.GL_ARRAY_BUFFER);

    public ColorBuffer(boolean useVBO) {
        numVertices = 0;
        this.useVBO = useVBO;
    }

    public int size() {
        return numVertices;
    }

    public void reset(int numVertices) {
        this.numVertices = numVertices;
        regenerateBuffer();
    }
    public void reload() {
        GLBuffer.reload();
    }

    public void addColor(int abgr) {
        colorBuffer.put(abgr);
    }

    public void addColor(int a, int r, int g, int b) {
        addColor(((a & 0xff) << 24) | ((b & 0xff) << 16) | ((g & 0xff) << 8) | (r & 0xff));
    }

    public void set(GL10 gl) {
        if (numVertices == 0) {
            return;
        }
        colorBuffer.position(0);

        if (useVBO && org.aossie.starcross.renderer.util.GLBuffer.canUseVBO()) {
            GL11 gl11 = (GL11) gl;
            GLBuffer.bind(gl11, colorBuffer, 4 * colorBuffer.capacity());
            gl11.glColorPointer(4, GL10.GL_UNSIGNED_BYTE, 0, 0);
        } else {
            gl.glColorPointer(4, GL10.GL_UNSIGNED_BYTE, 0, colorBuffer);
        }
    }

    private void regenerateBuffer() {
        if (numVertices == 0) {
            return;
        }

        ByteBuffer bb = ByteBuffer.allocateDirect(4 * numVertices);
        bb.order(ByteOrder.nativeOrder());
        IntBuffer ib = bb.asIntBuffer();
        ib.position(0);
        colorBuffer = ib;
    }
}