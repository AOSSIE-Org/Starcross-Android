package org.aossie.starcross.source;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;

import org.aossie.starcross.control.AstronomerModel;
import org.aossie.starcross.renderer.RendererObjectManager;
import org.aossie.starcross.source.data.ImageSource;
import org.aossie.starcross.source.data.PointSource;
import org.aossie.starcross.source.data.Source;
import org.aossie.starcross.source.data.TextSource;
import org.aossie.starcross.source.impl.ImageSourceImpl;
import org.aossie.starcross.source.impl.PointSourceImpl;
import org.aossie.starcross.source.impl.TextSourceImpl;
import org.aossie.starcross.util.GeocentricCoordinates;
import org.aossie.starcross.util.HeliocentricCoordinates;
import org.aossie.starcross.util.RaDec;
import org.aossie.starcross.util.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

public class PlanetSource extends AbstractAstronomicalSource {
    private static final int PLANET_SIZE = 3;
    private static final int PLANET_COLOR = Color.argb(20, 129, 126, 246);
    private static final int PLANET_LABEL_COLOR = 0xf67e81;
    private static final String SHOW_PLANETARY_IMAGES = "show_planetary_images";
    private static final Vector3 UP = new Vector3(0.0f, 1.0f, 0.0f);

    private final ArrayList<PointSource> pointSources = new ArrayList<PointSource>();
    private final ArrayList<TextSource> labelSources = new ArrayList<TextSource>();
    private final ArrayList<ImageSourceImpl> imageSources = new ArrayList<ImageSourceImpl>();
    private final Planet planet;
    private final Resources resources;
    private final AstronomerModel model;
    private final String name;
    private final SharedPreferences preferences;
    private final GeocentricCoordinates currentCoords = new GeocentricCoordinates(0, 0, 0);
    private HeliocentricCoordinates sunCoords;
    private int imageId = -1;

    private long lastUpdateTimeMs = 0L;

    public PlanetSource(Planet planet, Resources resources,
                        AstronomerModel model, SharedPreferences prefs) {

        this.planet = planet;
        this.resources = resources;
        this.model = model;
        this.name = resources.getString(planet.getNameResourceId());
        this.preferences = prefs;
    }

    @Override
    public List<String> getNames() {
        return  Arrays.asList(name);
    }

    @Override
    public GeocentricCoordinates getSearchLocation() {
        return currentCoords;
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
            boolean usePlanetaryImages = preferences.getBoolean(SHOW_PLANETARY_IMAGES, true);
            if (usePlanetaryImages || planet == Planet.Sun) {
                imageSources.add(new ImageSourceImpl(currentCoords, resources, imageId, UP,
                        planet.getPlanetaryImageSize()));
            } else {
                pointSources.add(new PointSourceImpl(currentCoords, PLANET_COLOR, PLANET_SIZE));
            }
        }
        labelSources.add(new TextSourceImpl(currentCoords, name, PLANET_LABEL_COLOR));

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
    public List<? extends TextSource> getLabels() {
        return labelSources;
    }

    @Override
    public List<? extends PointSource> getPoints() {
        return pointSources;
    }
}
