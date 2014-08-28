package com.derbysoft.common.hibernate.usertype;

import com.derbysoft.common.util.Charsets;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.usertype.ParameterizedType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.xml.transform.StringResult;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.util.Properties;

/**
 * @author zhupan
 * @version 1.9
 */
public abstract class AbstractJaxb2UserType extends AbstractCompositeUserType implements ParameterizedType {

    private static Log logger = LogFactory.getLog(AbstractJaxb2UserType.class);

    public static final String ENTITY_TYPE_KEY = "entityType";

    private Class<?> entityType;

    private static Jaxb2Marshaller jaxb2Marshaller;

    @Override
    public void setParameterValues(Properties parameters) {
        String entityTypeAsString = parameters.getProperty(ENTITY_TYPE_KEY);
        try {
            entityType = Thread.currentThread().getContextClassLoader().loadClass(entityTypeAsString);
        } catch (ClassNotFoundException e) {
            throw new HibernateException("Class [" + entityTypeAsString + "] not found", e);
        }
    }

    protected static String marshall(Object o) {
        initialize(o);
        Result result = new StringResult();
        jaxb2Marshaller.marshal(o, result);
        return result.toString();
    }

    protected Object unmarshall(String xml) {
        return jaxb2Marshaller.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes(Charsets.UTF_8))));
    }

    @Override
    public Class<?> returnedClass() {
        return entityType;
    }

    private static void initialize(Object obj) {
        if (jaxb2Marshaller != null) {
            return;
        }
        jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath(obj.getClass().getPackage().getName());
        try {
            jaxb2Marshaller.afterPropertiesSet();
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

}
