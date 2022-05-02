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

package systems.microservice.loghub;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public enum Platform {
    ANDROID((byte) 1),
    C((byte) 2),
    CPP((byte) 3),
    GO((byte) 4),
    JAVA((byte) 5),
    JS((byte) 6),
    NET((byte) 7),
    PHP((byte) 8),
    PYTHON((byte) 9),
    RUBY((byte) 10),
    SWIFT((byte) 11);

    private static final Map<Byte, Platform> platforms = createPlatforms();

    public final byte id;

    Platform(byte id) {
        this.id = id;
    }

    private static Map<Byte, Platform> createPlatforms() {
        Map<Byte, Platform> ls = new HashMap<>(16);
        ls.put(ANDROID.id, ANDROID);
        ls.put(C.id, C);
        ls.put(CPP.id, CPP);
        ls.put(GO.id, GO);
        ls.put(JAVA.id, JAVA);
        ls.put(JS.id, JS);
        ls.put(NET.id, NET);
        ls.put(PHP.id, PHP);
        ls.put(PYTHON.id, PYTHON);
        ls.put(RUBY.id, RUBY);
        ls.put(SWIFT.id, SWIFT);
        return ls;
    }

    public static Platform get(byte id) {
        return platforms.get(id);
    }
}
