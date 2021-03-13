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

package systems.microservice.loghub.sdk.v1;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class LogBlock implements AutoCloseable {
    private static final ThreadLocal<ThreadInfo> threadInfo = createThreadInfo();

    public final String logger;
    public final String name;
    public final LogTag[] tags;
    public final long begin;

    public LogBlock(String logger, String name, LogTag... tags) {
        this.logger = logger;
        this.name = name;
        this.tags = tags;
        this.begin = System.currentTimeMillis();
    }

    @Override
    public void close() throws Exception {
    }

    private static ThreadLocal<ThreadInfo> createThreadInfo() {
        return new ThreadLocal<ThreadInfo>() {
            @Override
            protected ThreadInfo initialValue() {
                return new ThreadInfo();
            }
        };
    }

    private static final class ThreadInfo {
        public int shift = 0;

        public ThreadInfo() {
        }
    }
}
