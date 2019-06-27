package org.aossie.starcross.source.data;

import org.aossie.starcross.util.GeocentricCoordinates;

import java.util.List;

public interface LineSource {
    public float getLineWidth();

    public List<GeocentricCoordinates> getVertices();
}
