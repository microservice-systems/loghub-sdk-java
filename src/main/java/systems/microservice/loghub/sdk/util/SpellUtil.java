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

package systems.microservice.loghub.sdk.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class SpellUtil {
    private static final Pattern environmentPattern = Pattern.compile("^[a-z][a-z0-9-]{3,46}[a-z0-9]$");
    private static final Pattern applicationPattern = Pattern.compile("^[a-z][a-z0-9-]{3,46}[a-z0-9]$");
    private static final Pattern versionPattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-._]{1,46}[a-zA-Z0-9]$");
    private static final Pattern instancePattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-._]{1,46}[a-zA-Z0-9]$");
    private static final Pattern processPattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-._]{1,46}[a-zA-Z0-9]$");
    private static final Pattern userPattern = Pattern.compile("^[a-z][a-z0-9-]{3,46}[a-z0-9]$");
    private static final Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$");

    private SpellUtil() {
    }

    public static boolean isEnvironment(String environment) {
        if (environment != null) {
            if (environment.length() <= 48) {
                return environmentPattern.matcher(environment).matches();
            }
        }
        return false;
    }

    public static boolean isApplication(String application) {
        if (application != null) {
            if (application.length() <= 48) {
                return applicationPattern.matcher(application).matches();
            }
        }
        return false;
    }

    public static boolean isVersion(String version) {
        if (version != null) {
            if (version.length() <= 48) {
                return versionPattern.matcher(version).matches();
            }
        }
        return false;
    }

    public static boolean isInstance(String instance) {
        if (instance != null) {
            if (instance.length() <= 48) {
                return instancePattern.matcher(instance).matches();
            }
        }
        return false;
    }

    public static boolean isProcess(String process) {
        if (process != null) {
            if (process.length() <= 48) {
                return processPattern.matcher(process).matches();
            }
        }
        return false;
    }

    public static boolean isSecret(String secret) {
        if (secret != null) {
            if (secret.length() <= 128) {
                return true;
            }
        }
        return false;
    }

    public static boolean isUser(String user) {
        if (user != null) {
            if (user.length() <= 48) {
                return userPattern.matcher(user).matches();
            }
        }
        return false;
    }

    public static boolean isUrl(String url) {
        if (url != null) {
            if (url.length() <= 4096) {
                try {
                    URL u = new URL(url);
                    return true;
                } catch (MalformedURLException e) {
                }
            }
        }
        return false;
    }

    public static boolean isEmail(String email) {
        if (email != null) {
            if (email.length() <= 128) {
                return emailPattern.matcher(email).matches();
            }
        }
        return false;
    }
}
