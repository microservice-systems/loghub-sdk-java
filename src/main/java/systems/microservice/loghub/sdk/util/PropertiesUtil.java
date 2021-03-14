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

package systems.microservice.loghub.sdk.util;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class PropertiesUtil {
    public static final String APPLICATION_PROPERTIES = "application/properties";

    private PropertiesUtil() {
    }

    public static String getProperty(Properties properties, String key) {
        String property = properties.getProperty(key);
        if (property != null) {
            return property;
        } else {
            throw new RuntimeException(String.format("Property '%s' is not found", key));
        }
    }

    public static Properties toProperties(String[][] properties) {
        Properties props = new Properties();
        for (String[] property : properties) {
            props.setProperty(property[0], property[1]);
        }
        return props;
    }

    public static byte[] serialize(Properties properties, String comment, boolean xml) {
        try {
            ByteArrayOutputStream d = new ByteArrayOutputStream(16384);
            try (ByteArrayOutputStream d1 = d) {
                if (xml) {
                    properties.storeToXML(d1, comment, "UTF-8");
                } else {
                    try (Writer w = new OutputStreamWriter(d1, StandardCharsets.UTF_8)) {
                        try (BufferedWriter bw = new BufferedWriter(w)) {
                            properties.store(bw, comment);
                        }
                    }
                }
            }
            return d.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Properties deserialize(byte[] data, Properties defaults) {
        try {
            Properties p = new Properties(defaults);
            try (java.io.ByteArrayInputStream d = new ByteArrayInputStream(data)) {
                try (Reader r = new InputStreamReader(d, StandardCharsets.UTF_8)) {
                    p.load(r);
                }
            }
            return p;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
