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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class ManifestUtil {
    private static final Map<String, String> attributes;

    private ManifestUtil() {
    }

    public static String findAttribute(String key) {
        return attributes.get(key);
    }

    public static String getAttribute(String key) {
        String v = findAttribute(key);
        if (v != null) {
            return v;
        } else {
            throw new RuntimeException(String.format("Attribute with key '%s' is not found", key));
        }
    }

    public static String getAttribute(String key, String value) {
        String v = findAttribute(key);
        if (v != null) {
            return v;
        } else {
            return value;
        }
    }

    public static Map<String, String> getAttributes() {
        return attributes;
    }

    static {
        try {
            LinkedHashMap<String, String> attrs = new LinkedHashMap<>(256);
            Enumeration<URL> resources = ManifestUtil.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                try (InputStream in = resources.nextElement().openStream()) {
                    Manifest manifest = new Manifest(in);
                    for (Map.Entry<Object, Object> attr : manifest.getMainAttributes().entrySet()) {
                        Object key = attr.getKey();
                        Object value = attr.getValue();
                        if (key instanceof Attributes.Name && value instanceof String) {
                            attrs.put(key.toString(), value.toString());
                        }
                    }
                }
            }
            attributes = Collections.unmodifiableMap(attrs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
