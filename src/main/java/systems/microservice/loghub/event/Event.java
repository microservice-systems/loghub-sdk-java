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

package systems.microservice.loghub.event;

import systems.microservice.loghub.Blob;
import systems.microservice.loghub.Image;
import systems.microservice.loghub.Level;
import systems.microservice.loghub.Tag;
import systems.microservice.loghub.Type;
import systems.microservice.loghub.io.FormatInputStream;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class Event implements Serializable {
    private static final long serialVersionUID = 1L;

    public final UUID id;
    public final long time;
    public final Level level;
    public final String logger;
    public final Type type;
    public final String message;
    public final EventSource source;
    public final EventException exception;
    public final Map<String, Tag> tags;
    public final EventThread thread;
    public final EventProcess process;
    public final EventUsage usage;
    public final EventConfig config;
    public final EventInfo info;
    public final Image image;
    public final Blob blob;

    public Event(FormatInputStream input) {
        this.id = null;
        this.time = 0L;
        this.level = null;
        this.logger = null;
        this.type = null;
        this.message = null;
        this.source = null;
        this.exception = null;
        this.tags = null;
        this.thread = null;
        this.process = null;
        this.usage = null;
        this.config = null;
        this.info = null;
        this.image = null;
        this.blob = null;
    }

    public Event(UUID id, long time, Level level, String logger, Type type, String message, EventSource source, EventException exception, Map<String, Tag> tags, EventThread thread, EventProcess process, EventUsage usage, EventConfig config, EventInfo info, Image image, Blob blob) {
        this.id = id;
        this.time = time;
        this.level = level;
        this.logger = logger;
        this.type = type;
        this.message = message;
        this.source = source;
        this.exception = exception;
        this.tags = tags;
        this.thread = thread;
        this.process = process;
        this.usage = usage;
        this.config = config;
        this.info = info;
        this.image = image;
        this.blob = blob;
    }
}
