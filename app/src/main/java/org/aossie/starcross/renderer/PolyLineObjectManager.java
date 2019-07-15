package org.aossie.starcross.renderer;

import org.aossie.starcross.R;
import org.aossie.starcross.renderer.util.IndexBuffer;
import org.aossie.starcross.renderer.util.TexCoordBuffer;
import org.aossie.starcross.renderer.util.TextureManager;
import org.aossie.starcross.renderer.util.TextureReference;
import org.aossie.starcross.renderer.util.VertexBuffer;
import org.aossie.starcross.renderer.util.VisionColorBuffer;
import org.aossie.starcross.source.data.LineSource;
import org.aossie.starcross.util.GeocentricCoordinates;
import org.aossie.starcross.util.MathUtil;
import org.aossie.starcross.util.Vector3;
import org.aossie.starcross.util.VectorUtil;

import java.util.EnumSet;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class PolyLineObjectManager extends RendererObjectManager {
    private VertexBuffer mVertexBuffer = new VertexBuffer(true);
    private VisionColorBuffer mColorBuffer = new VisionColorBuffer(true);
    private TexCoordBuffer mTexCoordBuffer = new TexCoordBuffer(true);
    private IndexBuffer mIndexBuffer = new IndexBuffer(true);
    private TextureReference mTexRef = null;
    private boolean mOpaque = true;

    public PolyLineObjectManager(int layer, TextureManager textureManager) {
        super(layer, textureManager);
    }

    public void updateObjects(List<LineSource> lines, EnumSet<UpdateType> updateType) {
        if (!updateType.contains(UpdateType.Reset) &&
                !updateType.contains(UpdateType.UpdatePositions)) {
            return;
        }
        int numLineSegments = 0;
        for (LineSource l : lines) {
            numLineSegments += l.getVertices().size() - 1;
        }

        int numVertices = 4 * numLineSegments;
        int numIndices = 6 * numLineSegments;

        VertexBuffer vb = mVertexBuffer;
        vb.reset(4 * numLineSegments);
        VisionColorBuffer cb = mColorBuffer;
        cb.reset(4 * numLineSegments);
        TexCoordBuffer tb = mTexCoordBuffer;
        tb.reset(numVertices);
        IndexBuffer ib = mIndexBuffer;
        ib.reset(numIndices);

        // See comment in PointObjectManager for justification of this calculation.
        float fovyInRadians = 60 * MathUtil.PI / 180.0f;
        float sizeFactor = MathUtil.tan(fovyInRadians * 0.5f) / 480;

        boolean opaque = false;

        short vertexIndex = 0;
        for (LineSource l : lines) {
            List<GeocentricCoordinates> coords = l.getVertices();
            if (coords.size() < 2)
                continue;

            for (int i = 0; i < coords.size() - 1; i++) {
                Vector3 p1 = coords.get(i);
                Vector3 p2 = coords.get(i + 1);
                Vector3 u = VectorUtil.difference(p2, p1);
                Vector3 avg = VectorUtil.sum(p1, p2);
                avg.scale(0.5f);
                Vector3 v = VectorUtil.normalized(VectorUtil.crossProduct(u, avg));
                v.scale(sizeFactor * l.getLineWidth());

                vb.addPoint(VectorUtil.difference(p1, v));
                cb.addColor(1);
                tb.addTexCoords(0, 1);

                // Upper left corner
                vb.addPoint(VectorUtil.sum(p1, v));
                cb.addColor(1);
                tb.addTexCoords(0, 0);

                // Lower left corner
                vb.addPoint(VectorUtil.difference(p2, v));
                cb.addColor(1);
                tb.addTexCoords(1, 1);

                // Upper left corner
                vb.addPoint(VectorUtil.sum(p2, v));
                cb.addColor(1);
                tb.addTexCoords(1, 0);


                // Add the indices
                short bottomLeft = vertexIndex++;
                short topLeft = vertexIndex++;
                short bottomRight = vertexIndex++;
                short topRight = vertexIndex++;

                // First triangle
                ib.addIndex(bottomLeft);
                ib.addIndex(topLeft);
                ib.addIndex(bottomRight);

                // Second triangle
                ib.addIndex(bottomRight);
                ib.addIndex(topLeft);
                ib.addIndex(topRight);
            }
        }
        mOpaque = opaque;
    }

    @Override
    public void reload(GL10 gl, boolean fullReload) {
        mTexRef = textureManager().getTextureFromResource(gl, R.drawable.line);
        mVertexBuffer.reload();
        mColorBuffer.reload();
        mTexCoordBuffer.reload();
        mIndexBuffer.reload();
    }

    @Override
    protected void drawInternal(GL10 gl) {
        if (mIndexBuffer.size() == 0)
            return;

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        gl.glEnable(GL10.GL_TEXTURE_2D);
        mTexRef.bind(gl);

        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glFrontFace(GL10.GL_CW);
        gl.glCullFace(GL10.GL_BACK);

        if (!mOpaque) {
            gl.glEnable(GL10.GL_BLEND);
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        }

        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);

        mVertexBuffer.set(gl);
        mColorBuffer.set(gl);
        mTexCoordBuffer.set(gl);

        mIndexBuffer.draw(gl, GL10.GL_TRIANGLES);

        if (!mOpaque) {
            gl.glDisable(GL10.GL_BLEND);
        }

        gl.glDisable(GL10.GL_TEXTURE_2D);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }
}
