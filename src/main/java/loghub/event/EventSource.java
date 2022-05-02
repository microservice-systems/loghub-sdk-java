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

package loghub.event;

import loghub.Platform;
import loghub.io.FormatInputStream;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class EventSource implements Serializable {
    private static final long serialVersionUID = 1L;

    public final Platform platform;
    public final String input;
    public final String clazz;
    public final String method;
    public final String statement;
    public final String file;
    public final int line;

    public EventSource(FormatInputStream input) {
        this.platform = null;
        this.input = null;
        this.clazz = null;
        this.method = null;
        this.statement = null;
        this.file = null;
        this.line = 0;
    }

    public EventSource(Platform platform, String input, String clazz, String method, String statement, String file, int line) {
        this.platform = platform;
        this.input = input;
        this.clazz = clazz;
        this.method = method;
        this.statement = statement;
        this.file = file;
        this.line = line;
    }
}
