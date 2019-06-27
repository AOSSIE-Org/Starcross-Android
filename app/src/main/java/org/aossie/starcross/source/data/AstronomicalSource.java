package org.aossie.starcross.source.data;

import org.aossie.starcross.renderer.RendererObjectManager;

import java.util.EnumSet;

public interface AstronomicalSource {
    Source initialize();

    EnumSet<RendererObjectManager.UpdateType> update();
}