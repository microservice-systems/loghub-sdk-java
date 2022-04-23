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

package systems.microservice.loghub.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class Property {
    private Property() {
    }

    private static String getVariable(String variable) {
        if (variable != null) {
            String v = System.getenv(variable);
            if (Validator.isPresent(v)) {
                return v;
            }
        }
        return null;
    }

    private static String getProperty(String property) {
        if (property != null) {
            String p = System.getProperty(property);
            if (Validator.isPresent(p)) {
                return p;
            }
        }
        return null;
    }

    private static String getResource(String resource) {
        if (resource != null) {
            String r = Resource.getString(resource);
            if (Validator.isPresent(r)) {
                return r;
            }
        }
        return null;
    }

    public static String get(String variable, String property, String resource, String defaultValue) {
        String p = getVariable(variable);
        if (p == null) {
            p = getProperty(property);
            if (p == null) {
                p = getResource(resource);
                if (p == null) {
                    p = defaultValue;
                }
            }
        }
        return p;
    }

    public static String[] getArray(String variable, String property, String resource, String[] defaultValue) {
        String p = get(variable, property, resource, null);
        if (p != null) {
            p = p.trim();
            if (p.startsWith("{")) {
                if (p.endsWith("}")) {
                    String[] a = p.substring(1, p.length() - 1).split(",");
                    for (int i = 0, ci = a.length; i < ci; ++i) {
                        a[i] = a[i].trim();
                    }
                    return a;
                } else {
                    throw new RuntimeException(String.format("Array '%s' is not ended with '}'", p));
                }
            } else {
                throw new RuntimeException(String.format("Array '%s' is not started with '{'", p));
            }
        } else {
            return defaultValue;
        }
    }

    public static List<String> getList(String variable, String property, String resource, List<String> defaultValue) {
        String p = get(variable, property, resource, null);
        if (p != null) {
            p = p.trim();
            if (p.startsWith("{")) {
                if (p.endsWith("}")) {
                    String[] a = p.substring(1, p.length() - 1).split(",");
                    for (int i = 0, ci = a.length; i < ci; ++i) {
                        a[i] = a[i].trim();
                    }
                    return Arrays.asList(a);
                } else {
                    throw new RuntimeException(String.format("List '%s' is not ended with '}'", p));
                }
            } else {
                throw new RuntimeException(String.format("List '%s' is not started with '{'", p));
            }
        } else {
            return defaultValue;
        }
    }

    public static Map<String, String> getMap(String variable, String property, String resource, Map<String, String> defaultValue) {
        String p = get(variable, property, resource, null);
        if (p != null) {
            p = p.trim();
            if (p.startsWith("{")) {
                if (p.endsWith("}")) {
                    String[] a = p.substring(1, p.length() - 1).split(",");
                    LinkedHashMap<String, String> m = new LinkedHashMap<>(a.length);
                    for (int i = 0, ci = a.length; i < ci; ++i) {
                        String[] e = a[i].trim().split(":");
                        if (e.length == 2) {
                            String k = e[0].trim();
                            String v = e[1].trim();
                            m.put(k, v);
                        } else {
                            throw new RuntimeException(String.format("Entry %d '%s' in map '%s' is not split with ':'", i, a[i], p));
                        }
                    }
                    return Collections.unmodifiableMap(m);
                } else {
                    throw new RuntimeException(String.format("Map '%s' is not ended with '}'", p));
                }
            } else {
                throw new RuntimeException(String.format("Map '%s' is not started with '{'", p));
            }
        } else {
            return defaultValue;
        }
    }
}
