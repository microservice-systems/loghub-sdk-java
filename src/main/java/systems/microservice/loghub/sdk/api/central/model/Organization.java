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

package systems.microservice.loghub.sdk.api.central.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String domain;
    public final String id;
    public final long createTime;
    public final String createUser;
    public long updateTime;
    public String updateUser;
    public String name;
    public String description;
    public String url;
    public String location;
    public String picture;

    public Organization(@JsonProperty("domain") String domain,
                        @JsonProperty("id") String id,
                        @JsonProperty("createTime") long createTime,
                        @JsonProperty("createUser") String createUser,
                        @JsonProperty("updateTime") long updateTime,
                        @JsonProperty("updateUser") String updateUser,
                        @JsonProperty("name") String name,
                        @JsonProperty("description") String description,
                        @JsonProperty("url") String url,
                        @JsonProperty("location") String location,
                        @JsonProperty("picture") String picture) {
        this.domain = domain;
        this.id = id;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
        this.name = name;
        this.description = description;
        this.url = url;
        this.location = location;
        this.picture = picture;
    }
}
