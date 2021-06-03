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

package systems.microservice.loghub.sdk.config;

import systems.microservice.loghub.sdk.buffer.BufferWriter;
import systems.microservice.loghub.sdk.buffer.Bufferable;
import systems.microservice.loghub.sdk.util.Argument;
import systems.microservice.loghub.sdk.util.Range;
import systems.microservice.loghub.sdk.util.ResourceUtil;

import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class Property implements Bufferable, Serializable {
    private static final long serialVersionUID = 1L;

    public final UUID uuid;
    public final String group;
    public final String key;
    public final PropertyType type;
    public final Value value;
    public final Value defaultValue;
    public final List<Value> possibleValues;
    public final Range<Value> rangeValues;
    public final String unit;
    public final String description;
    public final URL url;
    public final String user;
    public final String commit;
    public final long time;

    public Property(String group,
                    String key,
                    PropertyType type,
                    Value value,
                    Value defaultValue,
                    List<Value> possibleValues,
                    Range<Value> rangeValues,
                    String unit,
                    String description,
                    URL url,
                    String user,
                    String commit) {
        Argument.notNull("group", group);
        Argument.notNull("key", key);
        Argument.notNull("type", type);
        Argument.notNull("value", value);
        Argument.notNull("defaultValue", defaultValue);
        Argument.notNull("unit", unit);
        Argument.notNull("description", description);
        Argument.notNull("url", url);

        this.uuid = UUID.randomUUID();
        this.group = group;
        this.key = key;
        this.type = type;
        this.value = value;
        this.defaultValue = defaultValue;
        this.possibleValues = possibleValues;
        this.rangeValues = rangeValues;
        this.unit = unit;
        this.description = description;
        this.url = url;
        this.user = user;
        this.commit = commit;
        this.time = System.currentTimeMillis();
    }

    @Override
    public int write(byte[] buffer, int index, Map<String, Object> context) {
        index = BufferWriter.writeVersion(buffer, index, (byte) 1);
        index = BufferWriter.writeUUID(buffer, index, uuid);
        index = BufferWriter.writeString(buffer, index, group);
        index = BufferWriter.writeString(buffer, index, key);
//        index = BufferWriter.writeUUID(buffer, index, type);
        index = BufferWriter.writeBufferable(buffer, index, context, value);
        index = BufferWriter.writeBufferable(buffer, index, context, defaultValue);
        index = BufferWriter.writeObjectCollectionRef(buffer, index, context, possibleValues);
        index = BufferWriter.writeBufferableRef(buffer, index, context, rangeValues);
        index = BufferWriter.writeString(buffer, index, unit);
        index = BufferWriter.writeString(buffer, index, description);
        index = BufferWriter.writeURL(buffer, index, url);
        index = BufferWriter.writeStringRef(buffer, index, user);
        index = BufferWriter.writeStringRef(buffer, index, commit);
        index = BufferWriter.writeLong(buffer, index, time);
        return index;
    }
}
