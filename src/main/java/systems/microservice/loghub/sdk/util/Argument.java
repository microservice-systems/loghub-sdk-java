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

import systems.microservice.loghub.sdk.Property;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class Argument {
    private Argument() {
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

    public static <T> Property<T> notNullProperty(String argument, Property<T> property) {
        if (argument != null) {
            if (property != null) {
                if (property.get() != null) {
                    return property;
                } else {
                    throw new IllegalArgumentException(String.format("Property argument '%s' has null value", argument));
                }
            } else {
                throw new IllegalArgumentException(String.format("Property argument '%s' is null", argument));
            }
        } else {
            throw new IllegalArgumentException("Argument 'argument' is null");
        }
    }

    public static boolean True(String argument, boolean value) {
        Argument.notNull("argument", argument);

        if (value) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is not true", argument));
        }
    }

    public static boolean False(String argument, boolean value) {
        Argument.notNull("argument", argument);

        if (!value) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is not false", argument));
        }
    }

    public static byte inRangeByte(String argument, byte value, byte min, byte max) {
        Argument.notNull("argument", argument);

        if ((value >= min) && (value <= max)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is %d not in range [%d, %d]", argument, value, min, max));
        }
    }

    public static char inRangeChar(String argument, char value, char min, char max) {
        Argument.notNull("argument", argument);

        if ((value >= min) && (value <= max)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is %d not in range [%d, %d]", argument, (int) value, (int) min, (int) max));
        }
    }

    public static short inRangeShort(String argument, short value, short min, short max) {
        Argument.notNull("argument", argument);

        if ((value >= min) && (value <= max)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is %d not in range [%d, %d]", argument, value, min, max));
        }
    }

    public static int inRangeInt(String argument, int value, int min, int max) {
        Argument.notNull("argument", argument);

        if ((value >= min) && (value <= max)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is %d not in range [%d, %d]", argument, value, min, max));
        }
    }

    public static long inRangeLong(String argument, long value, long min, long max) {
        Argument.notNull("argument", argument);

        if ((value >= min) && (value <= max)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is %d not in range [%d, %d]", argument, value, min, max));
        }
    }

    public static float inRangeFloat(String argument, float value, float min, float max) {
        Argument.notNull("argument", argument);

        if ((value >= min) && (value <= max)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is %f not in range [%f, %f]", argument, value, min, max));
        }
    }

    public static double inRangeDouble(String argument, double value, double min, double max) {
        Argument.notNull("argument", argument);

        if ((value >= min) && (value <= max)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is %f not in range [%f, %f]", argument, value, min, max));
        }
    }

    public static <T extends Comparable<T>> T inRange(String argument, T value, T min, T max) {
        Argument.notNull("argument", argument);
        Argument.notNull("value", value);
        Argument.notNull("min", min);
        Argument.notNull("max", max);

        if ((value.compareTo(min) >= 0) && (value.compareTo(max) <= 0)) {
            return value;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is %s not in range [%s, %s]", argument, value.toString(), min.toString(), max.toString()));
        }
    }

    public static String id(String argument, String id) {
        Argument.notNull("argument", argument);
        Argument.notNull("id", id);

        if (ValidationUtil.isId(id)) {
            return id;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not an id", argument, id));
        }
    }

    public static String idNullable(String argument, String id) {
        Argument.notNull("argument", argument);

        return (id != null) ? id(argument, id) : null;
    }

    public static String name(String argument, String name) {
        Argument.notNull("argument", argument);
        Argument.notNull("name", name);

        if (ValidationUtil.isName(name)) {
            return name;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not a name", argument, name));
        }
    }

    public static String nameNullable(String argument, String name) {
        Argument.notNull("argument", argument);

        return (name != null) ? name(argument, name) : null;
    }

    public static String nameWithDots(String argument, String name) {
        Argument.notNull("argument", argument);
        Argument.notNull("name", name);

        if (ValidationUtil.isNameWithDots(name)) {
            return name;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not a name with dots", argument, name));
        }
    }

    public static String nameWithDotsNullable(String argument, String name) {
        Argument.notNull("argument", argument);

        return (name != null) ? nameWithDots(argument, name) : null;
    }

    public static String domain(String argument, String domain) {
        Argument.notNull("argument", argument);
        Argument.notNull("domain", domain);

        if (ValidationUtil.isDomain(domain)) {
            return domain;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not a domain", argument, domain));
        }
    }

    public static String domainNullable(String argument, String domain) {
        Argument.notNull("argument", argument);

        return (domain != null) ? domain(argument, domain) : null;
    }

    public static String version(String argument, String version) {
        Argument.notNull("argument", argument);
        Argument.notNull("version", version);

        if (ValidationUtil.isVersion(version)) {
            return version;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not a version", argument, version));
        }
    }

    public static String versionNullable(String argument, String version) {
        Argument.notNull("argument", argument);

        return (version != null) ? version(argument, version) : null;
    }

    public static String revision(String argument, String revision) {
        Argument.notNull("argument", argument);
        Argument.notNull("revision", revision);

        if (ValidationUtil.isRevision(revision)) {
            return revision;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not a revision", argument, revision));
        }
    }

    public static String revisionNullable(String argument, String revision) {
        Argument.notNull("argument", argument);

        return (revision != null) ? revision(argument, revision) : null;
    }

    public static String instance(String argument, String instance) {
        Argument.notNull("argument", argument);
        Argument.notNull("instance", instance);

        if (ValidationUtil.isInstance(instance)) {
            return instance;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not an instance", argument, instance));
        }
    }

    public static String instanceNullable(String argument, String instance) {
        Argument.notNull("argument", argument);

        return (instance != null) ? instance(argument, instance) : null;
    }

    public static String process(String argument, String process) {
        Argument.notNull("argument", argument);
        Argument.notNull("process", process);

        if (ValidationUtil.isProcess(process)) {
            return process;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not a process", argument, process));
        }
    }

    public static String processNullable(String argument, String process) {
        Argument.notNull("argument", argument);

        return (process != null) ? process(argument, process) : null;
    }

    public static String secret(String argument, String secret) {
        Argument.notNull("argument", argument);
        Argument.notNull("secret", secret);

        if (ValidationUtil.isSecret(secret)) {
            return secret;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not a secret", argument, secret));
        }
    }

    public static String secretNullable(String argument, String secret) {
        Argument.notNull("argument", argument);

        return (secret != null) ? secret(argument, secret) : null;
    }

    public static String user(String argument, String user) {
        Argument.notNull("argument", argument);
        Argument.notNull("user", user);

        if (ValidationUtil.isUser(user)) {
            return user;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not an user", argument, user));
        }
    }

    public static String userNullable(String argument, String user) {
        Argument.notNull("argument", argument);

        return (user != null) ? user(argument, user) : null;
    }

    public static String url(String argument, String url) {
        Argument.notNull("argument", argument);
        Argument.notNull("url", url);

        if (ValidationUtil.isUrl(url)) {
            return url;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not an url", argument, url));
        }
    }

    public static String urlNullable(String argument, String url) {
        Argument.notNull("argument", argument);

        return (url != null) ? url(argument, url) : null;
    }

    public static String email(String argument, String email) {
        Argument.notNull("argument", argument);
        Argument.notNull("email", email);

        if (ValidationUtil.isEmail(email)) {
            return email;
        } else {
            throw new IllegalArgumentException(String.format("Argument '%s' is '%s' not an email", argument, email));
        }
    }

    public static String emailNullable(String argument, String email) {
        Argument.notNull("argument", argument);

        return (email != null) ? email(argument, email) : null;
    }
}
