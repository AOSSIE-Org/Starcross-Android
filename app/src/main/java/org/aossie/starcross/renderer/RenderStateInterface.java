package org.aossie.starcross.renderer;

import android.content.res.Resources;

import org.aossie.starcross.renderer.util.SkyRegionMap;
import org.aossie.starcross.util.GeocentricCoordinates;
import org.aossie.starcross.util.Matrix4x4;

interface RenderStateInterface {
    public GeocentricCoordinates getLookDir();
    public GeocentricCoordinates getUpDir();

    float getRadiusOfView();

    public float getUpAngle();
    public int getScreenWidth();
    public int getScreenHeight();
    public Matrix4x4 getTransformToDeviceMatrix();
    public Matrix4x4 getTransformToScreenMatrix();

    Resources getResources();

    boolean getNightVisionMode();

    SkyRegionMap.ActiveRegionData getActiveSkyRegions();
}