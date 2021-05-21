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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class LogThreadInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    public final UUID uuid;
    public final long id;
    public final String name;
    public final int priority;
    public final LinkedHashMap<String, ArrayList<LogTag>> tags;
    public int depth;

    public LogThreadInfo() {
        Thread t = Thread.currentThread();

        this.uuid = UUID.randomUUID();
        this.id = t.getId();
        this.name = t.getName();
        this.priority = t.getPriority();
        this.tags = new LinkedHashMap<>(32);
        this.depth = 0;
    }

    public LogTag getTag(String key) {
        Argument.notNull("key", key);

        ArrayList<LogTag> ts = tags.get(key);
        if (ts != null) {
            return ts.get(ts.size() - 1);
        } else {
            return null;
        }
    }

    public LogTag addTag(LogTag tag) {
        Argument.notNull("tag", tag);

        ArrayList<LogTag> ts = tags.get(tag.key);
        if (ts == null) {
            ts = new ArrayList<>(4);
            tags.put(tag.key, ts);
        }
        ts.add(tag);
        return tag;
    }

    public LogTag addTag(String key, Object value) {
        return addTag(new LogTag(key, value));
    }

    public LogTag addTag(String key, Object value, String unit) {
        return addTag(new LogTag(key, value, unit));
    }

    public LogTag removeTag(String key) {
        Argument.notNull("key", key);

        ArrayList<LogTag> ts = tags.get(key);
        if (ts != null) {
            LogTag t = ts.remove(ts.size() - 1);
            if (ts.isEmpty()) {
                tags.remove(key);
            }
            return t;
        } else {
            return null;
        }
    }
}
