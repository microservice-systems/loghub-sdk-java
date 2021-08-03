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

package systems.microservice.loghub.sdk.http;

import systems.microservice.loghub.sdk.RangeProperty;
import systems.microservice.loghub.sdk.util.Argument;
import systems.microservice.loghub.sdk.util.Range;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class DefaultHttpClient implements HttpClient {
    protected static final RangeProperty<Integer> connectTimeout = new RangeProperty<>("loghub.sdk.http.client.default.connect.timeout", Integer.class, false, 30000, "ms", new Range<>(0, Integer.MAX_VALUE));

    protected final URL url;

    public DefaultHttpClient(URL url) {
        Argument.notNull("url", url);

        this.url = url;
    }

    public URL getURL() {
        return url;
    }

    @Override
    public HttpURLConnection get(String contentType, String accept) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setUseCaches(false);
        conn.setConnectTimeout(connectTimeout.get());
        if (contentType != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", contentType);
        } else {
            conn.setDoOutput(false);
        }
        if (accept != null) {
            conn.setDoInput(true);
            conn.setRequestProperty("Accept", accept);
        } else {
            conn.setDoInput(false);
        }
        return conn;
    }

    @Override
    public HttpURLConnection put(String contentType, String accept) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setUseCaches(false);
        conn.setConnectTimeout(connectTimeout.get());
        if (contentType != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", contentType);
        } else {
            conn.setDoOutput(false);
        }
        if (accept != null) {
            conn.setDoInput(true);
            conn.setRequestProperty("Accept", accept);
        } else {
            conn.setDoInput(false);
        }
        return conn;
    }

    @Override
    public HttpURLConnection post(String contentType, String accept) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setUseCaches(false);
        conn.setConnectTimeout(connectTimeout.get());
        if (contentType != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", contentType);
        } else {
            conn.setDoOutput(false);
        }
        if (accept != null) {
            conn.setDoInput(true);
            conn.setRequestProperty("Accept", accept);
        } else {
            conn.setDoInput(false);
        }
        return conn;
    }

    @Override
    public HttpURLConnection delete(String contentType, String accept) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setUseCaches(false);
        conn.setConnectTimeout(connectTimeout.get());
        if (contentType != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", contentType);
        } else {
            conn.setDoOutput(false);
        }
        if (accept != null) {
            conn.setDoInput(true);
            conn.setRequestProperty("Accept", accept);
        } else {
            conn.setDoInput(false);
        }
        return conn;
    }

    @Override
    public HttpURLConnection head(String contentType, String accept) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("HEAD");
        conn.setUseCaches(false);
        conn.setConnectTimeout(connectTimeout.get());
        if (contentType != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", contentType);
        } else {
            conn.setDoOutput(false);
        }
        if (accept != null) {
            conn.setDoInput(true);
            conn.setRequestProperty("Accept", accept);
        } else {
            conn.setDoInput(false);
        }
        return conn;
    }

    @Override
    public HttpURLConnection options(String contentType, String accept) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("OPTIONS");
        conn.setUseCaches(false);
        conn.setConnectTimeout(connectTimeout.get());
        if (contentType != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", contentType);
        } else {
            conn.setDoOutput(false);
        }
        if (accept != null) {
            conn.setDoInput(true);
            conn.setRequestProperty("Accept", accept);
        } else {
            conn.setDoInput(false);
        }
        return conn;
    }

    @Override
    public HttpURLConnection trace(String contentType, String accept) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("TRACE");
        conn.setUseCaches(false);
        conn.setConnectTimeout(connectTimeout.get());
        if (contentType != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", contentType);
        } else {
            conn.setDoOutput(false);
        }
        if (accept != null) {
            conn.setDoInput(true);
            conn.setRequestProperty("Accept", accept);
        } else {
            conn.setDoInput(false);
        }
        return conn;
    }
}
