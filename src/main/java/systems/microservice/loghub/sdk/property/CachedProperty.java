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

import systems.microservice.loghub.facade.Validator;
import systems.microservice.loghub.sdk.Property;
import systems.microservice.loghub.sdk.util.Eval;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class CachedProperty<T> implements Property<T>, Serializable {
    private static final long serialVersionUID = 1L;

    protected final long timeout;
    protected final Eval<T> eval;
    protected final AtomicReference<T> value;
    protected final AtomicBoolean lock;
    protected final AtomicBoolean invalid;
    protected final AtomicLong time;

    public CachedProperty(Eval<T> eval) {
        this(0L, eval);
    }

    public CachedProperty(long timeout, Eval<T> eval) {
        Validator.inRangeLong("timeout", timeout, 0L, Long.MAX_VALUE);
        Validator.notNull("eval", eval);

        T v = eval.eval();

        this.timeout = timeout;
        this.eval = eval;
        this.value = new AtomicReference<>(v);
        this.lock = new AtomicBoolean(false);
        this.invalid = new AtomicBoolean(false);
        this.time = new AtomicLong(System.currentTimeMillis());
    }

    public long getTimeout() {
        return timeout;
    }

    public Eval<T> getEval() {
        return eval;
    }

    public T getValue() {
        return value.get();
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
    public T get() {
        long t = System.currentTimeMillis();
        if ((timeout > 0L) && (t >= time.get() + timeout)) {
            invalidate();
        }
        if (invalid.get()) {
            if (lock.compareAndSet(false, true)) {
                try {
                    T v = eval.eval();
                    value.set(v);
                    invalid.set(false);
                    time.set(t);
                } finally {
                    lock.set(false);
                }
            }
        }
        return value.get();
    }
}
