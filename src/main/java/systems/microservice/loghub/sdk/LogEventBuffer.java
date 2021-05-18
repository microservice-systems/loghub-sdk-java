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
import systems.microservice.loghub.sdk.util.StringBuilderWriter;
import systems.microservice.loghub.sdk.util.ThreadSection;

import java.io.PrintWriter;
import java.util.Map;
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
    private final LogEventStringMap strings;
    private final int bufferSize;
    private final byte[] buffer;

    public LogEventBuffer(int bufferSize) {
        this.section = new ThreadSection(true);
        this.count = new AtomicInteger(0);
        this.size = new AtomicInteger(0);
        this.begin = new AtomicLong(Long.MAX_VALUE);
        this.end = new AtomicLong(0L);
        this.strings = new LogEventStringMap();
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

    private int writeTag(byte[] buffer, int index, String key, Object value) {
        index = BufferWriter.writeBoolean(buffer, index, true);
        index = BufferWriter.writeVersion(buffer, index, (byte) 1);
        short sid = strings.getStringID(key);
        index = BufferWriter.writeShort(buffer, index, sid);
        if (sid == LogEventStringMap.NOT_EXIST_ID) {
            index = BufferWriter.writeString(buffer, index, key);
        }
        index = BufferWriter.writeObject(buffer, index, null, value);
        return index;
    }

    private int writeThreadInfo(byte[] buffer, int index, LogThreadInfo threadInfo) {
        index = BufferWriter.writeVersion(buffer, index, (byte) 1);
        if (threadInfo != null) {
            index = writeTag(buffer, index, "exception.class", exception.getClass().getCanonicalName());
        }
        index = BufferWriter.writeBoolean(buffer, index, false);
        return index;
    }

    private int writeTags(byte[] buffer, int index, Throwable exception) {
        index = BufferWriter.writeVersion(buffer, index, (byte) 1);
        if (exception != null) {
            index = writeTag(buffer, index, "exception.class", exception.getClass().getCanonicalName());
            index = writeTag(buffer, index, "exception.message", exception.getMessage());
            try (StringBuilderWriter sbw = new StringBuilderWriter(4096)) {
                exception.printStackTrace(new PrintWriter(sbw, false));
                index = writeTag(buffer, index, "exception.stacktrace", sbw.toString());
            }
            Throwable[] sex = exception.getSuppressed();
            if (sex != null) {
                index = writeTag(buffer, index, "exception.suppressed.count", sex.length);
            }
            Throwable cex = exception.getCause();
            if (cex != null) {
                index = writeTag(buffer, index, "exception.cause.class", cex.getClass().getCanonicalName());
                index = writeTag(buffer, index, "exception.cause.message", cex.getMessage());
            }
        }
        index = BufferWriter.writeBoolean(buffer, index, false);
        return index;
    }

    private int writeEvent(byte[] buffer, int index,
                           long time, String logger, int level, String levelName, LogType type,
                           Throwable exception, LogThreadInfo threadInfo,
                           LogCPUUsage cpuUsage, LogMemoryUsage memoryUsage, LogDiskUsage diskUsage, LogClassUsage classUsage, LogThreadUsage threadUsage, LogDescriptorUsage descriptorUsage, LogGCUsage gcUsage,
                           Map<String, LogTag> tags, Map<String, LogImage> images, Map<String, LogBlob> blobs,
                           LogEventCallback callback,
                           String message) {
        index = BufferWriter.writeVersion(buffer, index, (byte) 1);
        index = BufferWriter.writeLong(buffer, index, time);
        index = BufferWriter.writeString(buffer, index, logger);
        index = BufferWriter.writeInt(buffer, index, level);
        index = BufferWriter.writeString(buffer, index, levelName);
        index = BufferWriter.writeByte(buffer, index, type.id);
        index = writeTags(buffer, index, exception);
        index = BufferWriter.writeString(buffer, index, message);
        return index;
    }

    public boolean logEvent(byte[] buffer, int index,
                            long time, String logger, int level, String levelName, LogType type,
                            Throwable exception, LogThreadInfo threadInfo,
                            LogCPUUsage cpuUsage, LogMemoryUsage memoryUsage, LogDiskUsage diskUsage, LogClassUsage classUsage, LogThreadUsage threadUsage, LogDescriptorUsage descriptorUsage, LogGCUsage gcUsage,
                            Map<String, LogTag> tags, Map<String, LogImage> images, Map<String, LogBlob> blobs,
                            LogEventCallback callback,
                            String message) {
        return false;
    }
}
