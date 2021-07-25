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
import systems.microservice.loghub.sdk.config.ConfigExtractor;
import systems.microservice.loghub.sdk.util.Range;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class Property<T> implements Bufferable, Serializable {
    private static final long serialVersionUID = 1L;

    public Property(String key, Class<T> clazz) {
    }

    public Property(String key, Class<T> clazz, T defaultValue) {
    }

    public Property(String key, Class<T> clazz, T defaultValue, String unit) {
    }

    public Property(String key, Class<T> clazz, T defaultValue, String unit, boolean nullable) {
    }

    public Property(String key, Class<T> clazz, T defaultValue, String unit, boolean nullable, T[] possibleValues) {
    }

    public <I, O> Property(String key, Class<I> clazz, I defaultValue, String unit, boolean nullable, I[] possibleValues, Class<O> outputClass) {
    }

    public <I, O> Property(String key, Class<I> clazz, I defaultValue, String unit, boolean nullable, I[] possibleValues, Class<O> outputClass, ConfigExtractor<I, O> extractor) {
    }


    public <I extends Comparable<I>, O> Property(String key, Class<I> clazz, I defaultValue, String unit, boolean nullable, Range<I> rangeValues, Class<O> outputClass) {
    }

    public <I extends Comparable<I>, O> Property(String key, Class<I> clazz, I defaultValue, String unit, boolean nullable, Range<I> rangeValues, Class<O> outputClass, ConfigExtractor<I, O> extractor) {
    }

    public T get() {
        return null;
    }

    @Override
    public int write(byte[] buffer, int index, Map<String, Object> context) {
        return 0;
    }

    static {
        BufferObjectType.registerBufferableClass(UUID.fromString("35f98446-317a-49a9-ba79-8d931d22113a"), Property.class);
    }
}
