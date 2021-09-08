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

import systems.microservice.loghub.sdk.serializer.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class ResourceUtil {
    private ResourceUtil() {
    }

    public static byte[] findData(Class<?> clazz, String name) {
        InputStream input = clazz.getResourceAsStream(name);
        if (input != null) {
            try (InputStream input1 = input) {
                byte[] data = new byte[input1.available()];
                int read = input1.read(data);
                if (read == data.length) {
                    return data;
                } else {
                    throw new RuntimeException(String.format("Read %d bytes from resource '%s:%s' of size %d", read, clazz.getCanonicalName(), name, data.length));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    public static byte[] getData(Class<?> clazz, String name) {
        byte[] d = findData(clazz, name);
        if (d != null) {
            return d;
        } else {
            throw new RuntimeException(String.format("Resource '%s:%s' is not found", clazz.getCanonicalName(), name));
        }
    }

    public static String findString(Class<?> clazz, String name) {
        byte[] d = findData(clazz, name);
        if (d != null) {
            return new String(d, StandardCharsets.UTF_8);
        } else {
            return null;
        }
    }

    public static String getString(Class<?> clazz, String name) {
        String s = findString(clazz, name);
        if (s != null) {
            return s;
        } else {
            throw new RuntimeException(String.format("Resource '%s:%s' is not found", clazz.getCanonicalName(), name));
        }
    }

    public static Properties findProperties(Class<?> clazz, String name) {
        byte[] d = findData(clazz, name);
        if (d != null) {
            return Serializer.PROPERTIES.read(d, Properties.class);
        } else {
            return null;
        }
    }

    public static Properties getProperties(Class<?> clazz, String name) {
        Properties ps = findProperties(clazz, name);
        if (ps != null) {
            return ps;
        } else {
            throw new RuntimeException(String.format("Resource with properties '%s:%s' is not found", clazz.getCanonicalName(), name));
        }
    }
}
