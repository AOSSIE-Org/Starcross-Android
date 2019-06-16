package org.aossie.starcross.source;

import org.aossie.starcross.renderer.RendererObjectManager;

import java.util.EnumSet;

public interface AstronomicalSource {
    Sources initialize();

    EnumSet<RendererObjectManager.UpdateType> update();
}