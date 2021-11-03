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

import systems.microservice.loghub.facade.Validator;
import systems.microservice.loghub.sdk.Property;
import systems.microservice.loghub.sdk.config.Config;
import systems.microservice.loghub.sdk.config.ConfigExtractor;
import systems.microservice.loghub.sdk.config.extractor.ConfigValueOfExtractor;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class ExtractablePossibleProperty<I, O> implements Property<O>, Serializable {
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

    public ExtractablePossibleProperty(String group, String key, Class<I> clazz, boolean nullable, boolean secure, I defaultValue, I[] possibleValues, String unit, Class<O> outputClass) {
        this(group, key, clazz, nullable, secure, defaultValue, possibleValues, unit, outputClass, ConfigValueOfExtractor.getInstance());
    }

    public ExtractablePossibleProperty(String group, String key, Class<I> clazz, boolean nullable, boolean secure, I defaultValue, I[] possibleValues, String unit, Class<O> outputClass, ConfigExtractor<I, O> extractor) {
        Validator.notNull("group", group);
        Validator.notNull("key", key);
        Validator.notNull("clazz", clazz);
        if (!nullable) {
            if (defaultValue == null) {
                throw new IllegalArgumentException(String.format("Default value is null for non nullable property '%s'", key));
            }
        }
        Validator.notNull("possibleValues", possibleValues);
        Validator.notNull("outputClass", outputClass);
        Validator.notNull("extractor", extractor);

        O ev = Config.getProperty(group, key, clazz, nullable, secure, defaultValue, possibleValues, unit, outputClass, extractor);

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
