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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class BufferWriter {
    private static final Map<Class, ObjectWriter> OBJECT_WRITERS = createObjectWriters();

    private BufferWriter() {
    }

    private static Map<Class, ObjectWriter> createObjectWriters() {
        Map<Class, ObjectWriter> ows = new HashMap<>(64);
        ows.put(Boolean.class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                Boolean v = (Boolean) value;
                return BufferWriter.writeBoolean(buffer, index, v);
            }
        });
        ows.put(Byte.class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                Byte v = (Byte) value;
                return BufferWriter.writeByte(buffer, index, v);
            }
        });
        ows.put(Character.class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                Character v = (Character) value;
                return BufferWriter.writeChar(buffer, index, v);
            }
        });
        ows.put(Short.class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                Short v = (Short) value;
                return BufferWriter.writeShort(buffer, index, v);
            }
        });
        ows.put(Integer.class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                Integer v = (Integer) value;
                return BufferWriter.writeInt(buffer, index, v);
            }
        });
        ows.put(Long.class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                Long v = (Long) value;
                return BufferWriter.writeLong(buffer, index, v);
            }
        });
        ows.put(Float.class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                Float v = (Float) value;
                return BufferWriter.writeFloat(buffer, index, v);
            }
        });
        ows.put(Double.class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                Double v = (Double) value;
                return BufferWriter.writeDouble(buffer, index, v);
            }
        });
        ows.put(boolean[].class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                boolean[] v = (boolean[]) value;
                return BufferWriter.writeBooleans(buffer, index, v);
            }
        });
        ows.put(byte[].class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                byte[] v = (byte[]) value;
                return BufferWriter.writeBytes(buffer, index, v);
            }
        });
        ows.put(char[].class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                char[] v = (char[]) value;
                return BufferWriter.writeChars(buffer, index, v);
            }
        });
        ows.put(short[].class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                short[] v = (short[]) value;
                return BufferWriter.writeShorts(buffer, index, v);
            }
        });
        ows.put(int[].class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                int[] v = (int[]) value;
                return BufferWriter.writeInts(buffer, index, v);
            }
        });
        ows.put(long[].class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                long[] v = (long[]) value;
                return BufferWriter.writeLongs(buffer, index, v);
            }
        });
        ows.put(float[].class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                float[] v = (float[]) value;
                return BufferWriter.writeFloats(buffer, index, v);
            }
        });
        ows.put(double[].class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                double[] v = (double[]) value;
                return BufferWriter.writeDoubles(buffer, index, v);
            }
        });
        ows.put(UUID.class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                UUID v = (UUID) value;
                return BufferWriter.writeUUID(buffer, index, v);
            }
        });
        ows.put(String.class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                String v = (String) value;
                return BufferWriter.writeString(buffer, index, v);
            }
        });
        ows.put(Bufferable.class, new ObjectWriter() {
            @Override
            public int write(byte[] buffer, int index, Map<String, Object> context, Object value) {
                Bufferable v = (Bufferable) value;
                return BufferWriter.writeBufferable(buffer, index, context, v);
            }
        });
        return ows;
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

    public static int writeVersion(byte[] buffer, int index, int value) {
        return writeInt(buffer, index, value);
    }

    public static int writeLength(byte[] buffer, int index, int value) {
        return writeInt(buffer, index, value);
    }

    public static int writeBooleans(byte[] buffer, int index, boolean[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeBoolean(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeBytes(byte[] buffer, int index, byte[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeByte(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeChars(byte[] buffer, int index, char[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeChar(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeShorts(byte[] buffer, int index, short[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeShort(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeInts(byte[] buffer, int index, int[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeInt(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeLongs(byte[] buffer, int index, long[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeLong(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeFloats(byte[] buffer, int index, float[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeFloat(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeDoubles(byte[] buffer, int index, double[] value) {
        Argument.notNull("value", value);

        int l = value.length;
        index = writeLength(buffer, index, l);
        for (int i = 0; i < l; ++i) {
            index = writeDouble(buffer, index, value[i]);
        }
        return index;
    }

    public static int writeUUID(byte[] buffer, int index, UUID value) {
        Argument.notNull("value", value);

        index = writeLong(buffer, index, value.getMostSignificantBits());
        return writeLong(buffer, index, value.getLeastSignificantBits());
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

    public static int writeBufferable(byte[] buffer, int index, Map<String, Object> context, Bufferable value) {
        Argument.notNull("value", value);

        return value.write(buffer, index, context);
    }

    public static int writeObject(byte[] buffer, int index, Map<String, Object> context, Class clazz, Object value) {
        Argument.notNull("clazz", clazz);
        Argument.notNull("value", value);

        ObjectWriter ow = OBJECT_WRITERS.get(clazz);
        if (ow != null) {
            return ow.write(buffer, index, context, value);
        } else {
            throw new BufferException(String.format("Class '%s' is not supported", clazz.getName()));
        }
    }

    public static int writeObjects(byte[] buffer, int index, Map<String, Object> context, Class clazz, Object[] value) {
        Argument.notNull("clazz", clazz);
        Argument.notNull("value", value);

        ObjectWriter ow = OBJECT_WRITERS.get(clazz);
        if (ow != null) {
            int l = value.length;
            index = writeLength(buffer, index, l);
            for (int i = 0; i < l; ++i) {
                index = ow.write(buffer, index, context, value[i]);
            }
            return index;
        } else {
            throw new BufferException(String.format("Class '%s' is not supported", clazz.getName()));
        }
    }

    public static int writeObjects(byte[] buffer, int index, Map<String, Object> context, Class clazz, Collection value) {
        Argument.notNull("clazz", clazz);
        Argument.notNull("value", value);

        ObjectWriter ow = OBJECT_WRITERS.get(clazz);
        if (ow != null) {
            int l = value.size();
            index = writeLength(buffer, index, l);
            for (Object e : value) {
                index = ow.write(buffer, index, context, e);
            }
            return index;
        } else {
            throw new BufferException(String.format("Class '%s' is not supported", clazz.getName()));
        }
    }

    public static int writeObjects(byte[] buffer, int index, Map<String, Object> context, Class keyClazz, Class valueClazz, Map value) {
        Argument.notNull("keyClazz", keyClazz);
        Argument.notNull("valueClazz", valueClazz);
        Argument.notNull("value", value);

        ObjectWriter kow = OBJECT_WRITERS.get(keyClazz);
        if (kow != null) {
            ObjectWriter vow = OBJECT_WRITERS.get(valueClazz);
            if (vow != null) {
                int l = value.size();
                index = writeLength(buffer, index, l);
                for (Object e : value.entrySet()) {
                    Map.Entry en = (Map.Entry) e;
                    index = kow.write(buffer, index, context, en.getKey());
                    index = vow.write(buffer, index, context, en.getValue());
                }
                return index;
            } else {
                throw new BufferException(String.format("Class '%s' is not supported", valueClazz.getName()));
            }
        } else {
            throw new BufferException(String.format("Class '%s' is not supported", keyClazz.getName()));
        }
    }

    private static abstract class ObjectWriter {
        public ObjectWriter() {
        }

        public abstract int write(byte[] buffer, int index, Map<String, Object> context, Object value);
    }
}
