package org.aossie.starcross.source;

import org.aossie.starcross.renderer.RendererObjectManager;
import org.aossie.starcross.source.data.AstronomicalSource;
import org.aossie.starcross.source.data.ImageSource;
import org.aossie.starcross.source.data.LabelSource;
import org.aossie.starcross.source.data.LineSource;
import org.aossie.starcross.source.data.PointSource;
import org.aossie.starcross.source.data.Source;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public abstract class AbstractAstronomicalSource implements AstronomicalSource, Source {
    @Override
    public Source initialize() {
        return this;
    }

    @Override
    public EnumSet<RendererObjectManager.UpdateType> update() {
        return EnumSet.noneOf(RendererObjectManager.UpdateType.class);
    }

    @Override
    public List<? extends PointSource> getPoints() {
        return Collections.emptyList();
    }

    @Override
    public List<? extends LineSource> getLines() {
        return Collections.emptyList();
    }

    @Override
    public List<? extends ImageSource> getImages() {
        return Collections.emptyList();
    }
}