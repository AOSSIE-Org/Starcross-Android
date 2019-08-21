package org.aossie.starcross.source.data;

import org.aossie.starcross.renderer.RendererObjectManager;
import org.aossie.starcross.util.GeocentricCoordinates;

import java.util.EnumSet;
import java.util.List;

public interface AstronomicalSource {

    List<String> getNames();

    GeocentricCoordinates getSearchLocation();

    Source initialize();

    EnumSet<RendererObjectManager.UpdateType> update();
}