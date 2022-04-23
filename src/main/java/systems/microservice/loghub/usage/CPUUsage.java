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

package systems.microservice.loghub.facade.usage;

import systems.microservice.loghub.facade.util.StringUtil;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class CPUUsage implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Runtime RUNTIME = Runtime.getRuntime();

    public final int count;
    public final float m01;
    public final float m05;
    public final float m15;
    public final int entityActive;
    public final int entityTotal;

    public CPUUsage() {
        String avg = StringUtil.load("/proc/loadavg", "0.0 0.0 0.0 0/0 0");
        String[] avgs = avg.split(" ");
        String[] ents = avgs[3].split("/");

        this.count = RUNTIME.availableProcessors();
        this.m01 = Float.parseFloat(avgs[0]);
        this.m05 = Float.parseFloat(avgs[1]);
        this.m15 = Float.parseFloat(avgs[2]);
        this.entityActive = Integer.parseInt(ents[0]);
        this.entityTotal = Integer.parseInt(ents[1]);
    }
}
