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

import loghub.io.FormatInputStream;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class EventInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    public final long number;
    public final int size;
    public final long totalCount;
    public final long totalSize;
    public final long sentCount;
    public final long sentSize;
    public final long lostCount;
    public final long lostSize;

    public EventInfo(FormatInputStream input) {
        this.number = 0L;
        this.size = 0;
        this.totalCount = 0L;
        this.totalSize = 0L;
        this.sentCount = 0L;
        this.sentSize = 0L;
        this.lostCount = 0L;
        this.lostSize = 0L;
    }

    public EventInfo(long number, int size, long totalCount, long totalSize, long sentCount, long sentSize, long lostCount, long lostSize) {
        this.number = number;
        this.size = size;
        this.totalCount = totalCount;
        this.totalSize = totalSize;
        this.sentCount = sentCount;
        this.sentSize = sentSize;
        this.lostCount = lostCount;
        this.lostSize = lostSize;
    }
}
