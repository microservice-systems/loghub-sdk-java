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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class NetUtil {
    private NetUtil() {
    }

    public static String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getHostIp(String host) {
        try {
            return InetAddress.getByName(host).getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLocalMac() {
        try {
            NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0, ci = mac.length; i < ci; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < ci - 1) ? "-" : ""));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getHostMac(String host) {
        try {
            NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getByName(host));
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0, ci = mac.length; i < ci; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < ci - 1) ? "-" : ""));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
