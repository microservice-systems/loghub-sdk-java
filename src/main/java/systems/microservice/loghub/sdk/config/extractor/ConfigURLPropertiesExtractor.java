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

package systems.microservice.loghub.sdk.config.extractor;

import systems.microservice.loghub.sdk.config.ConfigExtractor;
import systems.microservice.loghub.sdk.util.ByteArrayOutputStream;
import systems.microservice.loghub.sdk.util.PropertiesUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class ConfigURLPropertiesExtractor implements ConfigExtractor<URL, Map<String, String>> {
    private static final ConfigURLPropertiesExtractor instance = new ConfigURLPropertiesExtractor();

    private ConfigURLPropertiesExtractor() {
    }

    @Override
    public Map<String, String> extract(URL input, Class<Map<String, String>> outputClass) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(65536);
        try (ByteArrayOutputStream out1 = out) {
            try (InputStream in = input.openStream()) {
                byte[] b = new byte[65536];
                for (int n = in.read(b); n > 0; n = in.read(b)) {
                    out1.write(b, 0, n);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Properties ps = new Properties();
        try (StringReader r = new StringReader(out.toString())) {
            ps.load(r);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return PropertiesUtil.toMap(ps);
    }

    public static ConfigURLPropertiesExtractor getInstance() {
        return instance;
    }
}
