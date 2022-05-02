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

package loghub.concurrent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class ThreadSection {
    private final AtomicBoolean enabled;
    private final AtomicLong count = new AtomicLong(0L);

    public ThreadSection(boolean enabled) {
        this.enabled = new AtomicBoolean(enabled);
    }

    public boolean isEnabled() {
        return enabled.get();
    }

    public boolean isDisabled() {
        return !enabled.get() && (count.get() == 0L);
    }

    public boolean enable() {
        return enabled.compareAndSet(false, true);
    }

    public boolean disable() {
        return enabled.compareAndSet(true, false);
    }

    public void await() {
        while (!isDisabled()) {
        }
    }

    public boolean enter() {
        if (enabled.get()) {
            count.incrementAndGet();
            if (enabled.get()) {
                return true;
            } else {
                count.decrementAndGet();
                return false;
            }
        } else {
            return false;
        }
    }

    public void leave() {
        long c = count.decrementAndGet();
        if (c < 0L) {
            count.incrementAndGet();
            throw new IllegalStateException(String.format("Illegal enter/leave calls: %d", c));
        }
    }
}
