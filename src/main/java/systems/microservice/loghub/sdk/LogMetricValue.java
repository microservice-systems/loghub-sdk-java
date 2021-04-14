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

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
final class LogMetricValue implements Bufferable {
    private static final double[] POINTS = {1.0, 0.1, 0.01, 0.001, 0.0001, 0.00001, 0.000001, 0.0000001, 0.00000001,
                                            0.000000001, 0.0000000001, 0.00000000001, 0.000000000001, 0.0000000000001,
                                            0.00000000000001};

    public final String name;
    public final AtomicLong time;
    public final AtomicLong count;
    public final AtomicLong sum;
    public final AtomicLong min;
    public final AtomicLong max;
    public final int point;
    public final String unit;

    public LogMetricValue(String name, int point, String unit) {
        this.name = name;
        this.time = new AtomicLong(0L);
        this.count = new AtomicLong(0L);
        this.sum = new AtomicLong(0L);
        this.min = new AtomicLong(Long.MAX_VALUE);
        this.max = new AtomicLong(Long.MIN_VALUE);
        this.point = point;
        this.unit = unit;
    }

    public void log(long time, long count, long value) {
        this.time.set(time);
        this.count.addAndGet(count);
        this.sum.addAndGet(value * count);
        for (long m = this.min.get(); value < m; m = this.min.get()) {
            this.min.compareAndSet(m, value);
        }
        for (long m = this.max.get(); value > m; m = this.max.get()) {
            this.max.compareAndSet(m, value);
        }
    }

    @Override
    public int write(byte[] buffer, int index, Map<String, Object> context) {
        byte v = 1;
        double p = POINTS[point];
        index = BufferWriter.writeByte(buffer, index, v);
        index = BufferWriter.writeString(buffer, index, name);
        index = BufferWriter.writeLong(buffer, index, count.get());
        index = BufferWriter.writeDouble(buffer, index, ((double) sum.get()) * p);
        index = BufferWriter.writeDouble(buffer, index, ((double) min.get()) * p);
        index = BufferWriter.writeDouble(buffer, index, ((double) max.get()) * p);
        index = BufferWriter.writeByte(buffer, index, (byte) point);
        index = BufferWriter.writeString(buffer, index, unit);
        return index;
    }
}
