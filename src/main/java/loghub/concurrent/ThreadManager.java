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

package systems.microservice.loghub.concurrent;

import systems.microservice.loghub.config.Validator;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class ThreadManager {
    private static final AtomicBoolean alive = new AtomicBoolean(true);
    private static final AtomicBoolean terminated = new AtomicBoolean(false);
    private static final Semaphore sema = createSema();
    private static final AtomicLong count = new AtomicLong(0L);
    private static final Thread shutdownThread = createShutdownThread();

    private ThreadManager() {
    }

    private static Semaphore createSema() {
        try {
            Semaphore s = new Semaphore(1, false);
            s.acquire();
            return s;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Thread createShutdownThread() {
        Thread st = new Thread("loghub-shutdown-thread") {
            @Override
            public void run() {
                ThreadManager.shutdown();
                while (!ThreadManager.isTerminated()) {
                }
            }
        };
        st.setDaemon(false);
        Runtime.getRuntime().addShutdownHook(st);
        return st;
    }

    public static boolean isAlive() {
        return alive.get();
    }

    public static boolean isTerminated() {
        return terminated.get();
    }

    public static boolean sleep(long millis) {
        Validator.inRangeLong("millis", millis, 0L, Long.MAX_VALUE);

        if (alive.get()) {
            try {
                if (sema.tryAcquire(millis, TimeUnit.MILLISECONDS)) {
                    try {
                        return false;
                    } finally {
                        sema.release();
                    }
                } else {
                    return true;
                }
            } catch (InterruptedException e) {
                return true;
            }
        } else {
            return false;
        }
    }

    public static int sleep(long millis, long delay, AtomicBoolean cond1) {
        Validator.inRangeLong("millis", millis, 0L, Long.MAX_VALUE);
        Validator.inRangeLong("delay", delay, 0L, Long.MAX_VALUE);
        Validator.notNull("cond1", cond1);

        if (alive.get()) {
            for (long m = millis; m > 0L; m -= delay) {
                if (cond1.get()) {
                    return 1;
                } else {
                    try {
                        if (sema.tryAcquire(Math.min(m, delay), TimeUnit.MILLISECONDS)) {
                            try {
                                return -1;
                            } finally {
                                sema.release();
                            }
                        }
                    } catch (InterruptedException e) {
                        return 0;
                    }
                }
            }
            if (cond1.get()) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    public static int sleep(long millis, long delay, AtomicBoolean cond1, AtomicBoolean cond2) {
        Validator.inRangeLong("millis", millis, 0L, Long.MAX_VALUE);
        Validator.inRangeLong("delay", delay, 0L, Long.MAX_VALUE);
        Validator.notNull("cond1", cond1);
        Validator.notNull("cond2", cond2);

        if (alive.get()) {
            for (long m = millis; m > 0L; m -= delay) {
                if (cond1.get()) {
                    return 1;
                } else if (cond2.get()) {
                    return 2;
                } else {
                    try {
                        if (sema.tryAcquire(Math.min(m, delay), TimeUnit.MILLISECONDS)) {
                            try {
                                return -1;
                            } finally {
                                sema.release();
                            }
                        }
                    } catch (InterruptedException e) {
                        return 0;
                    }
                }
            }
            if (cond1.get()) {
                return 1;
            } else if (cond2.get()) {
                return 2;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    public static int sleep(long millis, long delay, AtomicBoolean cond1, AtomicBoolean cond2, AtomicBoolean cond3) {
        Validator.inRangeLong("millis", millis, 0L, Long.MAX_VALUE);
        Validator.inRangeLong("delay", delay, 0L, Long.MAX_VALUE);
        Validator.notNull("cond1", cond1);
        Validator.notNull("cond2", cond2);
        Validator.notNull("cond3", cond3);

        if (alive.get()) {
            for (long m = millis; m > 0L; m -= delay) {
                if (cond1.get()) {
                    return 1;
                } else if (cond2.get()) {
                    return 2;
                } else if (cond3.get()) {
                    return 3;
                } else {
                    try {
                        if (sema.tryAcquire(Math.min(m, delay), TimeUnit.MILLISECONDS)) {
                            try {
                                return -1;
                            } finally {
                                sema.release();
                            }
                        }
                    } catch (InterruptedException e) {
                        return 0;
                    }
                }
            }
            if (cond1.get()) {
                return 1;
            } else if (cond2.get()) {
                return 2;
            } else if (cond3.get()) {
                return 3;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    public static int sleep(long millis, long delay, AtomicBoolean cond1, AtomicBoolean cond2, AtomicBoolean cond3, AtomicBoolean cond4) {
        Validator.inRangeLong("millis", millis, 0L, Long.MAX_VALUE);
        Validator.inRangeLong("delay", delay, 0L, Long.MAX_VALUE);
        Validator.notNull("cond1", cond1);
        Validator.notNull("cond2", cond2);
        Validator.notNull("cond3", cond3);
        Validator.notNull("cond4", cond4);

        if (alive.get()) {
            for (long m = millis; m > 0L; m -= delay) {
                if (cond1.get()) {
                    return 1;
                } else if (cond2.get()) {
                    return 2;
                } else if (cond3.get()) {
                    return 3;
                } else if (cond4.get()) {
                    return 4;
                } else {
                    try {
                        if (sema.tryAcquire(Math.min(m, delay), TimeUnit.MILLISECONDS)) {
                            try {
                                return -1;
                            } finally {
                                sema.release();
                            }
                        }
                    } catch (InterruptedException e) {
                        return 0;
                    }
                }
            }
            if (cond1.get()) {
                return 1;
            } else if (cond2.get()) {
                return 2;
            } else if (cond3.get()) {
                return 3;
            } else if (cond4.get()) {
                return 4;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    public static int sleep(long millis, long delay, AtomicBoolean cond1, AtomicBoolean cond2, AtomicBoolean cond3, AtomicBoolean cond4, AtomicBoolean cond5) {
        Validator.inRangeLong("millis", millis, 0L, Long.MAX_VALUE);
        Validator.inRangeLong("delay", delay, 0L, Long.MAX_VALUE);
        Validator.notNull("cond1", cond1);
        Validator.notNull("cond2", cond2);
        Validator.notNull("cond3", cond3);
        Validator.notNull("cond4", cond4);
        Validator.notNull("cond5", cond5);

        if (alive.get()) {
            for (long m = millis; m > 0L; m -= delay) {
                if (cond1.get()) {
                    return 1;
                } else if (cond2.get()) {
                    return 2;
                } else if (cond3.get()) {
                    return 3;
                } else if (cond4.get()) {
                    return 4;
                } else if (cond5.get()) {
                    return 5;
                } else {
                    try {
                        if (sema.tryAcquire(Math.min(m, delay), TimeUnit.MILLISECONDS)) {
                            try {
                                return -1;
                            } finally {
                                sema.release();
                            }
                        }
                    } catch (InterruptedException e) {
                        return 0;
                    }
                }
            }
            if (cond1.get()) {
                return 1;
            } else if (cond2.get()) {
                return 2;
            } else if (cond3.get()) {
                return 3;
            } else if (cond4.get()) {
                return 4;
            } else if (cond5.get()) {
                return 5;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    public static boolean shutdown() {
        if (alive.get()) {
            if (alive.compareAndSet(true, false)) {
                sema.release();
                if (count.get() == 0L) {
                    terminated.compareAndSet(false, true);
                }
                return true;
            }
        }
        return false;
    }

    static void registerThread() {
        count.incrementAndGet();
    }

    static void deregisterThread() {
        long c = count.decrementAndGet();
        if (!alive.get()) {
            if (c == 0L) {
                terminated.compareAndSet(false, true);
            }
        }
    }
}
