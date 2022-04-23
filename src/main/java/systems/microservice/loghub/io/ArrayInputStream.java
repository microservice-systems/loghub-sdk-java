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

package systems.microservice.loghub.io;

import systems.microservice.loghub.config.Validator;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class ArrayInputStream extends InputStream {
    protected final byte[] array;
    protected int index;

    public ArrayInputStream(byte[] array) {
        Validator.notNull("array", array);

        this.array = array;
        this.index = 0;
    }

    public byte[] getArray() {
        return array;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public int read() throws IOException {
        return (index < array.length) ? ((int) array[index++]) & 0xFF : -1;
    }

    @Override
    public void close() throws IOException {
        index = 0;
    }
}
