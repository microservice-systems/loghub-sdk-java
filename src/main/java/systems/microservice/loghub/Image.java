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

package systems.microservice.loghub;

import systems.microservice.loghub.config.Validator;
import systems.microservice.loghub.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public final class Image implements Serializable {
    private static final long serialVersionUID = 1L;

    public final UUID guid;
    public final byte[] content;
    public final String contentType;

    public Image(byte[] content, String contentType) {
        Validator.notNull("content", content);
        Validator.notNull("contentType", contentType);

        this.guid = UUID.randomUUID();
        this.content = content;
        this.contentType = contentType;
    }

    public static String getContentType(String format) {
        Validator.notNull("format", format);

        if (format.equals("jpeg")) {
            return "image/jpeg";
        } else if (format.equals("png")) {
            return "image/png";
        } else if (format.equals("gif")) {
            return "image/gif";
        } else if (format.equals("bmp")) {
            return "image/bmp";
        } else if (format.equals("webmp")) {
            return "image/webmp";
        } else if (format.equals("svg")) {
            return "image/svg";
        } else {
            throw new IllegalArgumentException(String.format("Image format '%s' is not found", format));
        }
    }

    public static Image takeScreenshot(String format) {
        Validator.notNull("format", format);

        try {
            Rectangle r = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage bi = new Robot().createScreenCapture(r);
            try (ByteArrayOutputStream out = new ByteArrayOutputStream(131072)) {
                if (ImageIO.write(bi, format, out)) {
                    return new Image(out.toByteArray(), getContentType(format));
                } else {
                    throw new IllegalArgumentException(String.format("Image IO format '%s' is not found", format));
                }
            }
        } catch (AWTException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
