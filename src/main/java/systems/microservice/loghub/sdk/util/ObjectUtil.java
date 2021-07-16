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

package systems.microservice.loghub.sdk.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class ObjectUtil {
    public static final String APPLICATION_SERIALIZABLE = "application/serializable";

    private ObjectUtil() {
    }

    public static byte[] serialize(Serializable serializable, int initialSize) {
        try {
            ByteArrayOutputStream d = new ByteArrayOutputStream(initialSize);
            try (ByteArrayOutputStream d1 = d) {
                try (ObjectOutputStream d2 = new ObjectOutputStream(d1)) {
                    d2.writeObject(serializable);
                }
            }
            return d.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deserialize(byte[] data, Class<T> clazz) {
        try {
            try (ByteArrayInputStream d = new ByteArrayInputStream(data)) {
                try (ObjectInputStream d1 = new ObjectInputStream(d)) {
                    return (T) d1.readObject();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
