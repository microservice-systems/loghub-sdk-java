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

package systems.microservice.loghub.sdk.util;

import systems.microservice.loghub.sdk.LogHub;
import systems.microservice.loghub.sdk.LogLevel;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class Code implements AutoCloseable, Serializable {
    private static final long serialVersionUID = 1L;
    private static final int LOGGER_MAX_COUNT = 8192;
    private static final int NAME_MAX_COUNT = 4096;
    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, Info>> infos = new ConcurrentHashMap<>(LOGGER_MAX_COUNT, 0.75f, 64);

    public final String logger;
    public final String name;
    public final Tag[] tags;
    public final Info info;
    public final long begin;
    public long end;

    public Code(Log logger, String name, Tag... tags) {
        this(Argument.notNull("logger", logger).getLogger(), name, tags);
    }

    public Code(Class logger, String name, Tag... tags) {
        this(Argument.notNull("logger", logger).getCanonicalName(), name, tags);
    }

    public Code(String logger, String name, Tag... tags) {
        Argument.notNull("logger", logger);
        Argument.notNull("name", name);

        this.logger = logger;
        this.name = name;
        this.tags = tags;
        this.info = getInfo(logger, name);
        this.begin = System.currentTimeMillis();
        this.end = -1L;

        ThreadInfo ti = ThreadInfo.getInstance();
        for (Tag t : tags) {
            ti.addTag(t);
        }
        LogHub.logEventBegin(this.begin, logger, LogLevel.TRACE.id, LogLevel.TRACE.name(), this.info.beginMessage);
        ti.depth++;
    }

    @Override
    public void close() {
        end = System.currentTimeMillis();
        LogHub.logMetric(info.metric, end - begin, 0, "ms");
        ThreadInfo ti = ThreadInfo.getInstance();
        ti.depth--;
        LogHub.logEventEnd(end, logger, LogLevel.TRACE.id, LogLevel.TRACE.name(), info.endMessage);
        for (Tag t : tags) {
            ti.removeTag(t.key);
        }
    }

    private static Info getInfo(String logger, String name) {
        ConcurrentHashMap<String, Info> infs = infos.get(logger);
        if (infs == null) {
            if (infos.size() >= LOGGER_MAX_COUNT) {
                infos.clear();
            }
            infs = MapUtil.putIfAbsent(infos, logger, new ConcurrentHashMap<>(NAME_MAX_COUNT, 0.75f, 4));
        }
        Info inf = infs.get(name);
        if (inf == null) {
            if (infs.size() >= NAME_MAX_COUNT) {
                infs.clear();
            }
            inf = MapUtil.putIfAbsent(infs, name, new Info(logger, name));
        }
        return inf;
    }

    public static final class Info implements Serializable {
        private static final long serialVersionUID = 1L;

        public final String beginMessage;
        public final String endMessage;
        public final String metric;

        public Info(String logger, String name) {
            this.beginMessage = String.format("[BEGIN]: %s", name);
            this.endMessage = String.format("[END]: %s", name);
            this.metric = String.format("code.%s.%s.execution.time", logger, name);
        }
    }
}
