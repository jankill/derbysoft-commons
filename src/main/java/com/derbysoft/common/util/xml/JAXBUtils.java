package com.derbysoft.common.util.xml;

import com.derbysoft.common.util.collect.Maps;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.util.Map;

public abstract class JAXBUtils {

    private static final String JAXB_FORMATTED_OUTPUT = "jaxb.formatted.output";

    public static String marshal(Object message) {
        return marshal(message, Maps.<String, Object>newHashMap());
    }

    public static void marshal(Object message, Result result) {
        marshal(message, result, Maps.<String, Object>newHashMap());
    }

    public static String marshalWithFormat(Object message) {
        return marshal(message, Maps.map(JAXB_FORMATTED_OUTPUT, true));
    }

    public static void marshal(Object message, Result result, Map<String, Object> properties) {
        Class<?> msgClass = message.getClass();
        try {
            Marshaller marshaller = JAXBContext.newInstance(message.getClass()).createMarshaller();
            for (Map.Entry<String, Object> entry : properties.entrySet()) {
                marshaller.setProperty(entry.getKey(), entry.getValue());
            }
            if (msgClass.getAnnotation(XmlRootElement.class) != null) {
                marshaller.marshal(message, result);
            } else if (msgClass.getAnnotation(XmlType.class) != null) {
                QName qName = new QName(msgClass.getPackage().getAnnotation(XmlSchema.class).namespace(), msgClass.getSimpleName());
                marshaller.marshal(new JAXBElement(qName, msgClass, message), result);
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static String marshal(Object message, Map<String, Object> properties) {
        StringResult result = new StringResult();
        marshal(message, result, properties);
        return result.toString();
    }

    public static <T> T unmarshal(String xml, Class<T> clazz) {
        return unmarshal(new StringSource(xml), clazz);
    }

    public static <T> T unmarshal(InputStream inputStream, Class<T> clazz) {
        return unmarshal(new StreamSource(inputStream), clazz);
    }

    public static <T> T unmarshal(Source source, Class<T> clazz) {
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            JAXBElement<T> element = context.createUnmarshaller().unmarshal(source, clazz);
            return element.getValue();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}
