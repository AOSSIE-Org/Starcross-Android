// Copyright 2010 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.aossie.starcross.source;

import android.content.res.Resources;
import android.graphics.Color;

import org.aossie.starcross.control.AstronomerModel;
import org.aossie.starcross.renderer.RendererObjectManager;
import org.aossie.starcross.source.data.ImageSource;
import org.aossie.starcross.source.data.PointSource;
import org.aossie.starcross.source.data.Source;
import org.aossie.starcross.source.impl.ImageSourceImpl;
import org.aossie.starcross.source.impl.PointSourceImpl;
import org.aossie.starcross.util.GeocentricCoordinates;
import org.aossie.starcross.util.HeliocentricCoordinates;
import org.aossie.starcross.util.RaDec;
import org.aossie.starcross.util.Vector3;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

public class PlanetSource extends AbstractAstronomicalSource {
    private static final int PLANET_SIZE = 3;
    private static final int PLANET_COLOR = Color.argb(20, 129, 126, 246);
    private static final Vector3 UP = new Vector3(0.0f, 1.0f, 0.0f);

    private final ArrayList<PointSource> pointSources = new ArrayList<PointSource>();
    private final ArrayList<ImageSourceImpl> imageSources = new ArrayList<ImageSourceImpl>();
    private final Planet planet;
    private final Resources resources;
    private final AstronomerModel model;
    private final String name;
    private final GeocentricCoordinates currentCoords = new GeocentricCoordinates(0, 0, 0);
    private HeliocentricCoordinates sunCoords;
    private int imageId = -1;

    private long lastUpdateTimeMs = 0L;

    public PlanetSource(Planet planet, Resources resources,
                        AstronomerModel model) {

        this.planet = planet;
        this.resources = resources;
        this.model = model;
        this.name = resources.getString(planet.getNameResourceId());
    }

    private void updateCoords(Date time) {
        this.lastUpdateTimeMs = time.getTime();
        this.sunCoords = HeliocentricCoordinates.getInstance(Planet.Sun, time);
        this.currentCoords.updateFromRaDec(RaDec.getInstance(planet, time, sunCoords));
        for (ImageSourceImpl imageSource : imageSources) {
            imageSource.setUpVector(sunCoords);  // TODO(johntaylor): figure out why we do this.
        }
    }

    @Override
    public Source initialize() {
        Date time = model.getTime();
        updateCoords(time);
        this.imageId = planet.getImageResourceId(time);

        if (planet == Planet.Moon) {
            imageSources.add(new ImageSourceImpl(currentCoords, resources, imageId, sunCoords,
                    planet.getPlanetaryImageSize()));
        } else {
            imageSources.add(new ImageSourceImpl(currentCoords, resources, imageId, UP,
                    planet.getPlanetaryImageSize()));
        }

        return this;
    }

    @Override
    public EnumSet<RendererObjectManager.UpdateType> update() {
        EnumSet<RendererObjectManager.UpdateType> updates = EnumSet.noneOf(RendererObjectManager.UpdateType.class);

        Date modelTime = model.getTime();
        if (Math.abs(modelTime.getTime() - lastUpdateTimeMs) > planet.getUpdateFrequencyMs()) {
            updates.add(RendererObjectManager.UpdateType.UpdatePositions);
            // update location
            updateCoords(modelTime);

            // For moon only:
            if (planet == Planet.Moon && !imageSources.isEmpty()) {
                // Update up vector.
                imageSources.get(0).setUpVector(sunCoords);

                // update image:
                int newImageId = planet.getImageResourceId(modelTime);
                if (newImageId != imageId) {
                    imageId = newImageId;
                    imageSources.get(0).setImageId(imageId);
                    updates.add(RendererObjectManager.UpdateType.UpdateImages);
                }
            }
        }
        return updates;
    }

    @Override
    public List<? extends ImageSource> getImages() {
        return imageSources;
    }

    @Override
    public List<? extends PointSource> getPoints() {
        return pointSources;
    }
}
