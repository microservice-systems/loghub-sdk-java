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

package systems.microservice.loghub.sdk.stream;

import systems.microservice.loghub.sdk.metric.MetricCollector;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class SizeOutputStream extends OutputStream {
    protected final OutputStream output;
    protected final String metric;
    protected long size;
    protected final long begin;
    protected long end;

    public SizeOutputStream(OutputStream output) {
        this(output, null);
    }

    public SizeOutputStream(OutputStream output, String metric) {
        this.output = output;
        this.metric = metric;
        this.size = 0L;
        this.begin = System.currentTimeMillis();
        this.end = -1L;
    }

    public OutputStream getOutput() {
        return output;
    }

    public String getMetric() {
        return metric;
    }

    public long getSize() {
        return size;
    }

    public long getBegin() {
        return begin;
    }

    public long getEnd() {
        return end;
    }

    @Override
    public void write(int b) throws IOException {
        if (output != null) {
            output.write(b);
        }
        size++;
    }

    @Override
    public void write(byte[] b) throws IOException {
        if (output != null) {
            output.write(b);
        }
        size += b.length;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (output != null) {
            output.write(b, off, len);
        }
        size += len;
    }

    @Override
    public void flush() throws IOException {
        if (output != null) {
            output.flush();
        }
    }

    @Override
    public void close() throws IOException {
        try {
            if (output != null) {
                output.close();
            }
        } finally {
            end = System.currentTimeMillis();
            if (metric != null) {
                MetricCollector.collect(String.format("%s.size", metric), size, 0, "B");
                MetricCollector.collect(String.format("%s.time", metric), end - begin, 0, "ms");
            }
        }
    }
}
