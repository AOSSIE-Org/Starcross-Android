package org.aossie.starcross.renderer;

import android.util.Log;

import org.aossie.starcross.renderer.util.ColorBuffer;
import org.aossie.starcross.renderer.util.IndexBuffer;
import org.aossie.starcross.renderer.util.TextureManager;
import org.aossie.starcross.renderer.util.VertexBuffer;
import org.aossie.starcross.util.GeocentricCoordinates;
import org.aossie.starcross.util.MathUtil;
import org.aossie.starcross.util.Vector3;
import org.aossie.starcross.util.VectorUtil;

import javax.microedition.khronos.opengles.GL10;

public class SkyBox extends RendererObjectManager {

    private static final float EPSILON = 1e-3f;
    private static final short NUM_VERTEX_BANDS = 8;
    private static final short NUM_STEPS_IN_BAND = 10;
    private VertexBuffer vertexBuffer = new VertexBuffer(true);
    private ColorBuffer colorBuffer = new ColorBuffer(true);
    private IndexBuffer indexBuffer = new IndexBuffer(true);
    GeocentricCoordinates mSunPos = new GeocentricCoordinates(0, 1, 0);

    SkyBox(int layer, TextureManager textureManager) {
        super(layer, textureManager);

        int numVertices = NUM_VERTEX_BANDS * NUM_STEPS_IN_BAND;
        int numIndices = (NUM_VERTEX_BANDS - 1) * NUM_STEPS_IN_BAND * 6;
        vertexBuffer.reset(numVertices);
        colorBuffer.reset(numVertices);
        indexBuffer.reset(numIndices);

        float[] sinAngles = new float[NUM_STEPS_IN_BAND];
        float[] cosAngles = new float[NUM_STEPS_IN_BAND];

        float angleInBand = 0;
        float dAngle = MathUtil.TWO_PI / (NUM_STEPS_IN_BAND - 1);
        for (int i = 0; i < NUM_STEPS_IN_BAND; i++) {
            sinAngles[i] = MathUtil.sin(angleInBand);
            cosAngles[i] = MathUtil.cos(angleInBand);
            angleInBand += dAngle;
        }

        float bandStep = 2.0f / (NUM_VERTEX_BANDS - 1) + EPSILON;

        VertexBuffer vb = vertexBuffer;
        ColorBuffer cb = colorBuffer;
        float bandPos = 1;
        for (int band = 0; band < NUM_VERTEX_BANDS; band++, bandPos -= bandStep) {
            int color;
            if (bandPos > 0) {
                byte intensity = (byte) (bandPos * 20 + 50);
                color = (intensity << 16) | 0xff000000;
            } else {
                byte intensity = (byte) (bandPos * 40 + 40);
                color = (intensity << 16) | (intensity << 8) | intensity | 0xff000000;
            }

            float sinPhi = bandPos > -1 ? MathUtil.sqrt(1 - bandPos * bandPos) : 0;
            for (int i = 0; i < NUM_STEPS_IN_BAND; i++) {
                vb.addPoint(cosAngles[i] * sinPhi, bandPos, sinAngles[i] * sinPhi);
                cb.addColor(color);
            }
        }
        Log.d("SkyBox", "Vertices: " + vb.size());

        IndexBuffer ib = indexBuffer;

        // Set the indices for the first band.
        short topBandStart = 0;
        short bottomBandStart = NUM_STEPS_IN_BAND;
        for (short triangleBand = 0; triangleBand < NUM_VERTEX_BANDS - 1; triangleBand++) {
            for (short offsetFromStart = 0; offsetFromStart < NUM_STEPS_IN_BAND - 1; offsetFromStart++) {
                // Draw one quad as two triangles.
                short topLeft = (short) (topBandStart + offsetFromStart);
                short topRight = (short) (topLeft + 1);

                short bottomLeft = (short) (bottomBandStart + offsetFromStart);
                short bottomRight = (short) (bottomLeft + 1);

                // First triangle
                ib.addIndex(topLeft);
                ib.addIndex(bottomRight);
                ib.addIndex(bottomLeft);

                // Second triangle
                ib.addIndex(topRight);
                ib.addIndex(bottomRight);
                ib.addIndex(topLeft);
            }

            // Top left, bottom right, bottom left
            ib.addIndex((short) (topBandStart + NUM_STEPS_IN_BAND - 1));
            ib.addIndex(bottomBandStart);
            ib.addIndex((short) (bottomBandStart + NUM_STEPS_IN_BAND - 1));

            // Top right, bottom right, top left
            ib.addIndex(topBandStart);
            ib.addIndex(bottomBandStart);
            ib.addIndex((short) (topBandStart + NUM_STEPS_IN_BAND - 1));


            topBandStart += NUM_STEPS_IN_BAND;
            bottomBandStart += NUM_STEPS_IN_BAND;
        }
        Log.d("SkyBox", "Indices: " + ib.size());
    }

    @Override
    public void reload(GL10 gl, boolean fullReload) {
        vertexBuffer.reload();
        colorBuffer.reload();
        indexBuffer.reload();
    }

    public void setSunPosition(GeocentricCoordinates pos) {
        mSunPos = pos.copy();
        //Log.d("SkyBox", "SunPos: " + pos.toString());
    }

    @Override
    protected void drawInternal(GL10 gl) {
        if (getRenderState().getNightVisionMode()) {
            return;
        }

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glFrontFace(GL10.GL_CW);
        gl.glCullFace(GL10.GL_BACK);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glPushMatrix();

        Vector3 cp = VectorUtil.crossProduct(new Vector3(0, 1, 0), mSunPos);
        cp = VectorUtil.normalized(cp);
        float angle = 180.0f / MathUtil.PI * MathUtil.acos(mSunPos.y);
        gl.glRotatef(angle, cp.x, cp.y, cp.z);

        vertexBuffer.set(gl);
        colorBuffer.set(gl);
        indexBuffer.draw(gl, GL10.GL_TRIANGLES);
        gl.glPopMatrix();
    }
}