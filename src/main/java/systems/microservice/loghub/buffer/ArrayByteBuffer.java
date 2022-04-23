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

import systems.microservice.loghub.facade.concurrent.ThreadSection;
import systems.microservice.loghub.facade.config.Validator;

import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class ArrayByteBuffer implements ByteBuffer {
    private final ThreadSection section;
    private final byte[] array;
    private final AtomicInteger index;

    public ArrayByteBuffer(int length) {
        Validator.inRangeInt("length", length, 0, Integer.MAX_VALUE);

        this.section = new ThreadSection(true);
        this.array = new byte[length];
        this.index = new AtomicInteger(0);
    }

    @Override
    public boolean isReady() {
        return section.isEnabled();
    }

    @Override
    public boolean append(byte[] array, int offset, int length) {
        if (section.enter()) {
            try {
                byte[] a = this.array;
                int al = a.length;
                int l = length + 5;
                if (index.get() + l <= al) {
                    int bi = index.getAndAdd(l);
                    int ei = bi + l;
                    if (ei <= al) {
                        System.arraycopy(array, offset, a, bi, length);
                        a[ei - 1] = 0;
                        a[bi - 1] = 1;
                        return true;
                    }
                }
            } finally {
                section.leave();
            }
            section.disable();
        }
        return false;
    }

    @Override
    public boolean send(OutputStream output) {
        return false;
    }

    @Override
    public void reset() {
        index.set(0);
    }
}
