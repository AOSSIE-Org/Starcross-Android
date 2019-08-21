package org.aossie.starcross.source.impl;

import android.graphics.Color;

import org.aossie.starcross.source.data.Colorable;
import org.aossie.starcross.source.data.PositionSource;
import org.aossie.starcross.util.GeocentricCoordinates;

import java.util.List;

public abstract class AbstractSource implements PositionSource, Colorable {
    private final int color;
    private final GeocentricCoordinates xyz;
    private List<String> names;

    @Deprecated
    AbstractSource() {
        this(GeocentricCoordinates.getInstance(0.0f, 0.0f), Color.BLACK);
    }

    protected AbstractSource(int color) {
        this(GeocentricCoordinates.getInstance(0.0f, 0.0f), color);
    }

    protected AbstractSource(GeocentricCoordinates coords, int color) {
        this.xyz = coords;
        this.color = color;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public GeocentricCoordinates getLocation() {
        return xyz;
    }
}