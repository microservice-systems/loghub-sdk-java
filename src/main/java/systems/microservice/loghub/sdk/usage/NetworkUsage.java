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

import systems.microservice.loghub.sdk.util.Argument;
import systems.microservice.loghub.sdk.util.StringUtil;
import systems.microservice.loghub.sdk.util.ValidationUtil;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class NetworkUsage implements Serializable {
    private static final long serialVersionUID = 1L;

    public final Map<String, Interface> interfaces;

    public NetworkUsage() {
        this.interfaces = createInterfaces();
    }

    private static Map<String, Interface> createInterfaces() {
        try {
            String dev = StringUtil.load("/proc/self/net/dev", null);
            if (dev != null) {
                String[] devs = dev.split("\n");
                LinkedHashMap<String, Interface> is = new LinkedHashMap<>(devs.length);
                for (int i = 2, ci = devs.length; i < ci; ++i) {
                    String d = devs[i];
                    String n = null;
                    int j = 0;
                    int k = 0;
                    int cj = d.length();
                    for (; j < cj; ++j) {
                        if (d.charAt(j) == ':') {
                            n = d.substring(k, j).trim();
                            ++j;
                            break;
                        }
                    }
                    if (n != null) {
                        long rbs = 0L;
                        long rps = 0L;
                        long tbs = 0L;
                        long tps = 0L;
                        j = StringUtil.skipChars(d, j, ' ');
                        k = j;
                        j = StringUtil.skipDigits(d, j);
                        rbs = Long.parseLong(d.substring(k, j));
                        j = StringUtil.skipChars(d, j, ' ');
                        k = j;
                        j = StringUtil.skipDigits(d, j);
                        rps = Long.parseLong(d.substring(k, j));
                        j = StringUtil.skipChars(d, j, ' ');
                        j = StringUtil.skipDigits(d, j);
                        j = StringUtil.skipChars(d, j, ' ');
                        j = StringUtil.skipDigits(d, j);
                        j = StringUtil.skipChars(d, j, ' ');
                        j = StringUtil.skipDigits(d, j);
                        j = StringUtil.skipChars(d, j, ' ');
                        j = StringUtil.skipDigits(d, j);
                        j = StringUtil.skipChars(d, j, ' ');
                        j = StringUtil.skipDigits(d, j);
                        j = StringUtil.skipChars(d, j, ' ');
                        j = StringUtil.skipDigits(d, j);
                        j = StringUtil.skipChars(d, j, ' ');
                        k = j;
                        j = StringUtil.skipDigits(d, j);
                        tbs = Long.parseLong(d.substring(k, j));
                        j = StringUtil.skipChars(d, j, ' ');
                        k = j;
                        j = StringUtil.skipDigits(d, j);
                        tps = Long.parseLong(d.substring(k, j));
                        is.put(n, new Interface(n, new Interface.Receive(rbs, rps), new Interface.Transmit(tbs, tps)));
                    }
                }
                return Collections.unmodifiableMap(is);
            } else {
                return Collections.emptyMap();
            }
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    public static final class Interface implements Serializable {
        private static final long serialVersionUID = 1L;

        public final String name;
        public final Receive receive;
        public final Transmit transmit;

        public Interface(String name, Receive receive, Transmit transmit) {
            Argument.notNull("name", name);
            Argument.notNull("receive", receive);
            Argument.notNull("transmit", transmit);

            this.name = name;
            this.receive = receive;
            this.transmit = transmit;
        }

        public static final class Receive implements Serializable {
            private static final long serialVersionUID = 1L;

            public final long bytes;
            public final long packets;

            public Receive(long bytes, long packets) {
                Argument.inRangeLong("bytes", bytes, 0L, Long.MAX_VALUE);
                Argument.inRangeLong("packets", packets, 0L, Long.MAX_VALUE);

                this.bytes = bytes;
                this.packets = packets;
            }
        }

        public static final class Transmit implements Serializable {
            private static final long serialVersionUID = 1L;

            public final long bytes;
            public final long packets;

            public Transmit(long bytes, long packets) {
                Argument.inRangeLong("bytes", bytes, 0L, Long.MAX_VALUE);
                Argument.inRangeLong("packets", packets, 0L, Long.MAX_VALUE);

                this.bytes = bytes;
                this.packets = packets;
            }
        }
    }

    public static void main(String[] args) {
        UUID id = UUID.randomUUID();
        System.out.println(id);
        boolean f = ValidationUtil.isId(id.toString().substring(0, 36).replace('f', 'b'));
        System.out.println(f);
    }
}
