package org.aossie.starcross.source.impl;

import org.aossie.starcross.source.data.PointSource;
import org.aossie.starcross.util.GeocentricCoordinates;

public class PointSourceImpl extends AbstractSource implements PointSource {
    private final int size;

    public PointSourceImpl(GeocentricCoordinates coords, int size) {
        super(coords);
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
    }
}