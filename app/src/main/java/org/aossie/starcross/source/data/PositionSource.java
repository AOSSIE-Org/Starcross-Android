package org.aossie.starcross.source.data;

import org.aossie.starcross.util.GeocentricCoordinates;

public interface PositionSource {
    GeocentricCoordinates getLocation();
}