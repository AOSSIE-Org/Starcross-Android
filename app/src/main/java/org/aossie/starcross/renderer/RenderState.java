package org.aossie.starcross.renderer;

import android.content.res.Resources;

import org.aossie.starcross.renderer.util.SkyRegionMap;
import org.aossie.starcross.util.GeocentricCoordinates;

class RenderState implements RenderStateInterface {
    GeocentricCoordinates getLookDir() {
        return lookDir;
    }

    GeocentricCoordinates getUpDir() {
        return upDir;
    }

    public float getRadiusOfView() {
        return radiusOfView;
    }

    int getScreenWidth() {
        return screenWidth;
    }

    int getScreenHeight() {
        return screenHeight;
    }

    public Resources getResources() {
        return res;
    }

    public boolean getNightVisionMode() {
        return false;
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

    void setScreenSize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    public void setResources(Resources res) {
        this.res = res;
    }

    void setActiveSkyRegions(SkyRegionMap.ActiveRegionData set) {
        activeSkyRegionSet = set;
    }

    private GeocentricCoordinates lookDir = new GeocentricCoordinates(1, 0, 0);
    private GeocentricCoordinates upDir = new GeocentricCoordinates(0, 1, 0);
    private float radiusOfView = 45;
    private int screenWidth = 100;
    private int screenHeight = 100;
    private Resources res;
    private SkyRegionMap.ActiveRegionData activeSkyRegionSet = null;
}
