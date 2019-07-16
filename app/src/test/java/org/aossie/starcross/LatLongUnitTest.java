package org.aossie.starcross;

import org.aossie.starcross.util.LatLong;
import org.junit.Test;

import static org.junit.Assert.*;

public class LatLongUnitTest {
    @Test
    public void getLatitude() {
        LatLong latLong = new LatLong(0, 0);
        float lat = latLong.getLatitude();
        assertEquals(0, lat, 0);
    }
}