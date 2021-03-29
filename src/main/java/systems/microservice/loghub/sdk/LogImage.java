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

import systems.microservice.loghub.sdk.util.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class LogImage {
    protected final LogImageFormat format;
    protected final byte[] data;

    public LogImage(LogImageFormat format, byte[] data) {
        this.format = format;
        this.data = data;
    }

    public LogImageFormat getFormat() {
        return format;
    }

    public byte[] getData() {
        return data;
    }

    public static LogImage takeScreenshot(LogImageFormat format) {
        try {
            Rectangle r = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage bi = new Robot().createScreenCapture(r);
            try (ByteArrayOutputStream out = new ByteArrayOutputStream(262144)) {
                if (ImageIO.write(bi, format.name, out)) {
                    return new LogImage(format, out.toByteArray());
                } else {
                    throw new IllegalArgumentException(String.format("Image IO format '%s' is not found", format.name));
                }
            }
        } catch (AWTException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
