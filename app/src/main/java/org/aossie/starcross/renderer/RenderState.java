package org.aossie.starcross.renderer;

import android.content.res.Resources;

import org.aossie.starcross.renderer.util.SkyRegionMap;
import org.aossie.starcross.util.GeocentricCoordinates;
import org.aossie.starcross.util.Matrix4x4;

class RenderState implements RenderStateInterface {
    public GeocentricCoordinates getLookDir() {
        return lookDir;
    }

    public  GeocentricCoordinates getUpDir() {
        return upDir;
    }

    public float getRadiusOfView() {
        return radiusOfView;
    }

    public float getUpAngle() { return mUpAngle; }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public Matrix4x4 getTransformToDeviceMatrix() { return mTransformToDevice; }
    public Matrix4x4 getTransformToScreenMatrix() { return mTransformToScreen; }

    public Resources getResources() {
        return res;
    }

    public boolean getNightVisionMode() {
        return mNightVisionMode;
    }

    public SkyRegionMap.ActiveRegionData getActiveSkyRegions() {
        return activeSkyRegionSet;
    }

    void setLookDir(GeocentricCoordinates dir) {
        lookDir = dir.copy();
    }

    void setUpDir(GeocentricCoordinates dir) {
        upDir = dir.copy();
    }

    void setRadiusOfView(float radius) {
        radiusOfView = radius;
    }

    public void setUpAngle(float angle) {
        mUpAngle = angle;
    }

    void setScreenSize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    public void setTransformationMatrices(Matrix4x4 transformToDevice,
                                          Matrix4x4 transformToScreen) {
        mTransformToDevice = transformToDevice;
        mTransformToScreen = transformToScreen;
    }

    public void setResources(Resources res) {
        this.res = res;
    }

    public void setNightVisionMode(boolean enabled) { mNightVisionMode = enabled; }

    void setActiveSkyRegions(SkyRegionMap.ActiveRegionData set) {
        activeSkyRegionSet = set;
    }

    private GeocentricCoordinates lookDir = new GeocentricCoordinates(1, 0, 0);
    private GeocentricCoordinates upDir = new GeocentricCoordinates(0, 1, 0);
    private float radiusOfView = 45;
    private float mUpAngle = 0;
    private int screenWidth = 100;
    private int screenHeight = 100;
    private Matrix4x4 mTransformToDevice = Matrix4x4.createIdentity();
    private Matrix4x4 mTransformToScreen = Matrix4x4.createIdentity();
    private Resources res;
    private boolean mNightVisionMode = false;
    private SkyRegionMap.ActiveRegionData activeSkyRegionSet = null;
}
