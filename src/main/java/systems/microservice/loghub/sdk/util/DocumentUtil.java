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
import org.w3c.dom.Element;
import systems.microservice.loghub.connector.Validation;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class DocumentUtil {
    private DocumentUtil() {
    }

    public static String getAttribute(Element element, String attribute) {
        Validation.notNull("element", element);
        Validation.notNull("attribute", attribute);

        Attr a = element.getAttributeNode(attribute);
        if (a != null) {
            return a.getValue();
        } else {
            throw new RuntimeException(String.format("Attribute '%s' is not found in XML element '%s'", attribute, element.getTagName()));
        }
    }

    public static String getAttribute(Element element, String attribute, String defaultValue) {
        Validation.notNull("element", element);
        Validation.notNull("attribute", attribute);

        Attr a = element.getAttributeNode(attribute);
        if (a != null) {
            return a.getValue();
        } else {
            return defaultValue;
        }
    }
}
