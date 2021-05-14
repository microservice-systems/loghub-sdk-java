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

import systems.microservice.loghub.sdk.buffer.BufferWriter;
import systems.microservice.loghub.sdk.buffer.Bufferable;
import systems.microservice.loghub.sdk.util.MapUtil;
import systems.microservice.loghub.sdk.util.ThreadSection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
final class LogMetricBuffer implements Bufferable {
    private final ThreadSection section;
    private final ConcurrentHashMap<String, LogMetricValue> metrics;
    private final AtomicLong begin;
    private final AtomicLong end;

    public LogMetricBuffer(long begin, long end) {
        this.section = new ThreadSection(true);
        this.metrics = new ConcurrentHashMap<>(4096, 0.75f, 64);
        this.begin = new AtomicLong(begin);
        this.end = new AtomicLong(end);
    }

    public ThreadSection getSection() {
        return section;
    }

    public long getBegin() {
        return begin.get();
    }

    public long getEnd() {
        return end.get();
    }

    public void updateSpan() {
    }

    public boolean log(long time, String name, long count, long value, int point, String unit) {
        long b = begin.get();
        long e = end.get();
        if ((time >= b) && (time < e)) {
            if (section.enter()) {
                try {
                    LogMetricValue v = metrics.get(name);
                    if (v == null) {
                        v = MapUtil.putIfAbsent(metrics, name, new LogMetricValue(name, point, unit));
                    }
                    v.log(time, count, value);
                    return true;
                } finally {
                    section.leave();
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int write(byte[] buffer, int index, Map<String, Object> context) {
        byte v = 1;
        index = BufferWriter.writeVersion(buffer, index, v);
        index = BufferWriter.writeObjectMap(buffer, index, context, metrics);
        return index;
    }
}
