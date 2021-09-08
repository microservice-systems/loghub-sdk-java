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
import systems.microservice.loghub.sdk.serializer.handler.SerializerDocumentHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerHalHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerIonHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerJavaHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerJsonHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerMsgpackHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerPropertiesHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerPropsHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerProtobufHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerSmileHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerTomlHandler;
import systems.microservice.loghub.sdk.serializer.handler.SerializerVelocypackHandler;
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
    PROPERTIES(SerializerFormat.TEXT, "application/properties", "properties", null, createPropertiesHandler()),
    PROPS(SerializerFormat.TEXT, "application/properties", "properties", "com.fasterxml.jackson.dataformat:jackson-dataformat-properties", createPropsHandler()),
    DOCUMENT(SerializerFormat.TEXT, "application/xml", "xml", null, createDocumentHandler()),
    XML(SerializerFormat.TEXT, "application/xml", "xml", "com.fasterxml.jackson.dataformat:jackson-dataformat-xml", createXmlHandler()),
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

        if (handler != null) {
            return handler.read(array, clazz);
        } else {
            throw new SerializerException(this, SerializerOperation.READ, String.format("To serialize %s please include '%s' dependency", this, this.dependency));
        }
    }

    public <T> T read(InputStream input, Class<T> clazz) {
        Argument.notNull("input", input);
        Argument.notNull("clazz", clazz);

        if (handler != null) {
            return handler.read(input, clazz);
        } else {
            throw new SerializerException(this, SerializerOperation.READ, String.format("To serialize %s please include '%s' dependency", this, this.dependency));
        }
    }

    public <T> T read(String string, Class<T> clazz) {
        Argument.notNull("string", string);
        Argument.notNull("clazz", clazz);

        if (handler != null) {
            return handler.read(string, clazz);
        } else {
            throw new SerializerException(this, SerializerOperation.READ, String.format("To serialize %s please include '%s' dependency", this, this.dependency));
        }
    }

    public <T> T read(Reader reader, Class<T> clazz) {
        Argument.notNull("reader", reader);
        Argument.notNull("clazz", clazz);

        if (handler != null) {
            return handler.read(reader, clazz);
        } else {
            throw new SerializerException(this, SerializerOperation.READ, String.format("To serialize %s please include '%s' dependency", this, this.dependency));
        }
    }

    public <T> byte[] write(T object) {
        Argument.notNull("object", object);

        if (handler != null) {
            return handler.write(object);
        } else {
            throw new SerializerException(this, SerializerOperation.WRITE, String.format("To serialize %s please include '%s' dependency", this, this.dependency));
        }
    }

    public <T> OutputStream write(T object, OutputStream output) {
        Argument.notNull("object", object);
        Argument.notNull("output", output);

        if (handler != null) {
            return handler.write(object, output);
        } else {
            throw new SerializerException(this, SerializerOperation.WRITE, String.format("To serialize %s please include '%s' dependency", this, this.dependency));
        }
    }

    public <T> String writeS(T object) {
        Argument.notNull("object", object);

        if (handler != null) {
            return handler.writeS(object);
        } else {
            throw new SerializerException(this, SerializerOperation.WRITE, String.format("To serialize %s please include '%s' dependency", this, this.dependency));
        }
    }

    public <T> Writer write(T object, Writer writer) {
        Argument.notNull("object", object);
        Argument.notNull("writer", writer);

        if (handler != null) {
            return handler.write(object, writer);
        } else {
            throw new SerializerException(this, SerializerOperation.WRITE, String.format("To serialize %s please include '%s' dependency", this, this.dependency));
        }
    }

    private static SerializerJavaHandler createJavaHandler() {
        return new SerializerJavaHandler();
    }

    private static SerializerCborHandler createCborHandler() {
        try {
            Class.forName("com.fasterxml.jackson.dataformat.cbor.CBORFactory", false, Serializer.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
        return new SerializerCborHandler();
    }

    private static SerializerSmileHandler createSmileHandler() {
        try {
            Class.forName("com.fasterxml.jackson.dataformat.smile.SmileFactory", false, Serializer.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
        return new SerializerSmileHandler();
    }

    private static SerializerIonHandler createIonHandler() {
        try {
            Class.forName("com.fasterxml.jackson.dataformat.ion.IonFactory", false, Serializer.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
        return new SerializerIonHandler();
    }

    private static SerializerBsonHandler createBsonHandler() {
        try {
            Class.forName("de.undercouch.bson4jackson.BsonFactory", false, Serializer.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
        return new SerializerBsonHandler();
    }

    private static SerializerMsgpackHandler createMsgpackHandler() {
        try {
            Class.forName("org.msgpack.jackson.dataformat.MessagePackFactory", false, Serializer.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
        return new SerializerMsgpackHandler();
    }

    private static SerializerVelocypackHandler createVelocypackHandler() {
        try {
            Class.forName("com.arangodb.jackson.dataformat.velocypack.VPackFactory", false, Serializer.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
        return new SerializerVelocypackHandler();
    }

    private static SerializerProtobufHandler createProtobufHandler() {
        try {
            Class.forName("com.fasterxml.jackson.dataformat.protobuf.ProtobufFactory", false, Serializer.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
        return new SerializerProtobufHandler();
    }

    private static SerializerAvroHandler createAvroHandler() {
        try {
            Class.forName("com.fasterxml.jackson.dataformat.avro.AvroFactory", false, Serializer.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
        return new SerializerAvroHandler();
    }

    private static SerializerPropertiesHandler createPropertiesHandler() {
        return new SerializerPropertiesHandler();
    }

    private static SerializerPropsHandler createPropsHandler() {
        try {
            Class.forName("", false, Serializer.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
        return new SerializerPropsHandler();
    }

    private static SerializerDocumentHandler createDocumentHandler() {
        return new SerializerDocumentHandler();
    }

    private static SerializerXmlHandler createXmlHandler() {
        try {
            Class.forName("", false, Serializer.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
        return new SerializerXmlHandler();
    }

    private static SerializerJsonHandler createJsonHandler() {
        try {
            Class.forName("", false, Serializer.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
        return new SerializerJsonHandler();
    }

    private static SerializerYamlHandler createYamlHandler() {
        try {
            Class.forName("", false, Serializer.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
        return new SerializerYamlHandler();
    }

    private static SerializerTomlHandler createTomlHandler() {
        try {
            Class.forName("", false, Serializer.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
        return new SerializerTomlHandler();
    }

    private static SerializerHalHandler createHalHandler() {
        try {
            Class.forName("", false, Serializer.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
        return new SerializerHalHandler();
    }

    private static SerializerCsvHandler createCsvHandler() {
        try {
            Class.forName("", false, Serializer.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
        return new SerializerCsvHandler();
    }
}
