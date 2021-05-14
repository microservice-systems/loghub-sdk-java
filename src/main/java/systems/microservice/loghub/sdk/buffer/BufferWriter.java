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

import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

        index = writeLong(buffer, index, value.getMostSignificantBits());
        return writeLong(buffer, index, value.getLeastSignificantBits());
    }

    public static int writeDate(byte[] buffer, int index, Date value) {
        Argument.notNull("value", value);

        return writeLong(buffer, index, value.getTime());
    }

    public static int writeString(byte[] buffer, int index, String value) {
        Argument.notNull("value", value);

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

    public static int writeURL(byte[] buffer, int index, URL value) {
        Argument.notNull("value", value);

        return writeString(buffer, index, value.toExternalForm());
    }

    public static int writeBufferable(byte[] buffer, int index, Map<String, Object> context, Bufferable value) {
        Argument.notNull("value", value);

        return value.write(buffer, index, context);
    }

    public static int writeBooleanArray(byte[] buffer, int index, boolean[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeBoolean(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeByteArray(byte[] buffer, int index, byte[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeByte(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeCharArray(byte[] buffer, int index, char[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeChar(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeShortArray(byte[] buffer, int index, short[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeShort(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeIntArray(byte[] buffer, int index, int[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeInt(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeLongArray(byte[] buffer, int index, long[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeLong(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeFloatArray(byte[] buffer, int index, float[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeFloat(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeDoubleArray(byte[] buffer, int index, double[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeDouble(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeUUIDArray(byte[] buffer, int index, UUID[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeUUID(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeDateArray(byte[] buffer, int index, Date[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeDate(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeStringArray(byte[] buffer, int index, String[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeString(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeURLArray(byte[] buffer, int index, URL[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeURL(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeBufferableArray(byte[] buffer, int index, Map<String, Object> context, Bufferable[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeBufferable(buffer, index, context, value[i]);
        }
        return index;
    }

    public static int writeObject(byte[] buffer, int index, Map<String, Object> context, Object value) {
        Argument.notNull("value", value);

        Class c = value.getClass();
        BufferObjectType ot = BufferObjectType.getObjectType(c);
        if (ot != null) {
            index = writeByte(buffer, index, ot.id);
            return ot.writer.write(buffer, index, context, value);
        } else {
            throw new BufferException(String.format("Class '%s' is not supported", c.getCanonicalName()));
        }
    }

    public static int writeObjectArray(byte[] buffer, int index, Map<String, Object> context, Object[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeObject(buffer, index, context, value[i]);
        }
        return index;
    }

    public static int writeObjectCollection(byte[] buffer, int index, Map<String, Object> context, Collection value) {
        Argument.notNull("value", value);

        int l = value.size();
        index = writeLength(buffer, index, l);
        for (Object e : value) {
            index = writeObject(buffer, index, context, e);
        }
        return index;
    }

    public static int writeObjectMap(byte[] buffer, int index, Map<String, Object> context, Map value) {
        Argument.notNull("value", value);

        int l = value.size();
        index = writeLength(buffer, index, l);
        for (Object e : value.entrySet()) {
            Map.Entry en = (Map.Entry) e;
            index = writeObject(buffer, index, context, en.getKey());
            index = writeObject(buffer, index, context, en.getValue());
        }
        return index;
    }
}
