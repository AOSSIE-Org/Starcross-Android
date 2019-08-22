package org.aossie.starcross.search;

import org.aossie.starcross.util.GeocentricCoordinates;

public class SearchResult {
    public GeocentricCoordinates coords;
    public String capitalizedName;

    public SearchResult(String capitalizedName, GeocentricCoordinates coords) {
        this.capitalizedName = capitalizedName;
        this.coords = coords;
    }

    @Override
    public String toString() {
        return capitalizedName;
    }
}
