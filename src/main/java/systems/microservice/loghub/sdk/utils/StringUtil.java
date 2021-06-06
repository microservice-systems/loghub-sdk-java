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

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class StringUtil {
    public static final String EMPTY_STRING = "";
    public static final String[] EMPTY_ARRAY = new String[0];
    public static final String TEXT_PLAIN = "text/plain";

    private StringUtil() {
    }

    public static String cut(String value, int length) {
        if (value != null) {
            if (value.length() <= length) {
                return value;
            } else {
                return value.substring(0, length);
            }
        } else {
            return null;
        }
    }

    public static String getCommonPrefix(String value1, String value2) {
        int minLength = Math.min(value1.length(), value2.length());
        for (int i = 0; i < minLength; ++i) {
            if (value1.charAt(i) != value2.charAt(i)) {
                return value1.substring(0, i);
            }
        }
        return value1.substring(0, minLength);
    }

    public static byte[] serialize(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }

    public static String deserialize(byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }

    public static String load(String path, String defaultValue) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
