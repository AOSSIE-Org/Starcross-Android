package org.aossie.starcross.renderer.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

public final class TextureManager {
    private final Resources res;
    private Map<Integer, TextureData> resourceIdToTextureMap = new HashMap<>();
    private ArrayList<TextureReferenceImpl> allTextures = new ArrayList<>();

    public TextureManager(Resources res) {
        this.res = res;
    }

    public TextureReference getTextureFromResource(GL10 gl, int resourceID) {
        TextureData texData = resourceIdToTextureMap.get(resourceID);
        if (texData != null) {
            texData.refCount++;
            return texData.ref;
        }

        TextureReferenceImpl tex = createTextureFromResource(gl, resourceID);

        TextureData data = new TextureData();
        data.ref = tex;
        data.refCount = 1;
        resourceIdToTextureMap.put(resourceID, data);

        return tex;
    }

    public void reset() {
        resourceIdToTextureMap.clear();
        for (TextureReferenceImpl ref : allTextures) {
            ref.invalidate();
        }
        allTextures.clear();
    }

    private static class TextureReferenceImpl implements TextureReference {
        TextureReferenceImpl(int id) {
            mTextureID = id;
        }

        public void bind(GL10 gl) {
            checkValid();
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID);
        }

        void invalidate() {
            mValid = false;
        }

        private void checkValid() {
            if (!mValid) {
                Log.e("TextureManager", "Setting invalidated texture ID: " + mTextureID);
                StringWriter writer = new StringWriter();
                new Throwable().printStackTrace(new PrintWriter(writer));
                Log.e("TextureManager", writer.toString());
            }
        }

        private int mTextureID;
        private boolean mValid = true;
    }

    private static class TextureData {
        TextureReferenceImpl ref = null;
        int refCount = 0;
    }

    private TextureReferenceImpl createTextureFromResource(GL10 gl, int resourceID) {
        TextureReferenceImpl tex = createTextureInternal(gl);
        Options opts = new Options();
        opts.inScaled = false;
        Bitmap bmp = BitmapFactory.decodeResource(res, resourceID, opts);
        tex.bind(gl);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);

        bmp.recycle();
        return tex;
    }

    private TextureReferenceImpl createTextureInternal(GL10 gl) {
        int[] texID = new int[1];
        gl.glGenTextures(1, texID, 0);
        TextureReferenceImpl tex = new TextureReferenceImpl(texID[0]);
        allTextures.add(tex);
        return tex;
    }
}