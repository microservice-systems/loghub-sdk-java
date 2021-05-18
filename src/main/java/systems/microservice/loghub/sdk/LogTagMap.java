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
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class LogTagMap implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String threadUUID;
    private final long threadID;
    private final String threadName;
    private final int threadPriority;
    private final LinkedHashMap<String, ArrayList<LogTag>> tags;
    private long revision;

    public LogTagMap(String threadUUID, long threadID, String threadName, int threadPriority) {
        Argument.notNull("threadUUID", threadUUID);
        Argument.notNull("threadName", threadName);

        this.threadUUID = threadUUID;
        this.threadID = threadID;
        this.threadName = threadName;
        this.threadPriority = threadPriority;
        this.tags = new LinkedHashMap<>(32);
        this.revision = 1L;

        {
            LogTag t = new LogTag("thread.uuid", threadUUID);
            ArrayList<LogTag> ts = new ArrayList<>(1);
            ts.add(t);
            this.tags.put(t.key, ts);
        }
        {
            LogTag t = new LogTag("thread.id", threadID);
            ArrayList<LogTag> ts = new ArrayList<>(1);
            ts.add(t);
            this.tags.put(t.key, ts);
        }
        {
            LogTag t = new LogTag("thread.name", threadName);
            ArrayList<LogTag> ts = new ArrayList<>(1);
            ts.add(t);
            this.tags.put(t.key, ts);
        }
        {
            LogTag t = new LogTag("thread.priority", threadPriority);
            ArrayList<LogTag> ts = new ArrayList<>(1);
            ts.add(t);
            this.tags.put(t.key, ts);
        }
    }

    public String getThreadUUID() {
        return threadUUID;
    }

    public long getThreadID() {
        return threadID;
    }

    public String getThreadName() {
        return threadName;
    }

    public int getThreadPriority() {
        return threadPriority;
    }

    public int size() {
        return tags.size();
    }

    public boolean isEmpty() {
        return tags.isEmpty();
    }

    public boolean containsKey(String key) {
        Argument.notNull("key", key);

        return tags.containsKey(key);
    }

    public LogTag get(String key) {
        Argument.notNull("key", key);

        LinkedList<LogTag> ts = tags.get(key);
        if (ts != null) {
            return ts.peekLast();
        } else {
            return null;
        }
    }

    public LogTag put(LogTag tag) {
        Argument.notNull("tag", tag);

        String k = tag.key;
        LinkedList<LogTag> ts = tags.get(k);
        if (ts == null) {
            ts = new LinkedList<>();
            tags.put(k, ts);
        }
        ts.addLast(tag);
        revision++;
        return tag;
    }

    public LogTag put(String key, Object value) {
        return put(new LogTag(key, value));
    }

    public LogTag putIfAbsent(String key, Object value) {
        LogTag t = get(key);
        if (t == null) {
            return put(key, value);
        } else {
            return t;
        }
    }

    public LogTag remove(String key) {
        Argument.notNull("key", key);

        LinkedList<LogTag> ts = tags.get(key);
        if (ts != null) {
            LogTag t = ts.pollLast();
            if (ts.isEmpty()) {
                tags.remove(key);
            }
            revision++;
            return t;
        } else {
            return null;
        }
    }

    public void clear() {
        tags.clear();
        revision++;
    }

    public Map<String, LogTag> toMap() {
        LinkedHashMap<String, LogTag> m = new LinkedHashMap<>(tags.size());
        for (LinkedList<LogTag> ts : tags.values()) {
            LogTag t = ts.peekLast();
            m.put(t.key, t);
        }
        return m;
    }

    public long getRevision() {
        return revision;
    }
}
