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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class SpellUtil {
    private static final Pattern idPattern = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
    private static final Pattern namePattern = Pattern.compile("^[a-z0-9][a-z0-9-]{1,61}[a-z0-9]$");
    private static final Pattern nameWithDotsPattern = Pattern.compile("^[a-z0-9][a-z0-9-.]{1,61}[a-z0-9]$");
    private static final Pattern servicePattern = Pattern.compile("^[a-z0-9-._]{1,253}$");
    private static final Pattern environmentPattern = Pattern.compile("^[a-z][a-z0-9-]{1,61}[a-z0-9]$");
    private static final Pattern applicationPattern = Pattern.compile("^[a-z][a-z0-9-]{1,61}[a-z0-9]$");
    private static final Pattern versionPattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-._]{1,125}[a-zA-Z0-9]$");
    private static final Pattern revisionPattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-._]{1,125}[a-zA-Z0-9]$");
    private static final Pattern instancePattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-._]{1,125}[a-zA-Z0-9]$");
    private static final Pattern processPattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-._]{1,125}[a-zA-Z0-9]$");
    private static final Pattern secretPattern = Pattern.compile("^[a-zA-Z0-9]{40}$");
    private static final Pattern userPattern = Pattern.compile("^[a-z][a-z0-9-]{1,61}[a-z0-9]$");
    private static final Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$");

    private SpellUtil() {
    }

    public static boolean isID(String id) {
        if (id != null) {
            int l = id.length();
            if (l == 36) {
                return idPattern.matcher(id).matches();
            }
        }
        return false;
    }

    public static boolean isName(String name) {
        if (name != null) {
            int l = name.length();
            if ((l >= 3) && (l <= 63)) {
                return namePattern.matcher(name).matches();
            }
        }
        return false;
    }

    public static boolean isNameWithDots(String name) {
        if (name != null) {
            int l = name.length();
            if ((l >= 3) && (l <= 63)) {
                return nameWithDotsPattern.matcher(name).matches();
            }
        }
        return false;
    }

    public static boolean isService(String service) {
        if (service != null) {
            int l = service.length();
            if ((l >= 1) && (l <= 253)) {
                return servicePattern.matcher(service).matches();
            }
        }
        return false;
    }

    public static boolean isEnvironment(String environment) {
        if (environment != null) {
            int l = environment.length();
            if ((l >= 3) && (l <= 63)) {
                return environmentPattern.matcher(environment).matches();
            }
        }
        return false;
    }

    public static boolean isApplication(String application) {
        if (application != null) {
            int l = application.length();
            if ((l >= 3) && (l <= 63)) {
                return applicationPattern.matcher(application).matches();
            }
        }
        return false;
    }

    public static boolean isVersion(String version) {
        if (version != null) {
            int l = version.length();
            if ((l >= 3) && (l <= 127)) {
                return versionPattern.matcher(version).matches();
            }
        }
        return false;
    }

    public static boolean isRevision(String revision) {
        if (revision != null) {
            int l = revision.length();
            if ((l >= 3) && (l <= 127)) {
                return revisionPattern.matcher(revision).matches();
            }
        }
        return false;
    }

    public static boolean isInstance(String instance) {
        if (instance != null) {
            int l = instance.length();
            if ((l >= 3) && (l <= 127)) {
                return instancePattern.matcher(instance).matches();
            }
        }
        return false;
    }

    public static boolean isProcess(String process) {
        if (process != null) {
            int l = process.length();
            if ((l >= 3) && (l <= 127)) {
                return processPattern.matcher(process).matches();
            }
        }
        return false;
    }

    public static boolean isSecret(String secret) {
        if (secret != null) {
            int l = secret.length();
            if (l == 40) {
                return secretPattern.matcher(secret).matches();
            }
        }
        return false;
    }

    public static boolean isUser(String user) {
        if (user != null) {
            int l = user.length();
            if ((l >= 3) && (l <= 63)) {
                return userPattern.matcher(user).matches();
            }
        }
        return false;
    }

    public static boolean isUrl(String url) {
        if (url != null) {
            int l = url.length();
            if ((l >= 1) && (l <= 4096)) {
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
            int l = email.length();
            if ((l >= 3) && (l <= 127)) {
                return emailPattern.matcher(email).matches();
            }
        }
        return false;
    }
}
