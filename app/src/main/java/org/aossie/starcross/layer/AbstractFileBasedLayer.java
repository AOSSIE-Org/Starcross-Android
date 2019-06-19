package org.aossie.starcross.layer;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import org.aossie.starcross.renderer.RendererObjectManager;
import org.aossie.starcross.source.AstronomicalSource;
import org.aossie.starcross.source.proto.ProtobufAstronomicalSource;
import org.aossie.starcross.source.proto.SourceReader;
import org.aossie.starcross.util.MiscUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class AbstractFileBasedLayer extends AbstractSourceLayer {
    private static final String TAG = MiscUtil.getTag(AbstractFileBasedLayer.class);
    private static final Executor BACKGROUND_EXECUTOR = Executors.newFixedThreadPool(1);

    private final AssetManager assetManager;
    private final String fileName;
    private final List<AstronomicalSource> fileSources = new ArrayList<>();

    AbstractFileBasedLayer(AssetManager assetManager, Resources resources, String fileName) {
        super(resources, false);
        this.assetManager = assetManager;
        this.fileName = fileName;
    }

    @Override
    public synchronized void initialize() {
        BACKGROUND_EXECUTOR.execute(new Runnable() {
            public void run() {
                readSourceFile(fileName);
                AbstractFileBasedLayer.super.initialize();
            }
        });
    }

    @Override
    protected void initializeAstroSources(ArrayList<AstronomicalSource> sources) {
        sources.addAll(fileSources);
    }

    private void readSourceFile(String sourceFilename) {
        try (InputStream in = assetManager.open(sourceFilename, AssetManager.ACCESS_BUFFER)) {
            SourceReader.AstronomicalSourcesProto.Builder builder = SourceReader.AstronomicalSourcesProto.newBuilder();
            builder.mergeFrom(in);

            for (SourceReader.AstronomicalSourceProto proto : builder.build().getSourceList()) {
                fileSources.add(new ProtobufAstronomicalSource(proto));
            }
            Log.d(TAG, "Found: " + fileSources.size() + " sources");

            refreshSources(EnumSet.of(RendererObjectManager.UpdateType.Reset));
        } catch (IOException e) {
            Log.e(TAG, "Unable to open " + sourceFilename);
        }
    }
}