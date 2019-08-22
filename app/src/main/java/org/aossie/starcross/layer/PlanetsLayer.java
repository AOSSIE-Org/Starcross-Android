package org.aossie.starcross.layer;

import android.content.SharedPreferences;
import android.content.res.Resources;

import org.aossie.starcross.control.AstronomerModel;
import org.aossie.starcross.source.Planet;
import org.aossie.starcross.source.PlanetSource;
import org.aossie.starcross.source.data.AstronomicalSource;

import java.util.ArrayList;

public class PlanetsLayer extends AbstractSourceLayer {
    private final AstronomerModel model;
    private final SharedPreferences preferences;

    public PlanetsLayer(AstronomerModel model, Resources resources, SharedPreferences preferences) {
        super(resources, true);
        this.preferences = preferences;
        this.model = model;
    }


    @Override
    protected void initializeAstroSources(ArrayList<AstronomicalSource> sources) {
        for (Planet planet : Planet.values()) {
            sources.add(new PlanetSource(planet, getResources(), model, preferences));
        }
    }

    @Override
    public int getLayerDepthOrder() {
        return 60;
    }

}