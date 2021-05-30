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
public enum LogType {
    START((byte) 1),
    FINISH((byte) 2),
    BEGIN((byte) 3),
    END((byte) 4),
    DEFAULT((byte) 5),
    EXCEPTION((byte) 6),
    THREADS((byte) 7);

    private static final HashMap<Byte, LogType> types = createTypes();

    public final byte id;

    LogType(byte id) {
        this.id = id;
    }

    private static HashMap<Byte, LogType> createTypes() {
        HashMap<Byte, LogType> ts = new HashMap<>(32);
        ts.put(START.id, START);
        ts.put(FINISH.id, FINISH);
        ts.put(BEGIN.id, BEGIN);
        ts.put(END.id, END);
        ts.put(DEFAULT.id, DEFAULT);
        ts.put(EXCEPTION.id, EXCEPTION);
        ts.put(THREADS.id, THREADS);
        return ts;
    }

    public static LogType getType(byte id) {
        return types.get(id);
    }
}
