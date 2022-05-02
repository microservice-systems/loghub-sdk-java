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

package loghub;

import loghub.config.Validator;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class LogHub implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String logger;
    public final InputEvent error;
    public final InputEvent warn;
    public final InputEvent info;
    public final InputEvent debug;
    public final InputEvent trace;

    public LogHub(Class<?> logger) {
        this(Validator.notNull("logger", logger).getCanonicalName());
    }

    public LogHub(String logger) {
        this.logger = Validator.notNull("logger", logger);
        this.error = new InputEvent(Level.ERROR, logger, Type.DEFAULT, null, null, null);
        this.warn = new InputEvent(Level.WARN, logger, Type.DEFAULT, null, null, null);
        this.info = new InputEvent(Level.INFO, logger, Type.DEFAULT, null, null, null);
        this.debug = new InputEvent(Level.DEBUG, logger, Type.DEFAULT, null, null, null);
        this.trace = new InputEvent(Level.TRACE, logger, Type.DEFAULT, null, null, null);
    }
}
