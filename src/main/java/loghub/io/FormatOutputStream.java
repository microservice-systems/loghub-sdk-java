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
import java.io.OutputStream;
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
public class FormatOutputStream extends OutputStream {
    protected final OutputStream output;
    protected boolean closed;

    public FormatOutputStream(OutputStream output) {
        this.output = output;
        this.closed = false;
    }

    public OutputStream getOutput() {
        return output;
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public void write(int b) throws IOException {
        if (!closed) {
            if (output != null) {
                output.write(b);
            }
        } else {
            throw new IllegalStateException("FormatOutputStream is closed");
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        Validator.notNull("b", b);
        Validator.inRangeInt("off", off, 0, b.length);
        Validator.inRangeInt("len", len, 0, b.length - off);

        if (!closed) {
            if (output != null) {
                output.write(b, off, len);
            }
        } else {
            throw new IllegalStateException("FormatOutputStream is closed");
        }
    }

    @Override
    public void flush() throws IOException {
        if (!closed) {
            if (output != null) {
                output.flush();
            }
        } else {
            throw new IllegalStateException("FormatOutputStream is closed");
        }
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            if (output != null) {
                output.close();
            }
            closed = true;
        }
    }

    public final void writeVersion(byte value) throws IOException {
        writeByte(value);
    }

    public final void writeLength(int value) throws IOException {
        writeInt(value);
    }

    public final void writeBoolean(boolean value) throws IOException {
        writeByte((byte) (value ? 1 : 0));
    }

    public final void writeByte(byte value) throws IOException {
        write(value);
    }

    public final void writeChar(char value) throws IOException {
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
            writeByte(b);
        }
    }

    public final void writeShort(short value) throws IOException {
        int v = value;
        writeByte((byte) (v >> 8));
        writeByte((byte) (v));
    }

    public final void writeInt(int value) throws IOException {
        writeByte((byte) (value >> 24));
        writeByte((byte) (value >> 16));
        writeByte((byte) (value >> 8));
        writeByte((byte) (value));
    }

    public final void writeLong(long value) throws IOException {
        writeByte((byte) (value >> 56));
        writeByte((byte) (value >> 48));
        writeByte((byte) (value >> 40));
        writeByte((byte) (value >> 32));
        writeByte((byte) (value >> 24));
        writeByte((byte) (value >> 16));
        writeByte((byte) (value >> 8));
        writeByte((byte) (value));
    }

    public final void writeFloat(float value) throws IOException {
        writeInt(Float.floatToRawIntBits(value));
    }

    public final void writeDouble(double value) throws IOException {
        writeLong(Double.doubleToRawLongBits(value));
    }

    public final void writeUUID(UUID value) throws IOException {
        Validator.notNull("value", value);

        writeLong(value.getMostSignificantBits());
        writeLong(value.getLeastSignificantBits());
    }

    public final void writeBigInteger(BigInteger value) throws IOException {
        Validator.notNull("value", value);

        writeByteArray(value.toByteArray());
    }

    public final void writeBigDecimal(BigDecimal value) throws IOException {
        Validator.notNull("value", value);

        writeString(value.toString());
    }

    public final void writeDate(Date value) throws IOException {
        Validator.notNull("value", value);

        writeLong(value.getTime());
    }

    public final void writeColor(Color value) throws IOException {
        Validator.notNull("value", value);

        writeByte((byte) (value.r - ((short) 128)));
        writeByte((byte) (value.g - ((short) 128)));
        writeByte((byte) (value.b - ((short) 128)));
        writeByte((byte) (value.a - ((short) 128)));
    }

    public final void writeString(String value) throws IOException {
        Validator.notNull("value", value);

        int ci = value.length();
        writeLength(ci);
        for (int i = 0; i < ci; ++i) {
            writeChar(value.charAt(i));
        }
    }

    public final void writeString(String value, String param1) throws IOException {
        Validator.notNull("value", value);
        if (param1 == null) {
            param1 = "null";
        }

        int idx = xxxxx;
        int l = 0;
        int ci = value.length();
        writeLength(0);
        for (int i = 0, j = 1; i < ci; ++i) {
            char c = value.charAt(i);
            if (c != '{') {
                writeChar(c);
                l++;
            } else if ((i + 1 < ci) && (value.charAt(i + 1) == '}')) {
                String p = "{}";
                if (j == 1) {
                    p = param1;
                    j++;
                }
                for (int k = 0, ck = p.length(); k < ck; ++k) {
                    writeChar(p.charAt(k));
                    l++;
                }
                i++;
            } else {
                writeChar(c);
                l++;
            }
        }
        writeLength(buffer, idx, l);
    }

