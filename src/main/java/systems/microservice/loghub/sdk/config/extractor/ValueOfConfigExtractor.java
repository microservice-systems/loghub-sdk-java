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

package systems.microservice.loghub.sdk.config.extractors;

import systems.microservice.loghub.sdk.config.ConfigExtractor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class ValueOfConfigExtractor<I, O> implements ConfigExtractor<I, O> {
    private static final ValueOfConfigExtractor<Object, Object> instance = new ValueOfConfigExtractor<>();

    private ValueOfConfigExtractor() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public O extract(I input, Class<O> outputClass) {
        try {
            Method m = outputClass.getMethod("valueOf", input.getClass());
            return (O) m.invoke(null, input);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <I, O> ValueOfConfigExtractor<I, O> getInstance() {
        return (ValueOfConfigExtractor<I, O>) instance;
    }
}
