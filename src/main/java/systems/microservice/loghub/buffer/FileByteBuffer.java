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

package systems.microservice.loghub.facade.buffer;

import systems.microservice.loghub.facade.config.Validator;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class FileByteBuffer implements ByteBuffer {
    private final Object lock;
    private final String file;
    private final long length;
    private final RandomAccessFile pointer1;
    private final RandomAccessFile pointer2;
    private long index;

    public FileByteBuffer(String file, long length) {
        Validator.notNull("file", file);
        Validator.inRangeLong("length", length, 0L, Long.MAX_VALUE);

        try {
            RandomAccessFile ptr = new RandomAccessFile(file, "rw");
            ptr.setLength(length);

            this.lock = new Object();
            this.file = file;
            this.length = length;
            this.pointer1 = ptr;
            this.pointer2 = new RandomAccessFile(file, "rw");
            this.index = 0L;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public boolean append(byte[] array, int offset, int length) {
        return false;
    }

    @Override
    public boolean send(OutputStream output) {
        return false;
    }

    @Override
    public void reset() {

    }
}
