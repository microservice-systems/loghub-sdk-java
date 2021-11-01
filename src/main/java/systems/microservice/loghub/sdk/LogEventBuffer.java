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

import systems.microservice.loghub.connector.Validation;
import systems.microservice.loghub.sdk.buffer.BufferWriter;
import systems.microservice.loghub.sdk.usage.CPUUsage;
import systems.microservice.loghub.sdk.usage.ClassUsage;
import systems.microservice.loghub.sdk.usage.DescriptorUsage;
import systems.microservice.loghub.sdk.usage.DiskUsage;
import systems.microservice.loghub.sdk.usage.GCUsage;
import systems.microservice.loghub.sdk.usage.MemoryUsage;
import systems.microservice.loghub.sdk.usage.ThreadUsage;
import systems.microservice.loghub.sdk.util.Blob;
import systems.microservice.loghub.sdk.util.Image;
import systems.microservice.loghub.sdk.util.StringBuilderWriter;
import systems.microservice.loghub.sdk.util.Tag;
import systems.microservice.loghub.sdk.util.ThreadInfo;
import systems.microservice.loghub.sdk.util.ThreadSection;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
final class LogEventBuffer implements LogTagWriter, LogImageWriter, LogBlobWriter {
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

    @Override
    public int writeTag(byte[] buffer, int index, String key, Object value, String unit) {
        Validation.notNull("key", key);
        Validation.notNull("value", value);

        index = BufferWriter.writeBoolean(buffer, index, true);
        index = BufferWriter.writeVersion(buffer, index, (byte) 1);
        short sid = strings.getStringID(key);
        index = BufferWriter.writeShort(buffer, index, sid);
        if (sid == LogEventStringMap.NOT_EXIST_ID) {
            index = BufferWriter.writeString(buffer, index, key);
        }
        index = BufferWriter.writeObject(buffer, index, value);
        if (unit != null) {
            index = BufferWriter.writeByte(buffer, index, (byte) 1);
            sid = strings.getStringID(unit);
            index = BufferWriter.writeShort(buffer, index, sid);
            if (sid == LogEventStringMap.NOT_EXIST_ID) {
                index = BufferWriter.writeString(buffer, index, unit);
            }
        } else {
            index = BufferWriter.writeByte(buffer, index, (byte) 0);
        }
        return index;
    }

    @Override
    public int writeImage(byte[] buffer, int index, String key, byte[] content, String contentType) {
        Validation.notNull("key", key);
        Validation.notNull("content", content);
        Validation.notNull("contentType", contentType);

        index = BufferWriter.writeBoolean(buffer, index, true);
        index = BufferWriter.writeVersion(buffer, index, (byte) 1);
        short sid = strings.getStringID(key);
        index = BufferWriter.writeShort(buffer, index, sid);
        if (sid == LogEventStringMap.NOT_EXIST_ID) {
            index = BufferWriter.writeString(buffer, index, key);
        }
        index = BufferWriter.writeByteArray(buffer, index, content);
        sid = strings.getStringID(contentType);
        index = BufferWriter.writeShort(buffer, index, sid);
        if (sid == LogEventStringMap.NOT_EXIST_ID) {
            index = BufferWriter.writeString(buffer, index, contentType);
        }
        return index;
    }

    @Override
    public int writeBlob(byte[] buffer, int index, String key, byte[] content, String contentType) {
        Validation.notNull("key", key);
        Validation.notNull("content", content);
        Validation.notNull("contentType", contentType);

        index = BufferWriter.writeBoolean(buffer, index, true);
        index = BufferWriter.writeVersion(buffer, index, (byte) 1);
        short sid = strings.getStringID(key);
        index = BufferWriter.writeShort(buffer, index, sid);
        if (sid == LogEventStringMap.NOT_EXIST_ID) {
            index = BufferWriter.writeString(buffer, index, key);
        }
        index = BufferWriter.writeByteArray(buffer, index, content);
        sid = strings.getStringID(contentType);
        index = BufferWriter.writeShort(buffer, index, sid);
        if (sid == LogEventStringMap.NOT_EXIST_ID) {
            index = BufferWriter.writeString(buffer, index, contentType);
        }
        return index;
    }

