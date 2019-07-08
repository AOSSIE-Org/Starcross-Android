package org.aossie.starcross.positioning;

import org.junit.Test;

import static org.junit.Assert.*;

public class LatLongTest {

    @Test
    public void distanceFrom() {
        LatLong first = new LatLong(0, 0);
        LatLong second = new LatLong(1, 1);
        int distance = (int) first.distanceFrom(second);
        assertEquals(1, distance, 0);
    }

    @Test
    public void getLatitude() {
        LatLong latLong = new LatLong(0, 0);
        float lat = latLong.getLatitude();
        assertEquals(0, lat, 0);
    }
}