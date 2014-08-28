package com.derbysoft.common.hibernate.usertype;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.util.compare.EqualsHelper;
import org.hibernate.usertype.CompositeUserType;

import java.io.Serializable;

/**
 * @author zhupan
 * @version 1.9
 */
public abstract class AbstractCompositeUserType implements CompositeUserType {

    @Override
    public Object assemble(Serializable cached, SessionImplementor session, Object owner) {
        return cached;
    }

    @Override
    public Object deepCopy(Object value) {
        return value;
    }

    @Override
    public Serializable disassemble(Object value, SessionImplementor session) {
        return (Serializable) value;
    }

    @Override
    public boolean equals(Object x, Object y) {
        return EqualsHelper.equals(x, y);
    }

    @Override
    public int hashCode(Object x) {
        return x.hashCode();
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object replace(Object original, Object target, SessionImplementor session, Object owner) {
        return original;
    }

    @Override
    public void setPropertyValue(Object component, int property, Object value) {
        throw new UnsupportedOperationException("Immutable!");
    }

}