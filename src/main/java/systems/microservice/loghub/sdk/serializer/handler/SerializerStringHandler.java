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

import systems.microservice.loghub.sdk.serializer.SerializerHandler;
import systems.microservice.loghub.sdk.stream.Stream;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

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

        return (T) Stream.readS(input);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(String string, Class<T> clazz) {
        if (!clazz.equals(String.class)) {
            throw new IllegalArgumentException("Argument 'clazz' must be 'String.class'");
        }

        return (T) string;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(Reader reader, Class<T> clazz) {
        if (!clazz.equals(String.class)) {
            throw new IllegalArgumentException("Argument 'clazz' must be 'String.class'");
        }

        return (T) Stream.readS(reader);
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

        String s = (String) object;
        return Stream.writeS(s, output);
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

        String s = (String) object;
        return Stream.writeS(s, writer);
    }
}
