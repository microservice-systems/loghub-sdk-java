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

package systems.microservice.loghub.sdk.storage;

import systems.microservice.loghub.sdk.util.Argument;

import java.io.Serializable;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public abstract class Storage implements Serializable {
    private static final long serialVersionUID = 1L;

    protected final String type;
    protected final String bucket;
    protected final String prefix;
    protected final String target;
    protected final String endpoint;
    protected final String accessKey;

    protected Storage(String type, String bucket, String prefix) {
        this(type, bucket, prefix, null, null);
    }

    protected Storage(String type, String bucket, String prefix, String endpoint, String accessKey) {
        Argument.notNull("type", type);
        Argument.notNull("bucket", bucket);
        Argument.notNull("prefix", prefix);

        this.type = type;
        this.bucket = bucket;
        this.prefix = prefix;
        this.target = String.format("%s://%s/%s", type, bucket, prefix);
        this.endpoint = endpoint;
        this.accessKey = accessKey;
    }

    public String getType() {
        return type;
    }

    public String getBucket() {
        return bucket;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getTarget() {
        return target;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public abstract Storage substorage(String prefix);

    public Iterable<String> list(String prefix) {
        return list(prefix, (StorageFilter) null);
    }

    public Iterable<String> list(String prefix, Pattern filter) {
        if (filter != null) {
            return list(prefix, (storage, key) -> filter.matcher(key).matches());
        } else {
            return list(prefix, (StorageFilter) null);
        }
    }

    public abstract Iterable<String> list(String prefix, StorageFilter filter);

    public boolean contains(String key) {
        Argument.notNull("key", key);

        for (String k : list(key)) {
            if (key.equals(k)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAll(String... keys) {
        Argument.notNull("keys", keys);

        for (String key : keys) {
            if (!contains(key)) {
                return false;
            }
        }
        return true;
    }

    public boolean containsAll(Iterable<String> keys) {
        Argument.notNull("keys", keys);

        for (String key : keys) {
            if (!contains(key)) {
                return false;
            }
        }
        return true;
    }
}
