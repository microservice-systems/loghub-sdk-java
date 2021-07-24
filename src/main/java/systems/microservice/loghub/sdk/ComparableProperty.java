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

package systems.microservice.loghub.sdk;

import systems.microservice.loghub.sdk.buffer.BufferObjectType;
import systems.microservice.loghub.sdk.buffer.Bufferable;
import systems.microservice.loghub.sdk.config.Config;
import systems.microservice.loghub.sdk.util.Argument;
import systems.microservice.loghub.sdk.util.Range;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class ComparableProperty<T extends Comparable<T>> implements Bufferable, Serializable {
    private static final long serialVersionUID = 1L;

    protected final String key;
    protected final Class<T> clazz;
    protected final T defaultValue;
    protected final String unit;
    protected final boolean nullable;
    protected final Range<T> rangeValues;

    public ComparableProperty(String key, Class<T> clazz) {
        this(key, clazz, null);
    }

    public ComparableProperty(String key, Class<T> clazz, T defaultValue) {
        this(key, clazz, defaultValue, null);
    }

    public ComparableProperty(String key, Class<T> clazz, T defaultValue, String unit) {
        this(key, clazz, defaultValue, unit, false);
    }

    public ComparableProperty(String key, Class<T> clazz, T defaultValue, String unit, boolean nullable) {
        this(key, clazz, defaultValue, unit, nullable, null);
    }

    public ComparableProperty(String key, Class<T> clazz, T defaultValue, String unit, boolean nullable, Range<T> rangeValues) {
        Argument.notNull("key", key);
        Argument.notNull("clazz", clazz);
        if (!nullable && (defaultValue == null)) {
            throw new IllegalArgumentException(String.format("Not nullable comparable property '%s' has null default value", key));
        }

        this.key = key;
        this.clazz = clazz;
        this.defaultValue = defaultValue;
        this.unit = unit;
        this.nullable = nullable;
        this.rangeValues = rangeValues;
    }

    public String getKey() {
        return key;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isNullable() {
        return nullable;
    }

    public Range<T> getRangeValues() {
        return rangeValues;
    }

    public T get() {
        return Config.getProperty(key, clazz, defaultValue, unit, nullable, rangeValues);
    }

    @Override
    public int write(byte[] buffer, int index, Map<String, Object> context) {
        return 0;
    }

    static {
        BufferObjectType.registerBufferableClass(UUID.fromString("367513fb-ef76-4031-970f-b8b95392e2af"), ComparableProperty.class);
    }
}
