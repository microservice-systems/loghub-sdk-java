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

import systems.microservice.loghub.sdk.property.CachedProperty;
import systems.microservice.loghub.sdk.storage.Storage;
import systems.microservice.loghub.sdk.util.Argument;

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
    protected final CachedProperty<ConsumerList> consumers;

    public BrokerProducer(Storage storage, String topic) {
        Argument.notNull("storage", storage);
        Argument.notNull("topic", topic);

        this.storage = storage;
        this.topic = topic;
        this.id = UUID.randomUUID();
        this.consumers = new CachedProperty<>(60000L, () -> new ConsumerList(storage, topic));
    }

    protected static class ConsumerList implements Serializable {
        private static final long serialVersionUID = 1L;

        public final Consumer[] array;
        public final Map<UUID, Consumer> map;

        public ConsumerList(Storage storage, String topic) {
            Argument.notNull("storage", storage);
            Argument.notNull("topic", topic);

            LinkedHashMap<UUID, Consumer> m = new LinkedHashMap<>(64);
            for (String k : storage.list(String.format("/%s/consumer/list/", topic))) {
            }
            this.array = m.values().toArray(new Consumer[0]);
            this.map = Collections.unmodifiableMap(m);
        }
    }

    public static class Consumer implements Serializable {
        private static final long serialVersionUID = 1L;
    }
}
