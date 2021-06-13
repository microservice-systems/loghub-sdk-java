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

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
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
public final class BufferReader {
    public static final int ARRAY_READ_LENGTH_MAX = 65536;

    private final byte[] buffer;
    private int index;

    public BufferReader(byte[] buffer) {
        this(buffer, 0);
    }

    public BufferReader(byte[] buffer, int index) {
        Argument.notNull("buffer", buffer);
        Argument.inRangeInt("index", index, 0, buffer.length - 1);

        this.buffer = buffer;
        this.index = index;
    }

    public BufferReader(BufferReader reader) {
        this(reader, reader.getIndex());
    }

    public BufferReader(BufferReader reader, int index) {
        this(reader.getBuffer(), index);
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        Argument.inRangeInt("index", index, 0, buffer.length - 1);

        this.index = index;
    }

    public void skip(int count) {
        Argument.inRangeInt("count", count, -index, buffer.length - index - 1);

        this.index += count;
    }

    public byte[] readBytes() {
        int l = 1;
        if ((l >= 0) && (l <= buffer.length - index)) {
            byte[] v = new byte[l];
            for (int i = 0; i < l; ++i) {
                v[i] = readByte();
            }
            return v;
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: read bytes of length %d", buffer.length, index, l));
        }
    }

    public long[] readLongs() {
        int l = 1;
        if ((l >= 0) && (l <= buffer.length - index)) {
            long[] v = new long[l];
            for (int i = 0; i < l; ++i) {
                v[i] = readLong();
            }
            return v;
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: read longs of length %d", buffer.length, index, l));
        }
    }

    public byte readVersion() {
        return readByte();
    }

    public int readLength(int elementSize) {
        int idx = index;
        int v = readInt();
        if ((v >= 0) && (v <= (buffer.length - index) / elementSize)) {
            return v;
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal length %d for elements of size %d", buffer.length, idx, v, elementSize));
        }
    }

    public boolean readBoolean() {
        int idx = index;
        byte v = readByte();
        if (v == 1) {
            return true;
        } else if (v == 0) {
            return false;
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal boolean value %d", buffer.length, idx, v));
        }
    }

    public byte readByte() {
        byte v = buffer[index];
        index++;
        return v;
    }

    public char readChar() {
        int idx = index;
        int v = 0;
        int sh = 0;
        byte b = -1;
        for (; b < 0; sh += 7) {
            if (sh > 14) {
                throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: read char of length larger than 3 bytes", buffer.length, idx));
            }
            b = readByte();
            int l = b;
            l = l & 0x7F;
            l = l << sh;
            v = v | l;
        }
        return (char) v;
    }

    public short readShort() {
        byte b1 = readByte();
        byte b0 = readByte();
        int v = ((((int) b1 & 0xFF) << 8) |
                 (((int) b0 & 0xFF)));
        return (short) v;
    }

    public int readInt() {
        byte b3 = readByte();
        byte b2 = readByte();
        byte b1 = readByte();
        byte b0 = readByte();
        return ((((int) b3 & 0xFF) << 24) |
                (((int) b2 & 0xFF) << 16) |
                (((int) b1 & 0xFF) <<  8) |
                (((int) b0 & 0xFF)));
    }

