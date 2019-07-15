package org.aossie.starcross.source.proto;

import org.aossie.starcross.source.AbstractAstronomicalSource;
import org.aossie.starcross.source.data.LineSource;
import org.aossie.starcross.source.data.PointSource;
import org.aossie.starcross.source.impl.LineSourceImpl;
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

    @Override
    public List<LineSource> getLines() {
        if (proto.getLineCount() == 0) {
            return Collections.emptyList();
        }
        ArrayList<LineSource> points = new ArrayList<LineSource>(proto.getLineCount());
        for (SourceReader.LineElementProto element : proto.getLineList()) {
            ArrayList<GeocentricCoordinates> vertices =
                    new ArrayList<GeocentricCoordinates>(element.getVertexCount());
            for (SourceReader.GeocentricCoordinatesProto elementVertex : element.getVertexList()) {
                vertices.add(getCoords(elementVertex));
            }
            points.add(new LineSourceImpl(element.getColor(), vertices, element.getLineWidth()));
        }
        return points;
    }



    private static GeocentricCoordinates getCoords(SourceReader.GeocentricCoordinatesProto proto) {
        return GeocentricCoordinates.getInstance(proto.getRightAscension(), proto.getDeclination());
    }
}