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
import java.util.Map;

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
        ls.put(0, OFF);
        ls.put(1, LIFECYCLE);
        ls.put(2, DUMP);
        ls.put(50, LOGIC);
        ls.put(100, FATAL);
        ls.put(200, ERROR);
        ls.put(300, WARN);
        ls.put(400, INFO);
        ls.put(500, DEBUG);
        ls.put(600, TRACE);
        ls.put(Integer.MAX_VALUE, ALL);
        return ls;
    }

    public static LogLevel getLevel(int id) {
        return levels.get(id);
    }
}
