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

import systems.microservice.loghub.sdk.buffer.BufferReader;
import systems.microservice.loghub.sdk.serializer.Serializer;
import systems.microservice.loghub.sdk.util.Tag;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class BrokerConsumerMessage<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String topic;
    public final UUID id;
    public final String key;
    public final String type;
    public final Serializer serializer;
    public final Class<T> clazz;
    public final T content;
    public final Map<String, Tag> tags;
    public final Producer producer;
    public final long sendTime;
    public final BrokerConsumer<T> consumer;
    public final long receiveTime;

    protected BrokerConsumerMessage(BufferReader reader, String topic, Class<T> clazz, BrokerConsumer<T> consumer) {
        this.topic = topic;
        this.id = reader.readUUID();
        this.key = reader.readStringRef();
        this.type = reader.readStringRef();
        this.serializer = Serializer.valueOf(reader.readString());
        this.clazz = clazz;
        this.content = serializer.read(reader.readByteArray(), clazz);
        this.tags = reader.readObjectMap(new LinkedHashMap<>(8), String.class, Tag.class);
        this.producer = new Producer(reader);
        this.sendTime = reader.readLong();
        this.consumer = consumer;
        this.receiveTime = System.currentTimeMillis();
    }

    public static class Producer implements Serializable {
        private static final long serialVersionUID = 1L;

        public final UUID id;
        public final String name;
        public final String environment;
        public final String application;
        public final String version;
        public final String revision;
        public final String instance;
        public final Process process;
        public final String hostName;
        public final String hostIP;

        protected Producer(BufferReader reader) {
            this.id = reader.readUUID();
            this.name = reader.readStringRef();
            this.environment = reader.readStringRef();
            this.application = reader.readStringRef();
            this.version = reader.readStringRef();
            this.revision = reader.readStringRef();
            this.instance = reader.readStringRef();
            this.process = new Process(reader);
            this.hostName = reader.readStringRef();
            this.hostIP = reader.readStringRef();
        }

        public static class Process implements Serializable {
            private static final long serialVersionUID = 1L;

            public final UUID uuid;
            public final long id;
            public final long start;

            protected Process(BufferReader reader) {
                this.uuid = reader.readUUID();
                this.id = reader.readLong();
                this.start = reader.readLong();
            }
        }
    }
}
