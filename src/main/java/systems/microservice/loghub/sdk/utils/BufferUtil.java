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

package systems.microservice.loghub.sdk.utils;

import systems.microservice.loghub.sdk.buffer.BufferReader;
import systems.microservice.loghub.sdk.buffer.BufferWriter;
import systems.microservice.loghub.sdk.buffer.Bufferable;

import java.util.LinkedHashMap;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class BufferUtil {
    public static final String APPLICATION_BUFFERABLE = "application/bufferable";

    private BufferUtil() {
    }

    public static byte[] serialize(Bufferable bufferable) {
        int l = BufferWriter.writeBufferable(null, 0, new LinkedHashMap<>(), bufferable);
        byte[] d = new byte[l];
        BufferWriter.writeBufferable(d, 0, new LinkedHashMap<>(), bufferable);
        return d;
    }

    public static <T extends Bufferable> T deserialize(byte[] data, Class<T> clazz) {
        BufferReader r = new BufferReader(data);
        return r.readBufferable(clazz);
    }
}
