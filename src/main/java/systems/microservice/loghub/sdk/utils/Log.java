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

package systems.microservice.loghub.sdk.utils;

import systems.microservice.loghub.sdk.LogHub;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class Log implements Serializable {
    private static final long serialVersionUID = 1L;

    protected final String logger;

    @SuppressWarnings("rawtypes")
    public Log(Class logger) {
        this(Argument.notNull("logger", logger).getCanonicalName());
    }

    public Log(String logger) {
        this.logger = Argument.notNull("logger", logger);
    }

    public String getLogger() {
        return logger;
    }

    protected void logEvent(int level, String levelName, String message) {
        LogHub.log(System.currentTimeMillis(), logger, level, levelName, null, null, null, null, message);
    }

    protected void logEvent(int level, String levelName, Map<String, Tag> tags, String message) {
        LogHub.log(System.currentTimeMillis(), logger, level, levelName, null, tags, null, null, message);
    }

    protected void logEvent(int level, String levelName, Map<String, Tag> tags, Map<String, Image> images, String message) {
        LogHub.log(System.currentTimeMillis(), logger, level, levelName, null, tags, images, null, message);
    }

    protected void logEvent(int level, String levelName, Map<String, Tag> tags, Map<String, Image> images, Map<String, Blob> blobs, String message) {
        LogHub.log(System.currentTimeMillis(), logger, level, levelName, null, tags, images, blobs, message);
    }

    protected void logEvent(int level, String levelName, Throwable exception, String message) {
        LogHub.log(System.currentTimeMillis(), logger, level, levelName, exception, null, null, null, message);
    }

    protected void logEvent(int level, String levelName, Throwable exception, Map<String, Tag> tags, String message) {
        LogHub.log(System.currentTimeMillis(), logger, level, levelName, exception, tags, null, null, message);
    }

    protected void logEvent(int level, String levelName, Throwable exception, Map<String, Tag> tags, Map<String, Image> images, String message) {
        LogHub.log(System.currentTimeMillis(), logger, level, levelName, exception, tags, images, null, message);
    }

    protected void logEvent(int level, String levelName, Throwable exception, Map<String, Tag> tags, Map<String, Image> images, Map<String, Blob> blobs, String message) {
        LogHub.log(System.currentTimeMillis(), logger, level, levelName, exception, tags, images, blobs, message);
    }
}