    private int writeTags(byte[] buffer, int index,
                          Throwable exception, ThreadInfo threadInfo,
                          CPUUsage cpuUsage, MemoryUsage memoryUsage, DiskUsage diskUsage, ClassUsage classUsage, ThreadUsage threadUsage, DescriptorUsage descriptorUsage, GCUsage gcUsage,
                          Map<String, Tag> tags,
                          LogEventCallback callback) {
        index = BufferWriter.writeVersion(buffer, index, (byte) 1);
        if (exception != null) {
            index = writeTag(buffer, index, "exception.class", exception.getClass().getCanonicalName(), null);
            index = writeTag(buffer, index, "exception.message", exception.getMessage(), null);
            try (StringBuilderWriter sbw = new StringBuilderWriter(4096)) {
                exception.printStackTrace(new PrintWriter(sbw, false));
                index = writeTag(buffer, index, "exception.stacktrace", sbw.toString(), null);
            }
            Throwable[] sex = exception.getSuppressed();
            if (sex != null) {
                index = writeTag(buffer, index, "exception.suppressed.count", sex.length, null);
            }
            Throwable cex = exception.getCause();
            if (cex != null) {
                index = writeTag(buffer, index, "exception.cause.class", cex.getClass().getCanonicalName(), null);
                index = writeTag(buffer, index, "exception.cause.message", cex.getMessage(), null);
            }
        }
        if (threadInfo != null) {
            index = writeTag(buffer, index, "thread.id", threadInfo.thread.getId(), null);
            index = writeTag(buffer, index, "thread.name", threadInfo.thread.getName(), null);
            index = writeTag(buffer, index, "thread.priority", threadInfo.thread.getPriority(), null);
            index = writeTag(buffer, index, "thread.uuid", threadInfo.uuid, null);
            index = writeTag(buffer, index, "thread.depth", threadInfo.depth, null);
            for (ArrayList<Tag> ts : threadInfo.tags.values()) {
                Tag t = ts.get(ts.size() - 1);
                index = writeTag(buffer, index, t.key, t.value, t.unit);
            }
        }
        if (cpuUsage != null) {
            index = writeTag(buffer, index, "usage.cpu.count", cpuUsage.count, null);
            index = writeTag(buffer, index, "usage.cpu.m01", cpuUsage.m01, null);
            index = writeTag(buffer, index, "usage.cpu.m05", cpuUsage.m05, null);
            index = writeTag(buffer, index, "usage.cpu.m15", cpuUsage.m15, null);
            index = writeTag(buffer, index, "usage.cpu.entity.active", cpuUsage.entityActive, null);
            index = writeTag(buffer, index, "usage.cpu.entity.total", cpuUsage.entityTotal, null);
        }
        if (memoryUsage != null) {
            index = writeTag(buffer, index, "usage.memory.physical.total", memoryUsage.physicalTotal, "MB");
            index = writeTag(buffer, index, "usage.memory.physical.free", memoryUsage.physicalFree, "MB");
            index = writeTag(buffer, index, "usage.memory.heap.init", memoryUsage.heapInit, "MB");
            index = writeTag(buffer, index, "usage.memory.heap.used", memoryUsage.heapUsed, "MB");
            index = writeTag(buffer, index, "usage.memory.heap.committed", memoryUsage.heapCommitted, "MB");
            index = writeTag(buffer, index, "usage.memory.heap.max", memoryUsage.heapMax, "MB");
            index = writeTag(buffer, index, "usage.memory.nonheap.init", memoryUsage.nonheapInit, "MB");
            index = writeTag(buffer, index, "usage.memory.nonheap.used", memoryUsage.nonheapUsed, "MB");
            index = writeTag(buffer, index, "usage.memory.nonheap.committed", memoryUsage.nonheapCommitted, "MB");
            index = writeTag(buffer, index, "usage.memory.nonheap.max", memoryUsage.nonheapMax, "MB");
            index = writeTag(buffer, index, "usage.memory.object.pending.finalization", memoryUsage.objectPendingFinalization, null);
        }
        if (diskUsage != null) {
            index = writeTag(buffer, index, "usage.disk.total", diskUsage.total, "MB");
            index = writeTag(buffer, index, "usage.disk.free", diskUsage.free, "MB");
            index = writeTag(buffer, index, "usage.disk.usable", diskUsage.usable, "MB");
        }
        if (classUsage != null) {
            index = writeTag(buffer, index, "usage.class.active", classUsage.active, null);
            index = writeTag(buffer, index, "usage.class.loaded", classUsage.loaded, null);
            index = writeTag(buffer, index, "usage.class.unloaded", classUsage.unloaded, null);
        }
        if (threadUsage != null) {
            index = writeTag(buffer, index, "usage.thread.live", threadUsage.live, null);
            index = writeTag(buffer, index, "usage.thread.daemon", threadUsage.daemon, null);
            index = writeTag(buffer, index, "usage.thread.peak", threadUsage.peak, null);
            index = writeTag(buffer, index, "usage.thread.total", threadUsage.total, null);
        }
        if (descriptorUsage != null) {
            index = writeTag(buffer, index, "usage.descriptor.file.max", descriptorUsage.fileMax, null);
            index = writeTag(buffer, index, "usage.descriptor.file.open", descriptorUsage.fileOpen, null);
        }
        if (gcUsage != null) {
            index = writeTag(buffer, index, "usage.gc.collection.count", gcUsage.collectionCount, null);
            index = writeTag(buffer, index, "usage.gc.collection.time", gcUsage.collectionTime, "ms");
        }
        if (tags != null) {
            for (Tag t : tags.values()) {
                index = writeTag(buffer, index, t.key, t.value, t.unit);
            }
        }
        if (callback != null) {
            index = callback.writeTags(buffer, index, this);
        }
        index = BufferWriter.writeBoolean(buffer, index, false);
        return index;
    }

