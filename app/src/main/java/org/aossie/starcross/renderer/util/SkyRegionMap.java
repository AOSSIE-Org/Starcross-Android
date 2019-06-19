package org.aossie.starcross.renderer.util;

import android.util.Log;

import org.aossie.starcross.util.GeocentricCoordinates;
import org.aossie.starcross.util.MathUtil;
import org.aossie.starcross.util.VectorUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class SkyRegionMap<RegionRenderingData> {
    public static final int CATCHALL_REGION_ID = -1;

    public interface RegionDataFactory<RegionRenderingData> {
        RegionRenderingData construct();
    }

    public static class ActiveRegionData {
        private ActiveRegionData(float[] regionCenterDotProducts, float screenAngle,
                                 ArrayList<Integer> activeScreenRegions) {
            if (regionCenterDotProducts.length != REGION_CENTERS.length) {
                Log.e("SkyRegionMap", "Bad regionCenterDotProducts length: " +
                        regionCenterDotProducts.length + " vs " + REGION_CENTERS.length);
            }
            this.regionCenterDotProducts = regionCenterDotProducts;
            this.screenAngle = screenAngle;
            this.activeStandardRegions = activeScreenRegions;
        }

        private final float[] regionCenterDotProducts;
        private final float screenAngle;
        private final ArrayList<Integer> activeStandardRegions;

        private boolean regionIsActive(int region, float coverageAngle) {
            return regionCenterDotProducts[region] > MathUtil.cos(coverageAngle + screenAngle);
        }
    }

    public static class ObjectRegionData {
        int region = SkyRegionMap.CATCHALL_REGION_ID;
        float regionCenterDotProduct = -1;
    }

    private static final float REGION_COVERAGE_ANGLE_IN_RADIANS = 0.396023592f;
    private static final GeocentricCoordinates[] REGION_CENTERS = {
            new GeocentricCoordinates(-0.850649066269f, 0.525733930059f, -0.000001851469f),
            new GeocentricCoordinates(-0.934170971625f, 0.000004098751f, -0.356825719588f),
            new GeocentricCoordinates(0.577349931933f, 0.577346773818f, 0.577354100533f),
            new GeocentricCoordinates(0.577350600623f, -0.577350601554f, -0.577349603176f),
            new GeocentricCoordinates(-0.577354427427f, -0.577349954285f, 0.577346424572f),
            new GeocentricCoordinates(-0.577346098609f, 0.577353779227f, -0.577350928448f),
            new GeocentricCoordinates(-0.577349943109f, -0.577346729115f, -0.577354134060f),
            new GeocentricCoordinates(-0.577350598760f, 0.577350586653f, 0.577349620871f),
            new GeocentricCoordinates(0.577354458161f, 0.577349932864f, -0.577346415259f),
            new GeocentricCoordinates(0.577346091159f, -0.577353793196f, 0.577350921929f),
            new GeocentricCoordinates(-0.850652559660f, -0.525728277862f, -0.000004770234f),
            new GeocentricCoordinates(-0.934173742309f, 0.000002107583f, 0.356818466447f),
            new GeocentricCoordinates(0.525734450668f, 0.000000594184f, -0.850648744032f),
            new GeocentricCoordinates(0.000002468936f, -0.356819496490f, -0.934173349291f),
            new GeocentricCoordinates(0.525727798231f, -0.000004087575f, 0.850652855821f),
            new GeocentricCoordinates(-0.000002444722f, 0.356819517910f, 0.934173340909f),
            new GeocentricCoordinates(-0.525727787986f, 0.000004113652f, -0.850652862340f),
            new GeocentricCoordinates(0.000004847534f, 0.356824675575f, -0.934171371162f),
            new GeocentricCoordinates(-0.000004885718f, -0.850652267225f, 0.525728750974f),
            new GeocentricCoordinates(-0.356825215742f, -0.934171164408f, -0.000003995374f),
            new GeocentricCoordinates(0.000000767410f, 0.850649364293f, 0.525733447634f),
            new GeocentricCoordinates(0.356825180352f, 0.934171177447f, 0.000003952533f),
            new GeocentricCoordinates(-0.000000790693f, -0.850649344735f, -0.525733478367f),
            new GeocentricCoordinates(0.356818960048f, -0.934173554182f, -0.000001195818f),
            new GeocentricCoordinates(0.850652555004f, 0.525728284381f, 0.000004773028f),
            new GeocentricCoordinates(0.934170960449f, -0.000004090369f, 0.356825748459f),
            new GeocentricCoordinates(-0.525734410621f, -0.000000609085f, 0.850648769177f),
            new GeocentricCoordinates(-0.000004815869f, -0.356824668124f, 0.934171373956f),
            new GeocentricCoordinates(0.000004877336f, 0.850652255118f, -0.525728769600f),
            new GeocentricCoordinates(-0.356819001026f, 0.934173538350f, 0.000001183711f),
            new GeocentricCoordinates(0.850649050437f, -0.525733955204f, 0.000001879409f),
            new GeocentricCoordinates(0.934173759073f, -0.000002136454f, -0.356818422675f),
    };

    private float[] regionCoverageAngles = null;
    private Map<Integer, RegionRenderingData> regionData = new TreeMap<>();
    private RegionDataFactory<RegionRenderingData> regionDataFactory = null;

    public static ActiveRegionData getActiveRegions(GeocentricCoordinates lookDir, float fovyInDegrees,
                                                    float aspect) {
        float halfFovy = (fovyInDegrees * MathUtil.DEGREES_TO_RADIANS) / 2;
        float screenAngle = MathUtil.asin(
                MathUtil.sin(halfFovy) * MathUtil.sqrt(1 + aspect * aspect));
        float angleThreshold = screenAngle + REGION_COVERAGE_ANGLE_IN_RADIANS;
        float dotProductThreshold = MathUtil.cos(angleThreshold);
        float[] regionCenterDotProducts = new float[REGION_CENTERS.length];
        ArrayList<Integer> activeStandardRegions = new ArrayList<Integer>();
        for (int i = 0; i < REGION_CENTERS.length; i++) {
            float dotProduct = VectorUtil.dotProduct(lookDir, REGION_CENTERS[i]);
            regionCenterDotProducts[i] = dotProduct;
            if (dotProduct > dotProductThreshold) {
                activeStandardRegions.add(i);
            }
        }
        return new ActiveRegionData(regionCenterDotProducts, screenAngle, activeStandardRegions);
    }

    public static int getObjectRegion(GeocentricCoordinates position) {
        return getObjectRegionData(position).region;
    }

    private static ObjectRegionData getObjectRegionData(GeocentricCoordinates position) {
        ObjectRegionData data = new ObjectRegionData();
        for (int i = 0; i < REGION_CENTERS.length; i++) {
            float dotProduct = VectorUtil.dotProduct(REGION_CENTERS[i], position);
            if (dotProduct > data.regionCenterDotProduct) {
                data.regionCenterDotProduct = dotProduct;
                data.region = i;
            }
        }

        if (data.regionCenterDotProduct < MathUtil.cos(REGION_COVERAGE_ANGLE_IN_RADIANS)) {
            Log.e("ActiveSkyRegionData",
                    "Object put in region, but outside of coverage angle. " +
                            "Angle was " + MathUtil.acos(data.regionCenterDotProduct) + " vs " +
                            REGION_COVERAGE_ANGLE_IN_RADIANS + ". Region was " + data.region);
        }

        return data;
    }

    public void clear() {
        regionData.clear();
        regionCoverageAngles = null;
    }

    public void setRegionDataFactory(RegionDataFactory<RegionRenderingData> factory) {
        regionDataFactory = factory;
    }

    public RegionRenderingData getRegionData(int id) {
        RegionRenderingData data = regionData.get(id);
        if (data == null && regionDataFactory != null) {
            data = regionDataFactory.construct();
            regionData.put(id, data);
        }
        return data;
    }

    public ArrayList<RegionRenderingData> getDataForActiveRegions(ActiveRegionData regions) {
        ArrayList<RegionRenderingData> data = new ArrayList<>();

        RegionRenderingData catchallData = regionData.get(CATCHALL_REGION_ID);
        if (catchallData != null) {
            data.add(catchallData);
        }

        if (regionCoverageAngles == null) {
            for (int region : regions.activeStandardRegions) {
                RegionRenderingData regionData = this.regionData.get(region);
                if (regionData != null) {
                    data.add(regionData);
                }
            }
            return data;
        } else {
            for (int i = 0; i < REGION_CENTERS.length; i++) {
                if (regions.regionIsActive(i, regionCoverageAngles[i])) {
                    RegionRenderingData regionData = this.regionData.get(i);
                    if (regionData != null) {
                        data.add(regionData);
                    }
                }
            }
            return data;
        }
    }

    public Collection<RegionRenderingData> getDataForAllRegions() {
        return regionData.values();
    }
}