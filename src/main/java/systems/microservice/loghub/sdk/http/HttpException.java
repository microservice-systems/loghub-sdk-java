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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class HttpException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected final URL url;
    protected final String method;
    protected final int code;

    public HttpException(HttpURLConnection connection) {
        this(getURL(connection), getMethod(connection), getCode(connection));
    }

    public HttpException(HttpURLConnection connection, Throwable cause) {
        this(getURL(connection), getMethod(connection), getCode(connection), cause);
    }

    public HttpException(URL url, String method, int code) {
        super(String.format("[%s][%d]: %s", getString(method), code, getExternalForm(url)));

        this.url = url;
        this.method = method;
        this.code = code;
    }

    public HttpException(URL url, String method, int code, Throwable cause) {
        super(String.format("[%s][%d]: %s", getString(method), code, getExternalForm(url)), cause);

        this.url = url;
        this.method = method;
        this.code = code;
    }

    public HttpException(String message) {
        super(message);

        this.url = null;
        this.method = null;
        this.code = -1;
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);

        this.url = null;
        this.method = null;
        this.code = -1;
    }

    public HttpException(Throwable cause) {
        super(cause);

        this.url = null;
        this.method = null;
        this.code = -1;
    }

    public HttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

        this.url = null;
        this.method = null;
        this.code = -1;
    }

    public URL getURL() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public int getCode() {
        return code;
    }

    protected static URL getURL(HttpURLConnection connection) {
        return (connection != null) ? connection.getURL() : null;
    }

    protected static String getMethod(HttpURLConnection connection) {
        return (connection != null) ? connection.getRequestMethod() : null;
    }

    protected static int getCode(HttpURLConnection connection) {
        try {
            return (connection != null) ? connection.getResponseCode() : -1;
        } catch (IOException e) {
            return -1;
        }
    }

    protected static String getExternalForm(URL url) {
        return (url != null) ? url.toExternalForm() : "null";
    }

    protected static String getString(String value) {
        return (value != null) ? value : "null";
    }
}
