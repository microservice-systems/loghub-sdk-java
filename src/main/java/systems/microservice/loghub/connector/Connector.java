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

package systems.microservice.loghub.connector;

import systems.microservice.loghub.Blob;
import systems.microservice.loghub.Image;
import systems.microservice.loghub.Input;
import systems.microservice.loghub.Level;
import systems.microservice.loghub.Tag;
import systems.microservice.loghub.Type;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public interface Connector {
    public String getInfo();
    public boolean isEnabled(Level level);
    public void log(long time, long number, Input input,
                    String clazz, String method, String statement, String file, int line,
                    Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags,
                    String message);
    public void log(long time, long number, Input input,
                    String clazz, String method, String statement, String file, int line,
                    Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags,
                    String message, Object param1);
    public void log(long time, long number, Input input,
                    String clazz, String method, String statement, String file, int line,
                    Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags,
                    String message, Object param1, Object param2);
    public void log(long time, long number, Input input,
                    String clazz, String method, String statement, String file, int line,
                    Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags,
                    String message, Object param1, Object param2, Object param3);
    public void log(long time, long number, Input input,
                    String clazz, String method, String statement, String file, int line,
                    Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags,
                    String message, Object param1, Object param2, Object param3, Object param4);
    public void log(long time, long number, Input input,
                    String clazz, String method, String statement, String file, int line,
                    Level level, String logger, Type type, Throwable exception, Tag tag, Tag[] tags,
                    String message, Object param1, Object param2, Object param3, Object param4, Object param5);
    public void collect(String metric, long count, long value, int precision, String unit);
}
