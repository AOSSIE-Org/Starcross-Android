package org.aossie.starcross.layer;

import android.content.res.Resources;

import org.aossie.starcross.renderer.RendererObjectManager;
import org.aossie.starcross.renderer.util.AbstractUpdateClosure;
import org.aossie.starcross.source.data.AstronomicalSource;
import org.aossie.starcross.source.data.PointSource;
import org.aossie.starcross.source.data.Source;

import java.util.ArrayList;
import java.util.EnumSet;

public abstract class AbstractSourceLayer extends AbstractLayer {

    private final ArrayList<PointSource> pointSources = new ArrayList<>();
    private final ArrayList<AstronomicalSource> astroSources = new ArrayList<>();

    private final boolean shouldUpdate;
    private SourceUpdateClosure closure;

    AbstractSourceLayer(Resources resources, boolean shouldUpdate) {
        super(resources);
        this.shouldUpdate = shouldUpdate;
    }

    @Override
    public synchronized void initialize() {
        astroSources.clear();
        initializeAstroSources(astroSources);
        for (AstronomicalSource astroSource : astroSources) {
            Source sources = astroSource.initialize();
            pointSources.addAll(sources.getPoints());
        }
        updateLayerForControllerChange();
    }

    @Override
    protected void updateLayerForControllerChange() {
        refreshSources(EnumSet.of(RendererObjectManager.UpdateType.Reset));
        if (shouldUpdate) {
            if (closure == null) {
                closure = new SourceUpdateClosure(this);
            }
            addUpdateClosure(closure);
        }
    }

    protected abstract void initializeAstroSources(ArrayList<AstronomicalSource> sources);

    private void refreshSources() {
        refreshSources(EnumSet.noneOf(RendererObjectManager.UpdateType.class));
    }

    synchronized void refreshSources(EnumSet<RendererObjectManager.UpdateType> updateTypes) {
        for (AstronomicalSource astroSource : astroSources) {
            updateTypes.addAll(astroSource.update());
        }

        if (!updateTypes.isEmpty()) {
            redraw(updateTypes);
        }
    }

    private void redraw(EnumSet<RendererObjectManager.UpdateType> updateTypes) {
        super.redraw(pointSources, updateTypes);
    }

    public static class SourceUpdateClosure extends AbstractUpdateClosure {
        private final AbstractSourceLayer layer;

        SourceUpdateClosure(AbstractSourceLayer layer) {
            this.layer = layer;
        }

        @Override
        public void run() {
            layer.refreshSources();
        }
    }
}