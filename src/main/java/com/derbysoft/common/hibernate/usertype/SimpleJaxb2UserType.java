package com.derbysoft.common.hibernate.usertype;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zhupan
 * @version 1.9
 */
public class SimpleJaxb2UserType extends AbstractJaxb2UserType {

    @Override
    public String[] getPropertyNames() {
        return new String[]{"xml"};
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[]{StandardBasicTypes.STRING};
    }

    @Override
    public Object getPropertyValue(Object component, int property) {
        return marshall(component);
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws SQLException {
        String xml = (String) StandardBasicTypes.STRING.nullSafeGet(rs, names, session, owner);
        if (StringUtils.isEmpty(xml)) {
            return null;
        }
        return unmarshall(xml);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws SQLException {
        String xml = null;
        if (value != null) {
            xml = marshall(value);
        }
        StandardBasicTypes.STRING.nullSafeSet(st, xml, index, session);
    }

}
