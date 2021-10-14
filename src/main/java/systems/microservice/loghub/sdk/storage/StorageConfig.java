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
import systems.microservice.loghub.sdk.util.URLUtil;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public abstract class StorageConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String target;
    public final String type;
    public final String bucket;
    public final String prefix;

    protected StorageConfig(URL target) {
        this(Argument.notNull("target", target).getProtocol(),
             Argument.notNull("target", target).getHost(),
             Argument.notNull("target", target).getPath());
    }

    protected StorageConfig(String type, String bucket, String prefix) {
        Argument.notNull("type", type);
        Argument.notNull("bucket", bucket);
        Argument.notNull("prefix", prefix);

        this.target = String.format("%s://%s/%s", type, bucket, prefix);
        this.type = type;
        this.bucket = bucket;
        this.prefix = prefix;
    }

    public abstract Storage createStorage();

    public static StorageConfig create(String target) {
        Argument.notNull("target", target);

        return create(URLUtil.createURL(target));
    }

    public static StorageConfig create(URL target) {
        Argument.notNull("target", target);

        try {
            Class<?> c = Class.forName(String.format("systems.microservice.loghub.sdk.storage.%s.CustomStorageConfig", target.getProtocol()));
            return (StorageConfig) c.getConstructor(URL.class).newInstance(target);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
