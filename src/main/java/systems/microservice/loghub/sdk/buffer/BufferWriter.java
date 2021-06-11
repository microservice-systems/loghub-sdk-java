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

import systems.microservice.loghub.sdk.utils.Argument;
import systems.microservice.loghub.sdk.utils.Blob;
import systems.microservice.loghub.sdk.utils.Color;
import systems.microservice.loghub.sdk.utils.Image;
import systems.microservice.loghub.sdk.utils.Range;
import systems.microservice.loghub.sdk.utils.Tag;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class BufferWriter {
    private BufferWriter() {
    }

    public static int writeVersion(byte[] buffer, int index, byte value) {
        return writeByte(buffer, index, value);
    }

    public static int writeLength(byte[] buffer, int index, int value) {
        return writeInt(buffer, index, value);
    }

    public static int writeBoolean(byte[] buffer, int index, boolean value) {
        return writeByte(buffer, index, (byte) (value ? 1 : 0));
    }

    public static int writeByte(byte[] buffer, int index, byte value) {
        if (buffer != null) {
            buffer[index] = value;
        }
        index++;
        return index;
    }

    public static int writeChar(byte[] buffer, int index, char value) {
        int v = value;
        int l = v;
        l = l & 0x7F;
        byte b = -1;
        for (int sh = 7; b < 0; sh += 7) {
            int lp = l;
            l = v >> sh;
            if (l != 0) {
                lp = lp | 0x80;
                l = l & 0x7F;
            }
            b = (byte) lp;
            index = writeByte(buffer, index, b);
        }
        return index;
    }

    public static int writeShort(byte[] buffer, int index, short value) {
        int v = value;
        index = writeByte(buffer, index, (byte) (v >> 8));
        index = writeByte(buffer, index, (byte) (v));
        return index;
    }

    public static int writeInt(byte[] buffer, int index, int value) {
        index = writeByte(buffer, index, (byte) (value >> 24));
        index = writeByte(buffer, index, (byte) (value >> 16));
        index = writeByte(buffer, index, (byte) (value >> 8));
        index = writeByte(buffer, index, (byte) (value));
        return index;
    }

    public static int writeLong(byte[] buffer, int index, long value) {
        index = writeByte(buffer, index, (byte) (value >> 56));
        index = writeByte(buffer, index, (byte) (value >> 48));
        index = writeByte(buffer, index, (byte) (value >> 40));
        index = writeByte(buffer, index, (byte) (value >> 32));
        index = writeByte(buffer, index, (byte) (value >> 24));
        index = writeByte(buffer, index, (byte) (value >> 16));
        index = writeByte(buffer, index, (byte) (value >> 8));
        index = writeByte(buffer, index, (byte) (value));
        return index;
    }

    public static int writeFloat(byte[] buffer, int index, float value) {
        return writeInt(buffer, index, Float.floatToRawIntBits(value));
    }

    public static int writeDouble(byte[] buffer, int index, double value) {
        return writeLong(buffer, index, Double.doubleToRawLongBits(value));
    }

    public static int writeUUID(byte[] buffer, int index, UUID value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        index = writeLong(buffer, index, value.getMostSignificantBits());
        return writeLong(buffer, index, value.getLeastSignificantBits());
    }

    public static int writeBigInteger(byte[] buffer, int index, BigInteger value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        return writeByteArray(buffer, index, value.toByteArray());
    }

    public static int writeBigDecimal(byte[] buffer, int index, BigDecimal value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        return writeString(buffer, index, value.toString());
    }

    public static int writeDate(byte[] buffer, int index, Date value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        return writeLong(buffer, index, value.getTime());
    }

    public static int writeColor(byte[] buffer, int index, Color value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        index = writeByte(buffer, index, (byte) (value.r - ((short) 128)));
        index = writeByte(buffer, index, (byte) (value.g - ((short) 128)));
        index = writeByte(buffer, index, (byte) (value.b - ((short) 128)));
        return writeByte(buffer, index, (byte) (value.a - ((short) 128)));
    }

    public static int writeString(byte[] buffer, int index, String value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int ci = value.length();
        index = writeLength(buffer, index, ci);
        for (int i = 0; i < ci; ++i) {
            index = writeChar(buffer, index, value.charAt(i));
        }
        return index;
    }

    public static int writeString(byte[] buffer, int index, String value, String param1) {
        Argument.notNull("value", value);
        if (param1 == null) {
            param1 = "null";
        }

        index = writeVersion(buffer, index, (byte) 1);
        int idx = index;
        int l = 0;
        int ci = value.length();
        index = writeLength(buffer, index, 0);
        for (int i = 0, j = 1; i < ci; ++i) {
            char c = value.charAt(i);
            if (c != '{') {
                index = writeChar(buffer, index, c);
                l++;
            } else if ((i + 1 < ci) && (value.charAt(i + 1) == '}')) {
                String p = "{}";
                if (j == 1) {
                    p = param1;
                    j++;
                }
                for (int k = 0, ck = p.length(); k < ck; ++k) {
                    index = writeChar(buffer, index, p.charAt(k));
                    l++;
                }
                i++;
            } else {
                index = writeChar(buffer, index, c);
                l++;
            }
        }
        writeLength(buffer, idx, l);
        return index;
    }

    public static int writeString(byte[] buffer, int index, String value, String param1, String param2) {
        Argument.notNull("value", value);
        if (param1 == null) {
            param1 = "null";
        }
        if (param2 == null) {
            param2 = "null";
        }

        index = writeVersion(buffer, index, (byte) 1);
        int idx = index;
        int l = 0;
        int ci = value.length();
        index = writeLength(buffer, index, 0);
        for (int i = 0, j = 1; i < ci; ++i) {
            char c = value.charAt(i);
            if (c != '{') {
                index = writeChar(buffer, index, c);
                l++;
            } else if ((i + 1 < ci) && (value.charAt(i + 1) == '}')) {
                String p = "{}";
                if (j == 1) {
                    p = param1;
                    j++;
                } else if (j == 2) {
                    p = param2;
                    j++;
                }
                for (int k = 0, ck = p.length(); k < ck; ++k) {
                    index = writeChar(buffer, index, p.charAt(k));
                    l++;
                }
                i++;
            } else {
                index = writeChar(buffer, index, c);
                l++;
            }
        }
        writeLength(buffer, idx, l);
        return index;
    }

    public static int writeString(byte[] buffer, int index, String value, String param1, String param2, String param3) {
        Argument.notNull("value", value);
        if (param1 == null) {
            param1 = "null";
        }
        if (param2 == null) {
            param2 = "null";
        }
        if (param3 == null) {
            param3 = "null";
        }

        index = writeVersion(buffer, index, (byte) 1);
        int idx = index;
        int l = 0;
        int ci = value.length();
        index = writeLength(buffer, index, 0);
        for (int i = 0, j = 1; i < ci; ++i) {
            char c = value.charAt(i);
            if (c != '{') {
                index = writeChar(buffer, index, c);
                l++;
            } else if ((i + 1 < ci) && (value.charAt(i + 1) == '}')) {
                String p = "{}";
                if (j == 1) {
                    p = param1;
                    j++;
                } else if (j == 2) {
                    p = param2;
                    j++;
                } else if (j == 3) {
                    p = param3;
                    j++;
                }
                for (int k = 0, ck = p.length(); k < ck; ++k) {
                    index = writeChar(buffer, index, p.charAt(k));
                    l++;
                }
                i++;
            } else {
                index = writeChar(buffer, index, c);
                l++;
            }
        }
        writeLength(buffer, idx, l);
        return index;
    }

    public static int writeString(byte[] buffer, int index, String value, String param1, String param2, String param3, String param4) {
        Argument.notNull("value", value);
        if (param1 == null) {
            param1 = "null";
        }
        if (param2 == null) {
            param2 = "null";
        }
        if (param3 == null) {
            param3 = "null";
        }
        if (param4 == null) {
            param4 = "null";
        }

        index = writeVersion(buffer, index, (byte) 1);
        int idx = index;
        int l = 0;
        int ci = value.length();
        index = writeLength(buffer, index, 0);
        for (int i = 0, j = 1; i < ci; ++i) {
            char c = value.charAt(i);
            if (c != '{') {
                index = writeChar(buffer, index, c);
                l++;
            } else if ((i + 1 < ci) && (value.charAt(i + 1) == '}')) {
                String p = "{}";
                if (j == 1) {
                    p = param1;
                    j++;
                } else if (j == 2) {
                    p = param2;
                    j++;
                } else if (j == 3) {
                    p = param3;
                    j++;
                } else if (j == 4) {
                    p = param4;
                    j++;
                }
                for (int k = 0, ck = p.length(); k < ck; ++k) {
                    index = writeChar(buffer, index, p.charAt(k));
                    l++;
                }
                i++;
            } else {
                index = writeChar(buffer, index, c);
                l++;
            }
        }
        writeLength(buffer, idx, l);
        return index;
    }

    public static int writeString(byte[] buffer, int index, String value, String param1, String param2, String param3, String param4, String param5) {
        Argument.notNull("value", value);
        if (param1 == null) {
            param1 = "null";
        }
        if (param2 == null) {
            param2 = "null";
        }
        if (param3 == null) {
            param3 = "null";
        }
        if (param4 == null) {
            param4 = "null";
        }
        if (param5 == null) {
            param5 = "null";
        }

        index = writeVersion(buffer, index, (byte) 1);
        int idx = index;
        int l = 0;
        int ci = value.length();
        index = writeLength(buffer, index, 0);
        for (int i = 0, j = 1; i < ci; ++i) {
            char c = value.charAt(i);
            if (c != '{') {
                index = writeChar(buffer, index, c);
                l++;
            } else if ((i + 1 < ci) && (value.charAt(i + 1) == '}')) {
                String p = "{}";
                if (j == 1) {
                    p = param1;
                    j++;
                } else if (j == 2) {
                    p = param2;
                    j++;
                } else if (j == 3) {
                    p = param3;
                    j++;
                } else if (j == 4) {
                    p = param4;
                    j++;
                } else if (j == 5) {
                    p = param5;
                    j++;
                }
                for (int k = 0, ck = p.length(); k < ck; ++k) {
                    index = writeChar(buffer, index, p.charAt(k));
                    l++;
                }
                i++;
            } else {
                index = writeChar(buffer, index, c);
                l++;
            }
        }
        writeLength(buffer, idx, l);
        return index;
    }

    public static int writePattern(byte[] buffer, int index, Pattern value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        return writeString(buffer, index, value.pattern());
    }

    public static int writeURL(byte[] buffer, int index, URL value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        return writeString(buffer, index, value.toExternalForm());
    }

    public static <T extends Comparable<T>> int writeRange(byte[] buffer, int index, Map<String, Object> context, Range<T> value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        index = writeObject(buffer, index, context, value.min);
        return writeObject(buffer, index, context, value.max);
    }

    public static int writeTag(byte[] buffer, int index, Tag value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        index = writeString(buffer, index, value.key);
        index = writeObject(buffer, index, null, value.value);
        return writeStringRef(buffer, index, value.unit);
    }

    public static int writeImage(byte[] buffer, int index, Image value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        index = writeByteArray(buffer, index, value.content);
        return writeString(buffer, index, value.contentType);
    }

    public static int writeBlob(byte[] buffer, int index, Blob value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        index = writeByteArray(buffer, index, value.content);
        return writeString(buffer, index, value.contentType);
    }

    public static int writeBufferable(byte[] buffer, int index, Map<String, Object> context, Bufferable value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        return value.write(buffer, index, context);
    }

    public static int writeBooleanArray(byte[] buffer, int index, boolean[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeBoolean(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeByteArray(byte[] buffer, int index, byte[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeByte(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeCharArray(byte[] buffer, int index, char[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeChar(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeShortArray(byte[] buffer, int index, short[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeShort(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeIntArray(byte[] buffer, int index, int[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeInt(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeLongArray(byte[] buffer, int index, long[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeLong(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeFloatArray(byte[] buffer, int index, float[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeFloat(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeDoubleArray(byte[] buffer, int index, double[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeDouble(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeUUIDArray(byte[] buffer, int index, UUID[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeUUID(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeBigIntegerArray(byte[] buffer, int index, BigInteger[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeBigInteger(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeBigDecimalArray(byte[] buffer, int index, BigDecimal[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeBigDecimal(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeDateArray(byte[] buffer, int index, Date[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeDate(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeColorArray(byte[] buffer, int index, Color[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeColor(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeStringArray(byte[] buffer, int index, String[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeString(buffer, index, value[i]);
        }
        return index;
    }

    public static int writePatternArray(byte[] buffer, int index, Pattern[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writePattern(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeURLArray(byte[] buffer, int index, URL[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeURL(buffer, index, value[i]);
        }
        return index;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static int writeRangeArray(byte[] buffer, int index, Map<String, Object> context, Range[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeRange(buffer, index, context, value[i]);
        }
        return index;
    }

    public static int writeTagArray(byte[] buffer, int index, Tag[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeTag(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeImageArray(byte[] buffer, int index, Image[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeImage(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeBlobArray(byte[] buffer, int index, Blob[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeBlob(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeBufferableArray(byte[] buffer, int index, Map<String, Object> context, Bufferable[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeBufferable(buffer, index, context, value[i]);
        }
        return index;
    }

    public static int writeBooleanRef(byte[] buffer, int index, Boolean value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeBoolean(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeByteRef(byte[] buffer, int index, Byte value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeByte(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeCharacterRef(byte[] buffer, int index, Character value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeChar(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeShortRef(byte[] buffer, int index, Short value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeShort(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeIntegerRef(byte[] buffer, int index, Integer value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeInt(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeLongRef(byte[] buffer, int index, Long value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeLong(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeFloatRef(byte[] buffer, int index, Float value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeFloat(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeDoubleRef(byte[] buffer, int index, Double value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeDouble(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeUUIDRef(byte[] buffer, int index, UUID value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeUUID(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeBigIntegerRef(byte[] buffer, int index, BigInteger value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeBigInteger(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeBigDecimalRef(byte[] buffer, int index, BigDecimal value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeBigDecimal(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeDateRef(byte[] buffer, int index, Date value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeDate(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeColorRef(byte[] buffer, int index, Color value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeColor(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeStringRef(byte[] buffer, int index, String value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeString(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeStringRef(byte[] buffer, int index, String value, String param1) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeString(buffer, index, value, param1);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeStringRef(byte[] buffer, int index, String value, String param1, String param2) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeString(buffer, index, value, param1, param2);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeStringRef(byte[] buffer, int index, String value, String param1, String param2, String param3) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeString(buffer, index, value, param1, param2, param3);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeStringRef(byte[] buffer, int index, String value, String param1, String param2, String param3, String param4) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeString(buffer, index, value, param1, param2, param3, param4);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeStringRef(byte[] buffer, int index, String value, String param1, String param2, String param3, String param4, String param5) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeString(buffer, index, value, param1, param2, param3, param4, param5);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writePatternRef(byte[] buffer, int index, Pattern value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writePattern(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeURLRef(byte[] buffer, int index, URL value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeURL(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static <T extends Comparable<T>> int writeRangeRef(byte[] buffer, int index, Map<String, Object> context, Range<T> value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeRange(buffer, index, context, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeTagRef(byte[] buffer, int index, Tag value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeTag(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeImageRef(byte[] buffer, int index, Image value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeImage(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeBlobRef(byte[] buffer, int index, Blob value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeBlob(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeBufferableRef(byte[] buffer, int index, Map<String, Object> context, Bufferable value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeBufferable(buffer, index, context, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeBooleanArrayRef(byte[] buffer, int index, boolean[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeBooleanArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeByteArrayRef(byte[] buffer, int index, byte[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeByteArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeCharArrayRef(byte[] buffer, int index, char[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeCharArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeShortArrayRef(byte[] buffer, int index, short[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeShortArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeIntArrayRef(byte[] buffer, int index, int[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeIntArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeLongArrayRef(byte[] buffer, int index, long[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeLongArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeFloatArrayRef(byte[] buffer, int index, float[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeFloatArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeDoubleArrayRef(byte[] buffer, int index, double[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeDoubleArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeUUIDArrayRef(byte[] buffer, int index, UUID[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeUUIDArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeBigIntegerArrayRef(byte[] buffer, int index, BigInteger[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeBigIntegerArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeBigDecimalArrayRef(byte[] buffer, int index, BigDecimal[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeBigDecimalArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeDateArrayRef(byte[] buffer, int index, Date[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeDateArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeColorArrayRef(byte[] buffer, int index, Color[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeColorArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeStringArrayRef(byte[] buffer, int index, String[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeStringArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writePatternArrayRef(byte[] buffer, int index, Pattern[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writePatternArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeURLArrayRef(byte[] buffer, int index, URL[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeURLArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    @SuppressWarnings("rawtypes")
    public static int writeRangeArrayRef(byte[] buffer, int index, Map<String, Object> context, Range[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeRangeArray(buffer, index, context, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeTagArrayRef(byte[] buffer, int index, Tag[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeTagArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeImageArrayRef(byte[] buffer, int index, Image[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeImageArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeBlobArrayRef(byte[] buffer, int index, Blob[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeBlobArray(buffer, index, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeBufferableArrayRef(byte[] buffer, int index, Map<String, Object> context, Bufferable[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeBufferableArray(buffer, index, context, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    @SuppressWarnings("rawtypes")
    public static int writeObject(byte[] buffer, int index, Map<String, Object> context, Object value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        Class c = value.getClass();
        BufferObjectType ot = BufferObjectType.getObjectType(c);
        if (ot != null) {
            index = writeByte(buffer, index, ot.id);
            return ot.writer.write(buffer, index, context, value);
        } else {
            throw new BufferException(String.format("Class '%s' is not supported", c.getCanonicalName()));
        }
    }

    public static int writeObjectRef(byte[] buffer, int index, Map<String, Object> context, Object value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeObject(buffer, index, context, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    public static int writeObjectArray(byte[] buffer, int index, Map<String, Object> context, Object[] value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeObject(buffer, index, context, value[i]);
        }
        return index;
    }

    public static int writeObjectArrayRef(byte[] buffer, int index, Map<String, Object> context, Object[] value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeObjectArray(buffer, index, context, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    @SuppressWarnings("rawtypes")
    public static int writeObjectCollection(byte[] buffer, int index, Map<String, Object> context, Collection value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.size();
        index = writeLength(buffer, index, l);
        for (Object e : value) {
            index = writeObject(buffer, index, context, e);
        }
        return index;
    }

    @SuppressWarnings("rawtypes")
    public static int writeObjectCollectionRef(byte[] buffer, int index, Map<String, Object> context, Collection value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeObjectCollection(buffer, index, context, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }

    @SuppressWarnings("rawtypes")
    public static int writeObjectMap(byte[] buffer, int index, Map<String, Object> context, Map value) {
        Argument.notNull("value", value);

        index = writeVersion(buffer, index, (byte) 1);
        int l = value.size();
        index = writeLength(buffer, index, l);
        for (Object e : value.entrySet()) {
            Map.Entry en = (Map.Entry) e;
            index = writeObject(buffer, index, context, en.getKey());
            index = writeObject(buffer, index, context, en.getValue());
        }
        return index;
    }

    @SuppressWarnings("rawtypes")
    public static int writeObjectMapRef(byte[] buffer, int index, Map<String, Object> context, Map value) {
        if (value != null) {
            index = writeByte(buffer, index, (byte) 1);
            return writeObjectMap(buffer, index, context, value);
        } else {
            return writeByte(buffer, index, (byte) 0);
        }
    }
}
