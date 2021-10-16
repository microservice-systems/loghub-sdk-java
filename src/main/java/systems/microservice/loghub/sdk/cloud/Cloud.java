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
public enum Cloud {
    AC("ac", "Alibaba Cloud", "https://www.alibabacloud.com"),
    AWS("aws", "Amazon Web Services", "https://aws.amazon.com"),
    AZURE("azure", "Microsoft Azure Cloud", "https://azure.microsoft.com"),
    GCP("gcp", "Google Cloud Platform", "https://cloud.google.com"),
    IBM("ibm", "IBM Cloud", "https://www.ibm.com/cloud"),
    MCS("mcs", "Mail.ru Cloud Solutions", "https://mcs.mail.ru"),
    OCI("oci", "Oracle Cloud Infrastructure", "https://www.oracle.com/cloud/"),
    SBER("sber", "SberCloud", "https://sbercloud.ru/en"),
    SCP("scp", "Selectel Cloud Platform", "https://selectel.ru/services/cloud/"),
    YC("yc", "Yandex Cloud", "https://cloud.yandex.ru");

    public final String code;
    public final String name;
    public final String url;

    Cloud(String code, String name, String url) {
        this.code = code;
        this.name = name;
        this.url = url;
    }
}
