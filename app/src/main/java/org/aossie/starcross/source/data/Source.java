package org.aossie.starcross.source.data;

import java.util.List;

public interface Source {
    List<? extends PointSource> getPoints();
    List<? extends LineSource> getLines();
    List<? extends ImageSource> getImages();
    List<? extends TextSource> getLabels();
}