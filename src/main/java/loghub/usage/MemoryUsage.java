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

package systems.microservice.loghub.usage;

import com.sun.management.OperatingSystemMXBean;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class MemoryUsage implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final OperatingSystemMXBean OS_MX_BEAN = createOperatingSystemMXBean();
    private static final MemoryMXBean MEMORY_MX_BEAN = ManagementFactory.getMemoryMXBean();

    public final long total;
    public final long free;
    public final long heapInit;
    public final long heapUsed;
    public final long heapCommitted;
    public final long heapMax;
    public final long nonheapInit;
    public final long nonheapUsed;
    public final long nonheapCommitted;
    public final long nonheapMax;
    public final int objectPendingFinalization;

    public MemoryUsage() {
        long mb = 1048576L;
        java.lang.management.MemoryUsage hmu = MEMORY_MX_BEAN.getHeapMemoryUsage();
        java.lang.management.MemoryUsage nhmu = MEMORY_MX_BEAN.getNonHeapMemoryUsage();

        this.total = (OS_MX_BEAN != null) ? OS_MX_BEAN.getTotalPhysicalMemorySize() / mb : -1L;
        this.free = (OS_MX_BEAN != null) ? OS_MX_BEAN.getFreePhysicalMemorySize() / mb : -1L;
        this.heapInit = hmu.getInit() / mb;
        this.heapUsed = hmu.getUsed() / mb;
        this.heapCommitted = hmu.getCommitted() / mb;
        this.heapMax = hmu.getMax() / mb;
        this.nonheapInit = nhmu.getInit() / mb;
        this.nonheapUsed = nhmu.getUsed() / mb;
        this.nonheapCommitted = nhmu.getCommitted() / mb;
        this.nonheapMax = nhmu.getMax() / mb;
        this.objectPendingFinalization = MEMORY_MX_BEAN.getObjectPendingFinalizationCount();
    }

    private static OperatingSystemMXBean createOperatingSystemMXBean() {
        java.lang.management.OperatingSystemMXBean osb = ManagementFactory.getOperatingSystemMXBean();
        if (osb instanceof OperatingSystemMXBean) {
            return (OperatingSystemMXBean) osb;
        } else {
            return null;
        }
    }
}
