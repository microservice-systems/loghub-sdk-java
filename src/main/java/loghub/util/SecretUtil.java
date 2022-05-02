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

package loghub.util;

import java.security.SecureRandom;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class SecretUtil {
    private static final char[] SYMBOLS = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private SecretUtil() {
    }

    public static String randomSecret() {
        SecureRandom sr = new SecureRandom();
        char[] s = new char[40];
        for (int i = 0; i < 40; ++i) {
            s[i] = SYMBOLS[sr.nextInt(SYMBOLS.length)];
        }
        return new String(s);
    }
}
