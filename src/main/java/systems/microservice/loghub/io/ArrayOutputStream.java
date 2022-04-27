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
import java.io.OutputStream;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class ArrayOutputStream extends OutputStream {
    private final int capacityMin;
    private final int capacityMax;
    private byte[] array;
    private int index;

    public ArrayOutputStream(int capacityMin, int capacityMax) {
        Validator.inRangeInt("capacityMin", capacityMin, 0, Integer.MAX_VALUE);
        Validator.inRangeInt("capacityMax", capacityMax, 0, Integer.MAX_VALUE);
        if (capacityMin > capacityMax) {
            throw new IllegalArgumentException(String.format("Argument 'capacityMin = %d' is greater than argument 'capacityMax = %d'", capacityMin, capacityMax));
        }

        this.capacityMin = capacityMin;
        this.capacityMax = capacityMax;
        this.array = new byte[capacityMin];
        this.index = 0;
    }

    public int getCapacityMin() {
        return capacityMin;
    }

    public int getCapacityMax() {
        return capacityMax;
    }

    public byte[] getArray() {
        return array;
    }

    public int getSize() {
        return index;
    }

    @Override
    public void write(int b) throws IOException {
        if (index >= array.length) {
            byte[] a = new byte[(array.length > 0) ? array.length * 2 : 1];
            System.arraycopy(array, 0, a, 0, array.length);
            array = a;
        }
        array[index++] = (byte) b;
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
