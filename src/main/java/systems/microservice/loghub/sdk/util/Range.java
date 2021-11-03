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

package systems.microservice.loghub.sdk.util;

import systems.microservice.loghub.facade.Validator;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class Range<T extends Comparable<T>> implements Serializable {
    private static final long serialVersionUID = 1L;

    public final T min;
    public final T max;

    public Range(T min, T max) {
        Validator.notNull("min", min);
        Validator.notNull("max", max);

        this.min = min;
        this.max = max;
    }

    public boolean isLeft(T value) {
        Validator.notNull("value", value);

        return value.compareTo(min) < 0;
    }

    public boolean isIn(T value) {
        Validator.notNull("value", value);

        return (value.compareTo(min) >= 0) && (value.compareTo(max) <= 0);
    }

    public boolean isRight(T value) {
        Validator.notNull("value", value);

        return value.compareTo(max) > 0;
    }
}
