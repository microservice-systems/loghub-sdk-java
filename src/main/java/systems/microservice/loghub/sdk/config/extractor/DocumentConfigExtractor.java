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
import systems.microservice.loghub.sdk.config.ConfigExtractor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class DocumentConfigExtractor implements ConfigExtractor<String, Document> {
    private static final DocumentConfigExtractor instance = new DocumentConfigExtractor();

    private DocumentConfigExtractor() {
    }

    @Override
    public Document extract(String input, Class<Document> outputClass) {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            try (StringReader r = new StringReader(input)) {
                InputSource is = new InputSource(r);
                return b.parse(is);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static DocumentConfigExtractor getInstance() {
        return instance;
    }
}
