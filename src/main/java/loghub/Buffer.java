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
public enum Buffer {
    MEMORY((byte) 1),
    DISK((byte) 2);

    private static final Map<Byte, Buffer> buffers = createBuffers();

    public final byte id;

    Buffer(byte id) {
        this.id = id;
    }

    private static Map<Byte, Buffer> createBuffers() {
        Map<Byte, Buffer> os = new HashMap<>(2);
        os.put(MEMORY.id, MEMORY);
        os.put(DISK.id, DISK);
        return os;
    }

    public static Buffer get(byte id) {
        return buffers.get(id);
    }
}
