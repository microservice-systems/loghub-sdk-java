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
    private static final String instanceHost = createInstanceHost();
    private static final String instanceIp = createInstanceIp();
    private static final String process = createProcess();
    private static final long processId = createProcessId();
    private static final long processStart = createProcessStart();
    private static final URL url = createUrl(properties, account);
    private static final String basicUser = createBasicUser(properties);
    private static final String basicAuth = createBasicAuth(basicUser, createBasicPassword(properties));
    private static final String persistencePathBase = createPersistencePathBase(properties);
    private static final String persistencePathFull = createPersistencePathFull(persistencePathBase, account, environment, application, instance);
    private static final ThreadLocal<LogThreadInfo> threadInfo = ThreadLocal.withInitial(() -> new LogThreadInfo());
    private static final AtomicReference<LogCpuUsage> cpuUsage = new AtomicReference<>(new LogCpuUsage());
    private static final AtomicReference<LogMemoryUsage> memoryUsage = new AtomicReference<>(new LogMemoryUsage());
    private static final AtomicReference<LogDiskUsage> diskUsage = new AtomicReference<>(new LogDiskUsage());
    private static final AtomicReference<LogClassUsage> classUsage = new AtomicReference<>(new LogClassUsage());
    private static final AtomicReference<LogThreadUsage> threadUsage = new AtomicReference<>(new LogThreadUsage());
    private static final LogEventWriter eventWriter = new LogEventWriter();
    private static final LogMetricWriter metricWriter = new LogMetricWriter(60000L, 5);
    private static final Thread monitor3Thread;
    private static final Thread monitor10Thread;
    private static final Thread flushEventsThread;
    private static final Thread flushMetricsThread;

    private LogHub() {
    }

    static {
        monitor3Thread = new Thread("loghub-monitor-3") {
            @Override
            public void run() {
                while (true) {
                    try {
                        LogMemoryUsage mu = new LogMemoryUsage();
                        memoryUsage.set(mu);
                        logMetric("usage.memory.heapInit", mu.heapInit, 0, "MB");
                        logMetric("usage.memory.heapUsed", mu.heapUsed, 0, "MB");
                        logMetric("usage.memory.heapCommitted", mu.heapCommitted, 0, "MB");
                        logMetric("usage.memory.heapMax", mu.heapMax, 0, "MB");
                        logMetric("usage.memory.nonHeapInit", mu.nonHeapInit, 0, "MB");
                        logMetric("usage.memory.nonHeapUsed", mu.nonHeapUsed, 0, "MB");
                        logMetric("usage.memory.nonHeapCommitted", mu.nonHeapCommitted, 0, "MB");
                        logMetric("usage.memory.nonHeapMax", mu.nonHeapMax, 0, "MB");
                        logMetric("usage.memory.objectPendingFinalization", mu.objectPendingFinalization, 0);
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
                        try {
                            Thread.sleep(3000L);
                        } catch (InterruptedException e) {
                        }
                    } catch (Throwable e) {
                    }
                }
            }
        };
        monitor3Thread.setDaemon(true);
        monitor3Thread.start();
        monitor10Thread = new Thread("loghub-monitor-10") {
            @Override
            public void run() {
                while (true) {
                    try {
                        LogCpuUsage cu = new LogCpuUsage();
                        cpuUsage.set(cu);
                        logMetric("usage.cpu.cpu", LogCpuUsage.CPU, 0);
                        logMetric("usage.cpu.m1", (long) (cu.m1 * 100.0f), 0, "%");
                        logMetric("usage.cpu.m5", (long) (cu.m5 * 100.0f), 0, "%");
                        logMetric("usage.cpu.m15", (long) (cu.m15 * 100.0f), 0, "%");
                        logMetric("usage.cpu.entityActive", cu.entityActive, 0);
                        logMetric("usage.cpu.entityTotal", cu.entityTotal, 0);
                        LogDiskUsage du = new LogDiskUsage();
                        diskUsage.set(du);
                        logMetric("usage.disk.total", du.total, 0, "MB");
                        logMetric("usage.disk.free", du.free, 0, "MB");
                        logMetric("usage.disk.usable", du.usable, 0, "MB");
                        try {
                            Thread.sleep(10000L);
                        } catch (InterruptedException e) {
                        }
                    } catch (Throwable e) {
                    }
                }
            }
        };
        monitor10Thread.setDaemon(true);
        monitor10Thread.start();
        flushEventsThread = new Thread("loghub-flush-events") {
            @Override
            public void run() {
                while (true) {
                    try {
                        try {
                            Thread.sleep(3000L);
                        } catch (InterruptedException e) {
                        }
                    } catch (Throwable e) {
                    }
                }
            }
        };
        flushEventsThread.setDaemon(false);
        flushEventsThread.start();
        flushMetricsThread = new Thread("loghub-flush-metrics") {
            @Override
            public void run() {
                while (true) {
                    try {
                        try {
                            Thread.sleep(3000L);
                        } catch (InterruptedException e) {
                        }
                    } catch (Throwable e) {
                    }
                }
            }
        };
        flushMetricsThread.setDaemon(false);
        flushMetricsThread.start();
    }

    public static void logEvent(long time,
                                int level,
                                String levelName,
                                String logger,
                                Throwable exception,
                                Map<String, LogTag> tags,
                                Map<String, LogImage> images,
                                Map<String, LogBlob> blobs,
                                String message) {
    }

    public static void logMetric(String name, long value, int point) {
        logMetric(name, value, point, "");
    }

    public static void logMetric(String name, long value, int point, String unit) {
        Argument.notNull("name", name);
        Argument.inRangeInt("point", point, 0, 15);
        Argument.notNull("unit", unit);

        metricWriter.log(name, value, point, unit);
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
        String a = System.getProperty("loghub.account");
        if (a == null) {
            a = System.getenv("LOGHUB_ACCOUNT");
            if (a == null) {
                a = properties.get("loghub.account");
            }
        }
        return a;
    }

    private static String createEnvironment(Map<String, String> properties) {
        String e = System.getProperty("loghub.environment");
        if (e == null) {
            e = System.getenv("LOGHUB_ENVIRONMENT");
            if (e == null) {
                e = properties.get("loghub.environment");
            }
        }
        return e;
    }

    private static String createApplication(Map<String, String> properties) {
        String a = System.getProperty("loghub.application");
        if (a == null) {
            a = System.getenv("LOGHUB_APPLICATION");
            if (a == null) {
                a = properties.get("loghub.application");
            }
        }
        return a;
    }

    private static String createVersion(Map<String, String> properties) {
        String v = System.getProperty("loghub.version");
        if (v == null) {
            v = System.getenv("LOGHUB_VERSION");
            if (v == null) {
                v = properties.get("loghub.version");
            }
        }
        return v;
    }

    private static String createInstance(Map<String, String> properties) {
        String i = System.getProperty("loghub.instance");
        if (i == null) {
            i = System.getenv("LOGHUB_INSTANCE");
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

    private static String createInstanceHost() {
        String ih = getHostName();
        if (ih == null) {
            ih = "unknown";
        }
        return ih;
    }

    private static String createInstanceIp() {
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

    private static String createProcess() {
        return UUID.randomUUID().toString();
    }

    private static long createProcessId() {
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

    private static URL createUrl(Map<String, String> properties, String account) {
        String u = System.getProperty("loghub.url");
        if (u == null) {
            u = System.getenv("LOGHUB_URL");
            if (u == null) {
                u = properties.get("loghub.url");
                if (u == null) {
                    u = String.format("https://%s/loghub/api/account/log/event", account);
                }
            }
        }
        try {
            return new URL(u);
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private static String createBasicUser(Map<String, String> properties) {
        String bu = System.getProperty("loghub.basic.user");
        if (bu == null) {
            bu = System.getenv("LOGHUB_BASIC_USER");
            if (bu == null) {
                bu = properties.get("loghub.basic.user");
            }
        }
        return bu;
    }

    private static String createBasicPassword(Map<String, String> properties) {
        String bp = System.getProperty("loghub.basic.password");
        if (bp == null) {
            bp = System.getenv("LOGHUB_BASIC_PASSWORD");
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
        String ppb = System.getProperty("loghub.persistence.path.base");
        if (ppb == null) {
            ppb = System.getenv("LOGHUB_PERSISTENCE_PATH_BASE");
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

    private static String createPersistencePathFull(String persistencePathBase,
                                                    String account,
                                                    String environment,
                                                    String application,
                                                    String instance) {
        if (persistencePathBase != null) {
            String ppf = persistencePathBase + "/loghub/" + account + "/" + environment + "/" + application + "/" + instance;
            try {
                Files.createDirectories(Paths.get(ppf));
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
}
