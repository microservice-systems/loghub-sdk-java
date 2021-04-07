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

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
final class LogMetricValue {
    public final String name;
    public final AtomicLong count;
    public final AtomicLong sum;
    public final AtomicLong min;
    public final AtomicLong max;
    public final byte point;
    public final String unit;

    public LogMetricValue(String name, long value, int point, String unit) {
        this.name = name;
        this.count = new AtomicLong(1L);
        this.sum = new AtomicLong(value);
        this.min = new AtomicLong(value);
        this.max = new AtomicLong(value);
        this.point = (byte) point;
        this.unit = unit;
    }

    public boolean add(long begin, long end, long value) {
        long t = System.currentTimeMillis();
        if ((t >= begin) && (t < end)) {
            count.incrementAndGet();
            sum.addAndGet(value);
            for (long m = min.get(); value < m; m = min.get()) {
                min.compareAndSet(m, value);
            }
            for (long m = max.get(); value > m; m = max.get()) {
                max.compareAndSet(m, value);
            }
            return true;
        } else {
            return false;
        }
    }
}
