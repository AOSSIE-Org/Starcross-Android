package org.aossie.starcross.search;

import androidx.annotation.NonNull;

import org.aossie.starcross.util.GeocentricCoordinates;

public class SearchResult {
    private GeocentricCoordinates coords;
    private String capitalizedName;

    public SearchResult(String capitalizedName, GeocentricCoordinates coords) {
        this.capitalizedName = capitalizedName;
        this.coords = coords;
    }

    @NonNull
    @Override
    public String toString() {
        return capitalizedName;
    }
}
