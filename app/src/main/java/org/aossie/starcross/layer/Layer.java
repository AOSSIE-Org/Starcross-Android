package org.aossie.starcross.layer;

import org.aossie.starcross.renderer.RendererController;
import org.aossie.starcross.search.SearchResult;

import java.util.List;
import java.util.Set;

public interface Layer {
    void initialize();

    void registerWithRenderer(RendererController controller);

    int getLayerDepthOrder();

    void setVisible(boolean visible);

    List<SearchResult> searchByObjectName(String name);

    Set<String> getObjectNamesMatchingPrefix(String prefix);
}