package org.aossie.starcross.renderer.util;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.Buffer;

import javax.microedition.khronos.opengles.GL11;

public class GLBuffer {
    private Buffer buffer = null;
    private int bufferSize = 0;
    private int GLBufferID = -1;
    private int bufferType;
    private boolean hasLoggedStackTraceOnError = false;

    GLBuffer(int bufferType) {
        this.bufferType = bufferType;
    }

    static boolean canUseVBO() {
        return false;
    }

    static void unbind(GL11 gl) {
        if (canUseVBO()) {
            gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
            gl.glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, 0);
        }
    }

    void bind(GL11 gl, Buffer buffer, int bufferSize) {
        if (canUseVBO()) {
            maybeRegenerateBuffer(gl, buffer, bufferSize);
            gl.glBindBuffer(bufferType, GLBufferID);
        } else {
            Log.e("GLBuffer", "Trying to use a VBO, but they are unsupported");
            if (!hasLoggedStackTraceOnError) {
                StringWriter writer = new StringWriter();
                new Throwable().printStackTrace(new PrintWriter(writer));
                Log.e("SkyRenderer", writer.toString());
                hasLoggedStackTraceOnError = true;
            }
        }
    }

    public void reload() {
        buffer = null;
        bufferSize = 0;
        GLBufferID = -1;
    }

    private void maybeRegenerateBuffer(GL11 gl, Buffer buffer, int bufferSize) {
        if (buffer != this.buffer || bufferSize != this.bufferSize) {
            this.buffer = buffer;
            this.bufferSize = bufferSize;

            if (GLBufferID == -1) {
                int[] buffers = new int[1];
                gl.glGenBuffers(1, buffers, 0);
                GLBufferID = buffers[0];
            }

            gl.glBindBuffer(bufferType, GLBufferID);
            gl.glBufferData(bufferType, bufferSize, buffer, GL11.GL_STATIC_DRAW);
        }
    }
}