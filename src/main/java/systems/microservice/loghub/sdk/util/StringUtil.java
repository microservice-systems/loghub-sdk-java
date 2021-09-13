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
import java.util.HashSet;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class StringUtil {
    private static final HashSet<Character> slugChars = createSlugChars();

    private StringUtil() {
    }

    private static HashSet<Character> createSlugChars() {
        HashSet<Character> cs = new HashSet<>(128);
        cs.add('0'); cs.add('1'); cs.add('2'); cs.add('3'); cs.add('4'); cs.add('5'); cs.add('6'); cs.add('7'); cs.add('8'); cs.add('9');
        cs.add('a'); cs.add('b'); cs.add('c'); cs.add('d'); cs.add('e'); cs.add('f'); cs.add('g'); cs.add('h'); cs.add('i'); cs.add('j');
        cs.add('k'); cs.add('l'); cs.add('m'); cs.add('n'); cs.add('o'); cs.add('p'); cs.add('q'); cs.add('r'); cs.add('s'); cs.add('t');
        cs.add('u'); cs.add('v'); cs.add('w'); cs.add('x'); cs.add('y'); cs.add('z');
        cs.add('-');
        return cs;
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

    public static String slug(String value) {
        if (value != null) {
            value = cut(value, 63);
            value = value.toLowerCase();
            char[] cs = new char[value.length()];
            for (int i = 0, ci = cs.length; i < ci; ++i) {
                char c = value.charAt(i);
                if (slugChars.contains(c)) {
                    cs[i] = c;
                } else {
                    cs[i] = '-';
                }
            }
            int b = 0;
            for (int cb = cs.length; b < cb; ++b) {
                if (cs[b] != '-') {
                    break;
                }
            }
            int e = cs.length - 1;
            for (; e >= 0; --e) {
                if (cs[e] != '-') {
                    break;
                }
            }
            if (b <= e) {
                return new String(cs, b, e - b + 1);
            } else {
                return "";
            }
        } else {
            return null;
        }
    }

    public static int skipChars(String value, int index, char character) {
        for (int ci = value.length(); index < ci; ++index) {
            if (value.charAt(index) != character) {
                return index;
            }
        }
        return index;
    }

    public static int skipDigits(String value, int index) {
        for (int ci = value.length(); index < ci; ++index) {
            if (!Character.isDigit(value.charAt(index))) {
                return index;
            }
        }
        return index;
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

    public static String load(String path, String defaultValue) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
