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

package systems.microservice.loghub;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public enum Level {
    NONE((byte) 0),
    ERROR((byte) 10),
    WARN((byte) 20),
    INFO((byte) 30),
    DEBUG((byte) 40),
    TRACE((byte) 50),
    ALL(Byte.MAX_VALUE);

    private static final Map<Byte, Level> levels = createLevels();

    public final byte id;

    Level(byte id) {
        this.id = id;
    }

    private static Map<Byte, Level> createLevels() {
        Map<Byte, Level> ls = new HashMap<>(8);
        ls.put(NONE.id, NONE);
        ls.put(ERROR.id, ERROR);
        ls.put(WARN.id, WARN);
        ls.put(INFO.id, INFO);
        ls.put(DEBUG.id, DEBUG);
        ls.put(TRACE.id, TRACE);
        ls.put(ALL.id, ALL);
        return ls;
    }

    public static Level get(byte id) {
        return levels.get(id);
    }
}
