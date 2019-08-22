package org.aossie.starcross.renderer;

import android.content.res.Resources;

import org.aossie.starcross.R;
import org.aossie.starcross.renderer.util.SearchHelper;
import org.aossie.starcross.renderer.util.TextureManager;
import org.aossie.starcross.renderer.util.TextureReference;
import org.aossie.starcross.renderer.util.TexturedQuad;
import org.aossie.starcross.util.FixedPoint;
import org.aossie.starcross.util.MathUtil;
import org.aossie.starcross.util.Vector3;
import org.aossie.starcross.util.VectorUtil;

import javax.microedition.khronos.opengles.GL10;

public class SearchArrow {
    private final float ARROW_SIZE = 0.05f;
    private final float CIRCLE_SIZE = 0.2f;

    private float mTargetTheta = 0;
    private float mTargetPhi = 0;
    private TexturedQuad mArrowQuad = null;
    private float mArrowOffset = 0;
    private float mCircleSizeFactor = 0.01f;
    private float mArrowSizeFactor = 0.01f;
    private float mFullCircleScaleFactor = 0.01f;

    private TextureReference mArrowTex = null;

    public void reloadTextures(GL10 gl, Resources res, TextureManager textureManager) {
        gl.glEnable(GL10.GL_TEXTURE_2D);

        mArrowTex = textureManager.getTextureFromResource(gl, R.drawable.arrow);

        gl.glDisable(GL10.GL_TEXTURE_2D);
    }

    public void resize(GL10 gl, int screenWidth, int screenHeight, float fullCircleSize) {
        mArrowSizeFactor = ARROW_SIZE * Math.min(screenWidth, screenHeight);
        mArrowQuad = new TexturedQuad(mArrowTex,
                0, 0, 0,
                0.5f, 0, 0,
                0, 0.5f, 0);

        mFullCircleScaleFactor = fullCircleSize;
        mCircleSizeFactor = CIRCLE_SIZE * mFullCircleScaleFactor;

        mArrowOffset = mCircleSizeFactor + mArrowSizeFactor;
    }

    public void draw(GL10 gl, Vector3 lookDir, Vector3 upDir, SearchHelper searchHelper,
                     boolean nightVisionMode) {
        float lookPhi = MathUtil.acos(lookDir.y);
        float lookTheta = MathUtil.atan2(lookDir.z, lookDir.x);

        // Positive diffPhi means you need to look up.
        float diffPhi = lookPhi - mTargetPhi;

        // Positive diffTheta means you need to look right.
        float diffTheta = lookTheta - mTargetTheta;

        if (diffTheta > MathUtil.PI) {
            diffTheta -= MathUtil.TWO_PI;
        } else if (diffTheta < -MathUtil.PI) {
            diffTheta += MathUtil.TWO_PI;
        }

        float angle = MathUtil.atan2(diffPhi, diffTheta);

        float roll = angleBetweenVectorsWithRespectToAxis(new Vector3(0, 1, 0), upDir, lookDir);

        angle += roll;

        float distance = 1.0f / (1.414f * MathUtil.PI) *
                MathUtil.sqrt(diffTheta * diffTheta + diffPhi * diffPhi);

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        gl.glPushMatrix();
        gl.glRotatef(angle * 180.0f / MathUtil.PI, 0, 0, -1);

        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_BLEND);

        // 0 means the circle is not expanded at all.  1 means fully expanded.
        float expandFactor = searchHelper.getTransitionFactor();

        if (expandFactor == 0) {
            gl.glColor4x(FixedPoint.ONE, FixedPoint.ONE, FixedPoint.ONE, FixedPoint.ONE);

            float redFactor, blueFactor;
            if (nightVisionMode) {
                redFactor = 0.6f;
                blueFactor = 0;
            } else {
                redFactor = 1.0f - distance;
                blueFactor = distance;
            }

            gl.glTexEnvfv(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_COLOR,
                    new float[]{redFactor, 0.0f, blueFactor, 0.0f}, 0);

            gl.glPushMatrix();
            float circleScale = mCircleSizeFactor;
            gl.glScalef(circleScale, circleScale, circleScale);
//      mCircleQuad.draw(gl);
            gl.glPopMatrix();

            gl.glPushMatrix();
            float arrowScale = mArrowSizeFactor;
            gl.glTranslatef(mArrowOffset * 0.5f, 0, 0);
            gl.glScalef(arrowScale, arrowScale, arrowScale);
            mArrowQuad.draw(gl);
            gl.glPopMatrix();
        } else {
            gl.glColor4x(FixedPoint.ONE, FixedPoint.ONE, FixedPoint.ONE,
                    FixedPoint.floatToFixedPoint(0.7f));

            gl.glTexEnvfv(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_COLOR,
                    new float[]{1, nightVisionMode ? 0 : 0.5f, 0, 0.0f}, 0);

            gl.glPushMatrix();
            float circleScale = mFullCircleScaleFactor * expandFactor +
                    mCircleSizeFactor * (1 - expandFactor);
            gl.glScalef(circleScale, circleScale, circleScale);
//      mCircleQuad.draw(gl);
            gl.glPopMatrix();
        }
        gl.glPopMatrix();

        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);

        gl.glDisable(GL10.GL_BLEND);
    }

    public void setTarget(Vector3 position) {
        position = VectorUtil.normalized(position);
        mTargetPhi = MathUtil.acos(position.y);
        mTargetTheta = MathUtil.atan2(position.z, position.x);
    }

    private static float angleBetweenVectorsWithRespectToAxis(Vector3 v1, Vector3 v2, Vector3 axis) {
        Vector3 v1proj = VectorUtil.difference(v1, VectorUtil.projectOntoUnit(v1, axis));
        v1proj = VectorUtil.normalized(v1proj);

        Vector3 perp = VectorUtil.crossProduct(axis, v1proj);

        float cosAngle = VectorUtil.dotProduct(v1proj, v2);
        float sinAngle = -VectorUtil.dotProduct(perp, v2);

        return MathUtil.atan2(sinAngle, cosAngle);
    }
}
