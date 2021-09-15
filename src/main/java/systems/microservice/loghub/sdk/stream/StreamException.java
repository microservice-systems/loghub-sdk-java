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

import systems.microservice.loghub.sdk.util.Argument;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class StreamException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected final StreamOperation operation;
    protected final long position;

    public StreamException(StreamOperation operation, long position) {
        super(String.format("[%s]: at position %d", Argument.notNull("operation", operation), Argument.inRangeLong("position", position, 0L, Long.MAX_VALUE)));

        this.operation = operation;
        this.position = position;
    }

    public StreamException(StreamOperation operation, long position, Throwable cause) {
        super(String.format("[%s]: at position %d", Argument.notNull("operation", operation), Argument.inRangeLong("position", position, 0L, Long.MAX_VALUE)), cause);

        this.operation = operation;
        this.position = position;
    }

    public StreamOperation getOperation() {
        return operation;
    }

    public long getPosition() {
        return position;
    }
}