    public long readLong() {
        byte b7 = readByte();
        byte b6 = readByte();
        byte b5 = readByte();
        byte b4 = readByte();
        byte b3 = readByte();
        byte b2 = readByte();
        byte b1 = readByte();
        byte b0 = readByte();
        return ((((long) b7) << 56) |
                (((long) b6 & 0xFF) << 48) |
                (((long) b5 & 0xFF) << 40) |
                (((long) b4 & 0xFF) << 32) |
                (((long) b3 & 0xFF) << 24) |
                (((long) b2 & 0xFF) << 16) |
                (((long) b1 & 0xFF) <<  8) |
                (((long) b0 & 0xFF)));
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public UUID readUUID() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            long msb = readLong();
            long lsb = readLong();
            return new UUID(msb, lsb);
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public BigInteger readBigInteger() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            byte[] v = readByteArray();
            return new BigInteger(v);
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public BigDecimal readBigDecimal() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            String v = readString();
            return new BigDecimal(v);
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public Date readDate() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            long v = readLong();
            return new Date(v);
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public Color readColor() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            short r = readByte();
            short g = readByte();
            short b = readByte();
            short a = readByte();
            return new Color((short) (r + ((short) 128)), (short) (g + ((short) 128)), (short) (b + ((short) 128)), (short) (a + ((short) 128)));
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public String readString() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            int l = readLength(1);
            char[] v = new char[Math.min(l, ARRAY_READ_LENGTH_MAX)];
            for (int i = 0; i < l; ++i) {
                if (i >= v.length) {
                    char[] nv = new char[v.length * 2];
                    System.arraycopy(v, 0, nv, 0, v.length);
                    v = nv;
                }
                v[i] = readChar();
            }
            return new String(v, 0, l);
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public Pattern readPattern() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            String v = readString();
            return Pattern.compile(v);
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public URL readURL() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            String v = readString();
            try {
                return new URL(v);
            } catch (MalformedURLException e) {
                throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal url value '%s'", buffer.length, idx, v), e);
            }
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public <T extends Comparable<T>> Range<T> readRange(Class<T> clazz) {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            T min = readObject(clazz);
            T max = readObject(clazz);
            return new Range<>(min, max);
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public Tag readTag() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            String k = readString();
            Object v = readObject(Object.class);
            String u = readStringRef();
            return new Tag(k, v, u);
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public Image readImage() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            byte[] c = readByteArray();
            String ct = readString();
            return new Image(c, ct);
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public Blob readBlob() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            byte[] c = readByteArray();
            String ct = readString();
            return new Blob(c, ct);
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public <T extends Bufferable> T readBufferable(Class<T> clazz) {
        try {
            return clazz.getConstructor(BufferReader.class).newInstance(this);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BufferException(e);
        }
    }

    public boolean[] readBooleanArray() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            int l = readLength(1);
            boolean[] v = new boolean[Math.min(l, ARRAY_READ_LENGTH_MAX)];
            for (int i = 0; i < l; ++i) {
                if (i >= v.length) {
                    boolean[] nv = new boolean[v.length * 2];
                    System.arraycopy(v, 0, nv, 0, v.length);
                    v = nv;
                }
                v[i] = readBoolean();
            }
            if (l == v.length) {
                return v;
            } else {
                boolean[] nv = new boolean[l];
                System.arraycopy(v, 0, nv, 0, l);
                return nv;
            }
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public byte[] readByteArray() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            int l = readLength(1);
            byte[] v = new byte[Math.min(l, ARRAY_READ_LENGTH_MAX)];
            for (int i = 0; i < l; ++i) {
                if (i >= v.length) {
                    byte[] nv = new byte[v.length * 2];
                    System.arraycopy(v, 0, nv, 0, v.length);
                    v = nv;
                }
                v[i] = readByte();
            }
            if (l == v.length) {
                return v;
            } else {
                byte[] nv = new byte[l];
                System.arraycopy(v, 0, nv, 0, l);
                return nv;
            }
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public char[] readCharArray() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            int l = readLength(1);
            char[] v = new char[Math.min(l, ARRAY_READ_LENGTH_MAX)];
            for (int i = 0; i < l; ++i) {
                if (i >= v.length) {
                    char[] nv = new char[v.length * 2];
                    System.arraycopy(v, 0, nv, 0, v.length);
                    v = nv;
                }
                v[i] = readChar();
            }
            if (l == v.length) {
                return v;
            } else {
                char[] nv = new char[l];
                System.arraycopy(v, 0, nv, 0, l);
                return nv;
            }
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public short[] readShortArray() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            int l = readLength(1);
            short[] v = new short[Math.min(l, ARRAY_READ_LENGTH_MAX)];
            for (int i = 0; i < l; ++i) {
                if (i >= v.length) {
                    short[] nv = new short[v.length * 2];
                    System.arraycopy(v, 0, nv, 0, v.length);
                    v = nv;
                }
                v[i] = readShort();
            }
            if (l == v.length) {
                return v;
            } else {
                short[] nv = new short[l];
                System.arraycopy(v, 0, nv, 0, l);
                return nv;
            }
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public int[] readIntArray() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            int l = readLength(1);
            int[] v = new int[Math.min(l, ARRAY_READ_LENGTH_MAX)];
            for (int i = 0; i < l; ++i) {
                if (i >= v.length) {
                    int[] nv = new int[v.length * 2];
                    System.arraycopy(v, 0, nv, 0, v.length);
                    v = nv;
                }
                v[i] = readInt();
            }
            if (l == v.length) {
                return v;
            } else {
                int[] nv = new int[l];
                System.arraycopy(v, 0, nv, 0, l);
                return nv;
            }
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public long[] readLongArray() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            int l = readLength(1);
            long[] v = new long[Math.min(l, ARRAY_READ_LENGTH_MAX)];
            for (int i = 0; i < l; ++i) {
                if (i >= v.length) {
                    long[] nv = new long[v.length * 2];
                    System.arraycopy(v, 0, nv, 0, v.length);
                    v = nv;
                }
                v[i] = readLong();
            }
            if (l == v.length) {
                return v;
            } else {
                long[] nv = new long[l];
                System.arraycopy(v, 0, nv, 0, l);
                return nv;
            }
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public float[] readFloatArray() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            int l = readLength(1);
            float[] v = new float[Math.min(l, ARRAY_READ_LENGTH_MAX)];
            for (int i = 0; i < l; ++i) {
                if (i >= v.length) {
                    float[] nv = new float[v.length * 2];
                    System.arraycopy(v, 0, nv, 0, v.length);
                    v = nv;
                }
                v[i] = readFloat();
            }
            if (l == v.length) {
                return v;
            } else {
                float[] nv = new float[l];
                System.arraycopy(v, 0, nv, 0, l);
                return nv;
            }
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public double[] readDoubleArray() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            int l = readLength(1);
            double[] v = new double[Math.min(l, ARRAY_READ_LENGTH_MAX)];
            for (int i = 0; i < l; ++i) {
                if (i >= v.length) {
                    double[] nv = new double[v.length * 2];
                    System.arraycopy(v, 0, nv, 0, v.length);
                    v = nv;
                }
                v[i] = readDouble();
            }
            if (l == v.length) {
                return v;
            } else {
                double[] nv = new double[l];
                System.arraycopy(v, 0, nv, 0, l);
                return nv;
            }
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public UUID[] readUUIDArray() {
        int idx = index;
        byte ver = readVersion();
        if (ver == 1) {
            int l = readLength(1);
            UUID[] v = new UUID[Math.min(l, ARRAY_READ_LENGTH_MAX)];
            for (int i = 0; i < l; ++i) {
                if (i >= v.length) {
                    UUID[] nv = new UUID[v.length * 2];
                    System.arraycopy(v, 0, nv, 0, v.length);
                    v = nv;
                }
                v[i] = readUUID();
            }
            if (l == v.length) {
                return v;
            } else {
                UUID[] nv = new UUID[l];
                System.arraycopy(v, 0, nv, 0, l);
                return nv;
            }
        } else {
            throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: illegal version value %d", buffer.length, idx, ver));
        }
    }

    public BigInteger[] readBigIntegerArray() {
    }

    public BigDecimal[] readBigDecimalArray() {
    }

    public Date[] readDateArray() {
    }

    public Color[] readColorArray() {
    }

    public String[] readStringArray() {
    }

    public Pattern[] readPatternArray() {
    }

    public URL[] readURLArray() {
    }

    @SuppressWarnings("rawtypes")
    public <T extends Comparable<T>> Range[] readRangeArray(Class<T> clazz) {
    }

    public Tag[] readTagArray() {
    }

    public Image[] readImageArray() {
    }

    public Blob[] readBlobArray() {
    }

    public <T extends Bufferable> T[] readBufferableArray(Class<T> clazz) {
    }

    public Boolean readBooleanRef() {
    }

    public Byte readByteRef() {
    }

    public Character readCharacterRef() {
    }

    public Short readShortRef() {
    }

    public Integer readIntegerRef() {
    }

    public Long readLongRef() {
    }

    public Float readFloatRef() {
    }

    public Double readDoubleRef() {
    }

    public UUID readUUIDRef() {
    }

    public BigInteger readBigIntegerRef() {
    }

    public BigDecimal readBigDecimalRef() {
    }

    public Date readDateRef() {
    }

    public Color readColorRef() {
    }

    public String readStringRef() {
    }

    public Pattern readPatternRef() {
    }

    public URL readURLRef() {
    }

    public <T extends Comparable<T>> Range<T> readRangeRef(Class<T> clazz) {
    }

    public Tag readTagRef() {
    }

    public Image readImageRef() {
    }

    public Blob readBlobRef() {
    }

    public <T extends Bufferable> T readBufferableRef(Class<T> clazz) {
    }

    public boolean[] readBooleanArrayRef() {
    }

    public byte[] readByteArrayRef() {
    }

    public char[] readCharArrayRef() {
    }

    public short[] readShortArrayRef() {
    }

    public int[] readIntArrayRef() {
    }

    public long[] readLongArrayRef() {
    }

    public float[] readFloatArrayRef() {
    }

    public double[] readDoubleArrayRef() {
    }

    public UUID[] readUUIDArrayRef() {
    }

    public BigInteger[] readBigIntegerArrayRef() {
    }

    public BigDecimal[] readBigDecimalArrayRef() {
    }

    public Date[] readDateArrayRef() {
    }

    public Color[] readColorArrayRef() {
    }

    public String[] readStringArrayRef() {
    }

    public Pattern[] readPatternArrayRef() {
    }

    public URL[] readURLArrayRef() {
    }

    @SuppressWarnings("rawtypes")
    public <T extends Comparable<T>> Range[] readRangeArrayRef(Class<T> clazz) {
    }

    public Tag[] readTagArrayRef() {
    }

    public Image[] readImageArrayRef() {
    }

    public Blob[] readBlobArrayRef() {
    }

    public <T extends Bufferable> T[] readBufferableArrayRef(Class<T> clazz) {
    }

    public <T> T readObject(Class<T> clazz) {
    }

    public <T> T readObjectRef(Class<T> clazz) {
    }

    public <T> T[] readObjectArray(Class<T> clazz) {
    }

    public <T> T[] readObjectArrayRef(Class<T> clazz) {
    }

    public <C extends Collection<T>, T> C readObjectCollection(C collection, Class<T> clazz) {
    }

    public <C extends Collection<T>, T> C readObjectCollectionRef(C collection, Class<T> clazz) {
    }

    public <M extends Map<K, V>, K, V> M readObjectMap(M map, Class<K> keyClass, Class<V> valueClass) {
    }

    public <M extends Map<K, V>, K, V> M readObjectMapRef(M map, Class<K> keyClass, Class<V> valueClass) {
    }
}
