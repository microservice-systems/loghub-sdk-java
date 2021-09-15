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

import systems.microservice.loghub.sdk.util.Argument;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class Stream {
    private Stream() {
    }

    private static int readBytes(InputStream input, byte[] array, int offset, int length) throws IOException {
        int c = 0;
        for (int bc = input.read(array, offset, length); (length > 0) && (bc != -1); bc = input.read(array, offset, length)) {
            c += bc;
            offset += bc;
            length -= bc;
        }
        return c;
    }

    private static int readChars(Reader reader, char[] array, int offset, int length) throws IOException {
        int c = 0;
        for (int bc = reader.read(array, offset, length); (length > 0) && (bc != -1); bc = reader.read(array, offset, length)) {
            c += bc;
            offset += bc;
            length -= bc;
        }
        return c;
    }

    public static byte[] readArray(InputStream input) {
        Argument.notNull("input", input);

        int c = 0;
        try {
            byte[] a = new byte[4096];
            for (int l = a.length, bc = readBytes(input, a, 0, l); bc == l; bc = readBytes(input, a, l, l)) {
                l = a.length;
                byte[] b = new byte[l * 2];
                System.arraycopy(a, 0, b, 0, l);
                a = b;
                c += bc;
            }
            byte[] r = new byte[c];
            System.arraycopy(a, 0, r, 0, c);
            return r;
        } catch (IOException e) {
            throw new StreamException(StreamOperation.READ, c, e);
        }
    }

    public static char[] readArray(Reader reader) {
        Argument.notNull("reader", reader);

        int c = 0;
        try {
            char[] a = new char[4096];
            for (int l = a.length, bc = readChars(reader, a, 0, l); bc == l; bc = readChars(reader, a, l, l)) {
                l = a.length;
                char[] b = new char[l * 2];
                System.arraycopy(a, 0, b, 0, l);
                a = b;
                c += bc;
            }
            char[] r = new char[c];
            System.arraycopy(a, 0, r, 0, c);
            return r;
        } catch (IOException e) {
            throw new StreamException(StreamOperation.READ, c, e);
        }
    }

    public static String readString(InputStream input) {
        Argument.notNull("input", input);

        return readString(input, StandardCharsets.UTF_8);
    }

    public static String readString(InputStream input, Charset charset) {
        Argument.notNull("input", input);
        Argument.notNull("charset", charset);

        return new String(readArray(input), charset);
    }

    public static String readString(Reader reader) {
        Argument.notNull("reader", reader);

        return new String(readArray(reader));
    }

    public static OutputStream writeArray(byte[] array, OutputStream output) {
        Argument.notNull("array", array);
        Argument.notNull("output", output);

        try {
            output.write(array);
            return output;
        } catch (IOException e) {
            throw new StreamException(StreamOperation.WRITE, e);
        }
    }

    public static Writer writeArray(char[] array, Writer writer) {
        Argument.notNull("array", array);
        Argument.notNull("writer", writer);

        try {
            writer.write(array);
            return writer;
        } catch (IOException e) {
            throw new StreamException(StreamOperation.WRITE, e);
        }
    }

    public static OutputStream writeString(String string, OutputStream output) {
        Argument.notNull("string", string);
        Argument.notNull("output", output);

        return writeString(string, output, StandardCharsets.UTF_8);
    }

    public static OutputStream writeString(String string, OutputStream output, Charset charset) {
        Argument.notNull("string", string);
        Argument.notNull("output", output);
        Argument.notNull("charset", charset);

        return writeArray(string.getBytes(charset), output);
    }

    public static Writer writeString(String string, Writer writer) {
        Argument.notNull("string", string);
        Argument.notNull("writer", writer);

        return writeArray(string.toCharArray(), writer);
    }
}
