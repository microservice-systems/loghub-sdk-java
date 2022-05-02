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

package systems.microservice.loghub.event;

import systems.microservice.loghub.config.Validator;
import systems.microservice.loghub.io.FormatInputStream;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class EventUsage implements Serializable {
    private static final long serialVersionUID = 1L;

    public final CPU cpu;
    public final Memory memory;
    public final Disk disk;
    public final Network network;
    public final Class clazz;
    public final Thread thread;
    public final Descriptor descriptor;
    public final GC gc;

    public EventUsage(FormatInputStream input) {
        Validator.notNull("input", input);

        this.cpu = null;
        this.memory = null;
        this.disk = null;
        this.network = null;
        this.clazz = null;
        this.thread = null;
        this.descriptor = null;
        this.gc = null;
    }

    public EventUsage(CPU cpu, Memory memory, Disk disk, Network network, Class clazz, Thread thread, Descriptor descriptor, GC gc) {
        this.cpu = cpu;
        this.memory = memory;
        this.disk = disk;
        this.network = network;
        this.clazz = clazz;
        this.thread = thread;
        this.descriptor = descriptor;
        this.gc = gc;
    }

    public static final class CPU implements Serializable {
        private static final long serialVersionUID = 1L;

        public final int count;
        public final float m01;
        public final float m05;
        public final float m15;
        public final int entityActive;
        public final int entityTotal;

        public CPU(FormatInputStream input) {
            Validator.notNull("input", input);

            this.count = 0;
            this.m01 = 0.0f;
            this.m05 = 0.0f;
            this.m15 = 0.0f;
            this.entityActive = 0;
            this.entityTotal = 0;
        }

        public CPU(int count, float m01, float m05, float m15, int entityActive, int entityTotal) {
            this.count = count;
            this.m01 = m01;
            this.m05 = m05;
            this.m15 = m15;
            this.entityActive = entityActive;
            this.entityTotal = entityTotal;
        }
    }

    public static abstract class Memory implements Serializable {
        private static final long serialVersionUID = 1L;

        public final long total;
        public final long free;

        protected Memory(FormatInputStream input) {
            Validator.notNull("input", input);

            this.total = 0L;
            this.free = 0L;
        }

        protected Memory(long total, long free) {
            this.total = total;
            this.free = free;
        }
    }

    public static final class MemoryJava extends Memory {
        private static final long serialVersionUID = 1L;

        public final long heapInit;
        public final long heapUsed;
        public final long heapCommitted;
        public final long heapMax;
        public final long nonheapInit;
        public final long nonheapUsed;
        public final long nonheapCommitted;
        public final long nonheapMax;
        public final int objectPendingFinalization;

        public MemoryJava(FormatInputStream input) {
            super(input);

            this.heapInit = 0L;
            this.heapUsed = 0L;
            this.heapCommitted = 0L;
            this.heapMax = 0L;
            this.nonheapInit = 0L;
            this.nonheapUsed = 0L;
            this.nonheapCommitted = 0L;
            this.nonheapMax = 0L;
            this.objectPendingFinalization = 0;
        }

        public MemoryJava(long total, long free, long heapInit, long heapUsed, long heapCommitted, long heapMax, long nonheapInit, long nonheapUsed, long nonheapCommitted, long nonheapMax, int objectPendingFinalization) {
            super(total, free);

            this.heapInit = heapInit;
            this.heapUsed = heapUsed;
            this.heapCommitted = heapCommitted;
            this.heapMax = heapMax;
            this.nonheapInit = nonheapInit;
            this.nonheapUsed = nonheapUsed;
            this.nonheapCommitted = nonheapCommitted;
            this.nonheapMax = nonheapMax;
            this.objectPendingFinalization = objectPendingFinalization;
        }
    }

    public static final class Disk implements Serializable {
        private static final long serialVersionUID = 1L;

        public final long total;
        public final long free;
        public final long usable;

        public Disk(FormatInputStream input) {
            Validator.notNull("input", input);

            this.total = 0L;
            this.free = 0L;
            this.usable = 0L;
        }

        public Disk(long total, long free, long usable) {
            this.total = total;
            this.free = free;
            this.usable = usable;
        }
    }

    public static final class Network implements Serializable {
        private static final long serialVersionUID = 1L;

        public final Map<String, Interface> interfaces;

        public Network(FormatInputStream input) {
            Validator.notNull("input", input);

            this.interfaces = null;
        }

        public Network(Map<String, Interface> interfaces) {
            Validator.notNull("interfaces", interfaces);

            this.interfaces = interfaces;
        }

        public static final class Interface implements Serializable {
            private static final long serialVersionUID = 1L;

            public final String name;
            public final Receive receive;
            public final Transmit transmit;

            public Interface(FormatInputStream input) {
                Validator.notNull("input", input);

                this.name = null;
                this.receive = null;
                this.transmit = null;
            }

            public Interface(String name, Receive receive, Transmit transmit) {
                Validator.notNull("name", name);
                Validator.notNull("receive", receive);
                Validator.notNull("transmit", transmit);

                this.name = name;
                this.receive = receive;
                this.transmit = transmit;
            }

            public static final class Receive implements Serializable {
                private static final long serialVersionUID = 1L;

                public final long bytes;
                public final long packets;

                public Receive(FormatInputStream input) {
                    Validator.notNull("input", input);

                    this.bytes = 0L;
                    this.packets = 0L;
                }

                public Receive(long bytes, long packets) {
                    Validator.inRangeLong("bytes", bytes, 0L, Long.MAX_VALUE);
                    Validator.inRangeLong("packets", packets, 0L, Long.MAX_VALUE);

                    this.bytes = bytes;
                    this.packets = packets;
                }
            }

            public static final class Transmit implements Serializable {
                private static final long serialVersionUID = 1L;

                public final long bytes;
                public final long packets;

                public Transmit(FormatInputStream input) {
                    Validator.notNull("input", input);

                    this.bytes = 0L;
                    this.packets = 0L;
                }

                public Transmit(long bytes, long packets) {
                    Validator.inRangeLong("bytes", bytes, 0L, Long.MAX_VALUE);
                    Validator.inRangeLong("packets", packets, 0L, Long.MAX_VALUE);

                    this.bytes = bytes;
                    this.packets = packets;
                }
            }
        }
    }

    public static final class Class implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    public static final class Thread implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    public static final class Descriptor implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    public static abstract class GC implements Serializable {
        private static final long serialVersionUID = 1L;
    }
}
