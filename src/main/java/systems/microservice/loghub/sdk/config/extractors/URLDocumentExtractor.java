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

package systems.microservice.loghub.sdk.config.extractors;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import systems.microservice.loghub.sdk.config.Extractor;
import systems.microservice.loghub.sdk.util.ByteArrayOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class URLDocumentExtractor implements Extractor<URL, Document> {
    private static final URLDocumentExtractor instance = new URLDocumentExtractor();

    private URLDocumentExtractor() {
    }

    @Override
    public Document extract(URL input, Class<Document> outputClass) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(65536);
            try (ByteArrayOutputStream out1 = out) {
                try (InputStream in = input.openStream()) {
                    byte[] b = new byte[65536];
                    for (int n = in.read(b); n > 0; n = in.read(b)) {
                        out1.write(b, 0, n);
                    }
                }
            }
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            try (StringReader r = new StringReader(out.toString())) {
                InputSource is = new InputSource(r);
                return b.parse(is);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static URLDocumentExtractor getInstance() {
        return instance;
    }
}
