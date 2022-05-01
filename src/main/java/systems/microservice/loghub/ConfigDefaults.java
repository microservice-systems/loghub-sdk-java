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

package systems.microservice.loghub;

import java.net.InetAddress;
import java.util.UUID;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class ConfigDefaults {
    public static String LOGHUB_CENTRAL = "loghub.dev";
    public static String LOGHUB_ORGANIZATION = null;
    public static String LOGHUB_ENVIRONMENT = null;
    public static String LOGHUB_REGISTRY = null;
    public static String LOGHUB_GROUP = null;
    public static String LOGHUB_APPLICATION = null;
    public static String LOGHUB_VERSION = null;
    public static String LOGHUB_REVISION = null;
    public static String LOGHUB_NAME = null;
    public static String LOGHUB_DESCRIPTION = null;
    public static String LOGHUB_REPOSITORY = null;
    public static String LOGHUB_JOB = null;
    public static String LOGHUB_PIPELINE = null;
    public static String LOGHUB_BRANCH = null;
    public static String LOGHUB_COMMIT = null;
    public static String LOGHUB_COMMIT_BEFORE = null;
    public static String LOGHUB_COMMIT_MESSAGE = null;
    public static String LOGHUB_MAINTAINER = null;
    public static String LOGHUB_MAINTAINER_NAME = null;
    public static String LOGHUB_MAINTAINER_EMAIL = null;
    public static String LOGHUB_INSTANCE = createInstance();
    public static String LOGHUB_EVENT_INCLUDE = null;
    public static String LOGHUB_EVENT_EXCLUDE = null;

    private ConfigDefaults() {
    }

    private static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Throwable e) {
            return null;
        }
    }

    private static String getHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Throwable e) {
            return null;
        }
    }

    private static String createInstance() {
        String i = getHostName();
        if (i == null) {
            i = getHostAddress();
            if (i == null) {
                i = UUID.randomUUID().toString();
            }
        }
        return String.format("java-%s", i);
    }
}
