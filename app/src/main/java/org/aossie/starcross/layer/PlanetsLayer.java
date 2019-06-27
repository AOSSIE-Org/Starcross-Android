package org.aossie.starcross.layer;

import android.content.res.Resources;

import org.aossie.starcross.source.data.AstronomicalSource;

import java.util.ArrayList;

public class PlanetsLayer extends AbstractSourceLayer {

    public PlanetsLayer(Resources resources) {
        super(resources, true);
    }

    @Override
    protected void initializeAstroSources(ArrayList<AstronomicalSource> sources) {
        // TODO initialize planets
    }

    @Override
    public int getLayerDepthOrder() {
        return 60;
    }

}