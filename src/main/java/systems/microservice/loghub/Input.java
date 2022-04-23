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
public enum Input {
    FACADE((byte) 1),
    JUL((byte) 2),
    LOG4J((byte) 3),
    LOGBACK((byte) 4),
    CUSTOM((byte) 127);

    private static final Map<Byte, Input> inputs = createInputs();

    public final byte id;

    Input(byte id) {
        this.id = id;
    }

    private static Map<Byte, Input> createInputs() {
        Map<Byte, Input> is = new HashMap<>(8);
        is.put(FACADE.id, FACADE);
        is.put(JUL.id, JUL);
        is.put(LOG4J.id, LOG4J);
        is.put(LOGBACK.id, LOGBACK);
        is.put(CUSTOM.id, CUSTOM);
        return is;
    }

    public static Input get(byte id) {
        return inputs.get(id);
    }
}
