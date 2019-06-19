package org.aossie.starcross.renderer.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class IndexBuffer {
    private ShortBuffer indexBuffer = null;
    private int numIndices;
    private GLBuffer GLBuffer = new GLBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER);
    private boolean useVbo;

    public IndexBuffer(boolean useVBO) {
        numIndices = 0;
        this.useVbo = useVBO;
    }

    public int size() {
        return numIndices;
    }

    public void reset(int numVertices) {
        numIndices = numVertices;
        regenerateBuffer();
    }

    public void reload() {
        GLBuffer.reload();
    }

    private void regenerateBuffer() {
        if (numIndices == 0) {
            return;
        }

        ByteBuffer bb = ByteBuffer.allocateDirect(2 * numIndices);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer ib = bb.asShortBuffer();
        ib.position(0);
        indexBuffer = ib;
    }

    public void addIndex(short index) {
        indexBuffer.put(index);
    }

    public void draw(GL10 gl, int primitiveType) {
        if (numIndices == 0) {
            return;
        }
        indexBuffer.position(0);
        if (useVbo && org.aossie.starcross.renderer.util.GLBuffer.canUseVBO()) {
            GL11 gl11 = (GL11) gl;
            GLBuffer.bind(gl11, indexBuffer, 2 * indexBuffer.capacity());
            gl11.glDrawElements(primitiveType, size(), GL10.GL_UNSIGNED_SHORT, 0);
            org.aossie.starcross.renderer.util.GLBuffer.unbind(gl11);
        } else {
            gl.glDrawElements(primitiveType, size(), GL10.GL_UNSIGNED_SHORT, indexBuffer);
        }
    }
}