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

    private static int read(InputStream input, byte[] array, int offset, int length) throws IOException {
        int c = 0;
        for (int bc = input.read(array, offset, length); (length > 0) && (bc != -1); bc = input.read(array, offset, length)) {
            c += bc;
            offset += bc;
            length -= bc;
        }
        return c;
    }

    private static int read(Reader reader, char[] array, int offset, int length) throws IOException {
        int c = 0;
        for (int bc = reader.read(array, offset, length); (length > 0) && (bc != -1); bc = reader.read(array, offset, length)) {
            c += bc;
            offset += bc;
            length -= bc;
        }
        return c;
    }

    public static byte[] read(InputStream input) {
        Argument.notNull("input", input);

        int c = 0;
        try {
            byte[] a = new byte[4096];
            for (int l = a.length, bc = read(input, a, 0, l); bc == l; bc = read(input, a, l, l)) {
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
            throw new RuntimeException(e);
        }
    }

    public static char[] read(Reader reader) {
        Argument.notNull("reader", reader);

        int c = 0;
        try {
            char[] a = new char[4096];
            for (int l = a.length, bc = read(reader, a, 0, l); bc == l; bc = read(reader, a, l, l)) {
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
            throw new RuntimeException(e);
        }
    }

    public static String readS(InputStream input) {
        Argument.notNull("input", input);

        return readS(input, StandardCharsets.UTF_8);
    }

    public static String readS(InputStream input, Charset charset) {
        Argument.notNull("input", input);
        Argument.notNull("charset", charset);

        return new String(read(input), charset);
    }

    public static String readS(Reader reader) {
        Argument.notNull("reader", reader);

        return new String(read(reader));
    }

    public static OutputStream write(byte[] array, OutputStream output) {
        Argument.notNull("array", array);
        Argument.notNull("output", output);

        try {
            output.write(array);
            return output;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Writer write(char[] array, Writer writer) {
        Argument.notNull("array", array);
        Argument.notNull("writer", writer);

        try {
            writer.write(array);
            return writer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static OutputStream writeS(String string, OutputStream output) {
        Argument.notNull("string", string);
        Argument.notNull("output", output);

        return writeS(string, output, StandardCharsets.UTF_8);
    }

    public static OutputStream writeS(String string, OutputStream output, Charset charset) {
        Argument.notNull("string", string);
        Argument.notNull("output", output);
        Argument.notNull("charset", charset);

        return write(string.getBytes(charset), output);
    }

    public static Writer writeS(String string, Writer writer) {
        Argument.notNull("string", string);
        Argument.notNull("writer", writer);

        return write(string.toCharArray(), writer);
    }

    public static OutputStream copy(InputStream input, OutputStream output) {
        Argument.notNull("input", input);
        Argument.notNull("output", output);

        long c = 0L;
        try {
            byte[] a = new byte[4096];
            for (int l = a.length, bc = read(input, a, 0, l); bc == l; bc = read(input, a, 0, l)) {
                output.write(a, 0, bc);
                c += bc;
            }
            return output;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Writer copy(Reader reader, Writer writer) {
        Argument.notNull("reader", reader);
        Argument.notNull("writer", writer);

        long c = 0L;
        try {
            char[] a = new char[4096];
            for (int l = a.length, bc = read(reader, a, 0, l); bc == l; bc = read(reader, a, 0, l)) {
                writer.write(a, 0, bc);
                c += bc;
            }
            return writer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
