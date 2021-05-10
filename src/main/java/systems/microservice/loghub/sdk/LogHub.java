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
import systems.microservice.loghub.sdk.util.StringUtil;
import systems.microservice.loghub.sdk.util.TimeUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    private static final boolean enabled = createEnabled(properties, account, environment, application, version, instance);
    private static final String hostName = createHostName();
    private static final String hostIP = createHostIP();
    private static final int cpuCount = createCPUCount();
    private static final String osArch = createOSArch();
    private static final String osName = createOSName();
    private static final String osVersion = createOSVersion();
    private static final String processUUID = createProcessUUID();
    private static final long processID = createProcessID();
    private static final long processStart = createProcessStart();
    private static final String processCmdline = createProcessCmdline(processID);
    private static final URL url = createURL(properties, account);
    private static final String basicUser = createBasicUser(properties);
    private static final String basicAuth = createBasicAuth(basicUser, createBasicPassword(properties));
    private static final String persistencePathBase = createPersistencePathBase(properties);
    private static final String persistencePathFull = createPersistencePathFull(persistencePathBase, account, environment, application, instance);
    private static final LogLevel eventLevel = createEventLevel(properties);
    private static final int eventLevelID = createEventLevelID(properties, eventLevel);
    private static final int eventFlushSize = createEventFlushSize(properties);
    private static final long eventFlushSpan = createEventFlushSpan(properties);
    private static final long eventPersistenceSize = createEventPersistenceSize(properties);
    private static final int eventFlushRetryCount = createEventFlushRetryCount(properties);
    private static final long eventFlushRetryDelay = createEventFlushRetryDelay(properties);
    private static final boolean uncaughtExceptionHandler = createUncaughtExceptionHandler(properties);
    private static final boolean systemOut = createSystemOut(properties);
    private static final boolean fileOut = createFileOut(properties);
    private static final String filePath = createFilePath(properties, persistencePathFull, processStart);
    private static final boolean info = createInfo(properties);
    private static final boolean debug = createDebug(properties);
    private static final ThreadLocal<LogThreadInfo> threadInfo = ThreadLocal.withInitial(() -> new LogThreadInfo());
    private static final AtomicReference<LogCPUUsage> cpuUsage = new AtomicReference<>(new LogCPUUsage());
    private static final AtomicReference<LogMemoryUsage> memoryUsage = new AtomicReference<>(new LogMemoryUsage());
    private static final AtomicReference<LogDiskUsage> diskUsage = new AtomicReference<>(new LogDiskUsage());
    private static final AtomicReference<LogClassUsage> classUsage = new AtomicReference<>(new LogClassUsage());
    private static final AtomicReference<LogThreadUsage> threadUsage = new AtomicReference<>(new LogThreadUsage());
    private static final AtomicReference<LogDescriptorUsage> descriptorUsage = new AtomicReference<>(new LogDescriptorUsage());
    private static final AtomicReference<LogGCUsage> gcUsage = new AtomicReference<>(new LogGCUsage());
    private static final AtomicBoolean active = new AtomicBoolean(enabled);
    private static final AtomicInteger finished = new AtomicInteger(2);
    private static final Lock outLock = createOutLock(enabled, systemOut, fileOut);
    private static final PrintStream fileOutStream = createFileOutStream(enabled, fileOut, filePath);
    private static final LogEventWriter eventWriter = createEventWriter(enabled);
    private static final LogMetricWriter metricWriter = createMetricWriter(enabled);
    private static final Thread monitor3Thread = createMonitor3Thread(enabled);
    private static final Thread monitor10Thread = createMonitor10Thread(enabled);
    private static final Thread flushEventsThread = createFlushEventsThread(enabled);
    private static final Thread flushMetricsThread = createFlushMetricsThread(enabled);
    private static final Thread shutdownThread = createShutdownThread(enabled);

    private LogHub() {
    }

    static {
        if (enabled) {
            try {
                Files.createDirectories(Paths.get(persistencePathFull + "/config"));
                Files.createDirectories(Paths.get(persistencePathFull + "/event"));
                Files.createDirectories(Paths.get(persistencePathFull + "/metric"));
                Files.createDirectories(Paths.get(persistencePathFull + "/file"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            monitor3Thread.start();
            monitor10Thread.start();
            flushEventsThread.start();
            flushMetricsThread.start();
            Runtime.getRuntime().addShutdownHook(shutdownThread);
            logEvent(true, System.currentTimeMillis(), LogHub.class.getCanonicalName(), LogLevel.LIFECYCLE.id, LogLevel.LIFECYCLE.name(), "Hello World!");
            if (uncaughtExceptionHandler) {
                try {
                    Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread t, Throwable e) {
                            try {
                                logEvent(System.currentTimeMillis(), LogHub.class.getCanonicalName(), LogLevel.ERROR.id, LogLevel.ERROR.name(), e, null, null, null, "Uncaught exception: " + e.getClass().getCanonicalName());
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

    private static String createHostName() {
        String hn = getHostName();
        if (hn == null) {
            hn = "unknown";
        }
        return hn;
    }

    private static String createHostIP() {
        String hi = getHostAddress();
        if (hi == null) {
            hi = "unknown";
        }
        return hi;
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

    private static String createProcessCmdline(long processID) {
        if (processID != -1L) {
            return StringUtil.load(String.format("/proc/%d/cmdline", processID), "unknown");
        } else {
            return "unknown";
        }
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
        ppb = ppb.trim();
        while (ppb.endsWith("/")) {
            ppb = ppb.substring(0, ppb.length() - 1);
        }
        return ppb;
    }

    private static String createPersistencePathFull(String persistencePathBase, String account, String environment, String application, String instance) {
        if ((account != null) && (environment != null) && (application != null) && (instance != null)) {
            return persistencePathBase + "/loghub/" + account + "/" + environment + "/" + application + "/" + instance;
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

    private static long createEventPersistenceSize(Map<String, String> properties) {
        String eps = System.getenv("LOGHUB_EVENT_PERSISTENCE_SIZE");
        if (eps == null) {
            eps = System.getProperty("loghub.event.persistence.size");
            if (eps == null) {
                eps = properties.get("loghub.event.persistence.size");
                if (eps == null) {
                    return 1073741824L;
                }
            }
        }
        return Argument.inRangeLong("loghub.event.persistence.size", Long.parseLong(eps), 0L, Long.MAX_VALUE);
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
        String ueh = System.getenv("LOGHUB_UNCAUGHT_EXCEPTION_HANDLER");
        if (ueh == null) {
            ueh = System.getProperty("loghub.uncaught.exception.handler");
            if (ueh == null) {
                ueh = properties.get("loghub.uncaught.exception.handler");
                if (ueh == null) {
                    return true;
                }
            }
        }
        return Boolean.parseBoolean(ueh);
    }

    private static boolean createSystemOut(Map<String, String> properties) {
        String so = System.getenv("LOGHUB_SYSTEM_OUT");
        if (so == null) {
            so = System.getProperty("loghub.system.out");
            if (so == null) {
                so = properties.get("loghub.system.out");
                if (so == null) {
                    return false;
                }
            }
        }
        return Boolean.parseBoolean(so);
    }

    private static boolean createFileOut(Map<String, String> properties) {
        String fo = System.getenv("LOGHUB_FILE_OUT");
        if (fo == null) {
            fo = System.getProperty("loghub.file.out");
            if (fo == null) {
                fo = properties.get("loghub.file.out");
                if (fo == null) {
                    return false;
                }
            }
        }
        return Boolean.parseBoolean(fo);
    }

    private static String createFilePath(Map<String, String> properties, String persistencePathFull, long processStart) {
        String fp = System.getenv("LOGHUB_FILE_PATH");
        if (fp == null) {
            fp = System.getProperty("loghub.file.path");
            if (fp == null) {
                fp = properties.get("loghub.file.path");
                if (fp == null) {
                    if (persistencePathFull != null) {
                        return persistencePathFull + "/file/" + TimeUtil.formatName(processStart) + ".log";
                    } else {
                        return null;
                    }
                }
            }
        }
        return fp.trim();
    }

    private static boolean createInfo(Map<String, String> properties) {
        String i = System.getenv("LOGHUB_INFO");
        if (i == null) {
            i = System.getProperty("loghub.info");
            if (i == null) {
                i = properties.get("loghub.info");
                if (i == null) {
                    return true;
                }
            }
        }
        return Boolean.parseBoolean(i);
    }

    private static boolean createDebug(Map<String, String> properties) {
        String d = System.getenv("LOGHUB_DEBUG");
        if (d == null) {
            d = System.getProperty("loghub.debug");
            if (d == null) {
                d = properties.get("loghub.debug");
                if (d == null) {
                    return false;
                }
            }
        }
        return Boolean.parseBoolean(d);
    }

    private static Lock createOutLock(boolean enabled, boolean systemOut, boolean fileOut) {
        if (enabled && (systemOut || fileOut)) {
            return new ReentrantLock(false);
        } else {
            return null;
        }
    }

    private static PrintStream createFileOutStream(boolean enabled, boolean fileOut, String filePath) {
        if (enabled && fileOut) {
            try {
                return new PrintStream(filePath, StandardCharsets.UTF_8.name());
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    private static LogEventWriter createEventWriter(boolean enabled) {
        if (enabled) {
            return new LogEventWriter();
        } else {
            return null;
        }
    }

    private static LogMetricWriter createMetricWriter(boolean enabled) {
        if (enabled) {
            return new LogMetricWriter(60000L, 5);
        } else {
            return null;
        }
    }

    private static Thread createMonitor3Thread(boolean enabled) {
        if (enabled) {
            Thread t = new Thread("loghub-monitor-3") {
                @Override
                public void run() {
                    final AtomicBoolean a = LogHub.active;
                    while (a.get()) {
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
                    final AtomicBoolean a = LogHub.active;
                    while (a.get()) {
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
            t.setDaemon(true);
            return t;
        } else {
            return null;
        }
    }

    private static Thread createFlushEventsThread(boolean enabled) {
        if (enabled) {
            Thread t = new Thread("loghub-flush-events") {
                @Override
                public void run() {
                    final AtomicBoolean a = LogHub.active;
                    final AtomicInteger f = LogHub.finished;
                    final Lock ol = LogHub.outLock;
                    final PrintStream fos = LogHub.fileOutStream;
                    final LogEventWriter ew = LogHub.eventWriter;
                    try {
                        while (a.get()) {
                            try {
                                try {
                                    Thread.sleep(3000L);
                                } catch (InterruptedException ex) {
                                }
                            } catch (Throwable ex) {
                            }
                        }
                        if ((ol != null) && (fos != null)) {
                            ol.lock();
                            try {
                                try {
                                    fos.close();
                                } catch (Throwable ex) {
                                }
                            } finally {
                                ol.unlock();
                            }
                        }
                    } finally {
                        f.decrementAndGet();
                    }
                }
            };
            t.setDaemon(false);
            return t;
        } else {
            return null;
        }
    }

    private static Thread createFlushMetricsThread(boolean enabled) {
        if (enabled) {
            Thread t = new Thread("loghub-flush-metrics") {
                @Override
                public void run() {
                    final AtomicBoolean a = LogHub.active;
                    final AtomicInteger f = LogHub.finished;
                    final LogMetricWriter mw = LogHub.metricWriter;
                    try {
                        final long s = mw.getSpan();
                        final LogMetricFlushInfo fi = new LogMetricFlushInfo();
                        while (a.get()) {
                            try {
                                try {
                                    Thread.sleep(s);
                                } catch (InterruptedException ex) {
                                }
                                mw.flush(fi);
                            } catch (Throwable ex) {
                            }
                        }
                    } finally {
                        f.decrementAndGet();
                    }
                }
            };
            t.setDaemon(false);
            return t;
        } else {
            return null;
        }
    }

    private static Thread createShutdownThread(boolean enabled) {
        if (enabled) {
            Thread t = new Thread("loghub-shutdown") {
                @Override
                public void run() {
                    final AtomicBoolean a = LogHub.active;
                    final Thread fet = LogHub.flushEventsThread;
                    final Thread fmt = LogHub.flushMetricsThread;
                    a.set(false);
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
            t.setDaemon(false);
            return t;
        } else {
            return null;
        }
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

    private static void logEventOut(long time, String logger, int level, String levelName, Throwable exception, String message) {
        if (outLock != null) {
            String t = String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL", time);
            String s = String.format("%s [%s] [%s] %s - %s", t, Thread.currentThread().getName(), logger, levelName, message);
            outLock.lock();
            try {
                if (systemOut) {
                    System.out.println(s);
                }
                if (fileOutStream != null) {
                    fileOutStream.println(s);
                }
            } finally {
                outLock.unlock();
            }
        }
    }

    private static void logEvent(boolean start, long time, String logger, int level, String levelName, String message) {
    }

    public static void logEvent(long time, String logger, int level, String levelName, Throwable exception, Map<String, LogTag> tags, Map<String, LogImage> images, Map<String, LogBlob> blobs, String message) {
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
