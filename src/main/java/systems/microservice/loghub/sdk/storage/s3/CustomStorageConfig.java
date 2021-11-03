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

import systems.microservice.loghub.facade.Validator;
import systems.microservice.loghub.sdk.storage.Storage;
import systems.microservice.loghub.sdk.storage.StorageConfig;
import systems.microservice.loghub.sdk.util.URLUtil;

import java.net.URL;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class CustomStorageConfig extends StorageConfig {
    private static final long serialVersionUID = 1L;

    public final String endpoint;
    public final String accessKey;
    public final String secretKey;

    public CustomStorageConfig(URL target) {
        this(Validator.notNull("target", target).getHost(),
             Validator.notNull("target", target).getPath(),
             URLUtil.getParameter(target, "endpoint"),
             URLUtil.getUser(target),
             URLUtil.getPassword(target));
    }

    public CustomStorageConfig(String bucket, String prefix, String endpoint, String accessKey, String secretKey) {
        super("s3", bucket, prefix);

        Validator.notNull("endpoint", endpoint);
        Validator.notNull("accessKey", accessKey);
        Validator.notNull("secretKey", secretKey);

        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    @Override
    public Storage createStorage() {
        return null;
    }
}
