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

package systems.microservice.loghub.sdk.serializer.handler;

import systems.microservice.loghub.sdk.serializer.Serializer;
import systems.microservice.loghub.sdk.serializer.SerializerException;
import systems.microservice.loghub.sdk.serializer.SerializerHandler;
import systems.microservice.loghub.sdk.serializer.SerializerOperation;
import systems.microservice.loghub.sdk.util.ByteArrayInputStream;
import systems.microservice.loghub.sdk.util.ByteArrayOutputStream;
import systems.microservice.loghub.sdk.util.StringBuilderWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class SerializerStringHandler implements SerializerHandler, Serializable {
    private static final long serialVersionUID = 1L;

    public SerializerStringHandler() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(byte[] array, Class<T> clazz) {
        if (!clazz.equals(String.class)) {
            throw new IllegalArgumentException("Argument 'clazz' must be 'String.class'");
        }

        return (T) new String(array, StandardCharsets.UTF_8);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(InputStream input, Class<T> clazz) {
        if (!clazz.equals(String.class)) {
            throw new IllegalArgumentException("Argument 'clazz' must be 'String.class'");
        }

        try {
            String ps = new String();
            ps.load(new InputStreamReader(input, StandardCharsets.UTF_8));
            return (T) ps;
        } catch (IOException e) {
            throw new SerializerException(Serializer.STRING, SerializerOperation.READ, clazz, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(String string, Class<T> clazz) {
        if (!clazz.equals(String.class)) {
            throw new IllegalArgumentException("Argument 'clazz' must be 'String.class'");
        }

        try {
            String ps = new String();
            ps.load(new StringReader(string));
            return (T) ps;
        } catch (IOException e) {
            throw new SerializerException(Serializer.STRING, SerializerOperation.READ, clazz, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(Reader reader, Class<T> clazz) {
        if (!clazz.equals(String.class)) {
            throw new IllegalArgumentException("Argument 'clazz' must be 'String.class'");
        }

        try {
            String ps = new String();
            ps.load(reader);
            return (T) ps;
        } catch (IOException e) {
            throw new SerializerException(Serializer.STRING, SerializerOperation.READ, clazz, e);
        }
    }

    @Override
    public <T> byte[] write(T object) {
        if (!(object instanceof String)) {
            throw new IllegalArgumentException("Argument 'object' must be an instance of 'String.class'");
        }

        String s = (String) object;
        return s.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> OutputStream write(T object, OutputStream output) {
        if (!(object instanceof String)) {
            throw new IllegalArgumentException("Argument 'object' must be an instance of 'String.class'");
        }

        try {
            String ps = (String) object;
            ps.store(new OutputStreamWriter(output, StandardCharsets.UTF_8), null);
            return output;
        } catch (IOException e) {
            throw new SerializerException(Serializer.STRING, SerializerOperation.WRITE, object.getClass(), e);
        }
    }

    @Override
    public <T> String writeS(T object) {
        if (!(object instanceof String)) {
            throw new IllegalArgumentException("Argument 'object' must be an instance of 'String.class'");
        }

        return (String) object;
    }

    @Override
    public <T> Writer write(T object, Writer writer) {
        if (!(object instanceof String)) {
            throw new IllegalArgumentException("Argument 'object' must be an instance of 'String.class'");
        }

        try {
            String ps = (String) object;
            ps.store(writer, null);
            return writer;
        } catch (IOException e) {
            throw new SerializerException(Serializer.STRING, SerializerOperation.WRITE, object.getClass(), e);
        }
    }
}
