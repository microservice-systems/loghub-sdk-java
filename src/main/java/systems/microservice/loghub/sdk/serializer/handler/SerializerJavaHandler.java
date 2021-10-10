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
import systems.microservice.loghub.sdk.serializer.SerializerHandler;
import systems.microservice.loghub.sdk.stream.ByteArrayInputStream;
import systems.microservice.loghub.sdk.stream.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class SerializerJavaHandler implements SerializerHandler, Serializable {
    private static final long serialVersionUID = 1L;

    public SerializerJavaHandler() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(byte[] array, Class<T> clazz) {
        try {
            try (ByteArrayInputStream ain = new ByteArrayInputStream(array)) {
                ObjectInputStream oin = new ObjectInputStream(ain);
                return (T) oin.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(InputStream input, Class<T> clazz) {
        try {
            ObjectInputStream oin = new ObjectInputStream(input);
            return (T) oin.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T read(String string, Class<T> clazz) {
        throw new UnsupportedOperationException(String.format("[%s]: Text format is not supported", Serializer.JAVA));
    }

    @Override
    public <T> T read(Reader reader, Class<T> clazz) {
        throw new UnsupportedOperationException(String.format("[%s]: Text format is not supported", Serializer.JAVA));
    }

    @Override
    public <T> byte[] write(T object) {
        try {
            ByteArrayOutputStream aout = new ByteArrayOutputStream(4096);
            try (ByteArrayOutputStream aout1 = aout) {
                ObjectOutputStream oout = new ObjectOutputStream(aout1);
                oout.writeObject(object);
            }
            return aout.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> OutputStream write(T object, OutputStream output) {
        try {
            ObjectOutputStream oout = new ObjectOutputStream(output);
            oout.writeObject(object);
            return output;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> String writeS(T object) {
        throw new UnsupportedOperationException(String.format("[%s]: Text format is not supported", Serializer.JAVA));
    }

    @Override
    public <T> Writer write(T object, Writer writer) {
        throw new UnsupportedOperationException(String.format("[%s]: Text format is not supported", Serializer.JAVA));
    }
}
