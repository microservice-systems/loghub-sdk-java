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

package systems.microservice.loghub.facade;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;

    public final Level level;
    public final String logger;
    public final Type type;
    public final Throwable exception;
    public final Tag tag;
    public final Tag[] tags;
    public final Image image;
    public final Blob blob;

    public Event(Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags, Image image, Blob blob) {
        this.level = level;
        this.logger = logger;
        this.type = type;
        this.exception = exception;
        this.tag = tag;
        this.tags = tags;
        this.image = image;
        this.blob = blob;
    }
}
