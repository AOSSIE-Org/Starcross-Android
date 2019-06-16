package org.aossie.starcross.source;

import org.aossie.starcross.util.GeocentricCoordinates;

public interface PositionSource {
    GeocentricCoordinates getLocation();
}