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

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class TimeUtil {
    public static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("UTC");

    private TimeUtil() {
    }

    public static long currentTimeMicro() {
        return System.nanoTime() / 1000L;
    }

    public static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String format(long time) {
        return String.format("%1$tY.%1$tm.%1$td %1$tH:%1$tM:%1$tS.%1$tL", time);
    }

    public static String formatPath(long time) {
        Calendar calendar = new GregorianCalendar(TIMEZONE_UTC);
        calendar.setTimeInMillis(time);
        return String.format("/%04d/%02d/%02d/%02d/%02d/%02d/%03d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND),
                calendar.get(Calendar.MILLISECOND));
    }

    public static String formatMicroSpan(long time) {
        long timeHigh = time / 1000L;
        long timeLow = time % 1000L;
        return String.format("%d.%03d", timeHigh, timeLow);
    }

    public static Date parseDate(String date) {
        try {
            if (date != null) {
                String[] items = date.split("/");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.parseInt(items[2]), Integer.parseInt(items[0]) - 1, Integer.parseInt(items[1]));
                return new Date(calendar.getTimeInMillis());
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public static Integer getAge(Date dateOfBirth) {
        try {
            if (dateOfBirth != null) {
                Calendar today = new GregorianCalendar(TIMEZONE_UTC);
                Calendar birthDate = new GregorianCalendar(TIMEZONE_UTC);
                birthDate.setTime(dateOfBirth);
                int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
                if (age > 0) {
                    return age;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }
}
