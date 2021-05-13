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

import java.util.HashMap;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public enum LogLevel {
    OFF(0),
    LIFECYCLE(1),
    DUMP(2),
    LOGIC(50),
    FATAL(100),
    ERROR(200),
    WARN(300),
    INFO(400),
    DEBUG(500),
    TRACE(600),
    ALL(Integer.MAX_VALUE);

    private static final HashMap<Integer, LogLevel> levels = createLevels();

    public final int id;

    LogLevel(int id) {
        this.id = id;
    }

    private static HashMap<Integer, LogLevel> createLevels() {
        HashMap<Integer, LogLevel> ls = new HashMap<>(16);
        ls.put(OFF.id, OFF);
        ls.put(LIFECYCLE.id, LIFECYCLE);
        ls.put(DUMP.id, DUMP);
        ls.put(LOGIC.id, LOGIC);
        ls.put(FATAL.id, FATAL);
        ls.put(ERROR.id, ERROR);
        ls.put(WARN.id, WARN);
        ls.put(INFO.id, INFO);
        ls.put(DEBUG.id, DEBUG);
        ls.put(TRACE.id, TRACE);
        ls.put(ALL.id, ALL);
        return ls;
    }

    public static LogLevel getLevel(int id) {
        return levels.get(id);
    }
}
