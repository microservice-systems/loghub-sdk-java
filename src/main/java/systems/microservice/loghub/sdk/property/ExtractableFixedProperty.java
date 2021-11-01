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

import systems.microservice.loghub.connector.Validation;
import systems.microservice.loghub.sdk.Property;
import systems.microservice.loghub.sdk.config.ConfigExtractor;
import systems.microservice.loghub.sdk.config.extractor.ConfigValueOfExtractor;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class ExtractableFixedProperty<I, O> implements Property<O>, Serializable {
    private static final long serialVersionUID = 1L;

    protected final I value;
    protected final Class<O> outputClass;
    protected final ConfigExtractor<I, O> extractor;
    protected final O extractedValue;

    public ExtractableFixedProperty(I value, Class<O> outputClass) {
        this(value, outputClass, ConfigValueOfExtractor.getInstance());
    }

    public ExtractableFixedProperty(I value, Class<O> outputClass, ConfigExtractor<I, O> extractor) {
        Validation.notNull("outputClass", outputClass);
        Validation.notNull("extractor", extractor);

        O ev = extractor.extract(value, outputClass);

        this.value = value;
        this.outputClass = outputClass;
        this.extractor = extractor;
        this.extractedValue = ev;
    }

    public I getValue() {
        return value;
    }

    public Class<O> getOutputClass() {
        return outputClass;
    }

    public ConfigExtractor<I, O> getExtractor() {
        return extractor;
    }

    public O getExtractedValue() {
        return extractedValue;
    }

    @Override
    public O get() {
        return extractedValue;
    }
}
