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

package systems.microservice.loghub.sdk.broker;

import systems.microservice.loghub.facade.Validator;
import systems.microservice.loghub.sdk.buffer.BufferException;
import systems.microservice.loghub.sdk.buffer.BufferReader;
import systems.microservice.loghub.sdk.property.CachedProperty;
import systems.microservice.loghub.sdk.storage.Storage;
import systems.microservice.loghub.sdk.storage.StorageObject;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class BrokerProducer<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    protected final Storage storage;
    protected final String topic;
    protected final UUID id;
    protected final long consumerTimeout;
    protected final CachedProperty<ConsumerList> consumers;

    public BrokerProducer(Storage storage, String topic) {
        this(storage, topic, 60000L);
    }

    public BrokerProducer(Storage storage, String topic, long consumerTimeout) {
        Validator.notNull("storage", storage);
        Validator.notNull("topic", topic);
        Validator.inRangeLong("consumerTimeout", consumerTimeout, 5000L, Long.MAX_VALUE);

        this.storage = storage;
        this.topic = topic;
        this.id = UUID.randomUUID();
        this.consumerTimeout = consumerTimeout;
        this.consumers = new CachedProperty<>(consumerTimeout, () -> new ConsumerList(storage, topic, consumerTimeout));
    }

    public Storage getStorage() {
        return storage;
    }

    public String getTopic() {
        return topic;
    }

    public UUID getID() {
        return id;
    }

    public long getConsumerTimeout() {
        return consumerTimeout;
    }

    public Map<UUID, Consumer> getConsumers() {
        return consumers.get().map;
    }

    protected static class ConsumerList implements Serializable {
        private static final long serialVersionUID = 1L;

        public final Consumer[] array;
        public final Map<UUID, Consumer> map;

        protected ConsumerList(Storage storage, String topic, long consumerTimeout) {
            Validator.notNull("storage", storage);
            Validator.notNull("topic", topic);
            Validator.inRangeLong("consumerTimeout", consumerTimeout, 5000L, Long.MAX_VALUE);

            LinkedHashMap<UUID, Consumer> m = new LinkedHashMap<>(64);
            String p = String.format("/%s/consumer/list/", topic);
            long t = System.currentTimeMillis();
            for (StorageObject so : storage.list(p)) {
                UUID id = UUID.fromString(so.getKey());
                Consumer c = new Consumer(new BufferReader(storage.getArray(p + id.toString())), id);
                if (t < c.updateTime + consumerTimeout) {
                    m.put(id, c);
                }
            }

            this.array = m.values().toArray(new Consumer[0]);
            this.map = Collections.unmodifiableMap(m);
        }
    }

    public static class Consumer implements Serializable {
        private static final long serialVersionUID = 1L;

        public final UUID id;
        public final String idString;
        public final long createTime;
        public final long updateTime;

        protected Consumer(BufferReader reader, UUID id) {
            Validator.notNull("reader", reader);
            Validator.notNull("id", id);

            byte v = reader.readVersion();
            if (v == 1) {
                this.id = id;
                this.idString = id.toString();
                this.createTime = reader.readLong();
                this.updateTime = reader.readLong();
            } else {
                throw new BufferException(String.format("Illegal version '%d' of '%s' class", v, Consumer.class.getCanonicalName()));
            }
        }
    }
}
