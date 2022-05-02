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

package loghub;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public enum Order {
    DIRECT((byte) 1),
    REVERSE((byte) 2);

    private static final Map<Byte, Order> orders = createOrders();

    public final byte id;

    Order(byte id) {
        this.id = id;
    }

    private static Map<Byte, Order> createOrders() {
        Map<Byte, Order> os = new HashMap<>(2);
        os.put(DIRECT.id, DIRECT);
        os.put(REVERSE.id, REVERSE);
        return os;
    }

    public static Order get(byte id) {
        return orders.get(id);
    }
}
