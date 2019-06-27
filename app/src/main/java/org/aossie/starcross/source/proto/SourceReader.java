package org.aossie.starcross.source.proto;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageLite;

import java.io.IOException;
import java.util.Collections;

public final class SourceReader {
    private SourceReader() {
    }

    public static final class GeocentricCoordinatesProto extends GeneratedMessageLite {

        private GeocentricCoordinatesProto() {
            initFields();
        }

        private GeocentricCoordinatesProto(boolean noInit) {
        }

        private static final GeocentricCoordinatesProto defaultInstance;

        static GeocentricCoordinatesProto getDefaultInstance() {
            return defaultInstance;
        }

        public GeocentricCoordinatesProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        private boolean hasRightAscension;
        private float rightAscension_ = 0F;

        boolean hasRightAscension() {
            return hasRightAscension;
        }

        float getRightAscension() {
            return rightAscension_;
        }

        private boolean hasDeclination;
        private float declination_ = 0F;

        boolean hasDeclination() {
            return hasDeclination;
        }

        float getDeclination() {
            return declination_;
        }

        private void initFields() {
        }

        public final boolean isInitialized() {
            return true;
        }

        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            if (hasRightAscension()) {
                output.writeFloat(1, getRightAscension());
            }
            if (hasDeclination()) {
                output.writeFloat(2, getDeclination());
            }
        }

        private int memoizedSerializedSize = -1;

        public int getSerializedSize() {
            int size = memoizedSerializedSize;
            if (size != -1) return size;

            size = 0;
            if (hasRightAscension()) {
                size += CodedOutputStream.computeFloatSize(1, getRightAscension());
            }
            if (hasDeclination()) {
                size += CodedOutputStream.computeFloatSize(2, getDeclination());
            }
            memoizedSerializedSize = size;
            return size;
        }

        static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        static Builder newBuilder(SourceReader.GeocentricCoordinatesProto prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends
                GeneratedMessageLite.Builder<SourceReader.GeocentricCoordinatesProto, Builder> {
            private SourceReader.GeocentricCoordinatesProto result;

            private Builder() {
            }

            private static Builder create() {
                Builder builder = new Builder();
                builder.result = new SourceReader.GeocentricCoordinatesProto();
                return builder;
            }

            protected SourceReader.GeocentricCoordinatesProto internalGetResult() {
                return result;
            }

            public Builder clear() {
                if (result == null) {
                    throw new IllegalStateException("Cannot call clear() after build().");
                }
                result = new SourceReader.GeocentricCoordinatesProto();
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(result);
            }

            public SourceReader.GeocentricCoordinatesProto getDefaultInstanceForType() {
                return SourceReader.GeocentricCoordinatesProto.getDefaultInstance();
            }

            public boolean isInitialized() {
                return result.isInitialized();
            }

            public SourceReader.GeocentricCoordinatesProto build() {
                if (result != null && !isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return buildPartial();
            }

            public SourceReader.GeocentricCoordinatesProto buildPartial() {
                if (result == null) {
                    throw new IllegalStateException("build() has already been called on this Builder.");
                }
                SourceReader.GeocentricCoordinatesProto returnMe = result;
                result = null;
                return returnMe;
            }

            public Builder mergeFrom(SourceReader.GeocentricCoordinatesProto other) {
                if (other == SourceReader.GeocentricCoordinatesProto.getDefaultInstance())
                    return this;
                if (other.hasRightAscension()) {
                    setRightAscension(other.getRightAscension());
                }
                if (other.hasDeclination()) {
                    setDeclination(other.getDeclination());
                }
                return this;
            }

            public Builder mergeFrom(CodedInputStream input,
                                     ExtensionRegistryLite extensionRegistry) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            return this;
                        default: {
                            if (!parseUnknownField(input, extensionRegistry, tag)) {
                                return this;
                            }
                            break;
                        }
                        case 13: {
                            setRightAscension(input.readFloat());
                            break;
                        }
                        case 21: {
                            setDeclination(input.readFloat());
                            break;
                        }
                    }
                }
            }

            void setRightAscension(float value) {
                result.hasRightAscension = true;
                result.rightAscension_ = value;
            }

