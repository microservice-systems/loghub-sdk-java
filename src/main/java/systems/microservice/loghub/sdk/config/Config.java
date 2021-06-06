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

package systems.microservice.loghub.sdk.config;

import systems.microservice.loghub.sdk.buffer.Bufferable;
import systems.microservice.loghub.sdk.config.extractors.ValueOfExtractor;
import systems.microservice.loghub.sdk.utils.Argument;
import systems.microservice.loghub.sdk.utils.Range;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class Config implements Bufferable, Serializable {
    private static final long serialVersionUID = 1L;
    private static final ReentrantLock lock = new ReentrantLock(false);
    private static final AtomicReference<Config> config = new AtomicReference<>(new Config());
    private static final ThreadLocal<ThreadConfig> threadConfig = ThreadLocal.withInitial(() -> new ThreadConfig());
    private static final ConcurrentHashMap<String, Set<PropertyListener>> propertyListeners = new ConcurrentHashMap<>(256, 0.75f, 1);

    public final UUID uuid;
    public final Map<String, Property> properties;
    public final String comment;
    public final URL url;
    public final String user;
    public final String commit;
    public final long time;

    public Config() {
        this(Collections.emptyList());
    }

    public Config(Collection<Property> properties) {
        this(properties, null, null, null, null);
    }

    public Config(Collection<Property> properties, String comment, URL url, String user, String commit) {
        this(properties, null, null, null, null, null);
    }

    public Config(Collection<Property> properties, Property property) {
        this(properties, property, null, null, null, null);
    }

    public Config(Collection<Property> properties, Property property, String comment, URL url, String user, String commit) {
        Argument.notNull("properties", properties);

        this.uuid = UUID.randomUUID();
        this.properties = createProperties(properties, property);
        this.comment = comment;
        this.url = url;
        this.user = user;
        this.commit = commit;
        this.time = System.currentTimeMillis();
    }

    @Override
    public int write(byte[] buffer, int index, Map<String, Object> context) {
        return 0;
    }

    private static Map<String, Property> createProperties(Collection<Property> properties, Property property) {
        LinkedHashMap<String, Property> ps = new LinkedHashMap<>(properties.size() + ((property != null) ? 1 : 0));
        for (Property p : properties) {
            ps.put(p.key, p);
        }
        if (property != null) {
            ps.put(property.key, property);
        }
        return Collections.unmodifiableMap(ps);
    }

    public static Config getConfig() {
        Config cfg = threadConfig.get().config;
        return (cfg != null) ? cfg : Config.config.get();
    }

    private static Config setConfig(Config config) {
        Argument.notNull("config", config);

        ThreadConfig tc = threadConfig.get();
        if (tc.config != null) {
            Config cfg = tc.config;
            tc.config = config;
            Config.config.set(config);
            return cfg;
        } else {
            Config cfg = Config.config.get();
            Config.config.set(config);
            return cfg;
        }
    }

    public static Config acquireConfig() {
        ThreadConfig tc = threadConfig.get();
        long c = tc.count;
        if (c == 0L) {
            tc.config = Config.config.get();
            tc.count++;
        } else if (c > 0L) {
            tc.count++;
        } else {
            throw new Error("This should not happen");
        }
        return tc.config;
    }

    public static Config releaseConfig() {
        ThreadConfig tc = threadConfig.get();
        Config cfg = tc.config;
        long c = tc.count;
        if (c == 0L) {
            throw new RuntimeException("Config is not acquired");
        } else if (c == 1L) {
            tc.config = null;
            tc.count--;
        } else if (c > 1L) {
            tc.count--;
        } else {
            throw new Error("This should not happen");
        }
        return cfg;
    }

    public static List<PropertyEvent> applyConfig(Config config) {
        Argument.notNull("config", config);

        lock.lock();
        try {
            boolean v = true;
            Collection<Property> nps = config.properties.values();
            ArrayList<PropertyEvent> pes = new ArrayList<>(nps.size());
            for (Property np : nps) {
                Property op = getConfig().properties.get(np.key);
                if (op != null) {
                    if (!np.uuid.equals(op.uuid)) {
                        pes.add(new PropertyEvent(op, np));
                    }
                } else {
                    pes.add(new PropertyEvent(null, np));
                }
            }
            for (PropertyEvent pe : pes) {
                Set<PropertyListener> pls = propertyListeners.get(pe.key);
                if (pls != null) {
                    for (PropertyListener pl : pls) {
                        try {
                            if (!pl.onPropertyValidate(pe)) {
                                v = false;
                                pe.oldProperty.invalidProperty.set(pe.newProperty);
                            }
                        } catch (Throwable ex) {
                        }
                    }
                }
            }
            if (v) {
                for (PropertyEvent pe : pes) {
                    pe.newProperty.oldProperty.set(pe.oldProperty);
                }
                setConfig(config);
                for (PropertyEvent pe : pes) {
                    Set<PropertyListener> pls = propertyListeners.get(pe.key);
                    if (pls != null) {
                        for (PropertyListener pl : pls) {
                            try {
                                pl.onPropertyChange(pe);
                            } catch (Throwable ex) {
                            }
                        }
                    }
                }
            }
            return pes;
        } finally {
            lock.unlock();
        }
    }

    public static <T> T getProperty(String key, Class<T> clazz) {
        return getProperty(key, clazz, null, null, false, null);
    }

    public static <T> T getProperty(String key, Class<T> clazz, T defaultValue) {
        return getProperty(key, clazz, defaultValue, null, false, null);
    }

    public static <T> T getProperty(String key, Class<T> clazz, T defaultValue, String unit) {
        return getProperty(key, clazz, defaultValue, unit, false, null);
    }

    public static <T> T getProperty(String key, Class<T> clazz, T defaultValue, String unit, boolean nullable) {
        return getProperty(key, clazz, defaultValue, unit, nullable, null);
    }

    public static <T> T getProperty(String key, Class<T> clazz, T defaultValue, String unit, boolean nullable, T[] possibleValues) {
        Config cfg = getConfig();
        Property p = cfg.properties.get(key);
        if (p == null) {
            setConfig(new Config(cfg.properties.values(), new Property(null, key, null, null, null, null, null, null, null, null, null, null)));
        }
        return p.value.get(clazz);
    }

    public static <I, O> O getProperty(String key, Class<I> clazz, I defaultValue, String unit, boolean nullable, I[] possibleValues, Class<O> outputClass) {
        return getProperty(key, clazz, defaultValue, unit, nullable, possibleValues, outputClass, ValueOfExtractor.getInstance());
    }

    public static <I, O> O getProperty(String key, Class<I> clazz, I defaultValue, String unit, boolean nullable, I[] possibleValues, Class<O> outputClass, Extractor<I, O> extractor) {
        return null;
    }

    public static <T extends Comparable<T>> T getProperty(String key, Class<T> clazz, T defaultValue, String unit, boolean nullable, Range<T> rangeValues) {
        return null;
    }

    public static <I extends Comparable<I>, O> O getProperty(String key, Class<I> clazz, I defaultValue, String unit, boolean nullable, Range<I> rangeValues, Class<O> outputClass) {
        return getProperty(key, clazz, defaultValue, unit, nullable, rangeValues, outputClass, ValueOfExtractor.getInstance());
    }

    public static <I extends Comparable<I>, O> O getProperty(String key, Class<I> clazz, I defaultValue, String unit, boolean nullable, Range<I> rangeValues, Class<O> outputClass, Extractor<I, O> extractor) {
        return null;
    }

    private static final class ThreadConfig {
        public Config config;
        public long count;

        public ThreadConfig() {
            this.config = null;
            this.count = 0L;
        }
    }
}
