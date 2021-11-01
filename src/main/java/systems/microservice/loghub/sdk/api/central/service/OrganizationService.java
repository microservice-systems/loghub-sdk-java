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

package systems.microservice.loghub.sdk.api.central.service;

import systems.microservice.loghub.connector.Validation;
import systems.microservice.loghub.sdk.api.central.model.Organization;
import systems.microservice.loghub.sdk.http.HttpClient;
import systems.microservice.loghub.sdk.http.HttpException;
import systems.microservice.loghub.sdk.serializer.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class OrganizationService {
    protected final HttpClient client;
    protected final Serializer contentSerializer;
    protected final Serializer acceptSerializer;

    public OrganizationService(HttpClient client) {
        this(client, Serializer.CBOR, Serializer.CBOR);
    }

    public OrganizationService(HttpClient client, Serializer contentSerializer, Serializer acceptSerializer) {
        Validation.notNull("client", client);
        Validation.notNull("contentSerializer", contentSerializer);
        Validation.notNull("acceptSerializer", acceptSerializer);

        this.client = client;
        this.contentSerializer = contentSerializer;
        this.acceptSerializer = acceptSerializer;
    }

    public HttpClient getClient() {
        return client;
    }

    public Serializer getContentSerializer() {
        return contentSerializer;
    }

    public Serializer getAcceptSerializer() {
        return acceptSerializer;
    }

    public Organization get(String id) {
        Validation.notNull("id", id);

        try {
            HttpURLConnection conn = client.get("/loghub/api/central/organization", null, acceptSerializer.contentType);
            try {
                conn.setRequestProperty("loghub-id", id);
                conn.connect();
                try {
                    int rc = conn.getResponseCode();
                    if (rc == HttpURLConnection.HTTP_OK) {
                        try (InputStream in = conn.getInputStream()) {
                            return acceptSerializer.read(in, Organization.class);
                        }
                    } else {
                        throw new HttpException(conn);
                    }
                } finally {
                    conn.disconnect();
                }
            } catch (IOException e) {
                throw new HttpException(conn, e);
            }
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }
}
