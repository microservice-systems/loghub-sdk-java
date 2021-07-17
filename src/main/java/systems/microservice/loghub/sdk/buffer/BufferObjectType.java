/*
 * Copyright (C) 2020 Microservice Systems, Inc.
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package systems.microservice.loghub.sdk.buffer;

import systems.microservice.loghub.sdk.util.Argument;
import systems.microservice.loghub.sdk.util.Blob;
import systems.microservice.loghub.sdk.util.Color;
import systems.microservice.loghub.sdk.util.Image;
import systems.microservice.loghub.sdk.util.Range;
import systems.microservice.loghub.sdk.util.Tag;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public enum BufferObjectType {
    BOOLEAN((byte) 1, Boolean.class, new BooleanReader(), new BooleanWriter()),
    BYTE((byte) 2, Byte.class, new ByteReader(), new ByteWriter()),
    CHARACTER((byte) 3, Character.class, new CharacterReader(), new CharacterWriter()),
    SHORT((byte) 4, Short.class, new ShortReader(), new ShortWriter()),
    INTEGER((byte) 5, Integer.class, new IntegerReader(), new IntegerWriter()),
    LONG((byte) 6, Long.class, new LongReader(), new LongWriter()),
    FLOAT((byte) 7, Float.class, new FloatReader(), new FloatWriter()),
    DOUBLE((byte) 8, Double.class, new DoubleReader(), new DoubleWriter()),
    UUID((byte) 9, java.util.UUID.class, new UUIDReader(), new UUIDWriter()),
    BIG_INTEGER((byte) 10, BigInteger.class, new BigIntegerReader(), new BigIntegerWriter()),
    BIG_DECIMAL((byte) 11, BigDecimal.class, new BigDecimalReader(), new BigDecimalWriter()),
    DATE((byte) 12, Date.class, new DateReader(), new DateWriter()),
    COLOR((byte) 13, Color.class, new ColorReader(), new ColorWriter()),
    STRING((byte) 14, String.class, new StringReader(), new StringWriter()),
    PATTERN((byte) 15, Pattern.class, new PatternReader(), new PatternWriter()),
    URL((byte) 16, java.net.URL.class, new URLReader(), new URLWriter()),
    RANGE((byte) 17, Range.class, new RangeReader(), new RangeWriter()),
    TAG((byte) 18, Tag.class, new TagReader(), new TagWriter()),
    IMAGE((byte) 19, Image.class, new ImageReader(), new ImageWriter()),
    BLOB((byte) 20, Blob.class, new BlobReader(), new BlobWriter()),
    BUFFERABLE((byte) 49, Bufferable.class, new BufferableReader(), new BufferableWriter()),
    BOOLEAN_ARRAY((byte) 51, boolean[].class, new BooleanArrayReader(), new BooleanArrayWriter()),
    BYTE_ARRAY((byte) 52, byte[].class, new ByteArrayReader(), new ByteArrayWriter()),
    CHAR_ARRAY((byte) 53, char[].class, new CharArrayReader(), new CharArrayWriter()),
    SHORT_ARRAY((byte) 54, short[].class, new ShortArrayReader(), new ShortArrayWriter()),
    INT_ARRAY((byte) 55, int[].class, new IntArrayReader(), new IntArrayWriter()),
    LONG_ARRAY((byte) 56, long[].class, new LongArrayReader(), new LongArrayWriter()),
    FLOAT_ARRAY((byte) 57, float[].class, new FloatArrayReader(), new FloatArrayWriter()),
    DOUBLE_ARRAY((byte) 58, double[].class, new DoubleArrayReader(), new DoubleArrayWriter()),
    UUID_ARRAY((byte) 59, UUID[].class, new UUIDArrayReader(), new UUIDArrayWriter()),
    BIG_INTEGER_ARRAY((byte) 60, BigInteger[].class, new BigIntegerArrayReader(), new BigIntegerArrayWriter()),
    BIG_DECIMAL_ARRAY((byte) 61, BigDecimal[].class, new BigDecimalArrayReader(), new BigDecimalArrayWriter()),
    DATE_ARRAY((byte) 62, Date[].class, new DateArrayReader(), new DateArrayWriter()),
    COLOR_ARRAY((byte) 63, Color[].class, new ColorArrayReader(), new ColorArrayWriter()),
    STRING_ARRAY((byte) 64, String[].class, new StringArrayReader(), new StringArrayWriter()),
    URL_ARRAY((byte) 65, URL[].class, new URLArrayReader(), new URLArrayWriter()),
    BUFFERABLE_ARRAY((byte) 99, Bufferable[].class, new BufferableArrayReader(), new BufferableArrayWriter());

    private static final HashMap<Byte, BufferObjectType> idObjectTypes = createIDObjectTypes();
    private static final HashMap<Class, BufferObjectType> classObjectTypes = createClassObjectTypes();
    private static final ConcurrentHashMap<UUID, Class> bufferableClasses = new ConcurrentHashMap<>(16, 0.75f, 1);
    private static final ConcurrentHashMap<Class, UUID> bufferableUUIDs = new ConcurrentHashMap<>(16, 0.75f, 1);

    public final byte id;
    public final Class clazz;
    public final BufferObjectReader reader;
    public final BufferObjectWriter writer;

    BufferObjectType(byte id, Class clazz, BufferObjectReader reader, BufferObjectWriter writer) {
        this.id = id;
        this.clazz = clazz;
        this.reader = reader;
        this.writer = writer;
    }

    private static HashMap<Byte, BufferObjectType> createIDObjectTypes() {
        HashMap<Byte, BufferObjectType> iots = new HashMap<>(64);
        iots.put(BOOLEAN.id, BOOLEAN);
        iots.put(BYTE.id, BYTE);
        iots.put(CHARACTER.id, CHARACTER);
        iots.put(SHORT.id, SHORT);
        iots.put(INTEGER.id, INTEGER);
        iots.put(LONG.id, LONG);
        iots.put(FLOAT.id, FLOAT);
        iots.put(DOUBLE.id, DOUBLE);
        iots.put(UUID.id, UUID);
        iots.put(BIG_INTEGER.id, BIG_INTEGER);
        iots.put(BIG_DECIMAL.id, BIG_DECIMAL);
        iots.put(DATE.id, DATE);
        iots.put(COLOR.id, COLOR);
        iots.put(STRING.id, STRING);
        iots.put(PATTERN.id, PATTERN);
        iots.put(URL.id, URL);
        iots.put(RANGE.id, RANGE);
        iots.put(TAG.id, TAG);
        iots.put(IMAGE.id, IMAGE);
        iots.put(BLOB.id, BLOB);
        iots.put(BUFFERABLE.id, BUFFERABLE);
        iots.put(BOOLEAN_ARRAY.id, BOOLEAN_ARRAY);
        iots.put(BYTE_ARRAY.id, BYTE_ARRAY);
        iots.put(CHAR_ARRAY.id, CHAR_ARRAY);
        iots.put(SHORT_ARRAY.id, SHORT_ARRAY);
        iots.put(INT_ARRAY.id, INT_ARRAY);
        iots.put(LONG_ARRAY.id, LONG_ARRAY);
        iots.put(FLOAT_ARRAY.id, FLOAT_ARRAY);
        iots.put(DOUBLE_ARRAY.id, DOUBLE_ARRAY);
        iots.put(UUID_ARRAY.id, UUID_ARRAY);
        iots.put(BIG_INTEGER_ARRAY.id, BIG_INTEGER_ARRAY);
        iots.put(BIG_DECIMAL_ARRAY.id, BIG_DECIMAL_ARRAY);
        iots.put(DATE_ARRAY.id, DATE_ARRAY);
        iots.put(COLOR_ARRAY.id, COLOR_ARRAY);
        iots.put(STRING_ARRAY.id, STRING_ARRAY);
        iots.put(URL_ARRAY.id, URL_ARRAY);
        iots.put(BUFFERABLE_ARRAY.id, BUFFERABLE_ARRAY);
        return iots;
    }

    private static HashMap<Class, BufferObjectType> createClassObjectTypes() {
        HashMap<Class, BufferObjectType> cots = new HashMap<>(64);
        cots.put(BOOLEAN.clazz, BOOLEAN);
        cots.put(BYTE.clazz, BYTE);
        cots.put(CHARACTER.clazz, CHARACTER);
        cots.put(SHORT.clazz, SHORT);
        cots.put(INTEGER.clazz, INTEGER);
        cots.put(LONG.clazz, LONG);
        cots.put(FLOAT.clazz, FLOAT);
        cots.put(DOUBLE.clazz, DOUBLE);
        cots.put(UUID.clazz, UUID);
        cots.put(BIG_INTEGER.clazz, BIG_INTEGER);
        cots.put(BIG_DECIMAL.clazz, BIG_DECIMAL);
        cots.put(DATE.clazz, DATE);
        cots.put(COLOR.clazz, COLOR);
        cots.put(STRING.clazz, STRING);
        cots.put(PATTERN.clazz, PATTERN);
        cots.put(URL.clazz, URL);
        cots.put(RANGE.clazz, RANGE);
        cots.put(TAG.clazz, TAG);
        cots.put(IMAGE.clazz, IMAGE);
        cots.put(BLOB.clazz, BLOB);
        cots.put(BUFFERABLE.clazz, BUFFERABLE);
        cots.put(BOOLEAN_ARRAY.clazz, BOOLEAN_ARRAY);
        cots.put(BYTE_ARRAY.clazz, BYTE_ARRAY);
        cots.put(CHAR_ARRAY.clazz, CHAR_ARRAY);
        cots.put(SHORT_ARRAY.clazz, SHORT_ARRAY);
        cots.put(INT_ARRAY.clazz, INT_ARRAY);
        cots.put(LONG_ARRAY.clazz, LONG_ARRAY);
        cots.put(FLOAT_ARRAY.clazz, FLOAT_ARRAY);
        cots.put(DOUBLE_ARRAY.clazz, DOUBLE_ARRAY);
        cots.put(UUID_ARRAY.clazz, UUID_ARRAY);
        cots.put(BIG_INTEGER_ARRAY.clazz, BIG_INTEGER_ARRAY);
        cots.put(BIG_DECIMAL_ARRAY.clazz, BIG_DECIMAL_ARRAY);
        cots.put(DATE_ARRAY.clazz, DATE_ARRAY);
        cots.put(COLOR_ARRAY.clazz, COLOR_ARRAY);
        cots.put(STRING_ARRAY.clazz, STRING_ARRAY);
        cots.put(URL_ARRAY.clazz, URL_ARRAY);
        cots.put(BUFFERABLE_ARRAY.clazz, BUFFERABLE_ARRAY);
        return cots;
    }

    public static BufferObjectType getObjectType(byte id) {
        return idObjectTypes.get(id);
    }

    public static BufferObjectType getObjectType(Class clazz) {
        Argument.notNull("clazz", clazz);

        return classObjectTypes.get(clazz);
    }

    public static <T extends Bufferable> void registerBufferableClass(UUID uuid, Class<T> clazz) {
        Argument.notNull("uuid", uuid);
        Argument.notNull("clazz", clazz);

        bufferableClasses.put(uuid, clazz);
        bufferableUUIDs.put(clazz, uuid);
    }

    private static final class BooleanReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readBoolean();
        }
    }

    private static final class ByteReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readByte();
        }
    }

    private static final class CharacterReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readChar();
        }
    }

    private static final class ShortReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readShort();
        }
    }

    private static final class IntegerReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readInt();
        }
    }

    private static final class LongReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readLong();
        }
    }

    private static final class FloatReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readFloat();
        }
    }

    private static final class DoubleReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readDouble();
        }
    }

    private static final class UUIDReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readUUID();
        }
    }

    private static final class BigIntegerReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readBigInteger();
        }
    }

    private static final class BigDecimalReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readBigDecimal();
        }
    }

    private static final class DateReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readDate();
        }
    }

    private static final class ColorReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readColor();
        }
    }

    private static final class StringReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readString();
        }
    }

    private static final class PatternReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readPattern();
        }
    }

    private static final class URLReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readURL();
        }
    }

    private static final class RangeReader implements BufferObjectReader {
        @SuppressWarnings("unchecked")
        @Override
        public Object read(BufferReader reader) {
            return reader.readRange(Comparable.class);
        }
    }

    private static final class TagReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readTag();
        }
    }

    private static final class ImageReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readImage();
        }
    }

    private static final class BlobReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readBlob();
        }
    }

    private static final class BufferableReader implements BufferObjectReader {
        @SuppressWarnings("unchecked")
        @Override
        public Object read(BufferReader reader) {
            UUID uuid = reader.readUUID();
            Class c = bufferableClasses.get(uuid);
            if (c != null) {
                return reader.readBufferable(c);
            } else {
                throw new BufferException(String.format("Bufferable class with uuid '%s' is not registered", uuid));
            }
        }
    }

    private static final class BooleanArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readBooleanArray();
        }
    }

    private static final class ByteArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readByteArray();
        }
    }

    private static final class CharArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readCharArray();
        }
    }

    private static final class ShortArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readShortArray();
        }
    }

    private static final class IntArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readIntArray();
        }
    }

    private static final class LongArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readLongArray();
        }
    }

    private static final class FloatArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readFloatArray();
        }
    }

    private static final class DoubleArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readDoubleArray();
        }
    }

    private static final class UUIDArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readUUIDArray();
        }
    }

    private static final class BigIntegerArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readBigIntegerArray();
        }
    }

    private static final class BigDecimalArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readBigDecimalArray();
        }
    }

    private static final class DateArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readDateArray();
        }
    }

    private static final class ColorArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readColorArray();
        }
    }

    private static final class StringArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readStringArray();
        }
    }

    private static final class URLArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readURLArray();
        }
    }

    private static final class BufferableArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readBufferableArray(null);
        }
    }

    private static final class BooleanWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeBoolean(buffer, index, (Boolean) value);
        }
    }

    private static final class ByteWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeByte(buffer, index, (Byte) value);
        }
    }

    private static final class CharacterWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeChar(buffer, index, (Character) value);
        }
    }

    private static final class ShortWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeShort(buffer, index, (Short) value);
        }
    }

    private static final class IntegerWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeInt(buffer, index, (Integer) value);
        }
    }

    private static final class LongWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeLong(buffer, index, (Long) value);
        }
    }

    private static final class FloatWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeFloat(buffer, index, (Float) value);
        }
    }

    private static final class DoubleWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeDouble(buffer, index, (Double) value);
        }
    }

    private static final class UUIDWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeUUID(buffer, index, (java.util.UUID) value);
        }
    }

    private static final class BigIntegerWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeBigInteger(buffer, index, (BigInteger) value);
        }
    }

    private static final class BigDecimalWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeBigDecimal(buffer, index, (BigDecimal) value);
        }
    }

    private static final class DateWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeDate(buffer, index, (Date) value);
        }
    }

    private static final class ColorWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeColor(buffer, index, (Color) value);
        }
    }

    private static final class StringWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeString(buffer, index, (String) value);
        }
    }

    private static final class PatternWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writePattern(buffer, index, (Pattern) value);
        }
    }

    private static final class URLWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeURL(buffer, index, (java.net.URL) value);
        }
    }

    private static final class RangeWriter implements BufferObjectWriter {
        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeRange(buffer, index, context, (Range) value);
        }
    }

    private static final class TagWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeTag(buffer, index, (Tag) value);
        }
    }

    private static final class ImageWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeImage(buffer, index, (Image) value);
        }
    }

    private static final class BlobWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeBlob(buffer, index, (Blob) value);
        }
    }

    private static final class BufferableWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            Bufferable v = (Bufferable) value;
            Class c = v.getClass();
            UUID uuid = bufferableUUIDs.get(c);
            if (uuid != null) {
                index = BufferWriter.writeUUID(buffer, index, uuid);
                return BufferWriter.writeBufferable(buffer, index, context, v);
            } else {
                throw new BufferException(String.format("Bufferable class '%s' is not registered", c.getCanonicalName()));
            }
        }
    }

    private static final class BooleanArrayWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeBooleanArray(buffer, index, (boolean[]) value);
        }
    }

    private static final class ByteArrayWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeByteArray(buffer, index, (byte[]) value);
        }
    }

    private static final class CharArrayWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeCharArray(buffer, index, (char[]) value);
        }
    }

    private static final class ShortArrayWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeShortArray(buffer, index, (short[]) value);
        }
    }

    private static final class IntArrayWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeIntArray(buffer, index, (int[]) value);
        }
    }

    private static final class LongArrayWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeLongArray(buffer, index, (long[]) value);
        }
    }

    private static final class FloatArrayWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeFloatArray(buffer, index, (float[]) value);
        }
    }

    private static final class DoubleArrayWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeDoubleArray(buffer, index, (double[]) value);
        }
    }

    private static final class UUIDArrayWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeUUIDArray(buffer, index, (UUID[]) value);
        }
    }

    private static final class BigIntegerArrayWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeBigIntegerArray(buffer, index, (BigInteger[]) value);
        }
    }

    private static final class BigDecimalArrayWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeBigDecimalArray(buffer, index, (BigDecimal[]) value);
        }
    }

    private static final class DateArrayWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeDateArray(buffer, index, (Date[]) value);
        }
    }

    private static final class ColorArrayWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeColorArray(buffer, index, (Color[]) value);
        }
    }

    private static final class StringArrayWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeStringArray(buffer, index, (String[]) value);
        }
    }

    private static final class URLArrayWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeURLArray(buffer, index, (URL[]) value);
        }
    }

    private static final class BufferableArrayWriter implements BufferObjectWriter {
        @Override
        public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
            return BufferWriter.writeBufferableArray(buffer, index, context, (Bufferable[]) value);
        }
    }
}
