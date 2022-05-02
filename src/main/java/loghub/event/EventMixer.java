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

package loghub.event;

import loghub.Order;
import loghub.config.Validator;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class EventMixer implements EventIterator {
    private final Order order;
    private final AtomicLong count;
    private final AtomicLong size;
    private final EventIterator[] iterators;

    public EventMixer(Order order, EventIterator... iterators) {
        Validator.notNull("order", order);
        Validator.notNull("iterators", iterators);

        this.order = order;
        this.count = new AtomicLong(0L);
        this.size = new AtomicLong(0L);
        this.iterators = iterators;
    }

    @Override
    public Order order() {
        return order;
    }

    @Override
    public long count() {
        return count.get();
    }

    @Override
    public long size() {
        return size.get();
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Event next() {
        return null;
    }
}
