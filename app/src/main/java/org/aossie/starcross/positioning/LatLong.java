package org.aossie.starcross.positioning;

import org.aossie.starcross.util.GeocentricCoordinates;
import org.aossie.starcross.util.Geometry;
import org.aossie.starcross.util.MathUtil;

public class LatLong {
    private float latitude;
    private float longitude;

    private LatLong(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        if (this.latitude > 90f) {
            this.latitude = 90f;
        }
        if (this.latitude < -90f) {
            this.latitude = -90f;
        }
        this.longitude = flooredMod(this.longitude + 180f) - 180f;
    }

    public LatLong(double latitude, double longitude) {
        this((float) latitude, (float) longitude);
    }

    public float distanceFrom(LatLong other) {
        GeocentricCoordinates otherPnt = GeocentricCoordinates.getInstance(other.getLongitude(),
                other.getLatitude());
        GeocentricCoordinates thisPnt = GeocentricCoordinates.getInstance(this.getLongitude(),
                this.getLatitude());
        float cosTheta = Geometry.cosineSimilarity(thisPnt, otherPnt);
        return MathUtil.acos(cosTheta) * 180f / MathUtil.PI;
    }

    float getLatitude() {
        return latitude;
    }

    private float getLongitude() {
        return longitude;
    }

    private static float flooredMod(float a) {
        return a < 0 ? (a % (float) 360.0 + (float) 360.0) % (float) 360.0 : a % (float) 360.0;
    }
}
