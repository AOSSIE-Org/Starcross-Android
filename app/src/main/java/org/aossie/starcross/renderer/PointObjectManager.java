package org.aossie.starcross.renderer;

import org.aossie.starcross.R;
import org.aossie.starcross.renderer.util.IndexBuffer;
import org.aossie.starcross.renderer.util.VisionColorBuffer;
import org.aossie.starcross.renderer.util.SkyRegionMap;
import org.aossie.starcross.renderer.util.TexCoordBuffer;
import org.aossie.starcross.renderer.util.TextureManager;
import org.aossie.starcross.renderer.util.TextureReference;
import org.aossie.starcross.renderer.util.VertexBuffer;
import org.aossie.starcross.source.data.PointSource;
import org.aossie.starcross.util.Vector3;
import org.aossie.starcross.util.MathUtil;
import org.aossie.starcross.util.VectorUtil;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class PointObjectManager extends RendererObjectManager {
    private static final int NUM_STARS_IN_TEXTURE = 2;
    private static final int MINIMUM_NUM_POINTS_FOR_REGIONS = 200;

    private class RegionData {
        List<PointSource> sources = new ArrayList<>();

        private VertexBuffer vertexBuffer = new VertexBuffer(true);
        private VisionColorBuffer colorBuffer = new VisionColorBuffer(true);
        private TexCoordBuffer texCoordBuffer = new TexCoordBuffer(true);
        private IndexBuffer indexBuffer = new IndexBuffer(true);
    }

    private static final boolean COMPUTE_REGIONS = true;
    private int numPoints = 0;

    private SkyRegionMap<RegionData> mSkyRegions = new SkyRegionMap<>();

    private TextureReference textureRef = null;

    PointObjectManager(int layer, TextureManager textureManager) {
        super(layer, textureManager);
        mSkyRegions.setRegionDataFactory(
                new SkyRegionMap.RegionDataFactory<RegionData>() {
                    public RegionData construct() {
                        return new RegionData();
                    }
                });
    }

    void updateObjects(List<PointSource> points, EnumSet<UpdateType> updateType) {
        if (updateType.contains(UpdateType.UpdatePositions)) {
            if (points.size() != numPoints) {
                return;
            }
        }

        numPoints = points.size();

        mSkyRegions.clear();

        if (COMPUTE_REGIONS) {
            for (PointSource point : points) {
                int region = points.size() < MINIMUM_NUM_POINTS_FOR_REGIONS
                        ? SkyRegionMap.CATCHALL_REGION_ID
                        : SkyRegionMap.getObjectRegion(point.getLocation());
                mSkyRegions.getRegionData(region).sources.add(point);
            }
        } else {
            mSkyRegions.getRegionData(SkyRegionMap.CATCHALL_REGION_ID).sources = points;
        }

        for (RegionData data : mSkyRegions.getDataForAllRegions()) {
            int numVertices = 4 * data.sources.size();
            int numIndices = 6 * data.sources.size();

            data.vertexBuffer.reset(numVertices);
            data.colorBuffer.reset(numVertices);
            data.texCoordBuffer.reset(numVertices);
            data.indexBuffer.reset(numIndices);

            Vector3 up = new Vector3(0, 1, 0);

            float fovyInRadians = 60 * MathUtil.PI / 180.0f;
            float sizeFactor = MathUtil.tan(fovyInRadians * 0.5f) / 480;

            Vector3 bottomLeftPos = new Vector3(0, 0, 0);
            Vector3 topLeftPos = new Vector3(0, 0, 0);
            Vector3 bottomRightPos = new Vector3(0, 0, 0);
            Vector3 topRightPos = new Vector3(0, 0, 0);

            Vector3 su = new Vector3(0, 0, 0);
            Vector3 sv = new Vector3(0, 0, 0);

            short index = 0;

            float starWidthInTexels = 1.0f / NUM_STARS_IN_TEXTURE;

            for (PointSource p : data.sources) {
                short bottomLeft = index++;
                short topLeft = index++;
                short bottomRight = index++;
                short topRight = index++;

                // First triangle
                data.indexBuffer.addIndex(bottomLeft);
                data.indexBuffer.addIndex(topLeft);
                data.indexBuffer.addIndex(bottomRight);

                // Second triangle
                data.indexBuffer.addIndex(topRight);
                data.indexBuffer.addIndex(bottomRight);
                data.indexBuffer.addIndex(topLeft);

                int starIndex = 1;

                float texOffsetU = starWidthInTexels * starIndex;

                data.texCoordBuffer.addTexCoords(texOffsetU, 1);
                data.texCoordBuffer.addTexCoords(texOffsetU, 0);
                data.texCoordBuffer.addTexCoords(texOffsetU + starWidthInTexels, 1);
                data.texCoordBuffer.addTexCoords(texOffsetU + starWidthInTexels, 0);

                Vector3 pos = p.getLocation();
                Vector3 u = VectorUtil.normalized(VectorUtil.crossProduct(pos, up));
                Vector3 v = VectorUtil.crossProduct(u, pos);

                float s = p.getSize() * sizeFactor;

                su.assign(s * u.x, s * u.y, s * u.z);
                sv.assign(s * v.x, s * v.y, s * v.z);

                bottomLeftPos.assign(pos.x - su.x - sv.x, pos.y - su.y - sv.y, pos.z - su.z - sv.z);
                topLeftPos.assign(pos.x - su.x + sv.x, pos.y - su.y + sv.y, pos.z - su.z + sv.z);
                bottomRightPos.assign(pos.x + su.x - sv.x, pos.y + su.y - sv.y, pos.z + su.z - sv.z);
                topRightPos.assign(pos.x + su.x + sv.x, pos.y + su.y + sv.y, pos.z + su.z + sv.z);

                // Add the vertices
                data.vertexBuffer.addPoint(bottomLeftPos);
                data.colorBuffer.addColor();

                data.vertexBuffer.addPoint(topLeftPos);
                data.colorBuffer.addColor();

                data.vertexBuffer.addPoint(bottomRightPos);
                data.colorBuffer.addColor();

                data.vertexBuffer.addPoint(topRightPos);
                data.colorBuffer.addColor();
            }
            data.sources = null;
        }
    }

    @Override
    public void reload(GL10 gl, boolean fullReload) {
        textureRef = textureManager().getTextureFromResource(gl, R.drawable.stars_texture);
        for (RegionData data : mSkyRegions.getDataForAllRegions()) {
            data.vertexBuffer.reload();
            data.colorBuffer.reload();
            data.texCoordBuffer.reload();
            data.indexBuffer.reload();
        }
    }

    @Override
    protected void drawInternal(GL10 gl) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glFrontFace(GL10.GL_CW);
        gl.glCullFace(GL10.GL_BACK);

        gl.glEnable(GL10.GL_ALPHA_TEST);
        gl.glAlphaFunc(GL10.GL_GREATER, 0.5f);

        gl.glEnable(GL10.GL_TEXTURE_2D);

        textureRef.bind(gl);

        gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);

        SkyRegionMap.ActiveRegionData activeRegions = getRenderState().getActiveSkyRegions();
        ArrayList<RegionData> activeRegionData = mSkyRegions.getDataForActiveRegions(activeRegions);
        for (RegionData data : activeRegionData) {
            if (data.vertexBuffer.size() == 0) {
                continue;
            }

            data.vertexBuffer.set(gl);
            data.colorBuffer.set(gl);
            data.texCoordBuffer.set(gl);
            data.indexBuffer.draw(gl, GL10.GL_TRIANGLES);
        }

        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);
        gl.glDisable(GL10.GL_ALPHA_TEST);
    }
}