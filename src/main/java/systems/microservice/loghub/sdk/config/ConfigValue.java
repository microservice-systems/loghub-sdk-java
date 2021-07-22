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

package systems.microservice.loghub.sdk.config;

import systems.microservice.loghub.sdk.buffer.BufferWriter;
import systems.microservice.loghub.sdk.buffer.Bufferable;
import systems.microservice.loghub.sdk.util.Argument;
import systems.microservice.loghub.sdk.util.MapUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public final class ConfigValue implements Comparable<ConfigValue>, Bufferable, Serializable {
    private static final long serialVersionUID = 1L;

    private final Class clazz;
    private final Object object;
    private final transient ConcurrentHashMap<Class, Object> objects;

    public ConfigValue(Class clazz, Object object) {
        Argument.notNull("clazz", clazz);

        this.clazz = clazz;
        this.object = object;
        this.objects = (object != null) ? new ConcurrentHashMap<>(0, 0.75f, 1) : null;
    }

    public Class getClazz() {
        return clazz;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz) {
        return (T) object;
    }

    @SuppressWarnings("unchecked")
    public <I, O> O get(Class<O> outputClass, ConfigExtractor<I, O> extractor) {
        Argument.notNull("outputClass", outputClass);
        Argument.notNull("extractor", extractor);

        if (object != null) {
            Object o = objects.get(outputClass);
            if (o == null) {
                o = extractor.extract((I) object, outputClass);
                o = MapUtil.putIfAbsent(objects, outputClass, o);
            }
            return (O) o;
        } else {
            return null;
        }
    }

    public boolean isNull() {
        return object == null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(ConfigValue value) {
        Comparable tc = (Comparable) object;
        Comparable oc = (Comparable) value.object;
        return tc.compareTo(oc);
    }

    @Override
    public int write(byte[] buffer, int index, Map<String, Object> context) {
        index = BufferWriter.writeVersion(buffer, index, (byte) 1);
        index = BufferWriter.writeObjectRef(buffer, index, context, object);
        return index;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        try {
            Field objectsField = ConfigValue.class.getDeclaredField("objects");
            objectsField.setAccessible(true);
            objectsField.set(this, (object != null) ? new ConcurrentHashMap<>(0, 0.75f, 1) : null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new Error(e);
        }
    }
}
