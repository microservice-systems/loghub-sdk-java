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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class ConfigURLDataExtractor implements ConfigExtractor<URL, byte[]> {
    private static final ConfigURLDataExtractor instance = new ConfigURLDataExtractor();

    private ConfigURLDataExtractor() {
    }

    @Override
    public byte[] extract(URL input, Class<byte[]> outputClass) {
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
        return out.toByteArray();
    }

    public static ConfigURLDataExtractor getInstance() {
        return instance;
    }
}
