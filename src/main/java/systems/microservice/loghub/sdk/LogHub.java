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

import systems.microservice.loghub.sdk.buffer.Bufferable;
import systems.microservice.loghub.sdk.util.ArgumentUtil;
import systems.microservice.loghub.sdk.util.ResourceUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class LogHub {
    private static final Map<String, String> properties = createProperties();
    private static final String environment = createEnvironment(properties);
    private static final String application = createApplication(properties);
    private static final String version = createVersion(properties);
    private static final String instance = createInstance(properties);
    private static final LogEventWriter eventWriter = new LogEventWriter();
    private static final LogMetricWriter metricWriter = new LogMetricWriter();

    private LogHub() {
    }

    public static void logEvent(String type,
                                String thread,
                                long threadId,
                                String threadName,
                                int threadPriority,
                                long time,
                                int level,
                                String levelName,
                                String logger,
                                String message,
                                Bufferable tagsWriter,
                                Bufferable imagesWriter,
                                Bufferable blobsWriter) {
    }

    public static void logMetric(String name,
                                 long value,
                                 byte point,
                                 String unit) {
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

    private static String createEnvironment(Map<String, String> properties) {
        String e = System.getProperty("loghub.environment");
        if (e == null) {
            e = System.getenv("LOGHUB_ENVIRONMENT");
            if (e == null) {
                e = properties.get("loghub.environment");
                if (e == null) {
                    return null;
                }
            }
        }
        return ArgumentUtil.environment("loghub.environment", e);
    }

    private static String createApplication(Map<String, String> properties) {
        String a = System.getProperty("loghub.application");
        if (a == null) {
            a = System.getenv("LOGHUB_APPLICATION");
            if (a == null) {
                a = properties.get("loghub.application");
                if (a == null) {
                    return null;
                }
            }
        }
        return ArgumentUtil.application("loghub.application", a);
    }

    private static String createVersion(Map<String, String> properties) {
        String v = System.getProperty("loghub.version");
        if (v == null) {
            v = System.getenv("LOGHUB_VERSION");
            if (v == null) {
                v = properties.get("loghub.version");
                if (v == null) {
                    return null;
                }
            }
        }
        return ArgumentUtil.version("loghub.version", v);
    }

    private static String createInstance(Map<String, String> properties) {
        String i = System.getProperty("loghub.instance");
        if (i == null) {
            i = System.getenv("LOGHUB_INSTANCE");
            if (i == null) {
                i = properties.get("loghub.instance");
                if (i == null) {
                    i = createAWSInstance();
                    if (i == null) {
                        i = createLocalInstanceByHostName();
                        if (i == null) {
                            i = createLocalInstanceByHostIP();
                            if (i == null) {
                                return UUID.randomUUID().toString();
                            }
                        }
                    }
                }
            }
        }
        return ArgumentUtil.instance("loghub.instance", i);
    }

    private static String createAWSInstance() {
        try {
            URL u = new URL("http://169.254.169.254/latest/meta-data/instance-id");
            URLConnection conn = u.openConnection();
            conn.setConnectTimeout(10000);
            try (InputStream in = conn.getInputStream()) {
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                return r.readLine();
            }
        } catch (IOException e) {
            return null;
        }
    }

    private static String createLocalInstanceByHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    private static String createLocalInstanceByHostIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }
}
