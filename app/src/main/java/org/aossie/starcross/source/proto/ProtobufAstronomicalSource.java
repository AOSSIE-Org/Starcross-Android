package org.aossie.starcross.source.proto;

import android.content.res.Resources;

import org.aossie.starcross.source.AbstractAstronomicalSource;
import org.aossie.starcross.source.data.LineSource;
import org.aossie.starcross.source.data.PointSource;
import org.aossie.starcross.source.data.TextSource;
import org.aossie.starcross.source.impl.LineSourceImpl;
import org.aossie.starcross.source.impl.PointSourceImpl;
import org.aossie.starcross.source.impl.TextSourceImpl;
import org.aossie.starcross.util.DATAGEN;
import org.aossie.starcross.util.GeocentricCoordinates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProtobufAstronomicalSource extends AbstractAstronomicalSource {
    private static final Map<SourceReader.Shape, PointSource.Shape> shapeMap = new HashMap<>();

    static {
        shapeMap.put(SourceReader.Shape.CIRCLE, PointSource.Shape.CIRCLE);
        shapeMap.put(SourceReader.Shape.STAR, PointSource.Shape.CIRCLE);
        shapeMap.put(SourceReader.Shape.ELLIPTICAL_GALAXY, PointSource.Shape.ELLIPTICAL_GALAXY);
        shapeMap.put(SourceReader.Shape.SPIRAL_GALAXY, PointSource.Shape.SPIRAL_GALAXY);
        shapeMap.put(SourceReader.Shape.IRREGULAR_GALAXY, PointSource.Shape.IRREGULAR_GALAXY);
        shapeMap.put(SourceReader.Shape.LENTICULAR_GALAXY, PointSource.Shape.LENTICULAR_GALAXY);
        shapeMap.put(SourceReader.Shape.GLOBULAR_CLUSTER, PointSource.Shape.GLOBULAR_CLUSTER);
        shapeMap.put(SourceReader.Shape.OPEN_CLUSTER, PointSource.Shape.OPEN_CLUSTER);
        shapeMap.put(SourceReader.Shape.NEBULA, PointSource.Shape.NEBULA);
        shapeMap.put(SourceReader.Shape.HUBBLE_DEEP_FIELD, PointSource.Shape.HUBBLE_DEEP_FIELD);
    }

    private final SourceReader.AstronomicalSourceProto proto;
    private final Resources resources;
    private ArrayList<String> names;

    public ProtobufAstronomicalSource(SourceReader.AstronomicalSourceProto proto, Resources resources) {
        this.proto = proto;
        this.resources = resources;
    }

    @Override
    public synchronized ArrayList<String> getNames() {
        if (names == null) {
            names = new ArrayList<String>(proto.getNameIdsCount());
            for (int id : proto.getNameIdsList()) {

                names.add(DATAGEN.getLabel(resources, id));
            }
        }
        return names;
    }

    @Override
    public List<TextSource> getLabels() {
        if (proto.getLabelCount() == 0) {
            return Collections.<TextSource>emptyList();
        }
        ArrayList<TextSource> points = new ArrayList<TextSource>(proto.getLabelCount());
        for (SourceReader.LabelElementProto element : proto.getLabelList()) {
//      Log.d("makhan", "label" + element.getStringIndex());
//
//      try {
            points.add(new TextSourceImpl(getCoords(element.getLocation()),
                    DATAGEN.getLabel(resources, element.getStringIndex()),
                    element.getColor(), element.getOffset(), element.getFontSize()));
//      } catch (Exception e) {
//        points.add(new TextSourceImpl(getCoords(element.getLocation()),
//                "makhan",
//                element.getColor(), element.getOffset(), element.getFontSize()));
//      }
        }
        return points;

    }

    @Override
    public List<PointSource> getPoints() {
        if (proto.getPointCount() == 0) {
            return Collections.emptyList();
        }
        ArrayList<PointSource> points = new ArrayList<>(proto.getPointCount());
        for (SourceReader.PointElementProto element : proto.getPointList()) {
            points.add(new PointSourceImpl(getCoords(element.getLocation()),
                    element.getColor(), element.getSize(), shapeMap.get(element.getShape())));
        }
        return points;
    }

    @Override
    public GeocentricCoordinates getSearchLocation() {
        return getCoords(proto.getSearchLocation());
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