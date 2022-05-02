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

package loghub;

import loghub.config.Validator;
import loghub.connector.Connector;
import loghub.connector.ConnectorFactory;

import java.util.ArrayList;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class Facade {
    private static final AtomicLong eventNumber = new AtomicLong(0L);
    private static final InputEventFilter eventInclude = Config.LOGHUB_EVENT_INCLUDE;
    private static final InputEventFilter eventExclude = Config.LOGHUB_EVENT_EXCLUDE;
    private static final ThreadLocal<ThreadInfo> threadInfo = ThreadLocal.withInitial(() -> new ThreadInfo());
    private static final Connector[] connectors = createConnectors();

    private Facade() {
    }

    private static Connector[] createConnectors() {
        ArrayList<Connector> cs = new ArrayList<>(16);
        ServiceLoader<ConnectorFactory> sl = ServiceLoader.load(ConnectorFactory.class);
        for (ConnectorFactory cf : sl) {
            if (cf != null) {
                if (cf.isEnabled()) {
                    Connector c = cf.createConnector();
                    if (c != null) {
                        cs.add(c);
                        System.out.println(String.format("[LOGHUB-SDK-JAVA]: Connector '%s' is initialized: %s", c.getClass().getCanonicalName(), c.getInfo()));
                    }
                }
            }
        }
        return cs.toArray(new Connector[0]);
    }

    public static boolean isInside() {
        return threadInfo.get().inside;
    }

    public static void log(long time, Input input,
                           String clazz, String method, String statement, String file, int line,
                           Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags,
                           String message) {
        Validator.inRangeLong("time", time, 0L, Long.MAX_VALUE);
        Validator.notNull("input", input);
        Validator.notNull("level", level);
        Validator.notNull("logger", logger);
        Validator.notNull("type", type);
        Validator.notNull("message", message);

        if ((eventInclude == null) || (eventInclude.match(time, input, clazz, method, statement, file, line, level, logger, type, exception, tag, tags, message))) {
            if ((eventExclude == null) || (!eventExclude.match(time, input, clazz, method, statement, file, line, level, logger, type, exception, tag, tags, message))) {
                ThreadInfo ti = threadInfo.get();
                if (!ti.inside) {
                    ti.inside = true;
                    try {
                        Connector[] cs = connectors;
                        for (int i = 0, ci = cs.length; i < ci; ++i) {
                            cs[i].log(time, eventNumber.getAndIncrement(), input,
                                    clazz, method, statement, file, line,
                                    level, logger, type, exception, tag, tags,
                                    message);
                        }
                    } finally {
                        ti.inside = false;
                    }
                }
            }
        }
    }

    public static void log(long time, Input input,
                           String clazz, String method, String statement, String file, int line,
                           Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags,
                           String message, Object param1) {
        Validator.inRangeLong("time", time, 0L, Long.MAX_VALUE);
        Validator.notNull("input", input);
        Validator.notNull("level", level);
        Validator.notNull("logger", logger);
        Validator.notNull("type", type);
        Validator.notNull("message", message);

        if ((eventInclude == null) || (eventInclude.match(time, input, clazz, method, statement, file, line, level, logger, type, exception, tag, tags, message, param1))) {
            if ((eventExclude == null) || (!eventExclude.match(time, input, clazz, method, statement, file, line, level, logger, type, exception, tag, tags, message, param1))) {
                ThreadInfo ti = threadInfo.get();
                if (!ti.inside) {
                    ti.inside = true;
                    try {
                        Connector[] cs = connectors;
                        for (int i = 0, ci = cs.length; i < ci; ++i) {
                            cs[i].log(time, eventNumber.getAndIncrement(), input,
                                    clazz, method, statement, file, line,
                                    level, logger, type, exception, tag, tags,
                                    message, param1);
                        }
                    } finally {
                        ti.inside = false;
                    }
                }
            }
        }
    }

    public static void log(long time, Input input,
                           String clazz, String method, String statement, String file, int line,
                           Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags,
                           String message, Object param1, Object param2) {
        Validator.inRangeLong("time", time, 0L, Long.MAX_VALUE);
        Validator.notNull("input", input);
        Validator.notNull("level", level);
        Validator.notNull("logger", logger);
        Validator.notNull("type", type);
        Validator.notNull("message", message);

        if ((eventInclude == null) || (eventInclude.match(time, input, clazz, method, statement, file, line, level, logger, type, exception, tag, tags, message, param1, param2))) {
            if ((eventExclude == null) || (!eventExclude.match(time, input, clazz, method, statement, file, line, level, logger, type, exception, tag, tags, message, param1, param2))) {
                ThreadInfo ti = threadInfo.get();
                if (!ti.inside) {
                    ti.inside = true;
                    try {
                        Connector[] cs = connectors;
                        for (int i = 0, ci = cs.length; i < ci; ++i) {
                            cs[i].log(time, eventNumber.getAndIncrement(), input,
                                    clazz, method, statement, file, line,
                                    level, logger, type, exception, tag, tags,
                                    message, param1, param2);
                        }
                    } finally {
                        ti.inside = false;
                    }
                }
            }
        }
    }

    public static void log(long time, Input input,
                           String clazz, String method, String statement, String file, int line,
                           Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags,
                           String message, Object param1, Object param2, Object param3) {
        Validator.inRangeLong("time", time, 0L, Long.MAX_VALUE);
        Validator.notNull("input", input);
        Validator.notNull("level", level);
        Validator.notNull("logger", logger);
        Validator.notNull("type", type);
        Validator.notNull("message", message);

        if ((eventInclude == null) || (eventInclude.match(time, input, clazz, method, statement, file, line, level, logger, type, exception, tag, tags, message, param1, param2, param3))) {
            if ((eventExclude == null) || (!eventExclude.match(time, input, clazz, method, statement, file, line, level, logger, type, exception, tag, tags, message, param1, param2, param3))) {
                ThreadInfo ti = threadInfo.get();
                if (!ti.inside) {
                    ti.inside = true;
                    try {
                        Connector[] cs = connectors;
                        for (int i = 0, ci = cs.length; i < ci; ++i) {
                            cs[i].log(time, eventNumber.getAndIncrement(), input,
                                    clazz, method, statement, file, line,
                                    level, logger, type, exception, tag, tags,
                                    message, param1, param2, param3);
                        }
                    } finally {
                        ti.inside = false;
                    }
                }
            }
        }
    }

    public static void log(long time, Input input,
                           String clazz, String method, String statement, String file, int line,
                           Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags,
                           String message, Object param1, Object param2, Object param3, Object param4) {
        Validator.inRangeLong("time", time, 0L, Long.MAX_VALUE);
        Validator.notNull("input", input);
        Validator.notNull("level", level);
        Validator.notNull("logger", logger);
        Validator.notNull("type", type);
        Validator.notNull("message", message);

        if ((eventInclude == null) || (eventInclude.match(time, input, clazz, method, statement, file, line, level, logger, type, exception, tag, tags, message, param1, param2, param3, param4))) {
            if ((eventExclude == null) || (!eventExclude.match(time, input, clazz, method, statement, file, line, level, logger, type, exception, tag, tags, message, param1, param2, param3, param4))) {
                ThreadInfo ti = threadInfo.get();
                if (!ti.inside) {
                    ti.inside = true;
                    try {
                        Connector[] cs = connectors;
                        for (int i = 0, ci = cs.length; i < ci; ++i) {
                            cs[i].log(time, eventNumber.getAndIncrement(), input,
                                    clazz, method, statement, file, line,
                                    level, logger, type, exception, tag, tags,
                                    message, param1, param2, param3, param4);
                        }
                    } finally {
                        ti.inside = false;
                    }
                }
            }
        }
    }

    public static void log(long time, Input input,
                           String clazz, String method, String statement, String file, int line,
                           Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags,
                           String message, Object param1, Object param2, Object param3, Object param4, Object param5) {
        Validator.inRangeLong("time", time, 0L, Long.MAX_VALUE);
        Validator.notNull("input", input);
        Validator.notNull("level", level);
        Validator.notNull("logger", logger);
        Validator.notNull("type", type);
        Validator.notNull("message", message);

        if ((eventInclude == null) || (eventInclude.match(time, input, clazz, method, statement, file, line, level, logger, type, exception, tag, tags, message, param1, param2, param3, param4, param5))) {
            if ((eventExclude == null) || (!eventExclude.match(time, input, clazz, method, statement, file, line, level, logger, type, exception, tag, tags, message, param1, param2, param3, param4, param5))) {
                ThreadInfo ti = threadInfo.get();
                if (!ti.inside) {
                    ti.inside = true;
                    try {
                        Connector[] cs = connectors;
                        for (int i = 0, ci = cs.length; i < ci; ++i) {
                            cs[i].log(time, eventNumber.getAndIncrement(), input,
                                    clazz, method, statement, file, line,
                                    level, logger, type, exception, tag, tags,
                                    message, param1, param2, param3, param4, param5);
                        }
                    } finally {
                        ti.inside = false;
                    }
                }
            }
        }
    }

    public static void collect(String metric, long count, long value, int precision, String unit) {
    }

    private static final class ThreadInfo {
        public boolean inside;

        public ThreadInfo() {
            this.inside = false;
        }
    }
}
