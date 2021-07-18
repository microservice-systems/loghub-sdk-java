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

package systems.microservice.loghub.sdk.usage;

import systems.microservice.loghub.sdk.LogHub;
import systems.microservice.loghub.sdk.metric.MetricCollector;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class Usage {
    private static final boolean enabled = LogHub.isEnabled();
    private static final AtomicReference<CPUUsage> cpuUsage = new AtomicReference<>(new CPUUsage());
    private static final AtomicReference<MemoryUsage> memoryUsage = new AtomicReference<>(new MemoryUsage());
    private static final AtomicReference<DiskUsage> diskUsage = new AtomicReference<>(new DiskUsage());
    private static final AtomicReference<NetworkUsage> networkUsage = new AtomicReference<>(new NetworkUsage());
    private static final AtomicReference<ClassUsage> classUsage = new AtomicReference<>(new ClassUsage());
    private static final AtomicReference<ThreadUsage> threadUsage = new AtomicReference<>(new ThreadUsage());
    private static final AtomicReference<DescriptorUsage> descriptorUsage = new AtomicReference<>(new DescriptorUsage());
    private static final AtomicReference<GCUsage> gcUsage = new AtomicReference<>(new GCUsage());
    private static final Thread monitor3Thread = createMonitor3Thread(enabled);
    private static final Thread monitor10Thread = createMonitor10Thread(enabled);

    private Usage() {
    }

    static {
        if (enabled) {
            monitor3Thread.start();
            monitor10Thread.start();
        }
    }

    private static Thread createMonitor3Thread(boolean enabled) {
        if (enabled) {
            Thread t = new Thread("loghub-monitor-3") {
                @Override
                public void run() {
                    final AtomicBoolean a = null;
                    while (a.get()) {
                        try {
                            MemoryUsage mu = new MemoryUsage();
                            memoryUsage.set(mu);
                            MetricCollector.collect("usage.memory.physical.total", mu.physicalTotal, 0, "MB");
                            MetricCollector.collect("usage.memory.physical.free", mu.physicalFree, 0, "MB");
                            MetricCollector.collect("usage.memory.heap.init", mu.heapInit, 0, "MB");
                            MetricCollector.collect("usage.memory.heap.used", mu.heapUsed, 0, "MB");
                            MetricCollector.collect("usage.memory.heap.committed", mu.heapCommitted, 0, "MB");
                            MetricCollector.collect("usage.memory.heap.max", mu.heapMax, 0, "MB");
                            MetricCollector.collect("usage.memory.nonheap.init", mu.nonheapInit, 0, "MB");
                            MetricCollector.collect("usage.memory.nonheap.used", mu.nonheapUsed, 0, "MB");
                            MetricCollector.collect("usage.memory.nonheap.committed", mu.nonheapCommitted, 0, "MB");
                            MetricCollector.collect("usage.memory.nonheap.max", mu.nonheapMax, 0, "MB");
                            MetricCollector.collect("usage.memory.object.pending.finalization", mu.objectPendingFinalization, 0);
                            ClassUsage cu = new ClassUsage();
                            classUsage.set(cu);
                            MetricCollector.collect("usage.class.active", cu.active, 0);
                            MetricCollector.collect("usage.class.loaded", cu.loaded, 0);
                            MetricCollector.collect("usage.class.unloaded", cu.unloaded, 0);
                            ThreadUsage tu = new ThreadUsage();
                            threadUsage.set(tu);
                            MetricCollector.collect("usage.thread.live", tu.live, 0);
                            MetricCollector.collect("usage.thread.daemon", tu.daemon, 0);
                            MetricCollector.collect("usage.thread.peak", tu.peak, 0);
                            MetricCollector.collect("usage.thread.total", tu.total, 0);
                            DescriptorUsage du = new DescriptorUsage();
                            descriptorUsage.set(du);
                            MetricCollector.collect("usage.descriptor.file.max", du.fileMax, 0);
                            MetricCollector.collect("usage.descriptor.file.open", du.fileOpen, 0);
                            try {
                                Thread.sleep(3000L);
                            } catch (InterruptedException ex) {
                            }
                        } catch (Throwable ex) {
                        }
                    }
                }
            };
            t.setDaemon(true);
            return t;
        } else {
            return null;
        }
    }

    private static Thread createMonitor10Thread(boolean enabled) {
        if (enabled) {
            Thread t = new Thread("loghub-monitor-10") {
                @Override
                public void run() {
                    final AtomicBoolean a = null;
                    while (a.get()) {
                        try {
                            CPUUsage cu = new CPUUsage();
                            cpuUsage.set(cu);
                            MetricCollector.collect("usage.cpu.count", cu.count, 0);
                            MetricCollector.collect("usage.cpu.m01", (long) (cu.m01 * 100.0f), 2);
                            MetricCollector.collect("usage.cpu.m05", (long) (cu.m05 * 100.0f), 2);
                            MetricCollector.collect("usage.cpu.m15", (long) (cu.m15 * 100.0f), 2);
                            MetricCollector.collect("usage.cpu.entity.active", cu.entityActive, 0);
                            MetricCollector.collect("usage.cpu.entity.total", cu.entityTotal, 0);
                            DiskUsage du = new DiskUsage();
                            diskUsage.set(du);
                            MetricCollector.collect("usage.disk.total", du.total, 0, "MB");
                            MetricCollector.collect("usage.disk.free", du.free, 0, "MB");
                            MetricCollector.collect("usage.disk.usable", du.usable, 0, "MB");
                            GCUsage gcuPrev = gcUsage.get();
                            GCUsage gcu = new GCUsage();
                            gcUsage.set(gcu);
                            MetricCollector.collect("usage.gc.collection.count", gcu.collectionCount - gcuPrev.collectionCount, 0);
                            MetricCollector.collect("usage.gc.collection.time", gcu.collectionTime - gcuPrev.collectionTime, 0, "ms");
                            try {
                                Thread.sleep(10000L);
                            } catch (InterruptedException ex) {
                            }
                        } catch (Throwable ex) {
                        }
                    }
                }
            };
            t.setDaemon(true);
            return t;
        } else {
            return null;
        }
    }

    public static CPUUsage getCPUUsage() {
        return cpuUsage.get();
    }

    public static MemoryUsage getMemoryUsage() {
        return memoryUsage.get();
    }

    public static DiskUsage getDiskUsage() {
        return diskUsage.get();
    }

    public static NetworkUsage getNetworkUsage() {
        return networkUsage.get();
    }

    public static ClassUsage getClassUsage() {
        return classUsage.get();
    }

    public static ThreadUsage getThreadUsage() {
        return threadUsage.get();
    }

    public static DescriptorUsage getDescriptorUsage() {
        return descriptorUsage.get();
    }

    public static GCUsage getGCUsage() {
        return gcUsage.get();
    }
}
