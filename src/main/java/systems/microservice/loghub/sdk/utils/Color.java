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

package systems.microservice.loghub.sdk.utils;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class Color implements Serializable {
    private static final long serialVersionUID = 1L;

    public final short r;
    public final short g;
    public final short b;
    public final short a;

    public Color(short r, short g, short b, short a) {
        Argument.inRangeShort("r", r, (short) 0, (short) 255);
        Argument.inRangeShort("g", g, (short) 0, (short) 255);
        Argument.inRangeShort("b", b, (short) 0, (short) 255);
        Argument.inRangeShort("a", a, (short) 0, (short) 255);

        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    @Override
    public String toString() {
        return String.format("#%x%x%x%x", r, g, b, a);
    }
}
