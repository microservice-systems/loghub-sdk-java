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

import com.sun.management.UnixOperatingSystemMXBean;

import java.io.Serializable;
import java.lang.management.ManagementFactory;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class LogDescriptorUsage implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final UnixOperatingSystemMXBean OS_MX_BEAN = createOperatingSystemMXBean();

    public final long fileMax;
    public final long fileOpen;

    public LogDescriptorUsage() {
        this.fileMax = (OS_MX_BEAN != null) ? OS_MX_BEAN.getMaxFileDescriptorCount() : -1L;
        this.fileOpen = (OS_MX_BEAN != null) ? OS_MX_BEAN.getOpenFileDescriptorCount() : -1L;
    }

    private static UnixOperatingSystemMXBean createOperatingSystemMXBean() {
        java.lang.management.OperatingSystemMXBean osb = ManagementFactory.getOperatingSystemMXBean();
        if (osb instanceof UnixOperatingSystemMXBean) {
            return (UnixOperatingSystemMXBean) osb;
        } else {
            return null;
        }
    }
}
