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
import java.util.UUID;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class LogThreadInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    public final LogTagMap tags;
    public long revision;
    public int depth;

    public LogThreadInfo() {
        Thread t = Thread.currentThread();

        this.tags = new LogTagMap(UUID.randomUUID().toString(), t.getId(), t.getName(), t.getPriority());
        this.revision = 0L;
        this.depth = 0;
    }
}
