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

package systems.microservice.loghub.sdk.metric;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class Metric implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String name;
    public final long time;
    public final String platform;
    public final String environment;
    public final String application;
    public final String version;
    public final String instance;
    public final String logger;
    public final long count;
    public final double sum;
    public final double min;
    public final double max;

    public Metric(String name, long time, String platform, String environment, String application, String version, String instance, String logger, long count, double sum, double min, double max) {
        this.name = name;
        this.time = time;
        this.platform = platform;
        this.environment = environment;
        this.application = application;
        this.version = version;
        this.instance = instance;
        this.logger = logger;
        this.count = count;
        this.sum = sum;
        this.min = min;
        this.max = max;
    }
}
