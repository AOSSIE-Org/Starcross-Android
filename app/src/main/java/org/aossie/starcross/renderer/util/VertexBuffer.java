package org.aossie.starcross.renderer.util;

import org.aossie.starcross.util.Vector3;
import org.aossie.starcross.util.FixedPoint;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class VertexBuffer {
    public VertexBuffer(boolean useVBO) {
        numVertices = 0;
        this.useVBO = useVBO;
    }

    public VertexBuffer(int numVertices) {
        this(numVertices, false);
    }

    public VertexBuffer(int numVertices, boolean useVBO) {
        this.useVBO = useVBO;
        reset(numVertices);
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

    public void addPoint(Vector3 p) {
        addPoint(p.x, p.y, p.z);
    }

    public void addPoint(float x, float y, float z) {
        positionBuffer.put(FixedPoint.floatToFixedPoint(x));
        positionBuffer.put(FixedPoint.floatToFixedPoint(y));
        positionBuffer.put(FixedPoint.floatToFixedPoint(z));
    }

    public void set(GL10 gl) {
        if (numVertices == 0) {
            return;
        }

        positionBuffer.position(0);

        if (useVBO && org.aossie.starcross.renderer.util.GLBuffer.canUseVBO()) {
            GL11 gl11 = (GL11) gl;
            GLBuffer.bind(gl11, positionBuffer, 4 * positionBuffer.capacity());
            gl11.glVertexPointer(3, GL10.GL_FIXED, 0, 0);
        } else {
            gl.glVertexPointer(3, GL10.GL_FIXED, 0, positionBuffer);
        }
    }

    private void regenerateBuffer() {
        if (numVertices == 0) {
            return;
        }

        ByteBuffer bb = ByteBuffer.allocateDirect(4 * 3 * numVertices);
        bb.order(ByteOrder.nativeOrder());
        IntBuffer ib = bb.asIntBuffer();
        ib.position(0);
        positionBuffer = ib;
    }

    private IntBuffer positionBuffer = null;
    private int numVertices;
    private GLBuffer GLBuffer = new GLBuffer(GL11.GL_ARRAY_BUFFER);
    private boolean useVBO;
}