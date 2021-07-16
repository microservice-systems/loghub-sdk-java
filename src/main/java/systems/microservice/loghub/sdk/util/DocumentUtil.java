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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class DocumentUtil {
    private DocumentUtil() {
    }

    public static String getAttribute(Element element, String attribute) {
        Argument.notNull("element", element);
        Argument.notNull("attribute", attribute);

        Attr a = element.getAttributeNode(attribute);
        if (a != null) {
            return a.getValue();
        } else {
            throw new RuntimeException(String.format("Attribute '%s' is not found in XML element '%s'", attribute, element.getTagName()));
        }
    }

    public static String getAttribute(Element element, String attribute, String defaultValue) {
        Argument.notNull("element", element);
        Argument.notNull("attribute", attribute);

        Attr a = element.getAttributeNode(attribute);
        if (a != null) {
            return a.getValue();
        } else {
            return defaultValue;
        }
    }

    public static byte[] serialize(Document document) {
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            Source s = new DOMSource(document);
            ByteArrayOutputStream d = new ByteArrayOutputStream(65536);
            try (ByteArrayOutputStream d1 = d) {
                Result r = new StreamResult(d1);
                t.transform(s, r);
            }
            return d.toByteArray();
        } catch (IOException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public static Document deserialize(byte[] data) {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder bld = f.newDocumentBuilder();
            try (ByteArrayInputStream d = new ByteArrayInputStream(data)) {
                InputSource is = new InputSource(d);
                return bld.parse(is);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
