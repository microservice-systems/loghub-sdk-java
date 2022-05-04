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

package loghub.io;

import loghub.Blob;
import loghub.Image;
import loghub.Tag;
import loghub.config.Validator;
import loghub.util.Color;
import loghub.util.Range;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
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
public class FormatInputStream extends InputStream {
    protected static final int ARRAY_READ_LENGTH_MAX = 65536;

    protected final InputStream input;
    protected long size;
    protected boolean closed;

    public FormatInputStream(InputStream input) {
        Validator.notNull("input", input);

        this.input = input;
        this.size = 0L;
        this.closed = false;
    }

    public InputStream getInput() {
        return input;
    }

    public long getSize() {
        return size;
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public int available() throws IOException {
        return input.available();
    }

    @Override
    public int read() throws IOException {
        int v = input.read();
        if (v >= 0) {
            size++;
        }
        return v;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int s = input.read(b, off, len);
        if (s > 0) {
            size += s;
        }
        return s;
    }

    @Override
    public long skip(long n) throws IOException {
        long s = input.skip(n);
        if (s > 0L) {
            size += s;
        }
        return s;
    }

    @Override
    public void reset() throws IOException {
        input.reset();
    }

    @Override
    public void mark(int readlimit) {
        input.mark(readlimit);
    }

    @Override
    public boolean markSupported() {
        return input.markSupported();
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            input.close();
            closed = true;
        }
    }

    public final byte readVersion() throws IOException {
        return readByte();
    }

    public final int readLength() throws IOException {
        long s = size;
        int v = readInt();
        if (v >= 0) {
            return v;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal length %d", s, v));
        }
    }

    public final boolean readBoolean() throws IOException {
        long s = size;
        byte v = readByte();
        if (v == 1) {
            return true;
        } else if (v == 0) {
            return false;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal boolean value %d", s, v));
        }
    }

    public final byte readByte() throws IOException {
        int v = read();
        if (v >= 0) {
            if (v <= 255) {
                return (byte) v;
            } else {
                throw new RuntimeException();
            }
        } else {
            throw new FormatInputException(String.format("End of input stream reached"));
        }
    }

    public final char readChar() throws IOException {
        long s = size;
        int v = 0;
        int sh = 0;
        byte b = -1;
        for (; b < 0; sh += 7) {
            if (sh > 14) {
                throw new FormatInputException(String.format("Illegal format at size %d: read char of length larger than 3 bytes", s));
            }
            b = readByte();
            int l = b;
            l = l & 0x7F;
            l = l << sh;
            v = v | l;
        }
        return (char) v;
    }

    public final short readShort() throws IOException {
        byte b1 = readByte();
        byte b0 = readByte();
        int v = ((((int) b1 & 0xFF) << 8) |
                (((int) b0 & 0xFF)));
        return (short) v;
    }

    public final int readInt() throws IOException {
        byte b3 = readByte();
        byte b2 = readByte();
        byte b1 = readByte();
        byte b0 = readByte();
        return ((((int) b3 & 0xFF) << 24) |
                (((int) b2 & 0xFF) << 16) |
                (((int) b1 & 0xFF) <<  8) |
                (((int) b0 & 0xFF)));
    }

