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
    private final long span;
    private final int bufferCount;
    private final AtomicReference<LogMetricConfig> config;
    private final ConcurrentLinkedQueue<LogMetricBuffer> buffers;
    private final AtomicLong totalCount;
    private final AtomicLong sentCount;
    private final AtomicLong sentSize;
    private final AtomicLong lostCount;

    public LogMetricWriter(long span, int bufferCount) {
        this.span = span;
        this.bufferCount = bufferCount;
        this.config = new AtomicReference<>(null);
        this.buffers = new ConcurrentLinkedQueue<>();
        this.totalCount = new AtomicLong(0L);
        this.sentCount = new AtomicLong(0L);
        this.sentSize = new AtomicLong(0L);
        this.lostCount = new AtomicLong(0L);

        long b = System.currentTimeMillis() / span;
        b = b * span;
        for (int i = 0; i < bufferCount; ++i) {
            long e = b + span;
            this.buffers.add(new LogMetricBuffer(b, e));
            b = e;
        }
    }

    public long getSpan() {
        return span;
    }

    public int getBufferCount() {
        return bufferCount;
    }

    public LogMetricConfig getConfig() {
        return config.get();
    }

    public boolean log(String name, long value, int point, String unit) {
        if (config.get().enabled) {
            totalCount.incrementAndGet();
            try {
                long t = System.currentTimeMillis();
                LogMetricBuffer hb = buffers.peek();
                if (hb != null) {
                    if (hb.log(t, name, value, point, unit)) {
                        return true;
                    } else {
                        for (LogMetricBuffer b : buffers) {
                            if (b.log(t, name, value, point, unit)) {
                                return true;
                            }
                        }
                    }
                }
            } catch (Throwable ex) {
                lostCount.incrementAndGet();
                throw ex;
            }
        }
        return false;
    }

    public void flush() {
    }
}
