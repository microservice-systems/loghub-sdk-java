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

import java.io.Serializable;
import java.util.Map;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;

    public String id;
    public long time;
    public String platform;
    public String environment;
    public String application;
    public String version;
    public String instance;
    public String instanceHost;
    public String instanceIp;
    public String process;
    public long processId;
    public long processStart;
    public String thread;
    public long threadId;
    public String threadName;
    public int threadPriority;
    public int level;
    public String levelName;
    public String type;
    public String logger;
    public String message;
    public Map<String, Object> tags;
    public int size;
    public long totalCount;
    public long totalSize;
    public long lostCount;
    public long lostSize;
}
