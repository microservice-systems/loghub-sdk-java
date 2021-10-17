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

package systems.microservice.loghub.sdk.cloud;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public enum CloudRegion {
    AWS_US_EAST_1(Cloud.AWS, "us-east-1", "US East (N. Virginia)"),
    AWS_US_EAST_2(Cloud.AWS, "us-east-2", "US East (Ohio)"),
    AWS_US_WEST_1(Cloud.AWS, "us-west-1", "US West (N. California)"),
    AWS_US_WEST_2(Cloud.AWS, "us-west-2", "US West (Oregon)"),
    AWS_CA_CENTRAL_1(Cloud.AWS, "ca-central-1", "Canada (Central)"),
    AWS_EU_CENTRAL_1(Cloud.AWS, "eu-central-1", "Europe (Frankfurt)"),
    AWS_EU_WEST_1(Cloud.AWS, "eu-west-1", "Europe (Ireland)"),
    AWS_EU_WEST_2(Cloud.AWS, "eu-west-2", "Europe (London)"),
    AWS_EU_WEST_3(Cloud.AWS, "eu-west-3", "Europe (Paris)"),
    AWS_EU_SOUTH_1(Cloud.AWS, "eu-south-1", "Europe (Milan)"),
    AWS_EU_NORTH_1(Cloud.AWS, "eu-north-1", "Europe (Stockholm)"),
    AWS_ME_SOUTH_1(Cloud.AWS, "me-south-1", "Middle East (Bahrain)"),
    AWS_AP_EAST_1(Cloud.AWS, "ap-east-1", "Asia Pacific (Hong Kong)"),
    AWS_AP_SOUTH_1(Cloud.AWS, "ap-south-1", "Asia Pacific (Mumbai)"),
    AWS_AP_NORTHEAST_1(Cloud.AWS, "ap-northeast-1", "Asia Pacific (Tokyo)"),
    AWS_AP_NORTHEAST_2(Cloud.AWS, "ap-northeast-2", "Asia Pacific (Seoul)"),
    AWS_AP_NORTHEAST_3(Cloud.AWS, "ap-northeast-3", "Asia Pacific (Osaka)"),
    AWS_AP_SOUTHEAST_1(Cloud.AWS, "ap-southeast-1", "Asia Pacific (Singapore)"),
    AWS_AP_SOUTHEAST_2(Cloud.AWS, "ap-southeast-2", "Asia Pacific (Sydney)"),
    AWS_SA_EAST_1(Cloud.AWS, "sa-east-1", "South America (São Paulo)"),
    AWS_AF_SOUTH_1(Cloud.AWS, "af-south-1", "Africa (Cape Town)"),
    YC_RU_CENTRAL1(Cloud.YC, "ru-central1", "Russia (Vladimir, Ryazan, Moscow regions)");

    public final Cloud cloud;
    public final String key;
    public final String name;

    CloudRegion(Cloud cloud, String key, String name) {
        this.cloud = cloud;
        this.key = key;
        this.name = name;
    }
}
