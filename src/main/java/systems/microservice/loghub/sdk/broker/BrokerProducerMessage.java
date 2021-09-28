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

import systems.microservice.loghub.sdk.serializer.Serializer;
import systems.microservice.loghub.sdk.util.Tag;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class BrokerProducerMessage<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public final UUID id;
    public String key;
    public String type;
    public Serializer serializer;
    public T content;
    public Map<String, Tag> tags;

    public BrokerProducerMessage(String key, String type, Serializer serializer, T content, Map<String, Tag> tags) {
        this.id = UUID.randomUUID();
        this.key = key;
        this.type = type;
        this.serializer = serializer;
        this.content = content;
        this.tags = tags;
    }
}
