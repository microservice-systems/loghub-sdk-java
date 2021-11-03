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

package systems.microservice.loghub.sdk.stream;

import systems.microservice.loghub.facade.Validator;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class ByteArrayOutputStream extends OutputStream {
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    protected byte[] buffer;
    protected int count;

    public ByteArrayOutputStream() {
        this(4096);
    }

    public ByteArrayOutputStream(int size) {
        Validator.inRangeInt("size", size, 0, Integer.MAX_VALUE);

        this.buffer = new byte[size];
        this.count = 0;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity - buffer.length > 0) {
            grow(minCapacity);
        }
    }

    private void grow(int minCapacity) {
        int oldCapacity = buffer.length;
        int newCapacity = oldCapacity << 1;
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            newCapacity = hugeCapacity(minCapacity);
        }
        buffer = Arrays.copyOf(buffer, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) {
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
    }

    @Override
    public void write(int b) throws IOException {
        ensureCapacity(count + 1);
        buffer[count] = (byte) b;
        count += 1;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        Validator.notNull("b", b);
        if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) - b.length > 0)) {
            throw new IndexOutOfBoundsException();
        }

        ensureCapacity(count + len);
        System.arraycopy(b, off, buffer, count, len);
        count += len;
    }

    public void writeTo(OutputStream out) throws IOException {
        Validator.notNull("out", out);

        out.write(buffer, 0, count);
    }

    public void reset() {
        count = 0;
    }

    public byte[] toByteArray() {
        return Arrays.copyOf(buffer, count);
    }

    public int size() {
        return count;
    }

    @Override
    public String toString() {
        return new String(buffer, 0, count);
    }

    public String toString(String charsetName) {
        Validator.notNull("charsetName", charsetName);

        try {
            return new String(buffer, 0, count, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
    }
}
