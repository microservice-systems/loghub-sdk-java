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

package systems.microservice.loghub.sdk.event;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class EventList extends LinkedList<Event> implements EventIterable {
    private static final long serialVersionUID = 1L;

    public EventList() {
    }

    public EventList(Collection<? extends Event> c) {
        super(c);
    }

    @Override
    public EventIterator iterator() {
        final long c = this.size();
        final Iterator<Event> it = super.iterator();
        return new EventIterator() {
            private long count = c;
            private final Iterator<Event> iterator = it;

            @Override
            public long count() {
                return count;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Event next() {
                Event e = iterator.next();
                count--;
                return e;
            }
        };
    }
}
