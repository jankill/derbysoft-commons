package com.derbysoft.common.util;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.StringReader;
import java.io.StringWriter;
/**
 * @since 2009-5-25
 * @author zhupan
 * @version 1.0
 */
public class XmlFormatUtils {

    public static String formatXml(String value) {
        if (value == null || "".equals(value.trim())) {
            return value;
        }
        String formatXml = value.trim();
        try {
            SAXReader reader = new SAXReader();
            StringReader stringReader = new StringReader(formatXml);
            Document doc = reader.read(stringReader);
            OutputFormat outputFormat = OutputFormat.createPrettyPrint();
            outputFormat.setEncoding("utf-8");
            StringWriter stringWriter = new StringWriter();
            XMLWriter writer = new XMLWriter(stringWriter, outputFormat);
            writer.write(doc);
            formatXml = stringWriter.toString();
            writer.close();
        } catch (Exception e) {
            return formatXml;
        }
        return formatXml.trim();
    }

}
