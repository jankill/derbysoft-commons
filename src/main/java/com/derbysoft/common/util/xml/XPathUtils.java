package com.derbysoft.common.util.xml;

import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.w3c.dom.Node;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import java.util.HashMap;
import java.util.Map;

public class XPathUtils {
    private static final String NS_PREFIX = "ns";

    public static XPathExpression createExpression(Node node, String prefix, String xpath) {
        return createExpression(node.getNamespaceURI(), prefix, xpath);
    }

    public static XPathExpression createExpression(String ns, String prefix, String xpath) {
        Map<String, String> nsMap = new HashMap<String, String>();
        nsMap.put(prefix, ns);
        return XPathExpressionFactory.createXPathExpression(xpath, nsMap);
    }

    public static XPathExpression createExpression(Class<?> aClass) {
        Map<String, String> nsMap = new HashMap<String, String>();
        nsMap.put(NS_PREFIX, getNamespace(aClass));
        return XPathExpressionFactory.createXPathExpression("//" + NS_PREFIX + ":" + getName(aClass), nsMap);
    }

    private static String getNamespace(Class<?> aClass) {
        XmlSchema xmlSchema = aClass.getPackage().getAnnotation(XmlSchema.class);
        return xmlSchema.namespace();
    }

    private static String getName(Class<?> aClass) {
        XmlRootElement xmlRootElement = aClass.getAnnotation(XmlRootElement.class);
        return xmlRootElement.name();
    }
}