            void setDeclination(float value) {
                result.hasDeclination = true;
                result.declination_ = value;
            }
        }

        static {
            defaultInstance = new GeocentricCoordinatesProto(true);
            defaultInstance.initFields();
        }
    }

    public static final class PointElementProto extends GeneratedMessageLite {
        private PointElementProto() {
            initFields();
        }

        private PointElementProto(boolean noInit) {
        }

        private static final PointElementProto defaultInstance;

        static PointElementProto getDefaultInstance() {
            return defaultInstance;
        }

        public PointElementProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        private boolean hasLocation;
        private SourceReader.GeocentricCoordinatesProto location_;

        boolean hasLocation() {
            return hasLocation;
        }

        SourceReader.GeocentricCoordinatesProto getLocation() {
            return location_;
        }

        private boolean hasSize;
        private int size_ = 3;

        boolean hasSize() {
            return hasSize;
        }

        public int getSize() {
            return size_;
        }

        private void initFields() {
            location_ = SourceReader.GeocentricCoordinatesProto.getDefaultInstance();
        }

        public final boolean isInitialized() {
            return true;
        }

        public void writeTo(CodedOutputStream output)
                throws java.io.IOException {
            getSerializedSize();
            if (hasLocation()) {
                output.writeMessage(1, getLocation());
            }
            if (hasSize()) {
                output.writeInt32(3, getSize());
            }
        }

        private int memoizedSerializedSize = -1;

        public int getSerializedSize() {
            int size = memoizedSerializedSize;
            if (size != -1) return size;

            size = 0;
            if (hasLocation()) {
                size += CodedOutputStream
                        .computeMessageSize(1, getLocation());
            }
            if (hasSize()) {
                size += CodedOutputStream
                        .computeInt32Size(3, getSize());
            }
            memoizedSerializedSize = size;
            return size;
        }

        static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        static Builder newBuilder(SourceReader.PointElementProto prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends
                GeneratedMessageLite.Builder<SourceReader.PointElementProto, Builder> {
            private SourceReader.PointElementProto result;

            private Builder() {
            }

            private static Builder create() {
                Builder builder = new Builder();
                builder.result = new SourceReader.PointElementProto();
                return builder;
            }

            protected SourceReader.PointElementProto internalGetResult() {
                return result;
            }

            public Builder clear() {
                if (result == null) {
                    throw new IllegalStateException("Cannot call clear() after build().");
                }
                result = new SourceReader.PointElementProto();
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(result);
            }

            public SourceReader.PointElementProto getDefaultInstanceForType() {
                return SourceReader.PointElementProto.getDefaultInstance();
            }

            public boolean isInitialized() {
                return result.isInitialized();
            }

            public SourceReader.PointElementProto build() {
                if (result != null && !isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return buildPartial();
            }

            public SourceReader.PointElementProto buildPartial() {
                if (result == null) {
                    throw new IllegalStateException("build() has already been called on this Builder.");
                }
                SourceReader.PointElementProto returnMe = result;
                result = null;
                return returnMe;
            }

            public Builder mergeFrom(SourceReader.PointElementProto other) {
                if (other == SourceReader.PointElementProto.getDefaultInstance())
                    return this;
                if (other.hasLocation()) {
                    mergeLocation(other.getLocation());
                }
                if (other.hasSize()) {
                    setSize(other.getSize());
                }
                return this;
            }

            public Builder mergeFrom(CodedInputStream input,
                                     ExtensionRegistryLite extensionRegistry) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            return this;
                        default: {
                            if (!parseUnknownField(input, extensionRegistry, tag)) {
                                return this;
                            }
                            break;
                        }
                        case 10: {
                            SourceReader.GeocentricCoordinatesProto.Builder subBuilder = SourceReader.GeocentricCoordinatesProto.newBuilder();
                            if (hasLocation()) {
                                subBuilder.mergeFrom(getLocation());
                            }
                            input.readMessage(subBuilder, extensionRegistry);
                            setLocation(subBuilder.buildPartial());
                            break;
                        }
                        case 24: {
                            setSize(input.readInt32());
                            break;
                        }
                    }
                }
            }

            boolean hasLocation() {
                return result.hasLocation();
            }

            SourceReader.GeocentricCoordinatesProto getLocation() {
                return result.getLocation();
            }

            void setLocation(GeocentricCoordinatesProto value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasLocation = true;
                result.location_ = value;
            }

            void mergeLocation(GeocentricCoordinatesProto value) {
                if (result.hasLocation() && result.location_ != SourceReader.GeocentricCoordinatesProto.getDefaultInstance()) {
                    result.location_ = SourceReader.GeocentricCoordinatesProto.newBuilder(result.location_).mergeFrom(value).buildPartial();
                } else {
                    result.location_ = value;
                }
                result.hasLocation = true;
            }

            public Builder setSize(int value) {
                result.hasSize = true;
                result.size_ = value;
                return this;
            }
        }

        static {
            defaultInstance = new PointElementProto(true);
            defaultInstance.initFields();
        }
    }

    public static final class AstronomicalSourceProto extends GeneratedMessageLite {

        private AstronomicalSourceProto() {
            initFields();
        }

        private AstronomicalSourceProto(boolean noInit) {
        }

        private static final AstronomicalSourceProto defaultInstance;

        static AstronomicalSourceProto getDefaultInstance() {
            return defaultInstance;
        }

        public AstronomicalSourceProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        private java.util.List<Integer> nameIds_ = java.util.Collections.emptyList();

        java.util.List<Integer> getNameIdsList() {
            return nameIds_;
        }

        private boolean hasSearchLocation;
        private SourceReader.GeocentricCoordinatesProto searchLocation_;

        boolean hasSearchLocation() {
            return hasSearchLocation;
        }

        SourceReader.GeocentricCoordinatesProto getSearchLocation() {
            return searchLocation_;
        }

        private boolean hasSearchLevel;
        private float searchLevel_ = 0F;

        boolean hasSearchLevel() {
            return hasSearchLevel;
        }

        float getSearchLevel() {
            return searchLevel_;
        }

        private boolean hasLevel;
        private float level_ = 0F;

        boolean hasLevel() {
            return hasLevel;
        }

        float getLevel() {
            return level_;
        }

        private java.util.List<PointElementProto> point_ =
                java.util.Collections.emptyList();

        java.util.List<PointElementProto> getPointList() {
            return point_;
        }

        int getPointCount() {
            return point_.size();
        }

        private void initFields() {
            searchLocation_ = SourceReader.GeocentricCoordinatesProto.getDefaultInstance();
        }

        public final boolean isInitialized() {
            return true;
        }

        public void writeTo(CodedOutputStream output)
                throws IOException {
            getSerializedSize();
            for (int element : getNameIdsList()) {
                output.writeUInt32(1, element);
            }
            if (hasSearchLocation()) {
                output.writeMessage(2, getSearchLocation());
            }
            if (hasSearchLevel()) {
                output.writeFloat(3, getSearchLevel());
            }
            if (hasLevel()) {
                output.writeFloat(4, getLevel());
            }
            for (SourceReader.PointElementProto element : getPointList()) {
                output.writeMessage(5, element);
            }

        }

        private int memoizedSerializedSize = -1;

        public int getSerializedSize() {
            int size = memoizedSerializedSize;
            if (size != -1) return size;

            size = 0;
            {
                int dataSize = 0;
                for (int element : getNameIdsList()) {
                    dataSize += CodedOutputStream
                            .computeUInt32SizeNoTag(element);
                }
                size += dataSize;
                size += getNameIdsList().size();
            }
            if (hasSearchLocation()) {
                size += CodedOutputStream
                        .computeMessageSize(2, getSearchLocation());
            }
            if (hasSearchLevel()) {
                size += CodedOutputStream
                        .computeFloatSize(3, getSearchLevel());
            }
            if (hasLevel()) {
                size += CodedOutputStream
                        .computeFloatSize(4, getLevel());
            }
            for (SourceReader.PointElementProto element : getPointList()) {
                size += CodedOutputStream
                        .computeMessageSize(5, element);
            }

            memoizedSerializedSize = size;
            return size;
        }

        static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        static Builder newBuilder(SourceReader.AstronomicalSourceProto prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<
                        SourceReader.AstronomicalSourceProto, Builder> {
            private SourceReader.AstronomicalSourceProto result;

            private Builder() {
            }

            private static Builder create() {
                Builder builder = new Builder();
                builder.result = new SourceReader.AstronomicalSourceProto();
                return builder;
            }

            protected SourceReader.AstronomicalSourceProto internalGetResult() {
                return result;
            }

            public Builder clear() {
                if (result == null) {
                    throw new IllegalStateException("Cannot call clear() after build().");
                }
                result = new SourceReader.AstronomicalSourceProto();
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(result);
            }

            public SourceReader.AstronomicalSourceProto getDefaultInstanceForType() {
                return SourceReader.AstronomicalSourceProto.getDefaultInstance();
            }

            public boolean isInitialized() {
                return result.isInitialized();
            }

            public SourceReader.AstronomicalSourceProto build() {
                if (result != null && !isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return buildPartial();
            }

            public SourceReader.AstronomicalSourceProto buildPartial() {
                if (result == null) {
                    throw new IllegalStateException("build() has already been called on this Builder.");
                }
                if (result.nameIds_ != Collections.EMPTY_LIST) {
                    result.nameIds_ = Collections.unmodifiableList(result.nameIds_);
                }
                if (result.point_ != Collections.EMPTY_LIST) {
                    result.point_ = Collections.unmodifiableList(result.point_);
                }
                SourceReader.AstronomicalSourceProto returnMe = result;
                result = null;
                return returnMe;
            }

            public Builder mergeFrom(SourceReader.AstronomicalSourceProto other) {
                if (other == SourceReader.AstronomicalSourceProto.getDefaultInstance())
                    return this;
                if (!other.nameIds_.isEmpty()) {
                    if (result.nameIds_.isEmpty()) {
                        result.nameIds_ = new java.util.ArrayList<>();
                    }
                    result.nameIds_.addAll(other.nameIds_);
                }
                if (other.hasSearchLocation()) {
                    mergeSearchLocation(other.getSearchLocation());
                }
                if (other.hasSearchLevel()) {
                    setSearchLevel(other.getSearchLevel());
                }
                if (other.hasLevel()) {
                    setLevel(other.getLevel());
                }
                if (!other.point_.isEmpty()) {
                    if (result.point_.isEmpty()) {
                        result.point_ = new java.util.ArrayList<>();
                    }
                    result.point_.addAll(other.point_);
                }
                return this;
            }

            public Builder mergeFrom(CodedInputStream input,
                                     ExtensionRegistryLite extensionRegistry) throws IOException {
                while (true) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            return this;
                        default: {
                            if (!parseUnknownField(input, extensionRegistry, tag)) {
                                return this;
                            }
                            break;
                        }
                        case 8: {
                            addNameIds(input.readUInt32());
                            break;
                        }
                        case 10: {
                            int length = input.readRawVarint32();
                            int limit = input.pushLimit(length);
                            while (input.getBytesUntilLimit() > 0) {
                                addNameIds(input.readUInt32());
                            }
                            input.popLimit(limit);
                            break;
                        }
                        case 18: {
                            SourceReader.GeocentricCoordinatesProto.Builder subBuilder = SourceReader.GeocentricCoordinatesProto.newBuilder();
                            if (hasSearchLocation()) {
                                subBuilder.mergeFrom(getSearchLocation());
                            }
                            input.readMessage(subBuilder, extensionRegistry);
                            setSearchLocation(subBuilder.buildPartial());
                            break;
                        }
                        case 29: {
                            setSearchLevel(input.readFloat());
                            break;
                        }
                        case 37: {
                            setLevel(input.readFloat());
                            break;
                        }
                        case 42: {
                            SourceReader.PointElementProto.Builder subBuilder = SourceReader.PointElementProto.newBuilder();
                            input.readMessage(subBuilder, extensionRegistry);
                            addPoint(subBuilder.buildPartial());
                            break;
                        }
                    }
                }
            }

            void addNameIds(int value) {
                if (result.nameIds_.isEmpty()) {
                    result.nameIds_ = new java.util.ArrayList<>();
                }
                result.nameIds_.add(value);
            }

            boolean hasSearchLocation() {
                return result.hasSearchLocation();
            }

            SourceReader.GeocentricCoordinatesProto getSearchLocation() {
                return result.getSearchLocation();
            }

            void setSearchLocation(GeocentricCoordinatesProto value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasSearchLocation = true;
                result.searchLocation_ = value;
            }

            void mergeSearchLocation(GeocentricCoordinatesProto value) {
                if (result.hasSearchLocation() &&
                        result.searchLocation_ != SourceReader.GeocentricCoordinatesProto.getDefaultInstance()) {
                    result.searchLocation_ =
                            SourceReader.GeocentricCoordinatesProto.newBuilder(result.searchLocation_).mergeFrom(value).buildPartial();
                } else {
                    result.searchLocation_ = value;
                }
                result.hasSearchLocation = true;
            }

            void setSearchLevel(float value) {
                result.hasSearchLevel = true;
                result.searchLevel_ = value;
            }

            void setLevel(float value) {
                result.hasLevel = true;
                result.level_ = value;
            }

            void addPoint(PointElementProto value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                if (result.point_.isEmpty()) {
                    result.point_ = new java.util.ArrayList<>();
                }
                result.point_.add(value);
            }
        }

        static {
            defaultInstance = new AstronomicalSourceProto(true);
            defaultInstance.initFields();
        }

    }

    public static final class AstronomicalSourcesProto extends GeneratedMessageLite {

        private AstronomicalSourcesProto() {
            initFields();
        }

        private AstronomicalSourcesProto(boolean noInit) {
        }

        private static final AstronomicalSourcesProto defaultInstance;

        static AstronomicalSourcesProto getDefaultInstance() {
            return defaultInstance;
        }

        public AstronomicalSourcesProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        private java.util.List<AstronomicalSourceProto> source_ = java.util.Collections.emptyList();

        public java.util.List<AstronomicalSourceProto> getSourceList() {
            return source_;
        }

        private void initFields() {
        }

        public final boolean isInitialized() {
            return true;
        }

        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            for (SourceReader.AstronomicalSourceProto element : getSourceList()) {
                output.writeMessage(1, element);
            }
        }

        private int memoizedSerializedSize = -1;

        public int getSerializedSize() {
            int size = memoizedSerializedSize;
            if (size != -1) return size;

            size = 0;
            for (SourceReader.AstronomicalSourceProto element : getSourceList()) {
                size += CodedOutputStream.computeMessageSize(1, element);
            }
            memoizedSerializedSize = size;
            return size;
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        static Builder newBuilder(SourceReader.AstronomicalSourcesProto prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends GeneratedMessageLite.Builder<SourceReader.AstronomicalSourcesProto, Builder> {
            private SourceReader.AstronomicalSourcesProto result;

            private Builder() {
            }

            private static Builder create() {
                Builder builder = new Builder();
                builder.result = new SourceReader.AstronomicalSourcesProto();
                return builder;
            }

            protected SourceReader.AstronomicalSourcesProto internalGetResult() {
                return result;
            }

            public Builder clear() {
                if (result == null) {
                    throw new IllegalStateException("Cannot call clear() after build().");
                }
                result = new SourceReader.AstronomicalSourcesProto();
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(result);
            }

            public SourceReader.AstronomicalSourcesProto getDefaultInstanceForType() {
                return SourceReader.AstronomicalSourcesProto.getDefaultInstance();
            }

            public boolean isInitialized() {
                return result.isInitialized();
            }

            public SourceReader.AstronomicalSourcesProto build() {
                if (result != null && !isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return buildPartial();
            }

            public SourceReader.AstronomicalSourcesProto buildPartial() {
                if (result == null) {
                    throw new IllegalStateException("build() has already been called on this Builder.");
                }
                if (result.source_ != Collections.EMPTY_LIST) {
                    result.source_ = Collections.unmodifiableList(result.source_);
                }
                SourceReader.AstronomicalSourcesProto returnMe = result;
                result = null;
                return returnMe;
            }

            public Builder mergeFrom(SourceReader.AstronomicalSourcesProto other) {
                if (other == SourceReader.AstronomicalSourcesProto.getDefaultInstance())
                    return this;
                if (!other.source_.isEmpty()) {
                    if (result.source_.isEmpty()) {
                        result.source_ = new java.util.ArrayList<>();
                    }
                    result.source_.addAll(other.source_);
                }
                return this;
            }

            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry)
                    throws IOException {
                while (true) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            return this;
                        default: {
                            if (!parseUnknownField(input, extensionRegistry, tag)) {
                                return this;
                            }
                            break;
                        }
                        case 10: {
                            SourceReader.AstronomicalSourceProto.Builder subBuilder = SourceReader.AstronomicalSourceProto.newBuilder();
                            input.readMessage(subBuilder, extensionRegistry);
                            addSource(subBuilder.buildPartial());
                            break;
                        }
                    }
                }
            }

            void addSource(AstronomicalSourceProto value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                if (result.source_.isEmpty()) {
                    result.source_ = new java.util.ArrayList<>();
                }
                result.source_.add(value);
            }

        }

        static {
            defaultInstance = new AstronomicalSourcesProto(true);
            defaultInstance.initFields();
        }
    }

}
