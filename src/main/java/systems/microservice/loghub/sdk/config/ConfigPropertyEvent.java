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

package systems.microservice.loghub.sdk.config;

import systems.microservice.loghub.facade.Validator;

import java.io.Serializable;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class ConfigPropertyEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String key;
    public final ConfigProperty oldProperty;
    public final ConfigProperty newProperty;
    public final long time;

    public ConfigPropertyEvent(ConfigProperty oldProperty, ConfigProperty newProperty) {
        Validator.notNull("newProperty", newProperty);
        if (oldProperty != null) {
            if (!newProperty.key.equals(oldProperty.key)) {
                throw new IllegalArgumentException(String.format("New property key '%s' is not equal to old property key '%s'", newProperty.key, oldProperty.key));
            }
        }

        this.key = newProperty.key;
        this.oldProperty = oldProperty;
        this.newProperty = newProperty;
        this.time = System.currentTimeMillis();
    }
}
