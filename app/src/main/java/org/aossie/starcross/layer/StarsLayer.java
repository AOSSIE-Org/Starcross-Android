package org.aossie.starcross.layer;

import android.content.res.AssetManager;
import android.content.res.Resources;

public class StarsLayer extends AbstractFileBasedLayer {
    public StarsLayer(AssetManager assetManager, Resources resources) {
        super(assetManager, resources, "stars.binary");
    }

    @Override
    public int getLayerDepthOrder() {
        return 30;
    }
}