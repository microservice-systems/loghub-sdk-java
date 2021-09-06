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

package systems.microservice.loghub.sdk.serializer;

import systems.microservice.loghub.sdk.util.Argument;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class SerializerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected final Serializer serializer;
    protected final SerializerOperation operation;
    protected final Class<?> clazz;

    public SerializerException(Serializer serializer, SerializerOperation operation, String message) {
        super(String.format("[%s][%s]: %s", Argument.notNull("serializer", serializer), Argument.notNull("operation", operation), Argument.notNull("message", message)));

        this.serializer = serializer;
        this.operation = operation;
        this.clazz = null;
    }

    public SerializerException(Serializer serializer, SerializerOperation operation, String message, Throwable cause) {
        super(String.format("[%s][%s]: %s", Argument.notNull("serializer", serializer), Argument.notNull("operation", operation), Argument.notNull("message", message)), cause);

        this.serializer = serializer;
        this.operation = operation;
        this.clazz = null;
    }

    public SerializerException(Serializer serializer, SerializerOperation operation, Class<?> clazz) {
        super(String.format("[%s][%s]: %s", Argument.notNull("serializer", serializer), Argument.notNull("operation", operation), Argument.notNull("clazz", clazz).getCanonicalName()));

        this.serializer = serializer;
        this.operation = operation;
        this.clazz = clazz;
    }

    public SerializerException(Serializer serializer, SerializerOperation operation, Class<?> clazz, Throwable cause) {
        super(String.format("[%s][%s]: %s", Argument.notNull("serializer", serializer), Argument.notNull("operation", operation), Argument.notNull("clazz", clazz).getCanonicalName()), cause);

        this.serializer = serializer;
        this.operation = operation;
        this.clazz = clazz;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public SerializerOperation getOperation() {
        return operation;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
