package org.aossie.starcross.layer;

import android.content.res.AssetManager;
import android.content.res.Resources;

public class ConstellationsLayer extends AbstractFileBasedLayer {

    public ConstellationsLayer(AssetManager assetManager, Resources resources) {
        super(assetManager, resources, "constellations.binary");
    }

    @Override
    public int getLayerDepthOrder() {
        return 10;
    }

}
