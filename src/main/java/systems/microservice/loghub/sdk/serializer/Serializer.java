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

import systems.microservice.loghub.sdk.serializer.handler.SerializerAvroHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerBsonHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerCborHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerCsvHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerHalHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerIonHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerJavaHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerJsonHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerMsgpackHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerPropertiesHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerPropertiesJavaHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerProtobufHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerSmileHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerTomlHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerVelocypackHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerXmlDomHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerXmlHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerYamlHandler;
import systems.microservice.loghub.sdk.util.Argument;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public enum Serializer {
    JAVA(SerializerFormat.BINARY, "application/java-serialized-object", "ser", null, createJavaHandler()),
    CBOR(SerializerFormat.BINARY, "application/cbor", "cbor", "com.fasterxml.jackson.dataformat:jackson-dataformat-cbor", createCborHandler()),
    SMILE(SerializerFormat.BINARY, "application/smile", "smile", "com.fasterxml.jackson.dataformat:jackson-dataformat-smile", createSmileHandler()),
    ION(SerializerFormat.BINARY, "application/ion", "ion", "com.fasterxml.jackson.dataformat:jackson-dataformat-ion", createIonHandler()),
    BSON(SerializerFormat.BINARY, "application/bson", "bson", "de.undercouch:bson4jackson", createBsonHandler()),
    MSGPACK(SerializerFormat.BINARY, "application/msgpack", "msgpack", "org.msgpack:jackson-dataformat-msgpack", createMsgpackHandler()),
    VELOCYPACK(SerializerFormat.BINARY, "application/velocypack", "velocypack", "com.arangodb:jackson-dataformat-velocypack", createVelocypackHandler()),
    PROTOBUF(SerializerFormat.BINARY, "application/protobuf", "protobuf", "com.fasterxml.jackson.dataformat:jackson-dataformat-protobuf", createProtobufHandler()),
    AVRO(SerializerFormat.BINARY, "application/avro", "avro", "com.fasterxml.jackson.dataformat:jackson-dataformat-avro", createAvroHandler()),
    PROPERTIES(SerializerFormat.TEXT, "application/properties", "properties", "com.fasterxml.jackson.dataformat:jackson-dataformat-properties", createPropertiesHandler()),
    PROPERTIES_JAVA(SerializerFormat.TEXT, "application/properties-java", "properties", null, createPropertiesJavaHandler()),
    XML(SerializerFormat.TEXT, "application/xml", "xml", "com.fasterxml.jackson.dataformat:jackson-dataformat-xml", createXmlHandler()),
    XML_DOM(SerializerFormat.TEXT, "application/xml-dom", "xml", null, createXmlDomHandler()),
    JSON(SerializerFormat.TEXT, "application/json", "json", "com.fasterxml.jackson.core:jackson-databind", createJsonHandler()),
    YAML(SerializerFormat.TEXT, "application/yaml", "yaml", "com.fasterxml.jackson.dataformat:jackson-dataformat-yaml", createYamlHandler()),
    TOML(SerializerFormat.TEXT, "application/toml", "toml", "com.fasterxml.jackson.dataformat:jackson-dataformat-toml", createTomlHandler()),
    HAL(SerializerFormat.TEXT, "application/hal", "hal", "io.openapitools.jackson.dataformat:jackson-dataformat-hal", createHalHandler()),
    CSV(SerializerFormat.TEXT, "text/csv", "csv", "com.fasterxml.jackson.dataformat:jackson-dataformat-csv", createCsvHandler());

    public final SerializerFormat format;
    public final String contentType;
    public final String extension;
    public final String dependency;
    public final SerializerHandler handler;

    Serializer(SerializerFormat format, String contentType, String extension, String dependency, SerializerHandler handler) {
        this.format = format;
        this.contentType = contentType;
        this.extension = extension;
        this.dependency = dependency;
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

    private static SerializerJavaHandler createJavaHandler() {
        return new SerializerJavaHandler();
    }

    private static SerializerCborHandler createCborHandler() {
        return new SerializerCborHandler();
    }

    private static SerializerSmileHandler createSmileHandler() {
        return new SerializerSmileHandler();
    }

    private static SerializerIonHandler createIonHandler() {
        return new SerializerIonHandler();
    }

    private static SerializerBsonHandler createBsonHandler() {
        return new SerializerBsonHandler();
    }

    private static SerializerMsgpackHandler createMsgpackHandler() {
        return new SerializerMsgpackHandler();
    }

    private static SerializerVelocypackHandler createVelocypackHandler() {
        return new SerializerVelocypackHandler();
    }

    private static SerializerProtobufHandler createProtobufHandler() {
        return new SerializerProtobufHandler();
    }

    private static SerializerAvroHandler createAvroHandler() {
        return new SerializerAvroHandler();
    }

    private static SerializerPropertiesHandler createPropertiesHandler() {
        return new SerializerPropertiesHandler();
    }

    private static SerializerPropertiesJavaHandler createPropertiesJavaHandler() {
        return new SerializerPropertiesJavaHandler();
    }

    private static SerializerXmlHandler createXmlHandler() {
        return new SerializerXmlHandler();
    }

    private static SerializerXmlDomHandler createXmlDomHandler() {
        return new SerializerXmlDomHandler();
    }

    private static SerializerJsonHandler createJsonHandler() {
        return new SerializerJsonHandler();
    }

    private static SerializerYamlHandler createYamlHandler() {
        return new SerializerYamlHandler();
    }

    private static SerializerTomlHandler createTomlHandler() {
        return new SerializerTomlHandler();
    }

    private static SerializerHalHandler createHalHandler() {
        return new SerializerHalHandler();
    }

    private static SerializerCsvHandler createCsvHandler() {
        return new SerializerCsvHandler();
    }
}
