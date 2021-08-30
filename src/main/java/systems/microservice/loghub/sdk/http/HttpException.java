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

    protected final String target;
    protected final String method;
    protected final int status;

    public HttpException(HttpURLConnection connection) {
        this(getExternalForm(getURL(connection)), getMethod(connection), getStatus(connection));
    }

    public HttpException(HttpURLConnection connection, Throwable cause) {
        this(getExternalForm(getURL(connection)), getMethod(connection), getStatus(connection), cause);
    }

    public HttpException(String target, String method, int status) {
        super(String.format("[%s][%d]: %s", getString(method), status, getString(target)));

        this.target = target;
        this.method = method;
        this.status = status;
    }

    public HttpException(String target, String method, int status, Throwable cause) {
        super(String.format("[%s][%d]: %s", getString(method), status, getString(target)), cause);

        this.target = target;
        this.method = method;
        this.status = status;
    }

    public HttpException(String message) {
        super(message);

        this.target = null;
        this.method = null;
        this.status = -1;
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);

        this.target = null;
        this.method = null;
        this.status = -1;
    }

    public HttpException(Throwable cause) {
        super(cause);

        this.target = null;
        this.method = null;
        this.status = -1;
    }

    public HttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

        this.target = null;
        this.method = null;
        this.status = -1;
    }

    public String getTarget() {
        return target;
    }

    public String getMethod() {
        return method;
    }

    public int getStatus() {
        return status;
    }

    protected static URL getURL(HttpURLConnection connection) {
        return (connection != null) ? connection.getURL() : null;
    }

    protected static String getMethod(HttpURLConnection connection) {
        return (connection != null) ? connection.getRequestMethod() : null;
    }

    protected static int getStatus(HttpURLConnection connection) {
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
