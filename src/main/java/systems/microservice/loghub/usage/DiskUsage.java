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

import java.io.File;
import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class DiskUsage implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final File ROOT_DISK = new File("/");

    public final long total;
    public final long free;
    public final long usable;

    public DiskUsage() {
        long mb = 1048576L;

        this.total = ROOT_DISK.getTotalSpace() / mb;
        this.free = ROOT_DISK.getFreeSpace() / mb;
        this.usable = ROOT_DISK.getUsableSpace() / mb;
    }
}
