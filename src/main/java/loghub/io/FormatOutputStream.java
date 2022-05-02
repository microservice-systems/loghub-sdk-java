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

package loghub.io;

import loghub.config.Validator;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class FormatOutputStream extends OutputStream {
    protected final OutputStream output;
    protected boolean closed;

    public FormatOutputStream(OutputStream output) {
        this.output = output;
        this.closed = false;
    }

    public OutputStream getOutput() {
        return output;
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public void write(int b) throws IOException {
        if (!closed) {
            if (output != null) {
                output.write(b);
            }
        } else {
            throw new IllegalStateException("FormatOutputStream is closed");
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        Validator.notNull("b", b);
        Validator.inRangeInt("off", off, 0, b.length);
        Validator.inRangeInt("len", len, 0, b.length - off);

        if (!closed) {
            if (output != null) {
                output.write(b, off, len);
            }
        } else {
            throw new IllegalStateException("FormatOutputStream is closed");
        }
    }

    @Override
    public void flush() throws IOException {
        if (!closed) {
            if (output != null) {
                output.flush();
            }
        } else {
            throw new IllegalStateException("FormatOutputStream is closed");
        }
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            if (output != null) {
                output.close();
            }
            closed = true;
        }
    }
}
