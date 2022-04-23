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

import systems.microservice.loghub.io.FormatInputStream;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class EventException implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String clazz;
    public final String message;
    public final String stacktrace;
    public final String causeClass;
    public final String causeMessage;
    public final int suppressedCount;

    public EventException(FormatInputStream input) {
        this.clazz = null;
        this.message = null;
        this.stacktrace = null;
        this.causeClass = null;
        this.causeMessage = null;
        this.suppressedCount = 0;
    }

    public EventException(String clazz, String message, String stacktrace, String causeClass, String causeMessage, int suppressedCount) {
        this.clazz = clazz;
        this.message = message;
        this.stacktrace = stacktrace;
        this.causeClass = causeClass;
        this.causeMessage = causeMessage;
        this.suppressedCount = suppressedCount;
    }
}
