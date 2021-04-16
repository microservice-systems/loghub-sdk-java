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

import systems.microservice.loghub.sdk.util.Argument;
import systems.microservice.loghub.sdk.util.ResourceUtil;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class LogHub {
    private static final Map<String, String> properties = createProperties();
    private static final String account = createAccount(properties);
    private static final String environment = createEnvironment(properties);
    private static final String application = createApplication(properties);
    private static final String version = createVersion(properties);
    private static final String instance = createInstance(properties);
    private static final String hostName = createHostName();
    private static final String hostIP = createHostIP();
    private static final int cpuCount = createCPUCount();
    private static final String osArch = createOSArch();
    private static final String osName = createOSName();
    private static final String osVersion = createOSVersion();
    private static final String processUUID = createProcessUUID();
    private static final long processID = createProcessID();
    private static final long processStart = createProcessStart();
    private static final URL url = createURL(properties, account);
    private static final String basicUser = createBasicUser(properties);
    private static final String basicAuth = createBasicAuth(basicUser, createBasicPassword(properties));
    private static final String persistencePathBase = createPersistencePathBase(properties);
    private static final String persistencePathFull = createPersistencePathFull(persistencePathBase, account, environment, application, instance);
    private static final LogLevel eventLevel = createEventLevel(properties);
    private static final int eventLevelID = createEventLevelID(properties, eventLevel);
    private static final int eventFlushSize = createEventFlushSize(properties);
    private static final long eventFlushSpan = createEventFlushSpan(properties);
    private static final int eventFlushRetryCount = createEventFlushRetryCount(properties);
    private static final long eventFlushRetryDelay = createEventFlushRetryDelay(properties);
    private static final boolean uncaughtExceptionHandler = createUncaughtExceptionHandler(properties);
    private static final boolean info = createInfo(properties);
    private static final boolean debug = createDebug(properties);
    private static final AtomicBoolean enabled = new AtomicBoolean(createEnabled(properties, account, environment, application, version, instance));
    private static final ThreadLocal<LogThreadInfo> threadInfo = ThreadLocal.withInitial(() -> new LogThreadInfo());
    private static final AtomicReference<LogCPUUsage> cpuUsage = new AtomicReference<>(new LogCPUUsage());
    private static final AtomicReference<LogMemoryUsage> memoryUsage = new AtomicReference<>(new LogMemoryUsage());
    private static final AtomicReference<LogDiskUsage> diskUsage = new AtomicReference<>(new LogDiskUsage());
    private static final AtomicReference<LogClassUsage> classUsage = new AtomicReference<>(new LogClassUsage());
    private static final AtomicReference<LogThreadUsage> threadUsage = new AtomicReference<>(new LogThreadUsage());
    private static final AtomicReference<LogDescriptorUsage> descriptorUsage = new AtomicReference<>(new LogDescriptorUsage());
    private static final AtomicReference<LogGCUsage> gcUsage = new AtomicReference<>(new LogGCUsage());
    private static final LogEventWriter eventWriter;
    private static final LogMetricWriter metricWriter;
    private static final Thread monitor3Thread;
    private static final Thread monitor10Thread;
    private static final Thread flushEventsThread;
    private static final Thread flushMetricsThread;
    private static final Thread shutdownThread;

    private LogHub() {
    }

    static {
        if (enabled.get()) {
            eventWriter = new LogEventWriter();
            metricWriter = new LogMetricWriter(60000L, 5);
            monitor3Thread = new Thread("loghub-monitor-3") {
                @Override
                public void run() {
                    final AtomicBoolean e = enabled;
                    while (e.get()) {
                        try {
                            LogMemoryUsage mu = new LogMemoryUsage();
                            memoryUsage.set(mu);
                            logMetric("usage.memory.physical.total", mu.physicalTotal, 0, "MB");
                            logMetric("usage.memory.physical.free", mu.physicalFree, 0, "MB");
                            logMetric("usage.memory.heap.init", mu.heapInit, 0, "MB");
                            logMetric("usage.memory.heap.used", mu.heapUsed, 0, "MB");
                            logMetric("usage.memory.heap.committed", mu.heapCommitted, 0, "MB");
                            logMetric("usage.memory.heap.max", mu.heapMax, 0, "MB");
                            logMetric("usage.memory.nonheap.init", mu.nonheapInit, 0, "MB");
                            logMetric("usage.memory.nonheap.used", mu.nonheapUsed, 0, "MB");
                            logMetric("usage.memory.nonheap.committed", mu.nonheapCommitted, 0, "MB");
                            logMetric("usage.memory.nonheap.max", mu.nonheapMax, 0, "MB");
                            logMetric("usage.memory.object.pending.finalization", mu.objectPendingFinalization, 0);
                            LogClassUsage cu = new LogClassUsage();
                            classUsage.set(cu);
                            logMetric("usage.class.active", cu.active, 0);
                            logMetric("usage.class.loaded", cu.loaded, 0);
                            logMetric("usage.class.unloaded", cu.unloaded, 0);
                            LogThreadUsage tu = new LogThreadUsage();
                            threadUsage.set(tu);
                            logMetric("usage.thread.live", tu.live, 0);
                            logMetric("usage.thread.daemon", tu.daemon, 0);
                            logMetric("usage.thread.peak", tu.peak, 0);
                            logMetric("usage.thread.total", tu.total, 0);
                            LogDescriptorUsage du = new LogDescriptorUsage();
                            descriptorUsage.set(du);
                            logMetric("usage.descriptor.file.max", du.fileMax, 0);
                            logMetric("usage.descriptor.file.open", du.fileOpen, 0);
                            try {
                                Thread.sleep(3000L);
                            } catch (InterruptedException ex) {
                            }
                        } catch (Throwable ex) {
                        }
                    }
                }
            };
            monitor3Thread.setDaemon(true);
            monitor3Thread.start();
            monitor10Thread = new Thread("loghub-monitor-10") {
                @Override
                public void run() {
                    final AtomicBoolean e = enabled;
                    while (e.get()) {
                        try {
                            LogCPUUsage cu = new LogCPUUsage();
                            cpuUsage.set(cu);
                            logMetric("usage.cpu.count", LogCPUUsage.COUNT, 0);
                            logMetric("usage.cpu.m1", (long) (cu.m1 * 100.0f), 0, "%");
                            logMetric("usage.cpu.m5", (long) (cu.m5 * 100.0f), 0, "%");
                            logMetric("usage.cpu.m15", (long) (cu.m15 * 100.0f), 0, "%");
                            logMetric("usage.cpu.entity.active", cu.entityActive, 0);
                            logMetric("usage.cpu.entity.total", cu.entityTotal, 0);
                            LogDiskUsage du = new LogDiskUsage();
                            diskUsage.set(du);
                            logMetric("usage.disk.total", du.total, 0, "MB");
                            logMetric("usage.disk.free", du.free, 0, "MB");
                            logMetric("usage.disk.usable", du.usable, 0, "MB");
                            LogGCUsage gcuPrev = gcUsage.get();
                            LogGCUsage gcu = new LogGCUsage();
                            gcUsage.set(gcu);
                            logMetric("usage.gc.collection.count", gcu.collectionCount - gcuPrev.collectionCount, 0);
                            logMetric("usage.gc.collection.time", gcu.collectionTime - gcuPrev.collectionTime, 3, "s");
                            try {
                                Thread.sleep(10000L);
                            } catch (InterruptedException ex) {
                            }
                        } catch (Throwable ex) {
                        }
                    }
                }
            };
            monitor10Thread.setDaemon(true);
            monitor10Thread.start();
            flushEventsThread = new Thread("loghub-flush-events") {
                @Override
                public void run() {
                    final AtomicBoolean e = enabled;
                    while (e.get()) {
                        try {
                            try {
                                Thread.sleep(3000L);
                            } catch (InterruptedException ex) {
                            }
                        } catch (Throwable ex) {
                        }
                    }
                }
            };
            flushEventsThread.setDaemon(false);
            flushEventsThread.start();
            flushMetricsThread = new Thread("loghub-flush-metrics") {
                @Override
                public void run() {
                    final AtomicBoolean e = enabled;
                    final LogMetricWriter mw = LogHub.metricWriter;
                    final long s = mw.getSpan();
                    final LogMetricFlushInfo fi = new LogMetricFlushInfo();
                    while (e.get()) {
                        try {
                            try {
                                Thread.sleep(s);
                            } catch (InterruptedException ex) {
                            }
                            mw.flush(fi);
                        } catch (Throwable ex) {
                        }
                    }
                }
            };
            flushMetricsThread.setDaemon(false);
            flushMetricsThread.start();
            shutdownThread = new Thread("loghub-shutdown") {
                @Override
                public void run() {
                    final AtomicBoolean e = enabled;
                    final Thread fet = flushEventsThread;
                    final Thread fmt = flushMetricsThread;
                    e.set(false);
                    if (fet.isAlive()) {
                        try {
                            fet.join();
                        } catch (InterruptedException ex) {
                        }
                    }
                    if (fmt.isAlive()) {
                        try {
                            fmt.join();
                        } catch (InterruptedException ex) {
                        }
                    }
                }
            };
            shutdownThread.setDaemon(false);
            Runtime.getRuntime().addShutdownHook(shutdownThread);
            logEvent(true, System.currentTimeMillis(), LogLevel.LIFECYCLE.id, LogLevel.LIFECYCLE.name(), LogHub.class.getCanonicalName(), "Hello World!");
            if (uncaughtExceptionHandler) {
                try {
                    Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread t, Throwable e) {
                            try {
                                logEvent(System.currentTimeMillis(), LogLevel.ERROR.id, LogLevel.ERROR.name(), LogHub.class.getCanonicalName(), e, null, null, null, "Uncaught exception: " + e.getClass().getCanonicalName());
                            } catch (Throwable ex) {
                                info(LogHub.class, ex.toString());
                            }
                        }
                    });
                } catch (Exception ex) {
                    info(LogHub.class, ex.toString());
                }
            }
            info(LogHub.class, String.format("LogHub is ready for collecting events & metrics: [account='%s', environment='%s', application='%s', version='%s', instance='%s']",
                                             account, environment, application, version, instance));
        } else {
            eventWriter = null;
            metricWriter = null;
            monitor3Thread = null;
            monitor10Thread = null;
            flushEventsThread = null;
            flushMetricsThread = null;
            shutdownThread = null;
        }
    }

    private static Map<String, String> createProperties() {
        Map<String, String> ps = new LinkedHashMap<>(64);
        try {
            Properties p = ResourceUtil.getProperties(LogHub.class, "/loghub.properties", null);
            for (Map.Entry<Object, Object> e : p.entrySet()) {
                Object k = e.getKey();
                if (k instanceof String) {
                    Object v = e.getValue();
                    if (v instanceof String) {
                        String ks = (String) k;
                        String vs = (String) v;
                        ps.put(ks, vs);
                    }
                }
            }
        } catch (Exception e) {
        }
        return Collections.unmodifiableMap(ps);
    }

    private static String createAccount(Map<String, String> properties) {
        String a = System.getenv("LOGHUB_ACCOUNT");
        if (a == null) {
            a = System.getProperty("loghub.account");
            if (a == null) {
                a = properties.get("loghub.account");
            }
        }
        return a;
    }

    private static String createEnvironment(Map<String, String> properties) {
        String e = System.getenv("LOGHUB_ENVIRONMENT");
        if (e == null) {
            e = System.getProperty("loghub.environment");
            if (e == null) {
                e = properties.get("loghub.environment");
            }
        }
        return e;
    }

    private static String createApplication(Map<String, String> properties) {
        String a = System.getenv("LOGHUB_APPLICATION");
        if (a == null) {
            a = System.getProperty("loghub.application");
            if (a == null) {
                a = properties.get("loghub.application");
            }
        }
        return a;
    }

    private static String createVersion(Map<String, String> properties) {
        String v = System.getenv("LOGHUB_VERSION");
        if (v == null) {
            v = System.getProperty("loghub.version");
            if (v == null) {
                v = properties.get("loghub.version");
            }
        }
        return v;
    }

    private static String createInstance(Map<String, String> properties) {
        String i = System.getenv("LOGHUB_INSTANCE");
        if (i == null) {
            i = System.getProperty("loghub.instance");
            if (i == null) {
                i = properties.get("loghub.instance");
                if (i == null) {
                    i = String.format("java-%s", getHostName());
                    if (i == null) {
                        i = String.format("java-%s", getHostAddress());
                        if (i == null) {
                            i = String.format("java-%s", UUID.randomUUID().toString());
                        }
                    }
                }
            }
        }
        return i;
    }

    private static String createHostName() {
        String ih = getHostName();
        if (ih == null) {
            ih = "unknown";
        }
        return ih;
    }

    private static String createHostIP() {
        String ii = getHostAddress();
        if (ii == null) {
            ii = "unknown";
        }
        return ii;
    }

    private static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    private static String getHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    private static int createCPUCount() {
        return ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();
    }

    private static String createOSArch() {
        return ManagementFactory.getOperatingSystemMXBean().getArch();
    }

    private static String createOSName() {
        return ManagementFactory.getOperatingSystemMXBean().getName();
    }

    private static String createOSVersion() {
        return ManagementFactory.getOperatingSystemMXBean().getVersion();
    }

    private static String createProcessUUID() {
        return UUID.randomUUID().toString();
    }

    private static long createProcessID() {
        String n = ManagementFactory.getRuntimeMXBean().getName();
        if (n != null) {
            String[] ns = n.split("@");
            if (ns.length > 0) {
                try {
                    return Long.parseLong(ns[0]);
                } catch (Exception e) {
                }
            }
        }
        return -1L;
    }

    private static long createProcessStart() {
        return ManagementFactory.getRuntimeMXBean().getStartTime();
    }

    private static URL createURL(Map<String, String> properties, String account) {
        String u = System.getenv("LOGHUB_URL");
        if (u == null) {
            u = System.getProperty("loghub.url");
            if (u == null) {
                u = properties.get("loghub.url");
                if (u == null) {
                    u = String.format("https://%s/loghub/api/account/log", account);
                }
            }
        }
        try {
            return new URL(u.trim());
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private static String createBasicUser(Map<String, String> properties) {
        String bu = System.getenv("LOGHUB_BASIC_USER");
        if (bu == null) {
            bu = System.getProperty("loghub.basic.user");
            if (bu == null) {
                bu = properties.get("loghub.basic.user");
            }
        }
        return bu;
    }

    private static String createBasicPassword(Map<String, String> properties) {
        String bp = System.getenv("LOGHUB_BASIC_PASSWORD");
        if (bp == null) {
            bp = System.getProperty("loghub.basic.password");
            if (bp == null) {
                bp = properties.get("loghub.basic.password");
            }
        }
        return bp;
    }

    private static String createBasicAuth(String basicUser, String basicPassword) {
        if ((basicUser != null) && (basicPassword != null) && !basicUser.isEmpty()) {
            return "Basic " + new String(Base64.getEncoder().encode(String.format("%s:%s", basicUser, basicPassword).getBytes(StandardCharsets.UTF_8)));
        } else {
            return null;
        }
    }

    private static String createPersistencePathBase(Map<String, String> properties) {
        String ppb = System.getenv("LOGHUB_PERSISTENCE_PATH_BASE");
        if (ppb == null) {
            ppb = System.getProperty("loghub.persistence.path.base");
            if (ppb == null) {
                ppb = properties.get("loghub.persistence.path.base");
                if (ppb == null) {
                    String os = System.getProperty("os.name");
                    if ((os != null) && os.toLowerCase().contains("windows")) {
                        ppb = System.getProperty("java.io.tmpdir");
                    } else {
                        ppb = "/var/tmp";
                    }
                }
            }
        }
        if (ppb != null) {
            ppb = ppb.trim();
            while (ppb.endsWith("/")) {
                ppb = ppb.substring(0, ppb.length() - 1);
            }
        }
        return ppb;
    }

    private static String createPersistencePathFull(String persistencePathBase, String account, String environment, String application, String instance) {
        if (persistencePathBase != null) {
            String ppf = persistencePathBase + "/loghub/" + account + "/" + environment + "/" + application + "/" + instance;
            try {
                Files.createDirectories(Paths.get(ppf + "/config"));
                Files.createDirectories(Paths.get(ppf + "/event"));
                Files.createDirectories(Paths.get(ppf + "/metric"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return ppf;
        } else {
            return null;
        }
    }

    private static LogLevel createEventLevel(Map<String, String> properties) {
        String el = System.getenv("LOGHUB_EVENT_LEVEL");
        if (el == null) {
            el = System.getProperty("loghub.event.level");
            if (el == null) {
                el = properties.get("loghub.event.level");
                if (el == null) {
                    return LogLevel.ALL;
                }
            }
        }
        try {
            return LogLevel.valueOf(el);
        } catch (Exception ex) {
        }
        try {
            return LogLevel.getLevel(Integer.parseInt(el));
        } catch (Exception ex) {
            return null;
        }
    }

    private static int createEventLevelID(Map<String, String> properties, LogLevel eventLevel) {
        if (eventLevel != null) {
            return eventLevel.id;
        } else {
            String el = System.getenv("LOGHUB_EVENT_LEVEL");
            if (el == null) {
                el = System.getProperty("loghub.event.level");
                if (el == null) {
                    el = properties.get("loghub.event.level");
                    if (el == null) {
                        return LogLevel.ALL.id;
                    }
                }
            }
            return Integer.parseInt(el);
        }
    }

    private static int createEventFlushSize(Map<String, String> properties) {
        String efs = System.getenv("LOGHUB_EVENT_FLUSH_SIZE");
        if (efs == null) {
            efs = System.getProperty("loghub.event.flush.size");
            if (efs == null) {
                efs = properties.get("loghub.event.flush.size");
                if (efs == null) {
                    return 10485760;
                }
            }
        }
        return Argument.inRangeInt("loghub.event.flush.size", Integer.parseInt(efs), 0, Integer.MAX_VALUE);
    }

    private static long createEventFlushSpan(Map<String, String> properties) {
        String efs = System.getenv("LOGHUB_EVENT_FLUSH_SPAN");
        if (efs == null) {
            efs = System.getProperty("loghub.event.flush.span");
            if (efs == null) {
                efs = properties.get("loghub.event.flush.span");
                if (efs == null) {
                    return 60000L;
                }
            }
        }
        return Argument.inRangeLong("loghub.event.flush.span", Long.parseLong(efs), 3000L, 86400000L);
    }

    private static int createEventFlushRetryCount(Map<String, String> properties) {
        String efrc = System.getenv("LOGHUB_EVENT_FLUSH_RETRY_COUNT");
        if (efrc == null) {
            efrc = System.getProperty("loghub.event.flush.retry.count");
            if (efrc == null) {
                efrc = properties.get("loghub.event.flush.retry.count");
                if (efrc == null) {
                    return 5;
                }
            }
        }
        return Argument.inRangeInt("loghub.event.flush.retry.count", Integer.parseInt(efrc), 1, Integer.MAX_VALUE);
    }

    private static long createEventFlushRetryDelay(Map<String, String> properties) {
        String efrd = System.getenv("LOGHUB_EVENT_FLUSH_RETRY_DELAY");
        if (efrd == null) {
            efrd = System.getProperty("loghub.event.flush.retry.delay");
            if (efrd == null) {
                efrd = properties.get("loghub.event.flush.retry.delay");
                if (efrd == null) {
                    return 3000L;
                }
            }
        }
        return Argument.inRangeLong("loghub.event.flush.retry.delay", Long.parseLong(efrd), 0L, 86400000L);
    }

    private static boolean createUncaughtExceptionHandler(Map<String, String> properties) {
        String ie = System.getenv("LOGHUB_UNCAUGHT_EXCEPTION_HANDLER");
        if (ie == null) {
            ie = System.getProperty("loghub.uncaught.exception.handler");
            if (ie == null) {
                ie = properties.get("loghub.uncaught.exception.handler");
                if (ie == null) {
                    return true;
                }
            }
        }
        return Boolean.parseBoolean(ie);
    }

    private static boolean createInfo(Map<String, String> properties) {
        String ie = System.getenv("LOGHUB_INFO");
        if (ie == null) {
            ie = System.getProperty("loghub.info");
            if (ie == null) {
                ie = properties.get("loghub.info");
                if (ie == null) {
                    return true;
                }
            }
        }
        return Boolean.parseBoolean(ie);
    }

    private static boolean createDebug(Map<String, String> properties) {
        String de = System.getenv("LOGHUB_DEBUG");
        if (de == null) {
            de = System.getProperty("loghub.debug");
            if (de == null) {
                de = properties.get("loghub.debug");
                if (de == null) {
                    return false;
                }
            }
        }
        return Boolean.parseBoolean(de);
    }

    private static boolean createEnabled(Map<String, String> properties, String account, String environment, String application, String version, String instance) {
        String e = System.getenv("LOGHUB_ENABLED");
        if (e == null) {
            e = System.getProperty("loghub.enabled");
            if (e == null) {
                e = properties.get("loghub.enabled");
                if (e == null) {
                    e = "true";
                }
            }
        }
        boolean eb = Boolean.parseBoolean(e);
        return eb && (account != null) && (environment != null) && (application != null) && (version != null) && (instance != null);
    }

    protected static void info(Class logger, String message) {
        if (info) {
            Argument.notNull("logger", logger);
            Argument.notNull("message", message);

            String t = String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL", System.currentTimeMillis());
            System.out.println(String.format("%s [%s] [%s] INFO - %s", t, Thread.currentThread().getName(), logger.getCanonicalName(), message));
        }
    }

    protected static void debug(Class logger, String message) {
        if (debug) {
            Argument.notNull("logger", logger);
            Argument.notNull("message", message);

            String t = String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL", System.currentTimeMillis());
            System.out.println(String.format("%s [%s] [%s] DEBUG - %s", t, Thread.currentThread().getName(), logger.getCanonicalName(), message));
        }
    }

    private static void logEvent(boolean start, long time, int level, String levelName, String logger, String message) {
    }

    public static void logEvent(long time, int level, String levelName, String logger, Throwable exception, Map<String, LogTag> tags, Map<String, LogImage> images, Map<String, LogBlob> blobs, String message) {
    }

    public static void logMetric(String name, long value, int point) {
        logMetric(name, value, point, "");
    }

    public static void logMetric(String name, long value, int point, String unit) {
        logMetric(name, 1L, value, point, unit);
    }

    public static void logMetric(String name, long count, long value, int point) {
        logMetric(name, count, value, point, "");
    }

    public static void logMetric(String name, long count, long value, int point, String unit) {
        Argument.notNull("name", name);
        Argument.inRangeLong("count", count, 0L, Long.MAX_VALUE);
        Argument.inRangeInt("point", point, 0, 15);
        Argument.notNull("unit", unit);

        metricWriter.log(name, count, value, point, unit);
    }
}
