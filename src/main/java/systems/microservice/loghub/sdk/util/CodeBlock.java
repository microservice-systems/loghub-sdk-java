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

import systems.microservice.loghub.sdk.util.Tag;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class CodeBlock implements AutoCloseable, Serializable {
    private static final long serialVersionUID = 1L;
    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, Info>> infos = new ConcurrentHashMap<>(1024);

    public final String logger;
    public final String name;
    public final Tag[] tags;
    public final long begin;

    public CodeBlock(String logger, String name, Tag... tags) {
        this.logger = logger;
        this.name = name;
        this.tags = tags;
        this.begin = System.currentTimeMillis();
    }

    @Override
    public void close() throws Exception {
    }

    private static Info createInfo(String module, String name) {
        return new Info(String.format("[BEGIN]: %s", name), String.format("[END]: %s", name), String.format("loghub.block.%s.%s.span", module, name));
    }

    private static Info getInfo(String module, String name) {
        ConcurrentHashMap<String, Info> infs = infos.get(module);
        if (infs == null) {
            infs = new ConcurrentHashMap<>(32);
            infs.put(name, createInfo(module, name));
            infs = MapUtil.putIfAbsent(infos, module, infs);
        }
        Info inf = infs.get(name);
        if (inf == null) {
            inf = MapUtil.putIfAbsent(infs, name, createInfo(module, name));
        }
        return inf;
    }

    private static final class Info implements Serializable {
        private static final long serialVersionUID = 1L;

        public final String beginMessage;
        public final String endMessage;
        public final String metric;

        public Info(String beginMessage, String endMessage, String metric) {
            this.beginMessage = beginMessage;
            this.endMessage = endMessage;
            this.metric = metric;
        }
    }
}
