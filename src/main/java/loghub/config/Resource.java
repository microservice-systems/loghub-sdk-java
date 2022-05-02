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

package loghub.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class Resource {
    private Resource() {
    }

    private static int read(InputStream input, byte[] array, int offset, int length) throws IOException {
        int c = 0;
        for (int bc = input.read(array, offset, length); (length > 0) && (bc != -1); bc = input.read(array, offset, length)) {
            c += bc;
            offset += bc;
            length -= bc;
        }
        return c;
    }

    private static byte[] read(InputStream input) {
        int c = 0;
        try {
            byte[] a = new byte[256];
            for (int l = a.length, bc = read(input, a, 0, l); bc == l; bc = read(input, a, l, l)) {
                l = a.length;
                byte[] b = new byte[l * 2];
                System.arraycopy(a, 0, b, 0, l);
                a = b;
                c += bc;
            }
            byte[] r = new byte[c];
            System.arraycopy(a, 0, r, 0, c);
            return r;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getArray(String name) {
        Validator.notNull("name", name);

        InputStream input = Resource.class.getResourceAsStream(name);
        if (input != null) {
            try (InputStream in = input) {
                return read(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }

    public static String getString(String name) {
        Validator.notNull("name", name);

        byte[] a = getArray(name);
        if (a != null) {
            return new String(a, StandardCharsets.UTF_8);
        } else {
            return null;
        }
    }
}
