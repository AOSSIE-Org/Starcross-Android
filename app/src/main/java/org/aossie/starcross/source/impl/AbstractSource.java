package org.aossie.starcross.source.impl;

import org.aossie.starcross.source.data.PositionSource;
import org.aossie.starcross.util.GeocentricCoordinates;

public abstract class AbstractSource implements PositionSource {
    private final GeocentricCoordinates xyz;

    @Deprecated
    AbstractSource() {
        this(GeocentricCoordinates.getInstance(0.0f, 0.0f));
    }

    AbstractSource(GeocentricCoordinates coords) {
        this.xyz = coords;
    }

    @Override
    public GeocentricCoordinates getLocation() {
        return xyz;
    }
}