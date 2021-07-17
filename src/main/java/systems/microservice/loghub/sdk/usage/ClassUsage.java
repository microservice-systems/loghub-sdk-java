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

package systems.microservice.loghub.sdk.usage;

import java.io.Serializable;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class ClassUsage implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final ClassLoadingMXBean CLASS_LOADING_MX_BEAN = ManagementFactory.getClassLoadingMXBean();

    public final int active;
    public final long loaded;
    public final long unloaded;

    public ClassUsage() {
        this.active = CLASS_LOADING_MX_BEAN.getLoadedClassCount();
        this.loaded = CLASS_LOADING_MX_BEAN.getTotalLoadedClassCount();
        this.unloaded = CLASS_LOADING_MX_BEAN.getUnloadedClassCount();
    }
}
