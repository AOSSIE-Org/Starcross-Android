package org.aossie.starcross.layer;

import android.content.res.Resources;
import android.util.Log;

import org.aossie.starcross.renderer.RendererObjectManager;
import org.aossie.starcross.renderer.util.AbstractUpdateClosure;
import org.aossie.starcross.search.PrefixStore;
import org.aossie.starcross.search.SearchResult;
import org.aossie.starcross.source.data.AstronomicalSource;
import org.aossie.starcross.source.data.ImageSource;
import org.aossie.starcross.source.data.LineSource;
import org.aossie.starcross.source.data.PointSource;
import org.aossie.starcross.source.data.Source;
import org.aossie.starcross.source.data.TextSource;
import org.aossie.starcross.util.GeocentricCoordinates;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public abstract class AbstractSourceLayer extends AbstractLayer {

    private final ArrayList<TextSource> textSources = new ArrayList<>();
    private final ArrayList<PointSource> pointSources = new ArrayList<>();
    private final ArrayList<AstronomicalSource> astroSources = new ArrayList<>();
    private final ArrayList<LineSource> lineSources = new ArrayList<>();
    private final ArrayList<ImageSource> imageSources = new ArrayList<>();

    private HashMap<String, SearchResult> searchIndex = new HashMap<>();
    private PrefixStore prefixStore = new PrefixStore();
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
            textSources.addAll(sources.getLabels());
            pointSources.addAll(sources.getPoints());
            lineSources.addAll(sources.getLines());
            imageSources.addAll(sources.getImages());

            List<String> names = astroSource.getNames();
            if (!names.isEmpty()) {
                GeocentricCoordinates searchLoc = astroSource.getSearchLocation();
                for (String name : names) {
                    searchIndex.put(name.toLowerCase(), new SearchResult(name, searchLoc));
                    prefixStore.add(name.toLowerCase());
                }
            }
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
        super.redraw(textSources,pointSources, lineSources, imageSources, updateTypes);
    }

    @Override
    public List<SearchResult> searchByObjectName(String name) {
        List<SearchResult> matches = new ArrayList<>();
        SearchResult searchResult = searchIndex.get(name.toLowerCase());
        if (searchResult != null) {
            matches.add(searchResult);
        }
        return matches;
    }

    @Override
    public Set<String> getObjectNamesMatchingPrefix(String prefix) {
        Set<String> results = prefixStore.queryByPrefix(prefix);
        return results;
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