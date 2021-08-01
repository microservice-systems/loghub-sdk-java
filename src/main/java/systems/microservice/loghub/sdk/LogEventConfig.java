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

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class LogEventConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    public final boolean enabled;
    public final Pattern version;
    public final Pattern instance;
    public final Pattern instanceHost;
    public final Pattern instanceIp;
    public final Pattern threadName;
    public final int level;
    public final Pattern type;
    public final Pattern logger;
    public final Pattern message;
    public final Pattern tags;

    public LogEventConfig(boolean enabled,
                          Pattern version,
                          Pattern instance,
                          Pattern instanceHost,
                          Pattern instanceIp,
                          Pattern threadName,
                          int level,
                          Pattern type,
                          Pattern logger,
                          Pattern message,
                          Pattern tags) {
        this.enabled = enabled;
        this.version = version;
        this.instance = instance;
        this.instanceHost = instanceHost;
        this.instanceIp = instanceIp;
        this.threadName = threadName;
        this.level = level;
        this.type = type;
        this.logger = logger;
        this.message = message;
        this.tags = tags;
    }
}
