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

import systems.microservice.loghub.facade.Validator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class URLUtil {
    private URLUtil() {
    }

    public static URL create(String url) {
        Validator.notNull("url", url);

        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUser(URL url) {
        Validator.notNull("url", url);

        String ui = url.getUserInfo();
        if (ui != null) {
            int i = ui.indexOf(':');
            if (i >= 0) {
                return ui.substring(0, i);
            } else {
                return ui;
            }
        } else {
            return null;
        }
    }

    public static String getPassword(URL url) {
        Validator.notNull("url", url);

        String ui = url.getUserInfo();
        if (ui != null) {
            int i = ui.indexOf(':');
            if (i >= 0) {
                return ui.substring(i + 1);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static String getParameter(URL url, String parameter) {
        Validator.notNull("url", url);
        Validator.notNull("parameter", parameter);

        String q = url.getQuery();
        if (q != null) {
            String[] qs = q.split("&");
            for (String qp : qs) {
                String[] qps = qp.split("=");
                if (qps.length == 2) {
                    if (qps[0].equals(parameter)) {
                        return qps[1];
                    }
                } else if (qps.length == 1) {
                    if (qps[0].equals(parameter)) {
                        return "";
                    }
                }
            }
        }
        return null;
    }

    public static Map<String, String> getParameters(URL url) {
        Validator.notNull("url", url);

        String q = url.getQuery();
        if (q != null) {
            String[] qs = q.split("&");
            Map<String, String> queryParameters = new LinkedHashMap<>(qs.length);
            for (String qp : qs) {
                String[] qps = qp.split("=");
                if (qps.length == 2) {
                    queryParameters.put(qps[0], qps[1]);
                } else if (qps.length == 1) {
                    queryParameters.put(qps[0], "");
                }
            }
            return queryParameters;
        } else {
            return Collections.emptyMap();
        }
    }
}
