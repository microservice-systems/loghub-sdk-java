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

package systems.microservice.loghub.facade.event;

import systems.microservice.loghub.facade.io.FormatInputStream;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class EventConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String central;
    public final String organization;
    public final String environment;
    public final String registry;
    public final String group;
    public final String application;
    public final String version;
    public final String revision;
    public final String name;
    public final String description;
    public final String repository;
    public final String job;
    public final String pipeline;
    public final String branch;
    public final String commit;
    public final String commitBefore;
    public final String commitMessage;
    public final String maintainer;
    public final String maintainerName;
    public final String maintainerEmail;
    public final String instance;

    public EventConfig(FormatInputStream input) {
        this.central = null;
        this.organization = null;
        this.environment = null;
        this.registry = null;
        this.group = null;
        this.application = null;
        this.version = null;
        this.revision = null;
        this.name = null;
        this.description = null;
        this.repository = null;
        this.job = null;
        this.pipeline = null;
        this.branch = null;
        this.commit = null;
        this.commitBefore = null;
        this.commitMessage = null;
        this.maintainer = null;
        this.maintainerName = null;
        this.maintainerEmail = null;
        this.instance = null;
    }

    public EventConfig(String central, String organization, String environment, String registry, String group, String application, String version, String revision, String name, String description, String repository, String job, String pipeline, String branch, String commit, String commitBefore, String commitMessage, String maintainer, String maintainerName, String maintainerEmail, String instance) {
        this.central = central;
        this.organization = organization;
        this.environment = environment;
        this.registry = registry;
        this.group = group;
        this.application = application;
        this.version = version;
        this.revision = revision;
        this.name = name;
        this.description = description;
        this.repository = repository;
        this.job = job;
        this.pipeline = pipeline;
        this.branch = branch;
        this.commit = commit;
        this.commitBefore = commitBefore;
        this.commitMessage = commitMessage;
        this.maintainer = maintainer;
        this.maintainerName = maintainerName;
        this.maintainerEmail = maintainerEmail;
        this.instance = instance;
    }
}
