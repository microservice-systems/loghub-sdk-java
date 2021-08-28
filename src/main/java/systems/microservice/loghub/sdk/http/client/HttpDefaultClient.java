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

package systems.microservice.loghub.sdk.http.client;

import systems.microservice.loghub.sdk.Property;
import systems.microservice.loghub.sdk.http.HttpClient;
import systems.microservice.loghub.sdk.property.NullProperty;
import systems.microservice.loghub.sdk.property.RangeProperty;
import systems.microservice.loghub.sdk.util.Argument;
import systems.microservice.loghub.sdk.util.Range;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class HttpDefaultClient implements HttpClient, Serializable {
    private static final long serialVersionUID = 1L;

    protected static final Property<Integer> defaultConnectTimeout = new RangeProperty<>("LogHub", "loghub.sdk.http.client.default.connect.timeout", Integer.class, false, false, 60000, new Range<>(0, Integer.MAX_VALUE), "ms");

    protected final Property<URL> url;
    protected final Property<String> user;
    protected final Property<String> password;
    protected final Property<Integer> connectTimeout;

    public HttpDefaultClient(Property<URL> url) {
        this(url, defaultConnectTimeout);
    }

    public HttpDefaultClient(Property<URL> url, Property<Integer> connectTimeout) {
        this(url, NullProperty.getInstance(), NullProperty.getInstance(), connectTimeout);
    }

    public HttpDefaultClient(Property<URL> url, Property<String> user, Property<String> password) {
        this(url, user, password, defaultConnectTimeout);
    }

    public HttpDefaultClient(Property<URL> url, Property<String> user, Property<String> password, Property<Integer> connectTimeout) {
        Argument.notNullProperty("url", url);
        Argument.notNull("user", user);
        Argument.notNull("password", password);
        Argument.notNullProperty("connectTimeout", connectTimeout);

        this.url = url;
        this.user = user;
        this.password = password;
        this.connectTimeout = connectTimeout;
    }

    public URL getURL() {
        return url.get();
    }

    public String getUser() {
        return user.get();
    }

    public String getPassword() {
        return password.get();
    }

    public int getConnectTimeout() {
        return connectTimeout.get();
    }

    protected String createAuth(String user, String password) {
        if ((user != null) && (password != null) && !user.isEmpty()) {
            return "Basic " + new String(Base64.getEncoder().encode(String.format("%s:%s", user, password).getBytes(StandardCharsets.UTF_8)));
        } else {
            return null;
        }
    }

    protected HttpURLConnection createConnection(String spec, String method, String contentType, String acceptType) throws IOException {
        Argument.notNull("method", method);

        HttpURLConnection conn = (spec != null) ? (HttpURLConnection) new URL(url.get(), spec).openConnection() : (HttpURLConnection) url.get().openConnection();
        conn.setRequestMethod(method);
        conn.setUseCaches(false);
        conn.setConnectTimeout(connectTimeout.get());
        String auth = createAuth(user.get(), password.get());
        if (auth != null) {
            conn.setRequestProperty("Authorization", auth);
        }
        if (contentType != null) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", contentType);
        } else {
            conn.setDoOutput(false);
        }
        if (acceptType != null) {
            conn.setDoInput(true);
            conn.setRequestProperty("Accept", acceptType);
        } else {
            conn.setDoInput(false);
        }
        return conn;
    }

    @Override
    public HttpURLConnection get(String spec, String contentType, String acceptType) throws IOException {
        return createConnection(spec, "GET", contentType, acceptType);
    }

    @Override
    public HttpURLConnection post(String spec, String contentType, String acceptType) throws IOException {
        return createConnection(spec, "POST", contentType, acceptType);
    }

    @Override
    public HttpURLConnection put(String spec, String contentType, String acceptType) throws IOException {
        return createConnection(spec, "PUT", contentType, acceptType);
    }

    @Override
    public HttpURLConnection patch(String spec, String contentType, String acceptType) throws IOException {
        return createConnection(spec, "PATCH", contentType, acceptType);
    }

    @Override
    public HttpURLConnection delete(String spec, String contentType, String acceptType) throws IOException {
        return createConnection(spec, "DELETE", contentType, acceptType);
    }

    @Override
    public HttpURLConnection head(String spec, String contentType, String acceptType) throws IOException {
        return createConnection(spec, "HEAD", contentType, acceptType);
    }

    @Override
    public HttpURLConnection options(String spec, String contentType, String acceptType) throws IOException {
        return createConnection(spec, "OPTIONS", contentType, acceptType);
    }

    @Override
    public HttpURLConnection trace(String spec, String contentType, String acceptType) throws IOException {
        return createConnection(spec, "TRACE", contentType, acceptType);
    }
}
