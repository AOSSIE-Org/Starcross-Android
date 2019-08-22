package org.aossie.starcross.renderer;

import android.content.res.Resources;

import org.aossie.starcross.R;
import org.aossie.starcross.renderer.util.SearchHelper;
import org.aossie.starcross.renderer.util.TextureManager;
import org.aossie.starcross.renderer.util.TextureReference;
import org.aossie.starcross.renderer.util.TexturedQuad;
import org.aossie.starcross.util.MathUtil;
import org.aossie.starcross.util.Vector3;

import javax.microedition.khronos.opengles.GL10;

public class CrosshairOverlay {

    public void reloadTextures(GL10 gl, Resources res, TextureManager textureManager) {
        // Load the crosshair texture.
        mTex = textureManager.getTextureFromResource(gl, R.drawable.crosshair);
    }

    public void resize(GL10 gl, int screenWidth, int screenHeight) {
        mQuad = new TexturedQuad(mTex,
                0, 0, 0,
                40.0f / screenWidth, 0, 0,
                0, 40.0f / screenHeight, 0);
    }

    public void draw(GL10 gl, SearchHelper searchHelper, boolean nightVisionMode) {
        // Return if the label has a negative z.
        Vector3 position = searchHelper.getTransformedPosition();
        if (position.z < 0) {
            return;
        }

        gl.glPushMatrix();
        gl.glLoadIdentity();

        gl.glTranslatef(position.x, position.y, 0);

        int period = 1000;
        long time = System.currentTimeMillis();
        float intensity = 0.7f + 0.3f * MathUtil.sin((time % period) * MathUtil.TWO_PI / period);
        if (nightVisionMode) {
            gl.glColor4f(intensity, 0, 0, 0.7f);
        } else {
            gl.glColor4f(intensity, intensity, 0, 0.7f);
        }

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        mQuad.draw(gl);

        gl.glDisable(GL10.GL_BLEND);

        gl.glPopMatrix();
    }

    private TexturedQuad mQuad = null;
    private TextureReference mTex = null;
}
