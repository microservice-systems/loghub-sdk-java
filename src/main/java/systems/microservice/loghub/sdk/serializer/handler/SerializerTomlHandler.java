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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.toml.TomlFactory;
import systems.microservice.loghub.sdk.serializer.Serializer;
import systems.microservice.loghub.sdk.serializer.SerializerException;
import systems.microservice.loghub.sdk.serializer.SerializerHandler;
import systems.microservice.loghub.sdk.serializer.SerializerOperation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class SerializerTomlHandler implements SerializerHandler, Serializable {
    private static final long serialVersionUID = 1L;

    protected final TomlFactory factory = new TomlFactory();
    protected final ObjectMapper mapper = new ObjectMapper(factory).disable(JsonParser.Feature.AUTO_CLOSE_SOURCE).disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);

    public SerializerTomlHandler() {
    }

    @Override
    public <T> T read(byte[] array, Class<T> clazz) {
        try {
            return mapper.readValue(array, clazz);
        } catch (IOException e) {
            throw new SerializerException(Serializer.TOML, SerializerOperation.READ, clazz, e);
        }
    }

    @Override
    public <T> T read(InputStream input, Class<T> clazz) {
        try {
            return mapper.readValue(input, clazz);
        } catch (IOException e) {
            throw new SerializerException(Serializer.TOML, SerializerOperation.READ, clazz, e);
        }
    }

    @Override
    public <T> T read(String string, Class<T> clazz) {
        try {
            return mapper.readValue(string, clazz);
        } catch (IOException e) {
            throw new SerializerException(Serializer.TOML, SerializerOperation.READ, clazz, e);
        }
    }

    @Override
    public <T> T read(Reader reader, Class<T> clazz) {
        try {
            return mapper.readValue(reader, clazz);
        } catch (IOException e) {
            throw new SerializerException(Serializer.TOML, SerializerOperation.READ, clazz, e);
        }
    }

    @Override
    public <T> byte[] write(T object) {
        try {
            return mapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new SerializerException(Serializer.TOML, SerializerOperation.WRITE, object.getClass(), e);
        }
    }

    @Override
    public <T> OutputStream write(T object, OutputStream output) {
        try {
            mapper.writeValue(output, object);
            return output;
        } catch (IOException e) {
            throw new SerializerException(Serializer.TOML, SerializerOperation.WRITE, object.getClass(), e);
        }
    }

    @Override
    public <T> String writeS(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new SerializerException(Serializer.TOML, SerializerOperation.WRITE, object.getClass(), e);
        }
    }

    @Override
    public <T> Writer write(T object, Writer writer) {
        try {
            mapper.writeValue(writer, object);
            return writer;
        } catch (IOException e) {
            throw new SerializerException(Serializer.TOML, SerializerOperation.WRITE, object.getClass(), e);
        }
    }
}
