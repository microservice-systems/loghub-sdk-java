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

package loghub;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class InputEventFilter implements Serializable {
    private static final long serialVersionUID = 1L;

    public final long begin;
    public final long end;

    public InputEventFilter(long begin, long end) {
        this.begin = begin;
        this.end = end;
    }

    public boolean match(long time, Input input,
                         String clazz, String method, String statement, String file, int line,
                         Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags,
                         String message) {
        return (time >= begin) && (time <= end);
    }

    public boolean match(long time, Input input,
                         String clazz, String method, String statement, String file, int line,
                         Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags,
                         String message, Object param1) {
        return match(time, input, clazz, method, statement, file, line, level, logger, type, exception, tag, tags, message);
    }

    public boolean match(long time, Input input,
                         String clazz, String method, String statement, String file, int line,
                         Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags,
                         String message, Object param1, Object param2) {
        return match(time, input, clazz, method, statement, file, line, level, logger, type, exception, tag, tags, message);
    }

    public boolean match(long time, Input input,
                         String clazz, String method, String statement, String file, int line,
                         Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags,
                         String message, Object param1, Object param2, Object param3) {
        return match(time, input, clazz, method, statement, file, line, level, logger, type, exception, tag, tags, message);
    }

    public boolean match(long time, Input input,
                         String clazz, String method, String statement, String file, int line,
                         Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags,
                         String message, Object param1, Object param2, Object param3, Object param4) {
        return match(time, input, clazz, method, statement, file, line, level, logger, type, exception, tag, tags, message);
    }

    public boolean match(long time, Input input,
                         String clazz, String method, String statement, String file, int line,
                         Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags,
                         String message, Object param1, Object param2, Object param3, Object param4, Object param5) {
        return match(time, input, clazz, method, statement, file, line, level, logger, type, exception, tag, tags, message);
    }

    public static InputEventFilter parse(String value) {
        if (value != null) {
            return new InputEventFilter(0L, Long.MAX_VALUE);
        } else {
            return null;
        }
    }
}
