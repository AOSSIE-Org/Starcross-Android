package org.aossie.starcross.renderer;

import android.content.res.Resources;
import android.opengl.GLSurfaceView;

import org.aossie.starcross.renderer.util.SkyRegionMap;
import org.aossie.starcross.renderer.util.TextureManager;
import org.aossie.starcross.renderer.util.UpdateClosure;
import org.aossie.starcross.util.GeocentricCoordinates;
import org.aossie.starcross.util.Vector3;
import org.aossie.starcross.util.MathUtil;
import org.aossie.starcross.util.Matrix4x4;
import org.aossie.starcross.util.VectorUtil;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SkyRenderer implements GLSurfaceView.Renderer {

    private boolean mustUpdateView = true;
    private boolean mustUpdateProjection = true;
    private final TextureManager textureManager;
    private RenderState renderState = new RenderState();
    private Set<UpdateClosure> updateClosures = new TreeSet<>();
    private Set<RendererObjectManager> allManagers = new TreeSet<>();
    private TreeMap<Integer, Set<RendererObjectManager>> layersToManagersMap;
    private ArrayList<ManagerReloadData> managersToReload = new ArrayList<>();

    public SkyRenderer(Resources res) {
        renderState.setResources(res);
        layersToManagersMap = new TreeMap<>();
        textureManager = new TextureManager(res);
        SkyBox mSkyBox = new SkyBox(Integer.MIN_VALUE, textureManager);
        mSkyBox.enable(false);
        addObjectManager(mSkyBox);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glEnable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glDisable(GL10.GL_DEPTH_TEST);
        textureManager.reset();
        for (RendererObjectManager rom : allManagers)
            rom.reload(gl, true);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        renderState.setScreenSize(width, height);
        mustUpdateView = true;
        mustUpdateProjection = true;
        gl.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        for (ManagerReloadData data : managersToReload)
            data.manager.reload(gl, data.fullReload);
        managersToReload.clear();
        maybeUpdateMatrices(gl);
        renderState.setActiveSkyRegions(SkyRegionMap.getActiveRegions(renderState.getLookDir(), renderState.getRadiusOfView(),
                (float) renderState.getScreenWidth() / renderState.getScreenHeight()));

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        for (int layer : layersToManagersMap.keySet()) {
            Set<RendererObjectManager> managers = layersToManagersMap.get(layer);
            assert managers != null;
            for (RendererObjectManager rom : managers) {
                rom.draw(gl);
            }
        }

        for (UpdateClosure update : updateClosures)
            update.run();
    }

    void setRadiusOfView(float degrees) {
        renderState.setRadiusOfView(degrees);
        mustUpdateProjection = true;
    }

    void addUpdateClosure(UpdateClosure update) {
        updateClosures.add(update);
    }

    void addObjectManager(RendererObjectManager m) {
        m.setRenderState(renderState);
        allManagers.add(m);

        managersToReload.add(new ManagerReloadData(m, true));
        Set<RendererObjectManager> managers = layersToManagersMap.get(m.getLayer());
        if (managers == null) {
            managers = new TreeSet<>();
            layersToManagersMap.put(m.getLayer(), managers);
        }
        managers.add(m);
    }

    void setViewOrientation(float dirX, float dirY, float dirZ, float upX, float upY, float upZ) {
        float dirLen = MathUtil.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        float oneOverDirLen = 1.0f / dirLen;
        dirX *= oneOverDirLen;
        dirY *= oneOverDirLen;
        dirZ *= oneOverDirLen;

        float lookDotUp = dirX * upX + dirY * upY + dirZ * upZ;
        upX -= lookDotUp * dirX;
        upY -= lookDotUp * dirY;
        upZ -= lookDotUp * dirZ;

        float upLen = MathUtil.sqrt(upX * upX + upY * upY + upZ * upZ);
        float oneOverUpLen = 1.0f / upLen;
        upX *= oneOverUpLen;
        upY *= oneOverUpLen;
        upZ *= oneOverUpLen;

        renderState.setLookDir(new GeocentricCoordinates(dirX, dirY, dirZ));
        renderState.setUpDir(new GeocentricCoordinates(upX, upY, upZ));
        mustUpdateView = true;
    }

    private void updateView(GL10 gl) {
        Vector3 lookDir = renderState.getLookDir();
        Vector3 upDir = renderState.getUpDir();
        Vector3 right = VectorUtil.crossProduct(lookDir, upDir);
        Matrix4x4 mViewMatrix = Matrix4x4.createView(lookDir, upDir, right);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadMatrixf(mViewMatrix.getFloatArray(), 0);
    }

    private void updatePerspective(GL10 gl) {
        Matrix4x4 mProjectionMatrix = Matrix4x4.createPerspectiveProjection(renderState.getScreenWidth(),
                renderState.getScreenHeight(), renderState.getRadiusOfView() * 3.141593f / 360.0f);

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadMatrixf(mProjectionMatrix.getFloatArray(), 0);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

    private void maybeUpdateMatrices(GL10 gl) {
        if (mustUpdateView) {
            updateView(gl);
            mustUpdateView = false;
        }
        if (mustUpdateProjection) {
            updatePerspective(gl);
            mustUpdateProjection = false;
        }
    }

    PointObjectManager createPointManager(int layer) {
        return new PointObjectManager(layer, textureManager);
    }

}


