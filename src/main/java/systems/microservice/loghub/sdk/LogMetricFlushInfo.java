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

package systems.microservice.loghub.sdk;

import systems.microservice.loghub.sdk.buffer.BufferWriter;
import systems.microservice.loghub.sdk.buffer.Bufferable;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
final class LogMetricFlushInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    public byte[] buffer;
    public final int headerEnd;
    public int contentEnd;

    public LogMetricFlushInfo() {
        this.buffer = new byte[writeHeader(null) + 65536];
        this.headerEnd = writeHeader(this.buffer);
        this.contentEnd = -1;
    }

    public int writeContent(LogMetricBuffer content) {
        int index = BufferWriter.writeBufferable(null, headerEnd, null, content);
        if (index >= buffer.length) {
            byte[] b = new byte[index];
            System.arraycopy(buffer, 0, b, 0, headerEnd);
            buffer = b;
        }
        contentEnd = BufferWriter.writeBufferable(buffer, headerEnd, null, content);
        if (contentEnd != index) {
            throw new RuntimeException(String.format("Content end %d is not equal to index %d after two phase bufferization", contentEnd, index));
        }
        return contentEnd;
    }

    private static int writeHeader(byte[] buffer) {
        long h = 0x4C4F474855422D4DL;
        byte v = 1;
        int index = 0;
        index = BufferWriter.writeLong(buffer, index, h);
        index = BufferWriter.writeByte(buffer, index, v);
        return index;
    }
}
