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

package systems.microservice.loghub.sdk.serializer;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public enum SerializeFormat {
    JAVA("application/java-serialized-object", "ser", false),
    JAVA_XML("application/java-xml", "xml", false),
    JAVA_PROPERTIES("application/java-properties", "properties", false),
    CBOR("application/cbor", "cbor", true),
    SMILE("application/smile", "smile", true),
    XML("application/xml", "xml", true),
    JSON("application/json", "json", true),
    YAML("application/yaml", "yaml", true),
    CSV("text/csv", "csv", true),
    PROPERTIES("application/properties", "properties", true);

    public final String contentType;
    public final String extension;
    public final boolean safe;

    SerializeFormat(String contentType, String extension, boolean safe) {
        this.contentType = contentType;
        this.extension = extension;
        this.safe = safe;
    }
}
