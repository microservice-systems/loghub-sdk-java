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
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public abstract class Storage {
    protected final String target;
    protected final String type;
    protected final String bucket;
    protected final String prefix;

    protected Storage(StorageConfig config) {
        Argument.notNull("config", config);

        this.target = config.target;
        this.type = config.type;
        this.bucket = config.bucket;
        this.prefix = config.prefix;
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

        try (InputStream in = openInputStream(key, meta, tags)) {
            return Stream.read(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream openInputStream(String key) {
        Argument.notNull("key", key);

        return openInputStream(key, null);
    }

    public InputStream openInputStream(String key, Map<String, String> meta) {
        Argument.notNull("key", key);

        return openInputStream(key, meta, null);
    }

    public abstract InputStream openInputStream(String key, Map<String, String> meta, Map<String, String> tags);

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

        try (Reader r = openReader(key, meta, tags)) {
            return Stream.readS(r);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Reader openReader(String key) {
        Argument.notNull("key", key);

        return openReader(key, null);
    }

    public Reader openReader(String key, Map<String, String> meta) {
        Argument.notNull("key", key);

        return openReader(key, meta, null);
    }

    public abstract Reader openReader(String key, Map<String, String> meta, Map<String, String> tags);

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

        try (InputStream in = openInputStream(key, meta, tags)) {
            return serializer.read(in, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
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

        try (InputStream in = openInputStream(key, meta, tags)) {
            return Stream.copy(in, output);
        } catch (IOException e) {
            throw new RuntimeException(e);
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

        try (Reader r = openReader(key, meta, tags)) {
            return Stream.copy(r, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    public Storage putArray(String key, byte[] array, String contentType) {
        Argument.notNull("key", key);
        Argument.notNull("array", array);
        Argument.notNull("contentType", contentType);

        return putArray(key, array, contentType, null);
    }

    public Storage putArray(String key, byte[] array, String contentType, Map<String, String> meta) {
        Argument.notNull("key", key);
        Argument.notNull("array", array);
        Argument.notNull("contentType", contentType);

        return putArray(key, array, contentType, meta, null);
    }

    public Storage putArray(String key, byte[] array, String contentType, Map<String, String> meta, Map<String, String> tags) {
        Argument.notNull("key", key);
        Argument.notNull("array", array);
        Argument.notNull("contentType", contentType);

        try (OutputStream out = openOutputStream(key, array.length, contentType, meta, tags)) {
            Stream.write(array, out);
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public OutputStream openOutputStream(String key, long contentLength, String contentType) {
        Argument.notNull("key", key);
        Argument.inRangeLong("contentLength", contentLength, 0L, Long.MAX_VALUE);
        Argument.notNull("contentType", contentType);

        return openOutputStream(key, contentLength, contentType, null);
    }

    public OutputStream openOutputStream(String key, long contentLength, String contentType, Map<String, String> meta) {
        Argument.notNull("key", key);
        Argument.inRangeLong("contentLength", contentLength, 0L, Long.MAX_VALUE);
        Argument.notNull("contentType", contentType);

        return openOutputStream(key, contentLength, contentType, meta, null);
    }

    public abstract OutputStream openOutputStream(String key, long contentLength, String contentType, Map<String, String> meta, Map<String, String> tags);

    public Storage putString(String key, String string, String contentType) {
        Argument.notNull("key", key);
        Argument.notNull("string", string);
        Argument.notNull("contentType", contentType);

        return putString(key, string, contentType, null);
    }

    public Storage putString(String key, String string, String contentType, Map<String, String> meta) {
        Argument.notNull("key", key);
        Argument.notNull("string", string);
        Argument.notNull("contentType", contentType);

        return putString(key, string, contentType, meta, null);
    }

    public Storage putString(String key, String string, String contentType, Map<String, String> meta, Map<String, String> tags) {
        Argument.notNull("key", key);
        Argument.notNull("string", string);
        Argument.notNull("contentType", contentType);

        char[] a = string.toCharArray();
        try (Writer w = openWriter(key, a.length * 2, contentType, meta, tags)) {
            Stream.write(a, w);
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Writer openWriter(String key, long contentLength, String contentType) {
        Argument.notNull("key", key);
        Argument.inRangeLong("contentLength", contentLength, 0L, Long.MAX_VALUE);
        Argument.notNull("contentType", contentType);

        return openWriter(key, contentLength, contentType, null);
    }

    public Writer openWriter(String key, long contentLength, String contentType, Map<String, String> meta) {
        Argument.notNull("key", key);
        Argument.inRangeLong("contentLength", contentLength, 0L, Long.MAX_VALUE);
        Argument.notNull("contentType", contentType);

        return openWriter(key, contentLength, contentType, meta, null);
    }

    public abstract Writer openWriter(String key, long contentLength, String contentType, Map<String, String> meta, Map<String, String> tags);

    public <T> Storage put(String key, Serializer serializer, T object) {
        Argument.notNull("key", key);
        Argument.notNull("serializer", serializer);
        Argument.notNull("object", object);

        return put(key, serializer, object, null);
    }

    public <T> Storage put(String key, Serializer serializer, T object, Map<String, String> meta) {
        Argument.notNull("key", key);
        Argument.notNull("serializer", serializer);
        Argument.notNull("object", object);

        return put(key, serializer, object, meta, null);
    }

    public <T> Storage put(String key, Serializer serializer, T object, Map<String, String> meta, Map<String, String> tags) {
        Argument.notNull("key", key);
        Argument.notNull("serializer", serializer);
        Argument.notNull("object", object);

        return put(key, serializer, object, meta, tags, null);
    }

    public <T> Storage put(String key, Serializer serializer, T object, Map<String, String> meta, Map<String, String> tags, String contentType) {
        Argument.notNull("key", key);
        Argument.notNull("serializer", serializer);
        Argument.notNull("object", object);

        byte[] a = serializer.write(object);
        try (OutputStream out = openOutputStream(key, a.length, (contentType == null) ? serializer.contentType : contentType, meta, tags)) {
            Stream.write(a, out);
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Storage upload(String key, InputStream input, long contentLength, String contentType) {
        Argument.notNull("key", key);
        Argument.notNull("input", input);
        Argument.inRangeLong("contentLength", contentLength, 0L, Long.MAX_VALUE);
        Argument.notNull("contentType", contentType);

        return upload(key, input, contentLength, contentType, null);
    }

    public Storage upload(String key, InputStream input, long contentLength, String contentType, Map<String, String> meta) {
        Argument.notNull("key", key);
        Argument.notNull("input", input);
        Argument.inRangeLong("contentLength", contentLength, 0L, Long.MAX_VALUE);
        Argument.notNull("contentType", contentType);

        return upload(key, input, contentLength, contentType, meta, null);
    }

    public Storage upload(String key, InputStream input, long contentLength, String contentType, Map<String, String> meta, Map<String, String> tags) {
        Argument.notNull("key", key);
        Argument.notNull("input", input);
        Argument.inRangeLong("contentLength", contentLength, 0L, Long.MAX_VALUE);
        Argument.notNull("contentType", contentType);

        try (OutputStream out = openOutputStream(key, contentLength, contentType, meta, tags)) {
            Stream.copy(input, out);
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Storage upload(String key, Reader reader, long contentLength, String contentType) {
        Argument.notNull("key", key);
        Argument.notNull("reader", reader);
        Argument.inRangeLong("contentLength", contentLength, 0L, Long.MAX_VALUE);
        Argument.notNull("contentType", contentType);

        return upload(key, reader, contentLength, contentType, null);
    }

    public Storage upload(String key, Reader reader, long contentLength, String contentType, Map<String, String> meta) {
        Argument.notNull("key", key);
        Argument.notNull("reader", reader);
        Argument.inRangeLong("contentLength", contentLength, 0L, Long.MAX_VALUE);
        Argument.notNull("contentType", contentType);

        return upload(key, reader, contentLength, contentType, meta, null);
    }

    public Storage upload(String key, Reader reader, long contentLength, String contentType, Map<String, String> meta, Map<String, String> tags) {
        Argument.notNull("key", key);
        Argument.notNull("reader", reader);
        Argument.inRangeLong("contentLength", contentLength, 0L, Long.MAX_VALUE);
        Argument.notNull("contentType", contentType);

        try (Writer w = openWriter(key, contentLength, contentType, meta, tags)) {
            Stream.copy(reader, w);
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Storage putMeta(String key) {
        Argument.notNull("key", key);

        return putMeta(key, new LinkedHashMap<>(32));
    }

    public Storage putMeta(String key, Map<String, String> meta) {
        Argument.notNull("key", key);
        Argument.notNull("meta", meta);

        return putMeta(key, meta, null);
    }

    public abstract Storage putMeta(String key, Map<String, String> meta, Map<String, String> tags);

    public Storage putTags(String key) {
        Argument.notNull("key", key);

        return putTags(key, new LinkedHashMap<>(32));
    }

    public abstract Storage putTags(String key, Map<String, String> tags);
}
