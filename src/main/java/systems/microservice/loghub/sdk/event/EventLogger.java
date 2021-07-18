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

package systems.microservice.loghub.sdk.event;

import systems.microservice.loghub.sdk.LogEventCallback;
import systems.microservice.loghub.sdk.LogHub;
import systems.microservice.loghub.sdk.util.Blob;
import systems.microservice.loghub.sdk.util.Image;
import systems.microservice.loghub.sdk.util.Tag;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class EventLogger {
    private static final boolean enabled = LogHub.isEnabled();
    private static final AtomicLong totalCount = new AtomicLong(0L);
    private static final AtomicLong totalSize = new AtomicLong(0L);
    private static final AtomicLong sentCount = new AtomicLong(0L);
    private static final AtomicLong sentSize = new AtomicLong(0L);
    private static final AtomicLong lostCount = new AtomicLong(0L);
    private static final AtomicLong lostSize = new AtomicLong(0L);

    private EventLogger() {
    }

    public static void log(long time, String logger, int level, String levelName, Throwable exception, Map<String, Tag> tags, Map<String, Image> images, Map<String, Blob> blobs, LogEventCallback callback, String message) {
    }
}
