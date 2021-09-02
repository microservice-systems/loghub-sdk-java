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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class SerializerCborHandler implements SerializerHandler, Serializable {
    private static final long serialVersionUID = 1L;

    public SerializerCborHandler() {
    }

    @Override
    public <T> T read(byte[] array, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> T read(InputStream input, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> T read(String string, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> T read(Reader reader, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> byte[] write(T object) {
        return new byte[0];
    }

    @Override
    public <T> OutputStream write(T object, OutputStream output) {
        return null;
    }

    @Override
    public <T> String writeS(T object) {
        return null;
    }

    @Override
    public <T> Writer write(T object, Writer writer) {
        return null;
    }
}
