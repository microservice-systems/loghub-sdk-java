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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.avro.AvroFactory;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import systems.microservice.loghub.sdk.serializer.Serializer;
import systems.microservice.loghub.sdk.serializer.SerializerHandler;
import systems.microservice.loghub.sdk.util.MapUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class SerializerAvroHandler implements SerializerHandler, Serializable {
    private static final long serialVersionUID = 1L;

    protected final AvroFactory factory = new AvroFactory();
    protected final AvroMapper mapper = (AvroMapper) new AvroMapper(factory).disable(JsonParser.Feature.AUTO_CLOSE_SOURCE).disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
    protected final ConcurrentHashMap<Class<?>, AvroSchema> schemas = new ConcurrentHashMap<>(256, 0.75f, 4);

    public SerializerAvroHandler() {
    }

    protected <T> AvroSchema getSchema(Class<T> clazz) throws JsonMappingException {
        AvroSchema s = schemas.get(clazz);
        if (s == null) {
            s = MapUtil.putIfAbsent(schemas, clazz, mapper.schemaFor(clazz));
        }
        return s;
    }

    @Override
    public <T> T read(byte[] array, Class<T> clazz) {
        try {
            return mapper.readerFor(clazz).with(getSchema(clazz)).readValue(array);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T read(InputStream input, Class<T> clazz) {
        try {
            return mapper.readerFor(clazz).with(getSchema(clazz)).readValue(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T read(String string, Class<T> clazz) {
        throw new UnsupportedOperationException(String.format("[%s]: Text format is not supported", Serializer.AVRO));
    }

    @Override
    public <T> T read(Reader reader, Class<T> clazz) {
        throw new UnsupportedOperationException(String.format("[%s]: Text format is not supported", Serializer.AVRO));
    }

    @Override
    public <T> byte[] write(T object) {
        Class<?> c = object.getClass();
        try {
            return mapper.writer(getSchema(c)).writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> OutputStream write(T object, OutputStream output) {
        Class<?> c = object.getClass();
        try {
            mapper.writer(getSchema(c)).writeValue(output, object);
            return output;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> String writeS(T object) {
        throw new UnsupportedOperationException(String.format("[%s]: Text format is not supported", Serializer.AVRO));
    }

    @Override
    public <T> Writer write(T object, Writer writer) {
        throw new UnsupportedOperationException(String.format("[%s]: Text format is not supported", Serializer.AVRO));
    }
}
