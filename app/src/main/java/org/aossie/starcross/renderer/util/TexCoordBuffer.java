package org.aossie.starcross.renderer.util;

import android.util.Log;

import org.aossie.starcross.util.FixedPoint;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class TexCoordBuffer {
    public TexCoordBuffer(boolean useVBO) {
        numVertices = 0;
        this.useVBO = useVBO;
    }

    public void reset(int numVertices) {
        if (numVertices < 0) {
            Log.e("TexCoordBuffer", "reset attempting to set numVertices to " + numVertices);
            numVertices = 0;
        }
        this.numVertices = numVertices;
        regenerateBuffer();
    }

    public void reload() {
        GLBuffer.reload();
    }

    public void addTexCoords(float u, float v) {
        texCoordBuffer.put(FixedPoint.floatToFixedPoint(u));
        texCoordBuffer.put(FixedPoint.floatToFixedPoint(v));
    }

    public void set(GL10 gl) {
        if (numVertices == 0) {
            return;
        }
        texCoordBuffer.position(0);

        if (useVBO && org.aossie.starcross.renderer.util.GLBuffer.canUseVBO()) {
            GL11 gl11 = (GL11) gl;
            GLBuffer.bind(gl11, texCoordBuffer, 4 * texCoordBuffer.capacity());
            gl11.glTexCoordPointer(2, GL10.GL_FIXED, 0, 0);
        } else {
            gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, texCoordBuffer);
        }
    }

    private void regenerateBuffer() {
        if (numVertices == 0) {
            return;
        }

        ByteBuffer bb = ByteBuffer.allocateDirect(4 * 2 * numVertices);
        bb.order(ByteOrder.nativeOrder());
        IntBuffer ib = bb.asIntBuffer();
        ib.position(0);
        texCoordBuffer = ib;
    }

    private IntBuffer texCoordBuffer = null;
    private int numVertices;
    private GLBuffer GLBuffer = new GLBuffer(GL11.GL_ARRAY_BUFFER);
    private boolean useVBO;
}