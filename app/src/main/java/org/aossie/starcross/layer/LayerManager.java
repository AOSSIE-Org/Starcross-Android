package org.aossie.starcross.layer;

import org.aossie.starcross.renderer.RendererController;
import org.aossie.starcross.search.SearchResult;
import org.aossie.starcross.search.SearchTerm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LayerManager {

    private final List<Layer> layers = new ArrayList<>();

    public LayerManager() {
    }

    public void addLayer(Layer layer) {
        this.layers.add(layer);
    }

    public void initialize() {
        for (Layer layer : layers) {
            layer.initialize();
        }
    }

    public void registerWithRenderer(RendererController renderer) {
        for (Layer layer : layers) {
            layer.registerWithRenderer(renderer);
            layer.setVisible(true);
        }
    }

    public List<SearchResult> searchByObjectName(String name) {
        List<SearchResult> all = new ArrayList<>();
        for (Layer layer : layers) {
            all.addAll(layer.searchByObjectName(name));
        }
        return all;
    }

    public Set<SearchTerm> getObjectNamesMatchingPrefix(String prefix) {
        Set<SearchTerm> all = new HashSet<>();
        for (Layer layer : layers) {
            for (String query : layer.getObjectNamesMatchingPrefix(prefix)) {
                SearchTerm result = new SearchTerm(query);
                all.add(result);
            }
        }
        return all;
    }
}