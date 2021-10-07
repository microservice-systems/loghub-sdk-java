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
import systems.microservice.loghub.sdk.stream.Stream;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class SerializerArrayHandler implements SerializerHandler, Serializable {
    private static final long serialVersionUID = 1L;

    public SerializerArrayHandler() {
    }

    @Override
    public <T> T read(byte[] array, Class<T> clazz) {
        if (!clazz.equals(byte[].class)) {
            throw new IllegalArgumentException("Argument 'clazz' must be 'byte[].class'");
        }

        try {
            return (T) array.clone();
        } catch (Exception e) {
            throw new SerializerException(Serializer.ARRAY, SerializerOperation.READ, clazz, e);
        }
    }

    @Override
    public <T> T read(InputStream input, Class<T> clazz) {
        if (!clazz.equals(byte[].class)) {
            throw new IllegalArgumentException("Argument 'clazz' must be 'byte[].class'");
        }

        try {
            return (T) Stream.read(input);
        } catch (Exception e) {
            throw new SerializerException(Serializer.ARRAY, SerializerOperation.READ, clazz, e);
        }
    }

    @Override
    public <T> T read(String string, Class<T> clazz) {
        throw new UnsupportedOperationException(String.format("[%s][%s]: Text format is not supported", Serializer.ARRAY, SerializerOperation.READ));
    }

    @Override
    public <T> T read(Reader reader, Class<T> clazz) {
        throw new UnsupportedOperationException(String.format("[%s][%s]: Text format is not supported", Serializer.ARRAY, SerializerOperation.READ));
    }

    @Override
    public <T> byte[] write(T object) {
        if (!(object instanceof byte[])) {
            throw new IllegalArgumentException("Argument 'object' must be an instance of 'byte[].class'");
        }

        try {
            byte[] a = (byte[]) object;
            return a.clone();
        } catch (Exception e) {
            throw new SerializerException(Serializer.ARRAY, SerializerOperation.WRITE, object.getClass(), e);
        }
    }

    @Override
    public <T> OutputStream write(T object, OutputStream output) {
        if (!(object instanceof byte[])) {
            throw new IllegalArgumentException("Argument 'object' must be an instance of 'byte[].class'");
        }

        try {
            byte[] a = (byte[]) object;
            return Stream.write(a, output);
        } catch (Exception e) {
            throw new SerializerException(Serializer.ARRAY, SerializerOperation.WRITE, object.getClass(), e);
        }
    }

    @Override
    public <T> String writeS(T object) {
        throw new UnsupportedOperationException(String.format("[%s][%s]: Text format is not supported", Serializer.ARRAY, SerializerOperation.WRITE));
    }

    @Override
    public <T> Writer write(T object, Writer writer) {
        throw new UnsupportedOperationException(String.format("[%s][%s]: Text format is not supported", Serializer.ARRAY, SerializerOperation.WRITE));
    }
}
