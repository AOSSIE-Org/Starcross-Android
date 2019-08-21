package org.aossie.starcross.source.impl;

import org.aossie.starcross.source.data.PointSource;
import org.aossie.starcross.source.proto.SourceReader;
import org.aossie.starcross.util.GeocentricCoordinates;

public class PointSourceImpl extends AbstractSource implements PointSource {
    private final int size;
    private Shape pointShape;

    public PointSourceImpl(GeocentricCoordinates coords, int color, int size) {
        this(coords, color, size, Shape.CIRCLE);
    }

    public PointSourceImpl(GeocentricCoordinates coords, int color, int size, Shape pointShape) {
        super(coords, color);
        this.size = size;
        this.pointShape = pointShape;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Shape getPointShape() {
        return pointShape;
    }
}