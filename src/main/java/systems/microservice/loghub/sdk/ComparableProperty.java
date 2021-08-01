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

import systems.microservice.loghub.sdk.config.Config;
import systems.microservice.loghub.sdk.util.Argument;
import systems.microservice.loghub.sdk.util.Range;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class ComparableProperty<T extends Comparable<T>> implements Serializable {
    private static final long serialVersionUID = 1L;

    protected final String key;
    protected final Class<T> clazz;
    protected final boolean nullable;
    protected final T defaultValue;
    protected final String unit;
    protected final Range<T> rangeValues;

    public ComparableProperty(String key, Class<T> clazz) {
        this(key, clazz, false);
    }

    public ComparableProperty(String key, Class<T> clazz, boolean nullable) {
        this(key, clazz, nullable, null);
    }

    public ComparableProperty(String key, Class<T> clazz, boolean nullable, T defaultValue) {
        this(key, clazz, nullable, defaultValue, null);
    }

    public ComparableProperty(String key, Class<T> clazz, boolean nullable, T defaultValue, String unit) {
        this(key, clazz, nullable, defaultValue, unit, null);
    }

    public ComparableProperty(String key, Class<T> clazz, boolean nullable, T defaultValue, String unit, Range<T> rangeValues) {
        Argument.notNull("key", key);
        Argument.notNull("clazz", clazz);

        this.key = key;
        this.clazz = clazz;
        this.nullable = nullable;
        this.defaultValue = defaultValue;
        this.unit = unit;
        this.rangeValues = rangeValues;
    }

    public String getKey() {
        return key;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public boolean isNullable() {
        return nullable;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public String getUnit() {
        return unit;
    }

    public Range<T> getRangeValues() {
        return rangeValues;
    }

    public T get() {
        return Config.getProperty(key, clazz, nullable, defaultValue, unit, rangeValues);
    }
}
