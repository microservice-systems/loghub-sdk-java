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

package systems.microservice.loghub.sdk.property;

import systems.microservice.loghub.sdk.Property;
import systems.microservice.loghub.sdk.config.ConfigExtractor;
import systems.microservice.loghub.sdk.config.extractor.ConfigValueOfExtractor;
import systems.microservice.loghub.sdk.util.Argument;
import systems.microservice.loghub.sdk.util.Eval;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class ExtractableCachedProperty<I, O> implements Property<O>, Serializable {
    private static final long serialVersionUID = 1L;

    protected final long timeout;
    protected final Eval<I> eval;
    protected final AtomicReference<I> value;
    protected final Class<O> outputClass;
    protected final ConfigExtractor<I, O> extractor;
    protected final AtomicReference<O> extractedValue;
    protected final AtomicBoolean lock;
    protected final AtomicBoolean invalid;
    protected final AtomicLong time;

    public ExtractableCachedProperty(Class<O> outputClass, Eval<I> eval) {
        this(0L, outputClass, ConfigValueOfExtractor.getInstance(), eval);
    }

    public ExtractableCachedProperty(long timeout, Class<O> outputClass, ConfigExtractor<I, O> extractor, Eval<I> eval) {
        Argument.inRangeLong("timeout", timeout, 0L, Long.MAX_VALUE);
        Argument.notNull("outputClass", outputClass);
        Argument.notNull("extractor", extractor);
        Argument.notNull("eval", eval);

        I v = eval.eval();
        O ev = extractor.extract(v, outputClass);

        this.timeout = timeout;
        this.eval = eval;
        this.value = new AtomicReference<>(v);
        this.outputClass = outputClass;
        this.extractor = extractor;
        this.extractedValue = new AtomicReference<>(ev);
        this.lock = new AtomicBoolean(false);
        this.invalid = new AtomicBoolean(false);
        this.time = new AtomicLong(System.currentTimeMillis());
    }

    public long getTimeout() {
        return timeout;
    }

    public Eval<I> getEval() {
        return eval;
    }

    public I getValue() {
        return value.get();
    }

    public Class<O> getOutputClass() {
        return outputClass;
    }

    public ConfigExtractor<I, O> getExtractor() {
        return extractor;
    }

    public O getExtractedValue() {
        return extractedValue.get();
    }

    public boolean isLocked() {
        return lock.get();
    }

    public boolean isInvalid() {
        return invalid.get();
    }

    public long getTime() {
        return time.get();
    }

    public void invalidate() {
        invalid.set(true);
    }

    @Override
    public O get() {
        long t = System.currentTimeMillis();
        if ((timeout > 0L) && (t >= time.get() + timeout)) {
            invalidate();
        }
        if (invalid.get()) {
            if (lock.compareAndSet(false, true)) {
                try {
                    I v = eval.eval();
                    O ev = extractor.extract(v, outputClass);
                    value.set(v);
                    extractedValue.set(ev);
                    invalid.set(false);
                    time.set(t);
                } finally {
                    lock.set(false);
                }
            }
        }
        return extractedValue.get();
    }
}
