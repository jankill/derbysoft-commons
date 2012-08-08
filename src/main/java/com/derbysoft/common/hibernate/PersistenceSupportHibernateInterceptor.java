package com.derbysoft.common.hibernate;

import com.derbysoft.common.domain.PersistenceSupport;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Date;
/**
 * @since 2009-3-19
 * @author zhupan
 * @version 1.0
 */
public class PersistenceSupportHibernateInterceptor extends EmptyInterceptor {

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (PersistenceSupport.class.isInstance(entity)) {
            PersistenceSupport support = (PersistenceSupport) entity;
            boolean modified = false;
            for (int i = 0; i < propertyNames.length; i++) {
                if ("createTime".equals(propertyNames[i])) {
                    Date createTime = getCurrentTime();
                    state[i] = createTime;
                    support.setCreateTime(createTime);
                    modified = true;
                } else if ("lastModifyTime".equals(propertyNames[i])) {
                    Date updateTime = getCurrentTime();
                    state[i] = updateTime;
                    support.setLastModifyTime(updateTime);
                    modified = true;
                }
            }
            return modified;
        }
        return false;
    }

    @Override
    public boolean onFlushDirty(
            Object entity,
            Serializable id,
            Object[] currentState,
            Object[] previousState,
            String[] propertyNames,
            Type[] types) {

        if (PersistenceSupport.class.isInstance(entity)) {
            PersistenceSupport support = (PersistenceSupport) entity;
            boolean modified = false;
            for (int i = 0; i < propertyNames.length; i++) {
                if ("lastModifyTime".equals(propertyNames[i])) {
                    Date updateTime = getCurrentTime();
                    currentState[i] = updateTime;
                    support.setLastModifyTime(updateTime);
                    modified = true;
                }
            }
            return modified;
        }
        return false;
    }

    private Date getCurrentTime() {
        return new Date();
    }


}