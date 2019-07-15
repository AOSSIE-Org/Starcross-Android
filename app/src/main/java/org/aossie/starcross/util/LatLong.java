package org.aossie.starcross.util;

public class LatLong {
    private float latitude;
    private float longitude;

    public LatLong(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        // Silently enforce reasonable limits
        if (this.latitude > 90f) {
            this.latitude = 90f;
        }
        if (this.latitude < -90f) {
            this.latitude = -90f;
        }
        this.longitude = flooredMod(this.longitude + 180f, 360f) - 180f;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    /**
     * Returns the 'floored' mod assuming n>0.
     */
    private static float flooredMod(float a, float n) {
        return a < 0 ? (a % n + n) % n : a % n;
    }
}
