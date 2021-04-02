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

import java.lang.reflect.InvocationTargetException;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class BufferReader {
    private final byte[] buffer;
    private int index;

    public BufferReader(byte[] buffer) {
        this(buffer, 0);
    }

    public BufferReader(byte[] buffer, int index) {
        Argument.notNull("buffer", buffer);
        Argument.inRangeInt("index", index, 0, buffer.length);

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
        Argument.inRangeInt("index", index, 0, buffer.length);

        this.index = index;
    }

    public void skip(int count) {
        Argument.inRangeInt("count", count, -index, buffer.length - index);

        this.index += count;
    }

    public byte readByte() {
        byte v = buffer[index];
        index++;
        return v;
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

    public char readChar() {
        int v = 0;
        int sh = 0;
        byte b = -1;
        for (; b < 0; sh += 7) {
            if (sh > 14) {
                throw new BufferException(String.format("Buffer of size %d has illegal format at index %d: read char of length larger than 3 bytes", buffer.length, index));
            }
            b = readByte();
            int l = b;
            l = l & 0x7F;
            l = l << sh;
            v = v | l;
        }
        return (char) v;
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

    public <T extends Bufferable> T readBufferable(Class<T> clazz) {
        try {
            return clazz.getConstructor(BufferReader.class).newInstance(this);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BufferException(e);
        }
    }
}