    public final long readLong() throws IOException {
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

    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    public final UUID readUUID() throws IOException {
        long msb = readLong();
        long lsb = readLong();
        return new UUID(msb, lsb);
    }

    public final BigInteger readBigInteger() throws IOException {
        byte[] v = readByteArray();
        return new BigInteger(v);
    }

    public final BigDecimal readBigDecimal() throws IOException {
        String v = readString();
        return new BigDecimal(v);
    }

    public final Date readDate() throws IOException {
        long v = readLong();
        return new Date(v);
    }

    public final Color readColor() throws IOException {
        short r = readByte();
        short g = readByte();
        short b = readByte();
        short a = readByte();
        return new Color((short) (r + ((short) 128)), (short) (g + ((short) 128)), (short) (b + ((short) 128)), (short) (a + ((short) 128)));
    }

    public final String readString() throws IOException {
        int l = readLength();
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
    }

    public final Pattern readPattern() throws IOException {
        String v = readString();
        return Pattern.compile(v);
    }

    public final URL readURL() throws IOException {
        long s = size;
        String v = readString();
        try {
            return new URL(v);
        } catch (MalformedURLException e) {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal url value '%s'", s, v), e);
        }
    }

    public final <T extends Comparable<T>> Range<T> readRange(Class<T> clazz) throws IOException {
        T min = readObject(clazz);
        T max = readObject(clazz);
        return new Range<>(min, max);
    }

    public final Tag readTag() throws IOException {
        String k = readString();
        Object v = readObject(Object.class);
        String u = readStringRef();
        return new Tag(k, v, u);
    }

    public final Image readImage() throws IOException {
        byte[] c = readByteArray();
        String ct = readString();
        return new Image(c, ct);
    }

    public final Blob readBlob() throws IOException {
        byte[] c = readByteArray();
        String ct = readString();
        return new Blob(c, ct);
    }

    public final boolean[] readBooleanArray() throws IOException {
        int l = readLength();
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
    }

    public final byte[] readByteArray() throws IOException {
        int l = readLength();
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
    }

    public final char[] readCharArray() throws IOException {
        int l = readLength();
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
    }

    public final short[] readShortArray() throws IOException {
        int l = readLength();
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
    }

    public final int[] readIntArray() throws IOException {
        int l = readLength();
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
    }

    public final long[] readLongArray() throws IOException {
        int l = readLength();
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
    }

    public final float[] readFloatArray() throws IOException {
        int l = readLength();
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
    }

    public final double[] readDoubleArray() throws IOException {
        int l = readLength();
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
    }

    public final UUID[] readUUIDArray() throws IOException {
        int l = readLength();
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
    }

    public final BigInteger[] readBigIntegerArray() throws IOException {
        int l = readLength();
        BigInteger[] v = new BigInteger[Math.min(l, ARRAY_READ_LENGTH_MAX)];
        for (int i = 0; i < l; ++i) {
            if (i >= v.length) {
                BigInteger[] nv = new BigInteger[v.length * 2];
                System.arraycopy(v, 0, nv, 0, v.length);
                v = nv;
            }
            v[i] = readBigInteger();
        }
        if (l == v.length) {
            return v;
        } else {
            BigInteger[] nv = new BigInteger[l];
            System.arraycopy(v, 0, nv, 0, l);
            return nv;
        }
    }

    public final BigDecimal[] readBigDecimalArray() throws IOException {
        int l = readLength();
        BigDecimal[] v = new BigDecimal[Math.min(l, ARRAY_READ_LENGTH_MAX)];
        for (int i = 0; i < l; ++i) {
            if (i >= v.length) {
                BigDecimal[] nv = new BigDecimal[v.length * 2];
                System.arraycopy(v, 0, nv, 0, v.length);
                v = nv;
            }
            v[i] = readBigDecimal();
        }
        if (l == v.length) {
            return v;
        } else {
            BigDecimal[] nv = new BigDecimal[l];
            System.arraycopy(v, 0, nv, 0, l);
            return nv;
        }
    }

    public final Date[] readDateArray() throws IOException {
        int l = readLength();
        Date[] v = new Date[Math.min(l, ARRAY_READ_LENGTH_MAX)];
        for (int i = 0; i < l; ++i) {
            if (i >= v.length) {
                Date[] nv = new Date[v.length * 2];
                System.arraycopy(v, 0, nv, 0, v.length);
                v = nv;
            }
            v[i] = readDate();
        }
        if (l == v.length) {
            return v;
        } else {
            Date[] nv = new Date[l];
            System.arraycopy(v, 0, nv, 0, l);
            return nv;
        }
    }

    public final Color[] readColorArray() throws IOException {
        int l = readLength();
        Color[] v = new Color[Math.min(l, ARRAY_READ_LENGTH_MAX)];
        for (int i = 0; i < l; ++i) {
            if (i >= v.length) {
                Color[] nv = new Color[v.length * 2];
                System.arraycopy(v, 0, nv, 0, v.length);
                v = nv;
            }
            v[i] = readColor();
        }
        if (l == v.length) {
            return v;
        } else {
            Color[] nv = new Color[l];
            System.arraycopy(v, 0, nv, 0, l);
            return nv;
        }
    }

    public final String[] readStringArray() throws IOException {
        int l = readLength();
        String[] v = new String[Math.min(l, ARRAY_READ_LENGTH_MAX)];
        for (int i = 0; i < l; ++i) {
            if (i >= v.length) {
                String[] nv = new String[v.length * 2];
                System.arraycopy(v, 0, nv, 0, v.length);
                v = nv;
            }
            v[i] = readString();
        }
        if (l == v.length) {
            return v;
        } else {
            String[] nv = new String[l];
            System.arraycopy(v, 0, nv, 0, l);
            return nv;
        }
    }

    public final Pattern[] readPatternArray() throws IOException {
        int l = readLength();
        Pattern[] v = new Pattern[Math.min(l, ARRAY_READ_LENGTH_MAX)];
        for (int i = 0; i < l; ++i) {
            if (i >= v.length) {
                Pattern[] nv = new Pattern[v.length * 2];
                System.arraycopy(v, 0, nv, 0, v.length);
                v = nv;
            }
            v[i] = readPattern();
        }
        if (l == v.length) {
            return v;
        } else {
            Pattern[] nv = new Pattern[l];
            System.arraycopy(v, 0, nv, 0, l);
            return nv;
        }
    }

    public final URL[] readURLArray() throws IOException {
        int l = readLength();
        URL[] v = new URL[Math.min(l, ARRAY_READ_LENGTH_MAX)];
        for (int i = 0; i < l; ++i) {
            if (i >= v.length) {
                URL[] nv = new URL[v.length * 2];
                System.arraycopy(v, 0, nv, 0, v.length);
                v = nv;
            }
            v[i] = readURL();
        }
        if (l == v.length) {
            return v;
        } else {
            URL[] nv = new URL[l];
            System.arraycopy(v, 0, nv, 0, l);
            return nv;
        }
    }

    @SuppressWarnings("rawtypes")
    public final <T extends Comparable<T>> Range[] readRangeArray(Class<T> clazz) throws IOException {
        int l = readLength();
        Range[] v = new Range[Math.min(l, ARRAY_READ_LENGTH_MAX)];
        for (int i = 0; i < l; ++i) {
            if (i >= v.length) {
                Range[] nv = new Range[v.length * 2];
                System.arraycopy(v, 0, nv, 0, v.length);
                v = nv;
            }
            v[i] = readRange(clazz);
        }
        if (l == v.length) {
            return v;
        } else {
            Range[] nv = new Range[l];
            System.arraycopy(v, 0, nv, 0, l);
            return nv;
        }
    }

    public final Tag[] readTagArray() throws IOException {
        int l = readLength();
        Tag[] v = new Tag[Math.min(l, ARRAY_READ_LENGTH_MAX)];
        for (int i = 0; i < l; ++i) {
            if (i >= v.length) {
                Tag[] nv = new Tag[v.length * 2];
                System.arraycopy(v, 0, nv, 0, v.length);
                v = nv;
            }
            v[i] = readTag();
        }
        if (l == v.length) {
            return v;
        } else {
            Tag[] nv = new Tag[l];
            System.arraycopy(v, 0, nv, 0, l);
            return nv;
        }
    }

    public final Image[] readImageArray() throws IOException {
        int l = readLength();
        Image[] v = new Image[Math.min(l, ARRAY_READ_LENGTH_MAX)];
        for (int i = 0; i < l; ++i) {
            if (i >= v.length) {
                Image[] nv = new Image[v.length * 2];
                System.arraycopy(v, 0, nv, 0, v.length);
                v = nv;
            }
            v[i] = readImage();
        }
        if (l == v.length) {
            return v;
        } else {
            Image[] nv = new Image[l];
            System.arraycopy(v, 0, nv, 0, l);
            return nv;
        }
    }

    public final Blob[] readBlobArray() throws IOException {
        int l = readLength();
        Blob[] v = new Blob[Math.min(l, ARRAY_READ_LENGTH_MAX)];
        for (int i = 0; i < l; ++i) {
            if (i >= v.length) {
                Blob[] nv = new Blob[v.length * 2];
                System.arraycopy(v, 0, nv, 0, v.length);
                v = nv;
            }
            v[i] = readBlob();
        }
        if (l == v.length) {
            return v;
        } else {
            Blob[] nv = new Blob[l];
            System.arraycopy(v, 0, nv, 0, l);
            return nv;
        }
    }

    public final Boolean readBooleanRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readBoolean();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Byte readByteRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readByte();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Character readCharacterRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readChar();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Short readShortRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readShort();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Integer readIntegerRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readInt();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Long readLongRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readLong();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Float readFloatRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readFloat();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Double readDoubleRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readDouble();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final UUID readUUIDRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readUUID();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final BigInteger readBigIntegerRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readBigInteger();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final BigDecimal readBigDecimalRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readBigDecimal();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Date readDateRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readDate();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Color readColorRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readColor();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final String readStringRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readString();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Pattern readPatternRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readPattern();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final URL readURLRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readURL();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final <T extends Comparable<T>> Range<T> readRangeRef(Class<T> clazz) throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readRange(clazz);
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Tag readTagRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readTag();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Image readImageRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readImage();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Blob readBlobRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readBlob();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final boolean[] readBooleanArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readBooleanArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final byte[] readByteArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readByteArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final char[] readCharArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readCharArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final short[] readShortArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readShortArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final int[] readIntArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readIntArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final long[] readLongArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readLongArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final float[] readFloatArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readFloatArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final double[] readDoubleArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readDoubleArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final UUID[] readUUIDArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readUUIDArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final BigInteger[] readBigIntegerArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readBigIntegerArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final BigDecimal[] readBigDecimalArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readBigDecimalArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Date[] readDateArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readDateArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Color[] readColorArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readColorArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final String[] readStringArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readStringArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Pattern[] readPatternArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readPatternArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final URL[] readURLArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readURLArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    @SuppressWarnings("rawtypes")
    public final <T extends Comparable<T>> Range[] readRangeArrayRef(Class<T> clazz) throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readRangeArray(clazz);
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Tag[] readTagArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readTagArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Image[] readImageArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readImageArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final Blob[] readBlobArrayRef() throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readBlobArray();
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    @SuppressWarnings("unchecked")
    public final <T> T readObject(Class<T> clazz) throws IOException {
        byte otid = readByte();
        BufferObjectType ot = BufferObjectType.getObjectType(otid);
        if (ot != null) {
            return (T) ot.reader.read(this);
        } else {
            throw new FormatInputException(String.format("Class with id %d is not supported", otid));
        }
    }

    public final <T> T readObjectRef(Class<T> clazz) throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readObject(clazz);
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    @SuppressWarnings("unchecked")
    public final <T> T[] readObjectArray(Class<T> clazz) throws IOException {
        int l = readLength();
        T[] v = (T[]) Array.newInstance(clazz, Math.min(l, ARRAY_READ_LENGTH_MAX));
        for (int i = 0; i < l; ++i) {
            if (i >= v.length) {
                T[] nv = (T[]) Array.newInstance(clazz, v.length * 2);
                System.arraycopy(v, 0, nv, 0, v.length);
                v = nv;
            }
            v[i] = readObject(clazz);
        }
        if (l == v.length) {
            return v;
        } else {
            T[] nv = (T[]) Array.newInstance(clazz, l);
            System.arraycopy(v, 0, nv, 0, l);
            return nv;
        }
    }

    public final <T> T[] readObjectArrayRef(Class<T> clazz) throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readObjectArray(clazz);
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final <C extends Collection<T>, T> C readObjectCollection(C collection, Class<T> clazz) throws IOException {
        int l = readLength();
        for (int i = 0; i < l; ++i) {
            collection.add(readObject(clazz));
        }
        return collection;
    }

    public final <C extends Collection<T>, T> C readObjectCollectionRef(C collection, Class<T> clazz) throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readObjectCollection(collection, clazz);
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final <M extends Map<K, V>, K, V> M readObjectMap(M map, Class<K> keyClass, Class<V> valueClass) throws IOException {
        int l = readLength();
        for (int i = 0; i < l; ++i) {
            K k = readObject(keyClass);
            V v = readObject(valueClass);
            map.put(k, v);
        }
        return map;
    }

    public final <M extends Map<K, V>, K, V> M readObjectMapRef(M map, Class<K> keyClass, Class<V> valueClass) throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return readObjectMap(map, keyClass, valueClass);
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }

    public final <T> T readRef(FormatRefReader<T> reader) throws IOException {
        long s = size;
        byte ref = readByte();
        if (ref == (byte) 1) {
            return reader.read(this);
        } else if (ref == (byte) 0) {
            return null;
        } else {
            throw new FormatInputException(String.format("Illegal format at size %d: illegal reference value %d", s, ref));
        }
    }
}
