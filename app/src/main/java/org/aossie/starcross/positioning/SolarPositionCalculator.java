package org.aossie.starcross.positioning;

import java.util.Date;

public class SolarPositionCalculator {
    public static RaDec getSolarPosition(Date time) {
        HeliocentricCoordinates sunCoordinates = HeliocentricCoordinates.getInstance(null);
//    RaDec raDec = RaDec.getInstance(sunCoordinates);
        RaDec raDec = RaDec.getInstance(null); //TODO
        return raDec;
    }
}
