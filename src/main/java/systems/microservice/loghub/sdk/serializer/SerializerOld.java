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
public abstract class SerializerOld implements Serializable {
    private static final long serialVersionUID = 1L;

    protected SerializerOld() {
    }

    @SuppressWarnings("rawtypes")
    public abstract Object deserialize(byte[] array, Class clazz);

    @SuppressWarnings("rawtypes")
    public abstract Object deserialize(InputStream input, Class clazz);

    @SuppressWarnings("rawtypes")
    public abstract Object deserialize(String string, Class clazz);

    @SuppressWarnings("rawtypes")
    public abstract Object deserialize(Reader reader, Class clazz);

    public abstract byte[] serialize(Object object);
    public abstract OutputStream serialize(Object object, OutputStream output);
    public abstract String serializeS(Object object);
    public abstract Writer serialize(Object object, Writer writer);
}
