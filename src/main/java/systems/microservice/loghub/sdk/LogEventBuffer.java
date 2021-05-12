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

import systems.microservice.loghub.sdk.util.ThreadSection;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
final class LogEventBuffer {
    private final ThreadSection section;
    private final AtomicInteger count;
    private final AtomicInteger size;
    private final AtomicLong begin;
    private final AtomicLong end;
    private final int bufferSize;
    private final byte[] buffer;

    public LogEventBuffer(int bufferSize) {
        this.section = new ThreadSection(true);
        this.count = new AtomicInteger(0);
        this.size = new AtomicInteger(0);
        this.begin = new AtomicLong(Long.MAX_VALUE);
        this.end = new AtomicLong(0L);
        this.bufferSize = bufferSize;
        this.buffer = new byte[bufferSize];
    }

    public ThreadSection getSection() {
        return section;
    }

    public int getCount() {
        return count.get();
    }

    public int getSize() {
        return size.get();
    }

    public long getBegin() {
        return begin.get();
    }

    public long getEnd() {
        return end.get();
    }

    public int getBufferSize() {
        return bufferSize;
    }
}
