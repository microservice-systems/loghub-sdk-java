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

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class LogMemoryUsage implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final MemoryMXBean MEMORY_MX_BEAN = ManagementFactory.getMemoryMXBean();

    public final long heapInit;
    public final long heapUsed;
    public final long heapCommitted;
    public final long heapMax;
    public final long nonHeapInit;
    public final long nonHeapUsed;
    public final long nonHeapCommitted;
    public final long nonHeapMax;
    public final int objectPendingFinalization;

    public LogMemoryUsage() {
        long mb = 1048576L;
        MemoryUsage hmu = MEMORY_MX_BEAN.getHeapMemoryUsage();
        MemoryUsage nhmu = MEMORY_MX_BEAN.getNonHeapMemoryUsage();

        this.heapInit = hmu.getInit() / mb;
        this.heapUsed = hmu.getUsed() / mb;
        this.heapCommitted = hmu.getCommitted() / mb;
        this.heapMax = hmu.getMax() / mb;
        this.nonHeapInit = nhmu.getInit() / mb;
        this.nonHeapUsed = nhmu.getUsed() / mb;
        this.nonHeapCommitted = nhmu.getCommitted() / mb;
        this.nonHeapMax = nhmu.getMax() / mb;
        this.objectPendingFinalization = MEMORY_MX_BEAN.getObjectPendingFinalizationCount();
    }
}
