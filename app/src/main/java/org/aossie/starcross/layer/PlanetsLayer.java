package org.aossie.starcross.layer;

import android.content.res.Resources;

import org.aossie.starcross.control.AstronomerModel;
import org.aossie.starcross.ephemeris.Planet;
import org.aossie.starcross.ephemeris.PlanetSource;
import org.aossie.starcross.source.data.AstronomicalSource;

import java.util.ArrayList;

public class PlanetsLayer extends AbstractSourceLayer {
    private final AstronomerModel model;

    public PlanetsLayer(AstronomerModel model, Resources resources) {
        super(resources, true);
        this.model = model;
    }


    @Override
    protected void initializeAstroSources(ArrayList<AstronomicalSource> sources) {
        for (Planet planet : Planet.values()) {
            sources.add(new PlanetSource(planet, getResources(), model));
        }
    }

    @Override
    public int getLayerDepthOrder() {
        return 60;
    }

}