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

package systems.microservice.loghub.sdk;

import systems.microservice.loghub.sdk.util.Argument;

import java.util.Map;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class Log {
    protected final String logger;

    public Log(Class clazz) {
        this(Argument.notNull("clazz", clazz).getName());
    }

    public Log(String logger) {
        this.logger = Argument.notNull("logger", logger);
    }

    public String getLogger() {
        return logger;
    }

    protected void logEvent(int level, String levelName, String message) {
        LogHub.logEvent(System.currentTimeMillis(), level, levelName, logger, null, null, null, null, message);
    }

    protected void logEvent(int level, String levelName, Map<String, LogTag> tags, String message) {
        LogHub.logEvent(System.currentTimeMillis(), level, levelName, logger, null, tags, null, null, message);
    }

    protected void logEvent(int level, String levelName, Map<String, LogTag> tags, Map<String, LogImage> images, String message) {
        LogHub.logEvent(System.currentTimeMillis(), level, levelName, logger, null, tags, images, null, message);
    }

    protected void logEvent(int level, String levelName, Map<String, LogTag> tags, Map<String, LogImage> images, Map<String, LogBlob> blobs, String message) {
        LogHub.logEvent(System.currentTimeMillis(), level, levelName, logger, null, tags, images, blobs, message);
    }

    protected void logEvent(int level, String levelName, Throwable exception, String message) {
        LogHub.logEvent(System.currentTimeMillis(), level, levelName, logger, exception, null, null, null, message);
    }

    protected void logEvent(int level, String levelName, Throwable exception, Map<String, LogTag> tags, String message) {
        LogHub.logEvent(System.currentTimeMillis(), level, levelName, logger, exception, tags, null, null, message);
    }

    protected void logEvent(int level, String levelName, Throwable exception, Map<String, LogTag> tags, Map<String, LogImage> images, String message) {
        LogHub.logEvent(System.currentTimeMillis(), level, levelName, logger, exception, tags, images, null, message);
    }

    protected void logEvent(int level, String levelName, Throwable exception, Map<String, LogTag> tags, Map<String, LogImage> images, Map<String, LogBlob> blobs, String message) {
        LogHub.logEvent(System.currentTimeMillis(), level, levelName, logger, exception, tags, images, blobs, message);
    }
}
