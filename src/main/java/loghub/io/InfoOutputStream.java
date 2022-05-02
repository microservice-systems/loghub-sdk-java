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

import loghub.Metric;
import loghub.config.Validator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class InfoOutputStream extends OutputStream {
    protected final OutputStream output;
    protected final String name;
    protected final long begin;
    protected final AtomicLong end;
    protected final AtomicLong size;
    protected final Metric metricSize;
    protected boolean closed;

    public InfoOutputStream(OutputStream output, String name) {
        this.output = output;
        this.name = name;
        this.begin = System.currentTimeMillis();
        this.end = new AtomicLong(-1L);
        this.size = new AtomicLong(0L);
        this.metricSize = (name != null) ? new Metric(String.format("%s.instances.%s.size", this.getClass().getCanonicalName(), name), 0, "bytes") : null;
        this.closed = false;
    }

    public OutputStream getOutput() {
        return output;
    }

    public String getName() {
        return name;
    }

    public long getBegin() {
        return begin;
    }

    public long getEnd() {
        return end.get();
    }

    public long getSize() {
        return size.get();
    }

    public boolean isClosed() {
        return end.get() != -1L;
    }

    @Override
    public void write(int b) throws IOException {
        if (!closed) {
            if (output != null) {
                output.write(b);
            }
            size.incrementAndGet();
            if (metricSize != null) {
                metricSize.collect(1L);
            }
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        Validator.inRangeInt("len", len, 0, Integer.MAX_VALUE);

        if (!closed) {
            if (output != null) {
                output.write(b, off, len);
            }
            size.addAndGet(len);
            if (metricSize != null) {
                metricSize.collect(len);
            }
        }
    }

    @Override
    public void flush() throws IOException {
        if (!closed) {
            if (output != null) {
                output.flush();
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (!closed) {
            if (output != null) {
                output.close();
            }
            end.set(System.currentTimeMillis());
            closed = true;
        }
    }
}