    public final void writeString(String value, String param1, String param2) throws IOException {
        Validator.notNull("value", value);
        if (param1 == null) {
            param1 = "null";
        }
        if (param2 == null) {
            param2 = "null";
        }

        int idx = xxxxx;
        int l = 0;
        int ci = value.length();
        writeLength(0);
        for (int i = 0, j = 1; i < ci; ++i) {
            char c = value.charAt(i);
            if (c != '{') {
                writeChar(c);
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
                    writeChar(p.charAt(k));
                    l++;
                }
                i++;
            } else {
                writeChar(c);
                l++;
            }
        }
        writeLength(buffer, idx, l);
    }

    public final void writeString(String value, String param1, String param2, String param3) throws IOException {
        Validator.notNull("value", value);
        if (param1 == null) {
            param1 = "null";
        }
        if (param2 == null) {
            param2 = "null";
        }
        if (param3 == null) {
            param3 = "null";
        }

        int idx = xxxxx;
        int l = 0;
        int ci = value.length();
        writeLength(0);
        for (int i = 0, j = 1; i < ci; ++i) {
            char c = value.charAt(i);
            if (c != '{') {
                writeChar(c);
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
                    writeChar(p.charAt(k));
                    l++;
                }
                i++;
            } else {
                writeChar(c);
                l++;
            }
        }
        writeLength(buffer, idx, l);
    }

    public final void writeString(String value, String param1, String param2, String param3, String param4) throws IOException {
        Validator.notNull("value", value);
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

        int idx = xxxxx;
        int l = 0;
        int ci = value.length();
        writeLength(0);
        for (int i = 0, j = 1; i < ci; ++i) {
            char c = value.charAt(i);
            if (c != '{') {
                writeChar(c);
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
                    writeChar(p.charAt(k));
                    l++;
                }
                i++;
            } else {
                writeChar(c);
                l++;
            }
        }
        writeLength(buffer, idx, l);
    }

    public final void writeString(String value, String param1, String param2, String param3, String param4, String param5) throws IOException {
        Validator.notNull("value", value);
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

        int idx = xxxxx;
        int l = 0;
        int ci = value.length();
        writeLength(0);
        for (int i = 0, j = 1; i < ci; ++i) {
            char c = value.charAt(i);
            if (c != '{') {
                writeChar(c);
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
                    writeChar(p.charAt(k));
                    l++;
                }
                i++;
            } else {
                writeChar(c);
                l++;
            }
        }
        writeLength(buffer, idx, l);
    }

    public final void writePattern(Pattern value) throws IOException {
        Validator.notNull("value", value);

        writeString(value.pattern());
    }

    public final void writeURL(URL value) throws IOException {
        Validator.notNull("value", value);

        writeString(value.toExternalForm());
    }

    public final <T extends Comparable<T>> void writeRange(Range<T> value) throws IOException {
        Validator.notNull("value", value);

        writeObject(value.min);
        writeObject(value.max);
    }

    public final void writeTag(Tag value) throws IOException {
        Validator.notNull("value", value);

        writeString(value.key);
        writeObject(value.value);
        writeStringRef(value.unit);
    }

    public final void writeImage(Image value) throws IOException {
        Validator.notNull("value", value);

        writeByteArray(value.content);
        writeString(value.contentType);
    }

    public final void writeBlob(Blob value) throws IOException {
        Validator.notNull("value", value);

        writeByteArray(value.content);
        writeString(value.contentType);
    }

    public final void writeBooleanArray(boolean[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeBoolean(value[i]);
        }
    }

    public final void writeByteArray(byte[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeByte(value[i]);
        }
    }

    public final void writeCharArray(char[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeChar(value[i]);
        }
    }

    public final void writeShortArray(short[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeShort(value[i]);
        }
    }

    public final void writeIntArray(int[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeInt(value[i]);
        }
    }

    public final void writeLongArray(long[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeLong(value[i]);
        }
    }

    public final void writeFloatArray(float[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeFloat(value[i]);
        }
    }

    public final void writeDoubleArray(double[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeDouble(value[i]);
        }
    }

    public final void writeUUIDArray(UUID[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeUUID(value[i]);
        }
    }

    public final void writeBigIntegerArray(BigInteger[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeBigInteger(value[i]);
        }
    }

    public final void writeBigDecimalArray(BigDecimal[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeBigDecimal(value[i]);
        }
    }

    public final void writeDateArray(Date[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeDate(value[i]);
        }
    }

    public final void writeColorArray(Color[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeColor(value[i]);
        }
    }

    public final void writeStringArray(String[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeString(value[i]);
        }
    }

    public final void writePatternArray(Pattern[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writePattern(value[i]);
        }
    }

    public final void writeURLArray(URL[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeURL(value[i]);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public final void writeRangeArray(Range[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeRange(value[i]);
        }
    }

    public final void writeTagArray(Tag[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeTag(value[i]);
        }
    }

    public final void writeImageArray(Image[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeImage(value[i]);
        }
    }

    public final void writeBlobArray(Blob[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeBlob(value[i]);
        }
    }

    public final void writeBooleanRef(Boolean value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeBoolean(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeByteRef(Byte value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeByte(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeCharacterRef(Character value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeChar(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeShortRef(Short value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeShort(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeIntegerRef(Integer value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeInt(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeLongRef(Long value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeLong(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeFloatRef(Float value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeFloat(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeDoubleRef(Double value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeDouble(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeUUIDRef(UUID value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeUUID(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeBigIntegerRef(BigInteger value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeBigInteger(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeBigDecimalRef(BigDecimal value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeBigDecimal(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeDateRef(Date value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeDate(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeColorRef(Color value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeColor(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeStringRef(String value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeString(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeStringRef(String value, String param1) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeString(value, param1);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeStringRef(String value, String param1, String param2) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeString(value, param1, param2);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeStringRef(String value, String param1, String param2, String param3) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeString(value, param1, param2, param3);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeStringRef(String value, String param1, String param2, String param3, String param4) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeString(value, param1, param2, param3, param4);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeStringRef(String value, String param1, String param2, String param3, String param4, String param5) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeString(value, param1, param2, param3, param4, param5);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writePatternRef(Pattern value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writePattern(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeURLRef(URL value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeURL(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final <T extends Comparable<T>> void writeRangeRef(Range<T> value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeRange(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeTagRef(Tag value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeTag(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeImageRef(Image value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeImage(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeBlobRef(Blob value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeBlob(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeBooleanArrayRef(boolean[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeBooleanArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeByteArrayRef(byte[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeByteArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeCharArrayRef(char[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeCharArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeShortArrayRef(short[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeShortArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeIntArrayRef(int[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeIntArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeLongArrayRef(long[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeLongArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeFloatArrayRef(float[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeFloatArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeDoubleArrayRef(double[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeDoubleArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeUUIDArrayRef(UUID[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeUUIDArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeBigIntegerArrayRef(BigInteger[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeBigIntegerArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeBigDecimalArrayRef(BigDecimal[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeBigDecimalArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeDateArrayRef(Date[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeDateArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeColorArrayRef(Color[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeColorArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeStringArrayRef(String[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeStringArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writePatternArrayRef(Pattern[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writePatternArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeURLArrayRef(URL[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeURLArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    @SuppressWarnings("rawtypes")
    public final void writeRangeArrayRef(Range[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeRangeArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeTagArrayRef(Tag[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeTagArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeImageArrayRef(Image[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeImageArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeBlobArrayRef(Blob[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeBlobArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    @SuppressWarnings("rawtypes")
    public final void writeObject(Object value) throws IOException {
        Validator.notNull("value", value);

        Class c = value.getClass();
        BufferObjectType ot = BufferObjectType.getObjectType(c);
        if (ot != null) {
            writeByte(ot.id);
            ot.writer.write(value);
        } else {
            throw new IllegalArgumentException(String.format("Class '%s' is not supported", c.getCanonicalName()));
        }
    }

    public final void writeObjectRef(Object value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeObject(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final void writeObjectArray(Object[] value) throws IOException {
        Validator.notNull("value", value);

        int l = value.length;
        writeLength(l);
        for (int i = 0; i < l; ++i) {
            writeObject(value[i]);
        }
    }

    public final void writeObjectArrayRef(Object[] value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeObjectArray(value);
        } else {
            writeByte((byte) 0);
        }
    }

    @SuppressWarnings("rawtypes")
    public final void writeObjectCollection(Collection value) throws IOException {
        Validator.notNull("value", value);

        int l = value.size();
        writeLength(l);
        for (Object e : value) {
            writeObject(e);
        }
    }

    @SuppressWarnings("rawtypes")
    public final void writeObjectCollectionRef(Collection value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeObjectCollection(value);
        } else {
            writeByte((byte) 0);
        }
    }

    @SuppressWarnings("rawtypes")
    public final void writeObjectMap(Map value) throws IOException {
        Validator.notNull("value", value);

        int l = value.size();
        writeLength(l);
        for (Object e : value.entrySet()) {
            Map.Entry en = (Map.Entry) e;
            writeObject(en.getKey());
            writeObject(en.getValue());
        }
    }

    @SuppressWarnings("rawtypes")
    public final void writeObjectMapRef(Map value) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writeObjectMap(value);
        } else {
            writeByte((byte) 0);
        }
    }

    public final <T> void writeRef(T value, FormatRefWriter<T> writer) throws IOException {
        if (value != null) {
            writeByte((byte) 1);
            writer.write(this, value);
        } else {
            writeByte((byte) 0);
        }
    }
}
