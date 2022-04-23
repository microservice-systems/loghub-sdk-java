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

package systems.microservice.loghub.facade;

import systems.microservice.loghub.facade.config.Property;
import systems.microservice.loghub.facade.config.Validator;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class Config {
    public static final String CENTRAL = Validator.domainNullable("CENTRAL", Property.get("CENTRAL", "central", "/META-INF/loghub/CENTRAL", ConfigDefaults.central));
    public static final String ORGANIZATION = Validator.nameNullable("ORGANIZATION", Property.get("ORGANIZATION", "organization", "/META-INF/loghub/ORGANIZATION", ConfigDefaults.organization));
    public static final String ENVIRONMENT = Validator.nameNullable("ENVIRONMENT", Property.get("ENVIRONMENT", "environment", "/META-INF/loghub/ENVIRONMENT", ConfigDefaults.environment));
    public static final String REGISTRY = Validator.domainNullable("REGISTRY", Property.get("REGISTRY", "registry", "/META-INF/loghub/REGISTRY", ConfigDefaults.registry));
    public static final String GROUP = Validator.nameWithDotsNullable("GROUP", Property.get("GROUP", "group", "/META-INF/loghub/GROUP", ConfigDefaults.group));
    public static final String APPLICATION = Validator.nameNullable("APPLICATION", Property.get("APPLICATION", "application", "/META-INF/loghub/APPLICATION", ConfigDefaults.application));
    public static final String VERSION = Validator.versionNullable("VERSION", Property.get("VERSION", "version", "/META-INF/loghub/VERSION", ConfigDefaults.version));
    public static final String REVISION = Validator.revisionNullable("REVISION", Property.get("REVISION", "revision", "/META-INF/loghub/REVISION", ConfigDefaults.revision));
    public static final String NAME = Property.get("NAME", "name", "/META-INF/loghub/NAME", ConfigDefaults.name);
    public static final String DESCRIPTION = Property.get("DESCRIPTION", "description", "/META-INF/loghub/DESCRIPTION", ConfigDefaults.description);
    public static final String REPOSITORY = Property.get("REPOSITORY", "repository", "/META-INF/loghub/REPOSITORY", ConfigDefaults.repository);
    public static final String JOB = Property.get("JOB", "job", "/META-INF/loghub/JOB", ConfigDefaults.job);
    public static final String PIPELINE = Property.get("PIPELINE", "pipeline", "/META-INF/loghub/PIPELINE", ConfigDefaults.pipeline);
    public static final String BRANCH = Property.get("BRANCH", "branch", "/META-INF/loghub/BRANCH", ConfigDefaults.branch);
    public static final String COMMIT = Property.get("COMMIT", "commit", "/META-INF/loghub/COMMIT", ConfigDefaults.commit);
    public static final String COMMIT_BEFORE = Property.get("COMMIT_BEFORE", "commitBefore", "/META-INF/loghub/COMMIT_BEFORE", ConfigDefaults.commitBefore);
    public static final String COMMIT_MESSAGE = Property.get("COMMIT_MESSAGE", "commitMessage", "/META-INF/loghub/COMMIT_MESSAGE", ConfigDefaults.commitMessage);
    public static final String MAINTAINER = Property.get("MAINTAINER", "maintainer", "/META-INF/loghub/MAINTAINER", ConfigDefaults.maintainer);
    public static final String MAINTAINER_NAME = Property.get("MAINTAINER_NAME", "maintainerName", "/META-INF/loghub/MAINTAINER_NAME", ConfigDefaults.maintainerName);
    public static final String MAINTAINER_EMAIL = Property.get("MAINTAINER_EMAIL", "maintainerEmail", "/META-INF/loghub/MAINTAINER_EMAIL", ConfigDefaults.maintainerEmail);
    public static final String INSTANCE = Validator.instanceNullable("INSTANCE", Property.get("INSTANCE", "instance", "/META-INF/loghub/INSTANCE", ConfigDefaults.instance));

    private Config() {
    }
}
