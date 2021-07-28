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

import systems.microservice.loghub.sdk.buffer.BufferObjectType;
import systems.microservice.loghub.sdk.buffer.BufferWriter;
import systems.microservice.loghub.sdk.util.Argument;
import systems.microservice.loghub.sdk.util.Range;

import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class ConfigProperty implements Serializable {
    private static final long serialVersionUID = 1L;

    public final UUID uuid;
    public final String group;
    public final String key;
    public final ConfigPropertyType type;
    public final ConfigValue value;
    public final ConfigValue defaultValue;
    public final List<ConfigValue> possibleValues;
    public final Range<ConfigValue> rangeValues;
    public final String unit;
    public final String description;
    public final URL url;
    public final String user;
    public final String commit;
    public final long time;
    public final AtomicReference<ConfigProperty> oldProperty;
    public final AtomicReference<ConfigProperty> invalidProperty;

    public ConfigProperty(String group,
                          String key,
                          ConfigPropertyType type,
                          ConfigValue value,
                          ConfigValue defaultValue,
                          List<ConfigValue> possibleValues,
                          Range<ConfigValue> rangeValues,
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
        this.oldProperty = new AtomicReference<>(null);
        this.invalidProperty = new AtomicReference<>(null);
    }

    public int write(byte[] buffer, int index) {
        index = BufferWriter.writeVersion(buffer, index, (byte) 1);
        index = BufferWriter.writeUUID(buffer, index, uuid);
        index = BufferWriter.writeString(buffer, index, group);
        index = BufferWriter.writeString(buffer, index, key);
//        index = BufferWriter.writeUUID(buffer, index, type);
        index = value.write(buffer, index);
        index = defaultValue.write(buffer, index);
        index = BufferWriter.writeObjectCollectionRef(buffer, index, possibleValues);
        index = BufferWriter.writeRange(buffer, index, rangeValues);
        index = BufferWriter.writeString(buffer, index, unit);
        index = BufferWriter.writeString(buffer, index, description);
        index = BufferWriter.writeURL(buffer, index, url);
        index = BufferWriter.writeStringRef(buffer, index, user);
        index = BufferWriter.writeStringRef(buffer, index, commit);
        index = BufferWriter.writeLong(buffer, index, time);
        ConfigProperty op = oldProperty.get();
        if (op != null) {
            index = BufferWriter.writeByte(buffer, index, (byte) 1);
            index = op.write(buffer, index);
        } else {
            index = BufferWriter.writeByte(buffer, index, (byte) 0);
        }
        ConfigProperty ip = invalidProperty.get();
        if (ip != null) {
            index = BufferWriter.writeByte(buffer, index, (byte) 1);
            index = ip.write(buffer, index);
        } else {
            index = BufferWriter.writeByte(buffer, index, (byte) 0);
        }
        return index;
    }
}
