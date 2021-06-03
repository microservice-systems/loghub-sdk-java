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

import systems.microservice.loghub.sdk.LogHub;
import systems.microservice.loghub.sdk.buffer.Bufferable;
import systems.microservice.loghub.sdk.util.PropertiesUtil;
import systems.microservice.loghub.sdk.util.ResourceUtil;

import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class Config implements Bufferable, Serializable {
    private static final long serialVersionUID = 1L;

    public final UUID uuid;
    public final Map<String, Property> properties;
    public final String comment;
    public final URL url;
    public final String user;
    public final String commit;
    public final long time;

    public Config(Iterable<Property> properties, String comment, URL url, String user, String commit) {
        this.uuid = UUID.randomUUID();
        this.properties = createProperties(properties);
        this.comment = comment;
        this.url = url;
        this.user = user;
        this.commit = commit;
        this.time = System.currentTimeMillis();
    }

    @Override
    public int write(byte[] buffer, int index, Map<String, Object> context) {
        return 0;
    }

    private static Map<String, Property> createProperties(Iterable<Property> properties) {
        TreeMap<String, Property> ps = new TreeMap<>();
        for (Property p : properties) {
            ps.put(p.key, p);
        }
        return Collections.unmodifiableMap(ps);
    }
}
