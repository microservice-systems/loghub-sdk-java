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

package systems.microservice.loghub.sdk.storage;

import systems.microservice.loghub.sdk.util.Argument;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class StorageException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected final String target;
    protected final StorageOperation operation;

    public StorageException(String target, StorageOperation operation) {
        super(String.format("[%s][%s]", Argument.notNull("target", target), Argument.notNull("operation", operation)));

        this.target = target;
        this.operation = operation;
    }

    public StorageException(String target, StorageOperation operation, Throwable cause) {
        super(String.format("[%s][%s]", Argument.notNull("target", target), Argument.notNull("operation", operation)), cause);

        this.target = target;
        this.operation = operation;
    }

    public StorageException(String target, StorageOperation operation, String message) {
        super(String.format("[%s][%s]: %s", Argument.notNull("target", target), Argument.notNull("operation", operation), Argument.notNull("message", message)));

        this.target = target;
        this.operation = operation;
    }

    public StorageException(String target, StorageOperation operation, String message, Throwable cause) {
        super(String.format("[%s][%s]: %s", Argument.notNull("target", target), Argument.notNull("operation", operation), Argument.notNull("message", message)), cause);

        this.target = target;
        this.operation = operation;
    }

    public String getTarget() {
        return target;
    }

    public StorageOperation getOperation() {
        return operation;
    }
}
