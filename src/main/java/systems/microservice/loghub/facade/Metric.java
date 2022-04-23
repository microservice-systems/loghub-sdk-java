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

package systems.microservice.loghub.facade;

import systems.microservice.loghub.facade.config.Validator;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class Metric implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String metric;
    public final int precision;
    public final String unit;

    public Metric(String metric) {
        this(metric, 0);
    }

    public Metric(String metric, int precision) {
        this(metric, precision, null);
    }

    public Metric(String metric, int precision, String unit) {
        Validator.notNull("metric", metric);
        Validator.inRangeInt("precision", precision, 0, 14);

        this.metric = metric;
        this.precision = precision;
        this.unit = unit;
    }
}
