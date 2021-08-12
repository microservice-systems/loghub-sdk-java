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

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public interface HttpClient {
    public HttpURLConnection get(String contentType, String accept) throws IOException;
    public HttpURLConnection post(String contentType, String accept) throws IOException;
    public HttpURLConnection put(String contentType, String accept) throws IOException;
    public HttpURLConnection delete(String contentType, String accept) throws IOException;
    public HttpURLConnection head(String contentType, String accept) throws IOException;
    public HttpURLConnection options(String contentType, String accept) throws IOException;
    public HttpURLConnection trace(String contentType, String accept) throws IOException;
}
