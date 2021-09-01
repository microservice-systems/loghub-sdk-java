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

package systems.microservice.loghub.sdk.serializer;

import systems.microservice.loghub.sdk.util.Argument;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public enum Serializer {
    JAVA(SerializerFormat.BINARY, "application/java-serialized-object", "ser", new JavaHandler()),
    CBOR(SerializerFormat.BINARY, "application/cbor", "cbor", new JacksonHandler()),
    SMILE(SerializerFormat.BINARY, "application/smile", "smile", new JacksonHandler()),
    ION(SerializerFormat.BINARY, "application/ion", "ion", new JacksonHandler()),
    MSGPACK(SerializerFormat.BINARY, "application/msgpack", "msgpack", new JacksonHandler()),
    VELOCYPACK(SerializerFormat.BINARY, "application/velocypack", "velocypack", new JacksonHandler()),
    PROTOBUF(SerializerFormat.BINARY, "application/protobuf", "protobuf", new JacksonHandler()),
    AVRO(SerializerFormat.BINARY, "application/avro", "avro", new JacksonHandler()),
    PROPERTIES(SerializerFormat.TEXT, "application/properties", "properties", new JacksonHandler()),
    PROPERTIES_JAVA(SerializerFormat.TEXT, "application/properties-java", "properties", new JavaPropertiesHandler()),
    XML(SerializerFormat.TEXT, "application/xml", "xml", new JacksonHandler()),
    XML_DOM(SerializerFormat.TEXT, "application/xml-dom", "xml", new JavaXmlHandler()),
    JSON(SerializerFormat.TEXT, "application/json", "json", new JacksonHandler()),
    YAML(SerializerFormat.TEXT, "application/yaml", "yaml", new JacksonHandler()),
    TOML(SerializerFormat.TEXT, "application/toml", "toml", new JacksonHandler()),
    HAL(SerializerFormat.TEXT, "application/hal", "hal", new JacksonHandler()),
    CSV(SerializerFormat.TEXT, "text/csv", "csv", new JacksonHandler());

    public final SerializerFormat format;
    public final String contentType;
    public final String extension;
    public final SerializerHandler handler;

    Serializer(SerializerFormat format, String contentType, String extension, SerializerHandler handler) {
        this.format = format;
        this.contentType = contentType;
        this.extension = extension;
        this.handler = handler;
    }

    public <T> T read(byte[] array, Class<T> clazz) {
        Argument.notNull("array", array);
        Argument.notNull("clazz", clazz);

        return handler.read(array, clazz);
    }

    public <T> T read(InputStream input, Class<T> clazz) {
        Argument.notNull("input", input);
        Argument.notNull("clazz", clazz);

        return handler.read(input, clazz);
    }

    public <T> T read(String string, Class<T> clazz) {
        Argument.notNull("string", string);
        Argument.notNull("clazz", clazz);

        return handler.read(string, clazz);
    }

    public <T> T read(Reader reader, Class<T> clazz) {
        Argument.notNull("reader", reader);
        Argument.notNull("clazz", clazz);

        return handler.read(reader, clazz);
    }

    public <T> byte[] write(T object) {
        Argument.notNull("object", object);

        return handler.write(object);
    }

    public <T> OutputStream write(T object, OutputStream output) {
        Argument.notNull("object", object);
        Argument.notNull("output", output);

        return handler.write(object, output);
    }

    public <T> String writeS(T object) {
        Argument.notNull("object", object);

        return handler.writeS(object);
    }

    public <T> Writer write(T object, Writer writer) {
        Argument.notNull("object", object);
        Argument.notNull("writer", writer);

        return handler.write(object, writer);
    }
}
