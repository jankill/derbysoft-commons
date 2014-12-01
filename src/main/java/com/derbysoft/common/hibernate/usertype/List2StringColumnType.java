package com.derbysoft.common.hibernate.usertype;

import com.derbysoft.common.util.SeparatorStringBuilder;
import com.derbysoft.common.util.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class List2StringColumnType implements UserType {

    private static final String SPLITTER = ";";

    private static final int[] TYPES = new int[]{Types.VARCHAR};

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        Object value = StandardBasicTypes.STRING.nullSafeGet(rs, names[0], session, owner);
        if (value == null) {
            return null;
        }
        return parse(value.toString());
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value instanceof List) {
            StandardBasicTypes.STRING.nullSafeSet(st, disassemble((List) value), index, session);
            return;
        }
        StandardBasicTypes.STRING.nullSafeSet(st, null, index, session);
    }

    @SuppressWarnings("unchecked")
    public Object deepCopy(Object value) throws HibernateException {
        if (value == null) {
            return null;
        }
        List sourceList = (List) value;
        List targetList = new ArrayList();
        targetList.addAll(sourceList);
        return targetList;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    private String disassemble(List values) throws HibernateException {
        if (Lists.isEmpty(values)) {
            return "";
        }
        SeparatorStringBuilder sb = new SeparatorStringBuilder(SPLITTER);
        for (Object value : values) {
            sb.append(value);
        }
        return sb.toString();
    }

    private Object parse(String value) {
        List<String> quotes = new ArrayList<String>();
        Collections.addAll(quotes, StringUtils.split(value, SPLITTER));
        return quotes;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public int[] sqlTypes() {
        return TYPES;
    }

    @Override
    public Class returnedClass() {
        return List.class;
    }

    @Override
    public boolean equals(Object one, Object another) throws HibernateException {
        if (one == another) {
            return true;
        }
        if (one != null && another != null) {
            List a = (List) one;
            List b = (List) another;
            if (a.size() != b.size()) {
                return false;
            }
            for (int i = 0; i < a.size(); i++) {
                String str1 = (String) a.get(i);
                String str2 = (String) b.get(i);
                if (str1 == null || str2 == null) {
                    return false;
                }
                if (!str1.equals(str2)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }
}
