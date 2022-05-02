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

package loghub;

import loghub.config.Property;
import loghub.config.Validator;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class Config {
    public static final String LOGHUB_CENTRAL = Validator.domainNullable("LOGHUB_CENTRAL", Property.get("LOGHUB_CENTRAL", "LOGHUB_CENTRAL", "/META-INF/loghub/LOGHUB_CENTRAL", Defaults.LOGHUB_CENTRAL));
    public static final String LOGHUB_ORGANIZATION = Validator.nameNullable("LOGHUB_ORGANIZATION", Property.get("LOGHUB_ORGANIZATION", "LOGHUB_ORGANIZATION", "/META-INF/loghub/LOGHUB_ORGANIZATION", Defaults.LOGHUB_ORGANIZATION));
    public static final String LOGHUB_ENVIRONMENT = Validator.nameNullable("LOGHUB_ENVIRONMENT", Property.get("LOGHUB_ENVIRONMENT", "LOGHUB_ENVIRONMENT", "/META-INF/loghub/LOGHUB_ENVIRONMENT", Defaults.LOGHUB_ENVIRONMENT));
    public static final String LOGHUB_REGISTRY = Validator.domainNullable("LOGHUB_REGISTRY", Property.get("LOGHUB_REGISTRY", "LOGHUB_REGISTRY", "/META-INF/loghub/LOGHUB_REGISTRY", Defaults.LOGHUB_REGISTRY));
    public static final String LOGHUB_GROUP = Validator.nameWithDotsNullable("LOGHUB_GROUP", Property.get("LOGHUB_GROUP", "LOGHUB_GROUP", "/META-INF/loghub/LOGHUB_GROUP", Defaults.LOGHUB_GROUP));
    public static final String LOGHUB_APPLICATION = Validator.nameNullable("LOGHUB_APPLICATION", Property.get("LOGHUB_APPLICATION", "LOGHUB_APPLICATION", "/META-INF/loghub/LOGHUB_APPLICATION", Defaults.LOGHUB_APPLICATION));
    public static final String LOGHUB_VERSION = Validator.versionNullable("LOGHUB_VERSION", Property.get("LOGHUB_VERSION", "LOGHUB_VERSION", "/META-INF/loghub/LOGHUB_VERSION", Defaults.LOGHUB_VERSION));
    public static final String LOGHUB_REVISION = Validator.revisionNullable("LOGHUB_REVISION", Property.get("LOGHUB_REVISION", "LOGHUB_REVISION", "/META-INF/loghub/LOGHUB_REVISION", Defaults.LOGHUB_REVISION));
    public static final String LOGHUB_NAME = Property.get("LOGHUB_NAME", "LOGHUB_NAME", "/META-INF/loghub/LOGHUB_NAME", Defaults.LOGHUB_NAME);
    public static final String LOGHUB_DESCRIPTION = Property.get("LOGHUB_DESCRIPTION", "LOGHUB_DESCRIPTION", "/META-INF/loghub/LOGHUB_DESCRIPTION", Defaults.LOGHUB_DESCRIPTION);
    public static final String LOGHUB_REPOSITORY = Property.get("LOGHUB_REPOSITORY", "LOGHUB_REPOSITORY", "/META-INF/loghub/LOGHUB_REPOSITORY", Defaults.LOGHUB_REPOSITORY);
    public static final String LOGHUB_JOB = Property.get("LOGHUB_JOB", "LOGHUB_JOB", "/META-INF/loghub/LOGHUB_JOB", Defaults.LOGHUB_JOB);
    public static final String LOGHUB_PIPELINE = Property.get("LOGHUB_PIPELINE", "LOGHUB_PIPELINE", "/META-INF/loghub/LOGHUB_PIPELINE", Defaults.LOGHUB_PIPELINE);
    public static final String LOGHUB_BRANCH = Property.get("LOGHUB_BRANCH", "LOGHUB_BRANCH", "/META-INF/loghub/LOGHUB_BRANCH", Defaults.LOGHUB_BRANCH);
    public static final String LOGHUB_COMMIT = Property.get("LOGHUB_COMMIT", "LOGHUB_COMMIT", "/META-INF/loghub/LOGHUB_COMMIT", Defaults.LOGHUB_COMMIT);
    public static final String LOGHUB_COMMIT_BEFORE = Property.get("LOGHUB_COMMIT_BEFORE", "LOGHUB_COMMIT_BEFORE", "/META-INF/loghub/LOGHUB_COMMIT_BEFORE", Defaults.LOGHUB_COMMIT_BEFORE);
    public static final String LOGHUB_COMMIT_MESSAGE = Property.get("LOGHUB_COMMIT_MESSAGE", "LOGHUB_COMMIT_MESSAGE", "/META-INF/loghub/LOGHUB_COMMIT_MESSAGE", Defaults.LOGHUB_COMMIT_MESSAGE);
    public static final String LOGHUB_MAINTAINER = Property.get("LOGHUB_MAINTAINER", "LOGHUB_MAINTAINER", "/META-INF/loghub/LOGHUB_MAINTAINER", Defaults.LOGHUB_MAINTAINER);
    public static final String LOGHUB_MAINTAINER_NAME = Property.get("LOGHUB_MAINTAINER_NAME", "LOGHUB_MAINTAINER_NAME", "/META-INF/loghub/LOGHUB_MAINTAINER_NAME", Defaults.LOGHUB_MAINTAINER_NAME);
    public static final String LOGHUB_MAINTAINER_EMAIL = Property.get("LOGHUB_MAINTAINER_EMAIL", "LOGHUB_MAINTAINER_EMAIL", "/META-INF/loghub/LOGHUB_MAINTAINER_EMAIL", Defaults.LOGHUB_MAINTAINER_EMAIL);
    public static final String LOGHUB_INSTANCE = Validator.instanceNullable("LOGHUB_INSTANCE", Property.get("LOGHUB_INSTANCE", "LOGHUB_INSTANCE", "/META-INF/loghub/LOGHUB_INSTANCE", Defaults.LOGHUB_INSTANCE));
    public static final InputEventFilter LOGHUB_EVENT_INCLUDE = InputEventFilter.parse(Property.get("LOGHUB_EVENT_INCLUDE", "LOGHUB_EVENT_INCLUDE", "/META-INF/loghub/LOGHUB_EVENT_INCLUDE", Defaults.LOGHUB_EVENT_INCLUDE));
    public static final InputEventFilter LOGHUB_EVENT_EXCLUDE = InputEventFilter.parse(Property.get("LOGHUB_EVENT_EXCLUDE", "LOGHUB_EVENT_EXCLUDE", "/META-INF/loghub/LOGHUB_EVENT_EXCLUDE", Defaults.LOGHUB_EVENT_EXCLUDE));

    private Config() {
    }
}
