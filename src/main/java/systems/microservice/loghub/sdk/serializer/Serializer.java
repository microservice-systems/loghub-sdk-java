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
    JAVA("application/java-serialized-object", "ser", new JavaHandler()),
    JAVA_XML("application/java-xml", "xml", new JavaXmlHandler()),
    JAVA_PROPERTIES("application/java-properties", "properties", new JavaPropertiesHandler()),
    CBOR("application/cbor", "cbor", new JacksonHandler()),
    SMILE("application/smile", "smile", new JacksonHandler()),
    XML("application/xml", "xml", new JacksonHandler()),
    JSON("application/json", "json", new JacksonHandler()),
    YAML("application/yaml", "yaml", new JacksonHandler()),
    CSV("text/csv", "csv", new JacksonHandler()),
    PROPERTIES("application/properties", "properties", new JacksonHandler());

    public final String contentType;
    public final String extension;
    private final Handler handler;

    Serializer(String contentType, String extension, Handler handler) {
        this.contentType = contentType;
        this.extension = extension;
        this.handler = handler;
    }

    private static abstract class Handler implements Serializable {
        private static final long serialVersionUID = 1L;

        protected Handler() {
        }

        @SuppressWarnings("rawtypes")
        public abstract Object read(byte[] array, Class clazz);

        @SuppressWarnings("rawtypes")
        public abstract Object read(InputStream input, Class clazz);

        @SuppressWarnings("rawtypes")
        public abstract Object read(String string, Class clazz);

        @SuppressWarnings("rawtypes")
        public abstract Object read(Reader reader, Class clazz);

        public abstract byte[] write(Object object);
        public abstract OutputStream write(Object object, OutputStream output);
        public abstract String writeS(Object object);
        public abstract Writer write(Object object, Writer writer);
    }

    private static final class JavaHandler extends Handler {
        private static final long serialVersionUID = 1L;

        public JavaHandler() {
        }

        @Override
        public Object read(byte[] array, Class clazz) {
            return null;
        }

        @Override
        public Object read(InputStream input, Class clazz) {
            return null;
        }

        @Override
        public Object read(String string, Class clazz) {
            return null;
        }

        @Override
        public Object read(Reader reader, Class clazz) {
            return null;
        }

        @Override
        public byte[] write(Object object) {
            return new byte[0];
        }

        @Override
        public OutputStream write(Object object, OutputStream output) {
            return null;
        }

        @Override
        public String writeS(Object object) {
            return null;
        }

        @Override
        public Writer write(Object object, Writer writer) {
            return null;
        }
    }

    private static final class JavaXmlHandler extends Handler {
        private static final long serialVersionUID = 1L;

        public JavaXmlHandler() {
        }

        @Override
        public Object read(byte[] array, Class clazz) {
            return null;
        }

        @Override
        public Object read(InputStream input, Class clazz) {
            return null;
        }

        @Override
        public Object read(String string, Class clazz) {
            return null;
        }

        @Override
        public Object read(Reader reader, Class clazz) {
            return null;
        }

        @Override
        public byte[] write(Object object) {
            return new byte[0];
        }

        @Override
        public OutputStream write(Object object, OutputStream output) {
            return null;
        }

        @Override
        public String writeS(Object object) {
            return null;
        }

        @Override
        public Writer write(Object object, Writer writer) {
            return null;
        }
    }

    private static final class JavaPropertiesHandler extends Handler {
        private static final long serialVersionUID = 1L;

        public JavaPropertiesHandler() {
        }

        @Override
        public Object read(byte[] array, Class clazz) {
            return null;
        }

        @Override
        public Object read(InputStream input, Class clazz) {
            return null;
        }

        @Override
        public Object read(String string, Class clazz) {
            return null;
        }

        @Override
        public Object read(Reader reader, Class clazz) {
            return null;
        }

        @Override
        public byte[] write(Object object) {
            return new byte[0];
        }

        @Override
        public OutputStream write(Object object, OutputStream output) {
            return null;
        }

        @Override
        public String writeS(Object object) {
            return null;
        }

        @Override
        public Writer write(Object object, Writer writer) {
            return null;
        }
    }

    private static final class JacksonHandler extends Handler {
        private static final long serialVersionUID = 1L;

        public JacksonHandler() {
        }

        @Override
        public Object read(byte[] array, Class clazz) {
            return null;
        }

        @Override
        public Object read(InputStream input, Class clazz) {
            return null;
        }

        @Override
        public Object read(String string, Class clazz) {
            return null;
        }

        @Override
        public Object read(Reader reader, Class clazz) {
            return null;
        }

        @Override
        public byte[] write(Object object) {
            return new byte[0];
        }

        @Override
        public OutputStream write(Object object, OutputStream output) {
            return null;
        }

        @Override
        public String writeS(Object object) {
            return null;
        }

        @Override
        public Writer write(Object object, Writer writer) {
            return null;
        }
    }
}
