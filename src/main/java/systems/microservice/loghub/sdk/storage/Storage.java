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

import systems.microservice.loghub.sdk.serializer.Serializer;
import systems.microservice.loghub.sdk.stream.Stream;
import systems.microservice.loghub.sdk.util.Argument;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public abstract class Storage implements Serializable {
    private static final long serialVersionUID = 1L;

    protected final String target;
    protected final String type;
    protected final String bucket;
    protected final String prefix;

    protected Storage(String type, String bucket, String prefix) {
        Argument.notNull("type", type);
        Argument.notNull("bucket", bucket);
        Argument.notNull("prefix", prefix);

        this.target = String.format("%s://%s/%s", type, bucket, prefix);
        this.type = type;
        this.bucket = bucket;
        this.prefix = prefix;
    }

    public String getTarget() {
        return target;
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

    public abstract Storage substorage(String prefix);

    public Iterable<String> find(String prefix) {
        Argument.notNull("prefix", prefix);

        return find(prefix, (StorageFilter) null);
    }

    public Iterable<String> find(String prefix, Pattern filter) {
        Argument.notNull("prefix", prefix);

        if (filter != null) {
            return find(prefix, (storage, key) -> filter.matcher(key).matches());
        } else {
            return find(prefix, (StorageFilter) null);
        }
    }

    public abstract Iterable<String> find(String prefix, StorageFilter filter);

    public Iterable<String> list(String prefix) {
        Argument.notNull("prefix", prefix);

        return list(prefix, (StorageFilter) null);
    }

    public Iterable<String> list(String prefix, Pattern filter) {
        Argument.notNull("prefix", prefix);

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

    public abstract String getOwner(String key);
    public abstract String getVersion(String key);

    public byte[] getArray(String key) {
        Argument.notNull("key", key);

        return getArray(key, null);
    }

    public byte[] getArray(String key, Map<String, String> meta) {
        Argument.notNull("key", key);

        return getArray(key, meta, null);
    }

    public byte[] getArray(String key, Map<String, String> meta, Map<String, String> tags) {
        Argument.notNull("key", key);

        try (InputStream in = getInputStream(key, meta, tags)) {
            return Stream.read(in);
        } catch (IOException e) {
            throw new StorageException(target, StorageOperation.GET, e);
        }
    }

    public InputStream getInputStream(String key) {
        Argument.notNull("key", key);

        return getInputStream(key, null);
    }

    public InputStream getInputStream(String key, Map<String, String> meta) {
        Argument.notNull("key", key);

        return getInputStream(key, meta, null);
    }

    public abstract InputStream getInputStream(String key, Map<String, String> meta, Map<String, String> tags);

    public String getString(String key) {
        Argument.notNull("key", key);

        return getString(key, null);
    }

    public String getString(String key, Map<String, String> meta) {
        Argument.notNull("key", key);

        return getString(key, meta, null);
    }

    public String getString(String key, Map<String, String> meta, Map<String, String> tags) {
        Argument.notNull("key", key);

        try (Reader r = getReader(key, meta, tags)) {
            return Stream.readS(r);
        } catch (IOException e) {
            throw new StorageException(target, StorageOperation.GET, e);
        }
    }

    public Reader getReader(String key) {
        Argument.notNull("key", key);

        return getReader(key, null);
    }

    public Reader getReader(String key, Map<String, String> meta) {
        Argument.notNull("key", key);

        return getReader(key, meta, null);
    }

    public abstract Reader getReader(String key, Map<String, String> meta, Map<String, String> tags);

    public <T> T get(String key, Serializer serializer, Class<T> clazz) {
        Argument.notNull("key", key);
        Argument.notNull("serializer", serializer);
        Argument.notNull("clazz", clazz);

        return get(key, serializer, clazz, null);
    }

    public <T> T get(String key, Serializer serializer, Class<T> clazz, Map<String, String> meta) {
        Argument.notNull("key", key);
        Argument.notNull("serializer", serializer);
        Argument.notNull("clazz", clazz);

        return get(key, serializer, clazz, meta, null);
    }

    public <T> T get(String key, Serializer serializer, Class<T> clazz, Map<String, String> meta, Map<String, String> tags) {
        Argument.notNull("key", key);
        Argument.notNull("serializer", serializer);
        Argument.notNull("clazz", clazz);

        try (InputStream in = getInputStream(key, meta, tags)) {
            return serializer.read(in, clazz);
        } catch (IOException e) {
            throw new StorageException(target, StorageOperation.GET, e);
        }
    }

    public OutputStream download(String key, OutputStream output) {
        Argument.notNull("key", key);
        Argument.notNull("output", output);

        return download(key, output, null);
    }

    public OutputStream download(String key, OutputStream output, Map<String, String> meta) {
        Argument.notNull("key", key);
        Argument.notNull("output", output);

        return download(key, output, meta, null);
    }

    public OutputStream download(String key, OutputStream output, Map<String, String> meta, Map<String, String> tags) {
        Argument.notNull("key", key);
        Argument.notNull("output", output);

        try (InputStream in = getInputStream(key, meta, tags)) {
            return Stream.copy(in, output);
        } catch (IOException e) {
            throw new StorageException(target, StorageOperation.GET, e);
        }
    }

    public Writer download(String key, Writer writer) {
        Argument.notNull("key", key);
        Argument.notNull("writer", writer);

        return download(key, writer, null);
    }

    public Writer download(String key, Writer writer, Map<String, String> meta) {
        Argument.notNull("key", key);
        Argument.notNull("writer", writer);

        return download(key, writer, meta, null);
    }

    public Writer download(String key, Writer writer, Map<String, String> meta, Map<String, String> tags) {
        Argument.notNull("key", key);
        Argument.notNull("writer", writer);

        try (Reader r = getReader(key, meta, tags)) {
            return Stream.copy(r, writer);
        } catch (IOException e) {
            throw new StorageException(target, StorageOperation.GET, e);
        }
    }

    public Map<String, String> getMeta(String key) {
        Argument.notNull("key", key);

        return getMeta(key, new LinkedHashMap<>(32));
    }

    public Map<String, String> getMeta(String key, Map<String, String> meta) {
        Argument.notNull("key", key);
        Argument.notNull("meta", meta);

        return getMeta(key, meta, null);
    }

    public abstract Map<String, String> getMeta(String key, Map<String, String> meta, Map<String, String> tags);

    public Map<String, String> getTags(String key) {
        Argument.notNull("key", key);

        return getTags(key, new LinkedHashMap<>(32));
    }

    public abstract Map<String, String> getTags(String key, Map<String, String> tags);
}
