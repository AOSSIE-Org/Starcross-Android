package org.aossie.starcross.source.proto;

import org.aossie.starcross.source.AbstractAstronomicalSource;
import org.aossie.starcross.source.PointSource;
import org.aossie.starcross.source.impl.PointSourceImpl;
import org.aossie.starcross.util.GeocentricCoordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProtobufAstronomicalSource extends AbstractAstronomicalSource {

    private final SourceReader.AstronomicalSourceProto proto;

    public ProtobufAstronomicalSource(SourceReader.AstronomicalSourceProto proto) {
        this.proto = proto;
    }

    @Override
    public List<PointSource> getPoints() {
        if (proto.getPointCount() == 0) {
            return Collections.emptyList();
        }
        ArrayList<PointSource> points = new ArrayList<>(proto.getPointCount());
        for (SourceReader.PointElementProto element : proto.getPointList()) {
            points.add(new PointSourceImpl(getCoords(element.getLocation()), element.getSize()));
        }
        return points;
    }


    private static GeocentricCoordinates getCoords(SourceReader.GeocentricCoordinatesProto proto) {
        return GeocentricCoordinates.getInstance(proto.getRightAscension(), proto.getDeclination());
    }
}