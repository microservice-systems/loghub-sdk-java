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

package systems.microservice.loghub.sdk.storage.s3;

import io.minio.MinioClient;
import systems.microservice.loghub.sdk.storage.Storage;
import systems.microservice.loghub.sdk.storage.StorageFilter;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class CustomStorage extends Storage {
    protected final MinioClient minio;

    public CustomStorage(CustomStorageConfig config) {
        super(config);

        this.minio = MinioClient.builder().endpoint(config.endpoint).credentials(config.accessKey, config.secretKey).build();
    }

    public MinioClient getMinio() {
        return minio;
    }

    @Override
    public Storage substorage(String prefix) {
        return null;
    }

    @Override
    public Iterable<String> find(String prefix, StorageFilter filter) {
        return null;
    }

    @Override
    public Iterable<String> list(String prefix, StorageFilter filter) {
        return null;
    }

    @Override
    public String getOwner(String key) {
        return null;
    }

    @Override
    public String getVersion(String key) {
        return null;
    }

    @Override
    public InputStream openInputStream(String key, Map<String, String> meta, Map<String, String> tags) {
        return null;
    }

    @Override
    public Reader openReader(String key, Map<String, String> meta, Map<String, String> tags) {
        return null;
    }

    @Override
    public Map<String, String> getMeta(String key, Map<String, String> meta, Map<String, String> tags) {
        return null;
    }

    @Override
    public Map<String, String> getTags(String key, Map<String, String> tags) {
        return null;
    }

    @Override
    public OutputStream openOutputStream(String key, long contentLength, String contentType, Map<String, String> meta, Map<String, String> tags) {
        return null;
    }

    @Override
    public Writer openWriter(String key, long contentLength, String contentType, Map<String, String> meta, Map<String, String> tags) {
        return null;
    }

    @Override
    public Storage putMeta(String key, Map<String, String> meta, Map<String, String> tags) {
        return null;
    }

    @Override
    public Storage putTags(String key, Map<String, String> tags) {
        return null;
    }
}
