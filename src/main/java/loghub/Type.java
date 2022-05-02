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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public enum Type {
    DEFAULT((byte) 1),
    EXCEPTION((byte) 2),
    BEGIN((byte) 3),
    END((byte) 4),
    START((byte) 5),
    FINISH((byte) 6),
    CPU((byte) 7),
    MEMORY((byte) 8),
    DISK((byte) 9),
    NETWORK((byte) 10),
    CLASS((byte) 11),
    THREAD((byte) 12),
    DESCRIPTOR((byte) 13),
    GC((byte) 14);

    private static final Map<Byte, Type> types = createTypes();

    public final byte id;

    Type(byte id) {
        this.id = id;
    }

    private static Map<Byte, Type> createTypes() {
        Map<Byte, Type> ts = new HashMap<>(16);
        ts.put(DEFAULT.id, DEFAULT);
        ts.put(EXCEPTION.id, EXCEPTION);
        ts.put(BEGIN.id, BEGIN);
        ts.put(END.id, END);
        ts.put(START.id, START);
        ts.put(FINISH.id, FINISH);
        ts.put(CPU.id, CPU);
        ts.put(MEMORY.id, MEMORY);
        ts.put(DISK.id, DISK);
        ts.put(NETWORK.id, NETWORK);
        ts.put(CLASS.id, CLASS);
        ts.put(THREAD.id, THREAD);
        ts.put(DESCRIPTOR.id, DESCRIPTOR);
        ts.put(GC.id, GC);
        return ts;
    }

    public static Type get(byte id) {
        return types.get(id);
    }
}
