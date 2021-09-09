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

package systems.microservice.loghub.sdk.serializer.handler;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import systems.microservice.loghub.sdk.serializer.Serializer;
import systems.microservice.loghub.sdk.serializer.SerializerException;
import systems.microservice.loghub.sdk.serializer.SerializerHandler;
import systems.microservice.loghub.sdk.serializer.SerializerOperation;
import systems.microservice.loghub.sdk.util.ByteArrayInputStream;
import systems.microservice.loghub.sdk.util.ByteArrayOutputStream;
import systems.microservice.loghub.sdk.util.StringBuilderWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.Writer;

/**
 * @author Dmitry Kotlyarov
 * @since 1.0
 */
public class SerializerDocumentHandler implements SerializerHandler, Serializable {
    private static final long serialVersionUID = 1L;

    public SerializerDocumentHandler() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(byte[] array, Class<T> clazz) {
        if (!clazz.equals(Document.class)) {
            throw new IllegalArgumentException("Argument 'clazz' must be 'Document.class'");
        }

        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder bld = f.newDocumentBuilder();
            try (ByteArrayInputStream ain = new ByteArrayInputStream(array)) {
                return (T) bld.parse(new InputSource(ain));
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new SerializerException(Serializer.DOCUMENT, SerializerOperation.READ, clazz, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(InputStream input, Class<T> clazz) {
        if (!clazz.equals(Document.class)) {
            throw new IllegalArgumentException("Argument 'clazz' must be 'Document.class'");
        }

        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder bld = f.newDocumentBuilder();
            return (T) bld.parse(new InputSource(input));
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new SerializerException(Serializer.DOCUMENT, SerializerOperation.READ, clazz, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(String string, Class<T> clazz) {
        if (!clazz.equals(Document.class)) {
            throw new IllegalArgumentException("Argument 'clazz' must be 'Document.class'");
        }

        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder bld = f.newDocumentBuilder();
            try (StringReader r = new StringReader(string)) {
                return (T) bld.parse(new InputSource(r));
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new SerializerException(Serializer.DOCUMENT, SerializerOperation.READ, clazz, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(Reader reader, Class<T> clazz) {
        if (!clazz.equals(Document.class)) {
            throw new IllegalArgumentException("Argument 'clazz' must be 'Document.class'");
        }

        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder bld = f.newDocumentBuilder();
            return (T) bld.parse(new InputSource(reader));
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new SerializerException(Serializer.DOCUMENT, SerializerOperation.READ, clazz, e);
        }
    }

    @Override
    public <T> byte[] write(T object) {
        if (!(object instanceof Document)) {
            throw new IllegalArgumentException("Argument 'object' must be an instance of 'Document.class'");
        }

        try {
            Document doc = (Document) object;
            Transformer t = TransformerFactory.newInstance().newTransformer();
            Source s = new DOMSource(doc);
            ByteArrayOutputStream aout = new ByteArrayOutputStream(4096);
            try (ByteArrayOutputStream aout1 = aout) {
                Result r = new StreamResult(aout1);
                t.transform(s, r);
            }
            return aout.toByteArray();
        } catch (IOException | TransformerException e) {
            throw new SerializerException(Serializer.DOCUMENT, SerializerOperation.WRITE, object.getClass(), e);
        }
    }

    @Override
    public <T> OutputStream write(T object, OutputStream output) {
        if (!(object instanceof Document)) {
            throw new IllegalArgumentException("Argument 'object' must be an instance of 'Document.class'");
        }

        try {
            Document doc = (Document) object;
            Transformer t = TransformerFactory.newInstance().newTransformer();
            Source s = new DOMSource(doc);
            Result r = new StreamResult(output);
            t.transform(s, r);
            return output;
        } catch (TransformerException e) {
            throw new SerializerException(Serializer.DOCUMENT, SerializerOperation.WRITE, object.getClass(), e);
        }
    }

    @Override
    public <T> String writeS(T object) {
        if (!(object instanceof Document)) {
            throw new IllegalArgumentException("Argument 'object' must be an instance of 'Document.class'");
        }

        try {
            Document doc = (Document) object;
            Transformer t = TransformerFactory.newInstance().newTransformer();
            Source s = new DOMSource(doc);
            StringBuilderWriter sbw = new StringBuilderWriter(4096);
            try (StringBuilderWriter sbw1 = sbw) {
                Result r = new StreamResult(sbw1);
                t.transform(s, r);
            }
            return sbw.toString();
        } catch (TransformerException e) {
            throw new SerializerException(Serializer.DOCUMENT, SerializerOperation.WRITE, object.getClass(), e);
        }
    }

    @Override
    public <T> Writer write(T object, Writer writer) {
        if (!(object instanceof Document)) {
            throw new IllegalArgumentException("Argument 'object' must be an instance of 'Document.class'");
        }

        try {
            Document doc = (Document) object;
            Transformer t = TransformerFactory.newInstance().newTransformer();
            Source s = new DOMSource(doc);
            Result r = new StreamResult(writer);
            t.transform(s, r);
            return writer;
        } catch (TransformerException e) {
            throw new SerializerException(Serializer.DOCUMENT, SerializerOperation.WRITE, object.getClass(), e);
        }
    }
}
