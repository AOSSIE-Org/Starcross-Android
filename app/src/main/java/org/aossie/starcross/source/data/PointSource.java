package org.aossie.starcross.source.data;

public interface PointSource extends PositionSource {

    public enum Shape {
        CIRCLE(0),
        STAR(1),
        ELLIPTICAL_GALAXY(2),
        SPIRAL_GALAXY(3),
        IRREGULAR_GALAXY(4),
        LENTICULAR_GALAXY(3),
        GLOBULAR_CLUSTER(5),
        OPEN_CLUSTER(6),
        NEBULA(7),
        HUBBLE_DEEP_FIELD(8);

        private final int imageIndex;

        private Shape(int imageIndex) {
            this.imageIndex = imageIndex;
        }

        public int getImageIndex() {
            // return imageIndex;
            return 1;
        }
    }
    int getSize();
}