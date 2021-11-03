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

package systems.microservice.loghub.sdk.metric;

import systems.microservice.loghub.facade.Validator;
import systems.microservice.loghub.sdk.LogHub;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class MetricCollector {
    private static final boolean enabled = LogHub.isEnabled();

    private MetricCollector() {
    }

    public static void collect(String name, long value, int point) {
        collect(name, value, point, null);
    }

    public static void collect(String name, long value, int point, String unit) {
        collect(name, 1L, value, point, unit);
    }

    public static void collect(String name, long count, long value, int point) {
        collect(name, count, value, point, null);
    }

    public static void collect(String name, long count, long value, int point, String unit) {
        Validator.notNull("name", name);
        Validator.inRangeLong("count", count, 0L, Long.MAX_VALUE);
        Validator.inRangeInt("point", point, 0, 14);
    }

    public static void collectInRange(String name, long value, long min, long max, int point) {
        if ((value >= min) && (value <= max)) {
            collect(name, value, point);
        }
    }

    public static void collectInRange(String name, long value, long min, long max, int point, String unit) {
        if ((value >= min) && (value <= max)) {
            collect(name, value, point, unit);
        }
    }

    public static void collectInRange(String name, long count, long value, long min, long max, int point) {
        if ((value >= min) && (value <= max)) {
            collect(name, count, value, point);
        }
    }

    public static void collectInRange(String name, long count, long value, long min, long max, int point, String unit) {
        if ((value >= min) && (value <= max)) {
            collect(name, count, value, point, unit);
        }
    }
}
