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
import systems.microservice.loghub.sdk.config.ConfigExtractor;
import systems.microservice.loghub.sdk.config.extractor.ValueOfConfigExtractor;
import systems.microservice.loghub.sdk.util.Argument;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class ExtractableProperty<I, O> implements Property<O>, Serializable {
    private static final long serialVersionUID = 1L;

    protected final String key;
    protected final Class<I> clazz;
    protected final boolean nullable;
    protected final I defaultValue;
    protected final String unit;
    protected final I[] possibleValues;
    protected final Class<O> outputClass;
    protected final ConfigExtractor<I, O> extractor;

    public ExtractableProperty(String key, Class<I> clazz, boolean nullable, I defaultValue, String unit, I[] possibleValues, Class<O> outputClass) {
        this(key, clazz, nullable, defaultValue, unit, possibleValues, outputClass, ValueOfConfigExtractor.getInstance());
    }

    public ExtractableProperty(String key, Class<I> clazz, boolean nullable, I defaultValue, String unit, I[] possibleValues, Class<O> outputClass, ConfigExtractor<I, O> extractor) {
        Argument.notNull("key", key);
        Argument.notNull("clazz", clazz);
        Argument.notNull("outputClass", outputClass);
        Argument.notNull("extractor", extractor);

        this.key = key;
        this.clazz = clazz;
        this.nullable = nullable;
        this.defaultValue = defaultValue;
        this.unit = unit;
        this.possibleValues = possibleValues;
        this.outputClass = outputClass;
        this.extractor = extractor;
    }

    public String getKey() {
        return key;
    }

    public Class<I> getClazz() {
        return clazz;
    }

    public boolean isNullable() {
        return nullable;
    }

    public I getDefaultValue() {
        return defaultValue;
    }

    public String getUnit() {
        return unit;
    }

    public I[] getPossibleValues() {
        return possibleValues;
    }

    public Class<O> getOutputClass() {
        return outputClass;
    }

    public ConfigExtractor<I, O> getExtractor() {
        return extractor;
    }

    @Override
    public O get() {
        return Config.getProperty(key, clazz, nullable, defaultValue, unit, possibleValues, outputClass, extractor);
    }
}
