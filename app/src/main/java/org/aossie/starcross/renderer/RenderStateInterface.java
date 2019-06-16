package org.aossie.starcross.renderer;

import android.content.res.Resources;

import org.aossie.starcross.renderer.util.SkyRegionMap;

interface RenderStateInterface {

    float getRadiusOfView();

    Resources getResources();

    boolean getNightVisionMode();

    SkyRegionMap.ActiveRegionData getActiveSkyRegions();
}