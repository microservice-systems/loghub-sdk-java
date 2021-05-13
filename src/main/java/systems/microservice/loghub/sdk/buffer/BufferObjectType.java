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

import systems.microservice.loghub.sdk.LogType;

import java.util.HashMap;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public enum BufferObjectType {
    BOOLEAN((byte) 1),
    BYTE((byte) 2),
    CHARACTER((byte) 3),
    SHORT((byte) 4),
    INTEGER((byte) 5),
    LONG((byte) 6),
    FLOAT((byte) 7),
    DOUBLE((byte) 8),
    UUID((byte) 9),
    DATE((byte) 10),
    STRING((byte) 11),
    URL((byte) 12),
    BUFFERABLE((byte) 49),
    BOOLEAN_ARRAY((byte) 51),
    BYTE_ARRAY((byte) 52),
    CHARACTER_ARRAY((byte) 53),
    SHORT_ARRAY((byte) 54),
    INTEGER_ARRAY((byte) 55),
    LONG_ARRAY((byte) 56),
    FLOAT_ARRAY((byte) 57),
    DOUBLE_ARRAY((byte) 58),
    UUID_ARRAY((byte) 59),
    DATE_ARRAY((byte) 60),
    STRING_ARRAY((byte) 61),
    URL_ARRAY((byte) 62),
    BUFFERABLE_ARRAY((byte) 99);

    private static final HashMap<Byte, BufferObjectType> idObjectTypes = createIDObjectTypes();

    public final byte id;

    BufferObjectType(byte id) {
        this.id = id;
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
        iots.put(DATE.id, DATE);
        iots.put(STRING.id, STRING);
        iots.put(URL.id, URL);
        iots.put(BUFFERABLE.id, BUFFERABLE);
        iots.put(BOOLEAN_ARRAY.id, BOOLEAN_ARRAY);
        iots.put(BYTE_ARRAY.id, BYTE_ARRAY);
        iots.put(CHARACTER_ARRAY.id, CHARACTER_ARRAY);
        iots.put(SHORT_ARRAY.id, SHORT_ARRAY);
        iots.put(INTEGER_ARRAY.id, INTEGER_ARRAY);
        iots.put(LONG_ARRAY.id, LONG_ARRAY);
        iots.put(FLOAT_ARRAY.id, FLOAT_ARRAY);
        iots.put(DOUBLE_ARRAY.id, DOUBLE_ARRAY);
        iots.put(UUID_ARRAY.id, UUID_ARRAY);
        iots.put(DATE_ARRAY.id, DATE_ARRAY);
        iots.put(STRING_ARRAY.id, STRING_ARRAY);
        iots.put(URL_ARRAY.id, URL_ARRAY);
        iots.put(BUFFERABLE_ARRAY.id, BUFFERABLE_ARRAY);
        return iots;
    }

    public static BufferObjectType getObjectType(byte id) {
        return idObjectTypes.get(id);
    }

    private static final class BooleanReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class ByteReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return new Byte(reader.readByte());
        }
    }

    private static final class CharacterReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return new Character(reader.readChar());
        }
    }

    private static final class ShortReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class IntegerReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class LongReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return new Long(reader.readLong());
        }
    }

    private static final class FloatReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class DoubleReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class UUIDReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class DateReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class StringReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class URLReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class BufferableReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class BooleanArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class ByteArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readBytes();
        }
    }

    private static final class CharacterArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class ShortArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class IntegerArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class LongArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return reader.readLongs();
        }
    }

    private static final class FloatArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class DoubleArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class UUIDArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class DateArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class StringArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class URLArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }

    private static final class BufferableArrayReader implements BufferObjectReader {
        @Override
        public Object read(BufferReader reader) {
            return null;
        }
    }
}
