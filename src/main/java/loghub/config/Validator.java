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

package loghub.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class Validator {
    private static final Pattern idPattern = Pattern.compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
    private static final Pattern namePattern = Pattern.compile("^[a-z0-9][a-z0-9-]{1,61}[a-z0-9]$");
    private static final Pattern nameWithDotsPattern = Pattern.compile("^[a-z0-9][a-z0-9-.]{1,61}[a-z0-9]$");
    private static final Pattern domainPattern = Pattern.compile("^[a-z0-9-._]{1,253}$");
    private static final Pattern versionPattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-._]{1,125}[a-zA-Z0-9]$");
    private static final Pattern revisionPattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-._]{1,125}[a-zA-Z0-9]$");
    private static final Pattern instancePattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-._]{1,125}[a-zA-Z0-9]$");
    private static final Pattern processPattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-._]{1,125}[a-zA-Z0-9]$");
    private static final Pattern secretPattern = Pattern.compile("^[a-z]{40}$");
    private static final Pattern userPattern = Pattern.compile("^[a-z][a-z0-9-]{1,61}[a-z0-9]$");
    private static final Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$");

    private Validator() {
    }

    public static boolean isPresent(String value) {
        return (value != null) && !value.isEmpty();
    }

    public static boolean isAbsent(String value) {
        return !Validator.isPresent(value);
    }

    public static boolean isId(String id) {
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

    public static boolean isDomain(String domain) {
        if (domain != null) {
            int l = domain.length();
            if ((l >= 1) && (l <= 253)) {
                return domainPattern.matcher(domain).matches();
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

    public static <T> T notNull(String argument, T value) {
        if (argument != null) {
            if (value != null) {
                return value;
            } else {
                throw new IllegalArgumentException(String.format("Argument '%s' is null", argument));
            }
        } else {
            throw new IllegalArgumentException("Argument 'argument' is null");
        }
    }

    public static boolean True(String argument, boolean value) {
        Validator.notNull("argument", argument);

        if (value) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is not true", argument));
        }
    }

    public static boolean False(String argument, boolean value) {
        Validator.notNull("argument", argument);

        if (!value) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is not false", argument));
        }
    }

    public static byte inRangeByte(String argument, byte value, byte min, byte max) {
        Validator.notNull("argument", argument);

        if ((value >= min) && (value <= max)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is %d not in range [%d, %d]", argument, value, min, max));
        }
    }

    public static char inRangeChar(String argument, char value, char min, char max) {
        Validator.notNull("argument", argument);

        if ((value >= min) && (value <= max)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is %d not in range [%d, %d]", argument, (int) value, (int) min, (int) max));
        }
    }

    public static short inRangeShort(String argument, short value, short min, short max) {
        Validator.notNull("argument", argument);

        if ((value >= min) && (value <= max)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is %d not in range [%d, %d]", argument, value, min, max));
        }
    }

    public static int inRangeInt(String argument, int value, int min, int max) {
        Validator.notNull("argument", argument);

        if ((value >= min) && (value <= max)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is %d not in range [%d, %d]", argument, value, min, max));
        }
    }

    public static long inRangeLong(String argument, long value, long min, long max) {
        Validator.notNull("argument", argument);

        if ((value >= min) && (value <= max)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is %d not in range [%d, %d]", argument, value, min, max));
        }
    }

    public static float inRangeFloat(String argument, float value, float min, float max) {
        Validator.notNull("argument", argument);

        if ((value >= min) && (value <= max)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is %f not in range [%f, %f]", argument, value, min, max));
        }
    }

    public static double inRangeDouble(String argument, double value, double min, double max) {
        Validator.notNull("argument", argument);

        if ((value >= min) && (value <= max)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is %f not in range [%f, %f]", argument, value, min, max));
        }
    }

    public static <T extends Comparable<T>> T inRange(String argument, T value, T min, T max) {
        Validator.notNull("argument", argument);
        Validator.notNull("value", value);
        Validator.notNull("min", min);
        Validator.notNull("max", max);

        if ((value.compareTo(min) >= 0) && (value.compareTo(max) <= 0)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is %s not in range [%s, %s]", argument, value.toString(), min.toString(), max.toString()));
        }
    }

    public static String present(String argument, String value) {
        Validator.notNull("argument", argument);

        if (Validator.isPresent(value)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not present", argument, value));
        }
    }

    public static String absent(String argument, String value) {
        Validator.notNull("argument", argument);

        if (Validator.isAbsent(value)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not absent", argument, value));
        }
    }

    public static String id(String argument, String id) {
        Validator.notNull("argument", argument);
        Validator.notNull("id", id);

        if (Validator.isId(id)) {
            return id;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not an id", argument, id));
        }
    }

    public static String idNullable(String argument, String id) {
        Validator.notNull("argument", argument);

        return (id != null) ? id(argument, id) : null;
    }

    public static String name(String argument, String name) {
        Validator.notNull("argument", argument);
        Validator.notNull("name", name);

        if (Validator.isName(name)) {
            return name;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not a name", argument, name));
        }
    }

    public static String nameNullable(String argument, String name) {
        Validator.notNull("argument", argument);

        return (name != null) ? name(argument, name) : null;
    }

    public static String nameWithDots(String argument, String name) {
        Validator.notNull("argument", argument);
        Validator.notNull("name", name);

        if (Validator.isNameWithDots(name)) {
            return name;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not a name with dots", argument, name));
        }
    }

    public static String nameWithDotsNullable(String argument, String name) {
        Validator.notNull("argument", argument);

        return (name != null) ? nameWithDots(argument, name) : null;
    }

    public static String domain(String argument, String domain) {
        Validator.notNull("argument", argument);
        Validator.notNull("domain", domain);

        if (Validator.isDomain(domain)) {
            return domain;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not a domain", argument, domain));
        }
    }

    public static String domainNullable(String argument, String domain) {
        Validator.notNull("argument", argument);

        return (domain != null) ? domain(argument, domain) : null;
    }

    public static String version(String argument, String version) {
        Validator.notNull("argument", argument);
        Validator.notNull("version", version);

        if (Validator.isVersion(version)) {
            return version;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not a version", argument, version));
        }
    }

    public static String versionNullable(String argument, String version) {
        Validator.notNull("argument", argument);

        return (version != null) ? version(argument, version) : null;
    }

    public static String revision(String argument, String revision) {
        Validator.notNull("argument", argument);
        Validator.notNull("revision", revision);

        if (Validator.isRevision(revision)) {
            return revision;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not a revision", argument, revision));
        }
    }

    public static String revisionNullable(String argument, String revision) {
        Validator.notNull("argument", argument);

        return (revision != null) ? revision(argument, revision) : null;
    }

    public static String instance(String argument, String instance) {
        Validator.notNull("argument", argument);
        Validator.notNull("instance", instance);

        if (Validator.isInstance(instance)) {
            return instance;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not an instance", argument, instance));
        }
    }

    public static String instanceNullable(String argument, String instance) {
        Validator.notNull("argument", argument);

        return (instance != null) ? instance(argument, instance) : null;
    }

    public static String process(String argument, String process) {
        Validator.notNull("argument", argument);
        Validator.notNull("process", process);

        if (Validator.isProcess(process)) {
            return process;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not a process", argument, process));
        }
    }

    public static String processNullable(String argument, String process) {
        Validator.notNull("argument", argument);

        return (process != null) ? process(argument, process) : null;
    }

    public static String secret(String argument, String secret) {
        Validator.notNull("argument", argument);
        Validator.notNull("secret", secret);

        if (Validator.isSecret(secret)) {
            return secret;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not a secret", argument, secret));
        }
    }

    public static String secretNullable(String argument, String secret) {
        Validator.notNull("argument", argument);

        return (secret != null) ? secret(argument, secret) : null;
    }

    public static String user(String argument, String user) {
        Validator.notNull("argument", argument);
        Validator.notNull("user", user);

        if (Validator.isUser(user)) {
            return user;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not an user", argument, user));
        }
    }

    public static String userNullable(String argument, String user) {
        Validator.notNull("argument", argument);

        return (user != null) ? user(argument, user) : null;
    }

    public static String url(String argument, String url) {
        Validator.notNull("argument", argument);
        Validator.notNull("url", url);

        if (Validator.isUrl(url)) {
            return url;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not an url", argument, url));
        }
    }

    public static String urlNullable(String argument, String url) {
        Validator.notNull("argument", argument);

        return (url != null) ? url(argument, url) : null;
    }

    public static String email(String argument, String email) {
        Validator.notNull("argument", argument);
        Validator.notNull("email", email);

        if (Validator.isEmail(email)) {
            return email;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not an email", argument, email));
        }
    }

    public static String emailNullable(String argument, String email) {
        Validator.notNull("argument", argument);

        return (email != null) ? email(argument, email) : null;
    }
}