    private int writeEvent(byte[] buffer, int index,
                           long time, String logger, int level, String levelName, LogType type,
                           Throwable exception, ThreadInfo threadInfo,
                           CPUUsage cpuUsage, MemoryUsage memoryUsage, DiskUsage diskUsage, ClassUsage classUsage, ThreadUsage threadUsage, DescriptorUsage descriptorUsage, GCUsage gcUsage,
                           Map<String, Tag> tags, Map<String, Image> images, Map<String, Blob> blobs,
                           LogEventCallback callback,
                           String message) {
        index = BufferWriter.writeVersion(buffer, index, (byte) 1);
        index = BufferWriter.writeLong(buffer, index, time);
        index = BufferWriter.writeString(buffer, index, logger);
        index = BufferWriter.writeInt(buffer, index, level);
        index = BufferWriter.writeString(buffer, index, levelName);
        index = BufferWriter.writeByte(buffer, index, type.id);
        index = writeTags(buffer, index, exception, threadInfo, cpuUsage, memoryUsage, diskUsage, classUsage, threadUsage, descriptorUsage, gcUsage, tags, callback);
        index = BufferWriter.writeString(buffer, index, message);
        return index;
    }

    public boolean logEvent(byte[] buffer, int index,
                            long time, String logger, int level, String levelName, LogType type,
                            Throwable exception, ThreadInfo threadInfo,
                            CPUUsage cpuUsage, MemoryUsage memoryUsage, DiskUsage diskUsage, ClassUsage classUsage, ThreadUsage threadUsage, DescriptorUsage descriptorUsage, GCUsage gcUsage,
                            Map<String, Tag> tags, Map<String, Image> images, Map<String, Blob> blobs,
                            LogEventCallback callback,
                            String message) {
        return false;
    }
}
