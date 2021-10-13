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

import java.util.Map;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class StorageMeta {
    private StorageMeta() {
    }

    public static long getContentLength(Map<String, String> meta) {
        Argument.notNull("meta", meta);

        String cl = meta.get("Content-Length");
        if (cl != null) {
            return Long.parseLong(cl);
        } else {
            throw new IllegalArgumentException("Meta property 'Content-Length' is not found");
        }
    }

    public static String getContentType(Map<String, String> meta) {
        Argument.notNull("meta", meta);

        String ct = meta.get("Content-Type");
        if (ct != null) {
            return ct;
        } else {
            throw new IllegalArgumentException("Meta property 'Content-Type' is not found");
        }
    }

    public static long getContentTime(Map<String, String> meta) {
        Argument.notNull("meta", meta);

        String ct = meta.get("Content-Time");
        if (ct != null) {
            return Long.parseLong(ct);
        } else {
            throw new IllegalArgumentException("Meta property 'Content-Time' is not found");
        }
    }

    public static String getETag(Map<String, String> meta) {
        Argument.notNull("meta", meta);

        String et = meta.get("ETag");
        if (et != null) {
            return et;
        } else {
            throw new IllegalArgumentException("Meta property 'ETag' is not found");
        }
    }
}
