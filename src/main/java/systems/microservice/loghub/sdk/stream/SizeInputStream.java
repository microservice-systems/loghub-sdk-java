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

import systems.microservice.loghub.connector.Validation;
import systems.microservice.loghub.sdk.metric.MetricCollector;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class SizeInputStream extends InputStream {
    protected final InputStream input;
    protected final String metric;
    protected long size;
    protected final long begin;
    protected long end;

    public SizeInputStream(InputStream input) {
        this(input, null);
    }

    public SizeInputStream(InputStream input, String metric) {
        Validation.notNull("input", input);

        this.input = input;
        this.metric = metric;
        this.size = 0L;
        this.begin = System.currentTimeMillis();
        this.end = -1L;
    }

    public InputStream getInput() {
        return input;
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
    public int read() throws IOException {
        int b = input.read();
        if (b >= 0) {
            size++;
        }
        return b;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int c = input.read(b);
        if (c >= 0) {
            size += c;
        }
        return c;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int c = input.read(b, off, len);
        if (c >= 0) {
            size += c;
        }
        return c;
    }

    @Override
    public long skip(long n) throws IOException {
        return input.skip(n);
    }

    @Override
    public int available() throws IOException {
        return input.available();
    }

    @Override
    public void close() throws IOException {
        try {
            input.close();
        } finally {
            end = System.currentTimeMillis();
            if (metric != null) {
                MetricCollector.collect(String.format("%s.size", metric), size, 0, "B");
                MetricCollector.collect(String.format("%s.time", metric), end - begin, 0, "ms");
            }
        }
    }

    @Override
    public void mark(int readlimit) {
        input.mark(readlimit);
    }

    @Override
    public void reset() throws IOException {
        input.reset();
    }

    @Override
    public boolean markSupported() {
        return input.markSupported();
    }
}
