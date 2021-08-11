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

package systems.microservice.loghub.sdk.property;

import systems.microservice.loghub.sdk.Property;
import systems.microservice.loghub.sdk.config.Config;
import systems.microservice.loghub.sdk.config.ConfigExtractor;
import systems.microservice.loghub.sdk.config.extractor.ConfigValueOfExtractor;
import systems.microservice.loghub.sdk.util.Argument;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class ExtractableProperty<I, O> implements Property<O>, Serializable {
    private static final long serialVersionUID = 1L;

    protected final String group;
    protected final String key;
    protected final Class<I> clazz;
    protected final boolean nullable;
    protected final boolean secure;
    protected final I defaultValue;
    protected final I[] possibleValues;
    protected final String unit;
    protected final Class<O> outputClass;
    protected final ConfigExtractor<I, O> extractor;

    public ExtractableProperty(String group, String key, Class<I> clazz, boolean nullable, boolean secure, I defaultValue, I[] possibleValues, String unit, Class<O> outputClass) {
        this(group, key, clazz, nullable, secure, defaultValue, possibleValues, unit, outputClass, ConfigValueOfExtractor.getInstance());
    }

    public ExtractableProperty(String group, String key, Class<I> clazz, boolean nullable, boolean secure, I defaultValue, I[] possibleValues, String unit, Class<O> outputClass, ConfigExtractor<I, O> extractor) {
        Argument.notNull("group", group);
        Argument.notNull("key", key);
        Argument.notNull("clazz", clazz);
        if (!nullable) {
            if (defaultValue == null) {
                throw new IllegalArgumentException(String.format("Default value is null for non nullable property '%s'", key));
            }
        }
        Argument.notNull("possibleValues", possibleValues);
        Argument.notNull("outputClass", outputClass);
        Argument.notNull("extractor", extractor);

        this.group = group;
        this.key = key;
        this.clazz = clazz;
        this.nullable = nullable;
        this.secure = secure;
        this.defaultValue = defaultValue;
        this.possibleValues = possibleValues;
        this.unit = unit;
        this.outputClass = outputClass;
        this.extractor = extractor;
    }

    public String getGroup() {
        return group;
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

    public boolean isSecure() {
        return secure;
    }

    public I getDefaultValue() {
        return defaultValue;
    }

    public I[] getPossibleValues() {
        return possibleValues;
    }

    public String getUnit() {
        return unit;
    }

    public Class<O> getOutputClass() {
        return outputClass;
    }

    public ConfigExtractor<I, O> getExtractor() {
        return extractor;
    }

    @Override
    public O get() {
        return Config.getProperty(group, key, clazz, nullable, secure, defaultValue, possibleValues, unit, outputClass, extractor);
    }
}
