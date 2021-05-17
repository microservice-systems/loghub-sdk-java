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

import systems.microservice.loghub.sdk.buffer.BufferWriter;
import systems.microservice.loghub.sdk.buffer.Bufferable;
import systems.microservice.loghub.sdk.util.MapUtil;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
final class LogEventStringMap implements Bufferable {
    private static final int MIN_ID = Short.MIN_VALUE;
    private static final int MAX_ID = Short.MAX_VALUE;

    public static final short NOT_EXIST_ID = Short.MAX_VALUE;

    private final AtomicInteger idGenerator = new AtomicInteger(MIN_ID);
    private final ConcurrentHashMap<String, StringID> stringIDs = new ConcurrentHashMap<>(4096, 0.75f, 64);

    public LogEventStringMap() {
    }

    public short getStringID(String string) {
        StringID sid = stringIDs.get(string);
        if (sid == null) {
            if (idGenerator.get() < MAX_ID) {
                int id = idGenerator.getAndIncrement();
                if (id < MAX_ID) {
                    sid = MapUtil.putIfAbsent(stringIDs, string, new StringID(id));
                } else {
                    return NOT_EXIST_ID;
                }
            } else {
                return NOT_EXIST_ID;
            }
        }
        sid.touch();
        return sid.getID();
    }

    @SuppressWarnings("unchecked")
    public void reset() {
        int id = MIN_ID;
        Map.Entry<String, StringID>[] ens = stringIDs.entrySet().toArray(new Map.Entry[0]);
        for (Map.Entry<String, StringID> en : ens) {
            StringID v = en.getValue();
            if (v.isUsed()) {
                v.reset(id);
                id++;
            } else {
                stringIDs.remove(en.getKey());
            }
        }
        idGenerator.set(id);
    }

    @Override
    public int write(byte[] buffer, int index, Map<String, Object> context) {
        index = BufferWriter.writeVersion(buffer, index, (byte) 1);
        for (Map.Entry<String, StringID> en : stringIDs.entrySet()) {
            StringID v = en.getValue();
            if (v.isUsed()) {
                index = BufferWriter.writeBoolean(buffer, index, true);
                index = BufferWriter.writeString(buffer, index, en.getKey());
                index = BufferWriter.writeShort(buffer, index, v.getID());
            }
        }
        index = BufferWriter.writeBoolean(buffer, index, false);
        return index;
    }

    private static final class StringID {
        private final AtomicInteger id;
        private final AtomicBoolean used;

        public StringID(int id) {
            this.id = new AtomicInteger(id);
            this.used = new AtomicBoolean(false);
        }

        public short getID() {
            return (short) id.get();
        }

        public boolean isUsed() {
            return used.get();
        }

        public void reset(int id) {
            this.id.set(id);
            this.used.set(false);
        }

        public void touch() {
            this.used.compareAndSet(false, true);
        }
    }
}
