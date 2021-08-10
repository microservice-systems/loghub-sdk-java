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

package systems.microservice.loghub.sdk;

import systems.microservice.loghub.sdk.config.ConfigPropertyListener;
import systems.microservice.loghub.sdk.config.ConfigPropertyValidator;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public interface Property<T> {
    public T get();
    public void addValidator(ConfigPropertyValidator validator);
    public void removeValidator(ConfigPropertyValidator validator);
    public void addListener(ConfigPropertyListener listener);
    public void removeListener(ConfigPropertyListener listener);
}
