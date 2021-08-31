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
    JAVA("application/java-serialized-object", "ser"),
    JAVA_XML("application/java-xml", "xml"),
    JAVA_PROPERTIES("application/java-properties", "properties"),
    CBOR("application/cbor", "cbor"),
    SMILE("application/smile", "smile"),
    XML("application/xml", "xml"),
    JSON("application/json", "json"),
    YAML("application/yaml", "yaml"),
    CSV("text/csv", "csv"),
    PROPERTIES("application/properties", "properties");

    public final String contentType;
    public final String extension;

    SerializeFormat(String contentType, String extension) {
        this.contentType = contentType;
        this.extension = extension;
    }
}
