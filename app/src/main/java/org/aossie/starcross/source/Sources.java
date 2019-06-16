package org.aossie.starcross.source;

import java.util.List;

public interface Sources {
    List<? extends PointSource> getPoints();
}