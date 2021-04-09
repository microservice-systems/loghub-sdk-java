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

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
final class LogMetricWriter {
    private final AtomicReference<LogMetricConfig> config = new AtomicReference<>(null);
    private final ConcurrentLinkedQueue<LogMetricBuffer> buffers = new ConcurrentLinkedQueue<>();
    private final AtomicLong total = new AtomicLong(0L);
    private final AtomicLong lost = new AtomicLong(0L);

    public LogMetricWriter() {
        long b = System.currentTimeMillis() / 60000L;
        b = b * 60000L;
        this.buffers.add(new LogMetricBuffer(b, b + 60000L));
        b += 60000L;
        this.buffers.add(new LogMetricBuffer(b, b + 60000L));
        b += 60000L;
        this.buffers.add(new LogMetricBuffer(b, b + 60000L));
        b += 60000L;
        this.buffers.add(new LogMetricBuffer(b, b + 60000L));
        b += 60000L;
        this.buffers.add(new LogMetricBuffer(b, b + 60000L));
    }

    public LogMetricConfig getConfig() {
        return config.get();
    }

    public long getTotal() {
        return total.get();
    }

    public long getLost() {
        return lost.get();
    }

    public void log(String name, long value, int point, String unit) {
        if (config.get().enabled) {
            total.incrementAndGet();
            long t = System.currentTimeMillis();
            for (LogMetricBuffer b : buffers) {
                if (b.log(t, name, value, point, unit)) {
                    return;
                }
            }
            lost.incrementAndGet();
        }
    }
}
