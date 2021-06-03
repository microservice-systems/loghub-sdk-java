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

import systems.microservice.loghub.sdk.buffer.BufferWriter;
import systems.microservice.loghub.sdk.buffer.Bufferable;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class Range<T extends Comparable<T>> implements Bufferable, Serializable {
    private static final long serialVersionUID = 1L;

    public final T min;
    public final T max;

    public Range(T min, T max) {
        Argument.notNull("min", min);
        Argument.notNull("max", max);

        this.min = min;
        this.max = max;
    }

    public boolean isLeft(T value) {
        Argument.notNull("value", value);

        return value.compareTo(min) < 0;
    }

    public boolean isIn(T value) {
        Argument.notNull("value", value);

        return (value.compareTo(min) >= 0) && (value.compareTo(max) <= 0);
    }

    public boolean isRight(T value) {
        Argument.notNull("value", value);

        return value.compareTo(max) > 0;
    }

    @Override
    public int write(byte[] buffer, int index, Map<String, Object> context) {
        index = BufferWriter.writeVersion(buffer, index, (byte) 1);
        index = BufferWriter.writeObject(buffer, index, context, min);
        index = BufferWriter.writeObject(buffer, index, context, max);
        return index;
    }
}