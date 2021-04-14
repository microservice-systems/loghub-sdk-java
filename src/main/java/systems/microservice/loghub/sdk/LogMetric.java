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

package systems.microservice.loghub.sdk;

import systems.microservice.loghub.sdk.util.Argument;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class LogMetric implements Serializable {
    private static final long serialVersionUID = 1L;

    protected final String name;
    protected final int point;
    protected final String unit;

    public LogMetric(String name, int point, String unit) {
        Argument.notNull("name", name);
        Argument.inRangeInt("point", point, 0, 15);
        Argument.notNull("unit", unit);

        this.name = name;
        this.point = point;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public int getPoint() {
        return point;
    }

    public String getUnit() {
        return unit;
    }

    public void log(long value) {
        LogHub.logMetric(name, value, point, unit);
    }

    public void log(long count, long value) {
        LogHub.logMetric(name, count, value, point, unit);
    }
}
