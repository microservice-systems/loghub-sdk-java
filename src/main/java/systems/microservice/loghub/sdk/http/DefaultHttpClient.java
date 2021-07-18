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

import systems.microservice.loghub.sdk.config.Config;
import systems.microservice.loghub.sdk.util.Range;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class DefaultHttpClient implements HttpClient {
    private static final String CONNECT_TIMEOUT_PROPERTY = "loghub.sdk.http.client.default.connect.timeout";
    private static final Integer CONNECT_TIMEOUT_DEFAULT = 30000;
    private static final Range<Integer> CONNECT_TIMEOUT_RANGE = new Range<>(0, Integer.MAX_VALUE);

    public DefaultHttpClient() {
    }

    @Override
    public HttpURLConnection get(URL url, String contentType, String accept) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setUseCaches(false);
        conn.setConnectTimeout(Config.getProperty(CONNECT_TIMEOUT_PROPERTY, Integer.class, CONNECT_TIMEOUT_DEFAULT, "ms", false, CONNECT_TIMEOUT_RANGE));
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
    public HttpURLConnection put(URL url, String contentType, String accept) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setUseCaches(false);
        conn.setConnectTimeout(Config.getProperty(CONNECT_TIMEOUT_PROPERTY, Integer.class, CONNECT_TIMEOUT_DEFAULT, "ms", false, CONNECT_TIMEOUT_RANGE));
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
    public HttpURLConnection post(URL url, String contentType, String accept) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setUseCaches(false);
        conn.setConnectTimeout(Config.getProperty(CONNECT_TIMEOUT_PROPERTY, Integer.class, CONNECT_TIMEOUT_DEFAULT, "ms", false, CONNECT_TIMEOUT_RANGE));
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
    public HttpURLConnection delete(URL url, String contentType, String accept) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setUseCaches(false);
        conn.setConnectTimeout(Config.getProperty(CONNECT_TIMEOUT_PROPERTY, Integer.class, CONNECT_TIMEOUT_DEFAULT, "ms", false, CONNECT_TIMEOUT_RANGE));
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
    public HttpURLConnection head(URL url, String contentType, String accept) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("HEAD");
        conn.setUseCaches(false);
        conn.setConnectTimeout(Config.getProperty(CONNECT_TIMEOUT_PROPERTY, Integer.class, CONNECT_TIMEOUT_DEFAULT, "ms", false, CONNECT_TIMEOUT_RANGE));
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
    public HttpURLConnection options(URL url, String contentType, String accept) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("OPTIONS");
        conn.setUseCaches(false);
        conn.setConnectTimeout(Config.getProperty(CONNECT_TIMEOUT_PROPERTY, Integer.class, CONNECT_TIMEOUT_DEFAULT, "ms", false, CONNECT_TIMEOUT_RANGE));
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
    public HttpURLConnection trace(URL url, String contentType, String accept) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("TRACE");
        conn.setUseCaches(false);
        conn.setConnectTimeout(Config.getProperty(CONNECT_TIMEOUT_PROPERTY, Integer.class, CONNECT_TIMEOUT_DEFAULT, "ms", false, CONNECT_TIMEOUT_RANGE));
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
