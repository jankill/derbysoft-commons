package com.derbysoft.common.service;

import com.derbysoft.common.repository.CommonRepository;
import com.derbysoft.common.util.ReflectionUtils;
import com.derbysoft.common.paginater.Paginater;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * @since 2009-3-19
 * @author zhupan
 * @version 1.0
 */
public abstract class CommonService<T> {

    @Autowired
    @Qualifier("commonRepository")
    protected CommonRepository commonRepository;

    private Class<T> clazz;

    @SuppressWarnings({"unchecked"})
    public CommonService() {
        this.clazz = ReflectionUtils.getSuperClassGenericType(getClass());
    }

    public T load(Long id) {
        return id == null ? null : commonRepository.load(clazz, id);
    }

    public T load(String propertyName, Object propertyValue) {
        return commonRepository.load(clazz, propertyName, propertyValue);
    }

    public T load(String[] propertyNames, Object[] properties) {
        return commonRepository.load(clazz, propertyNames, properties);
    }

    public List<T> loadAll() {
        return commonRepository.loadAll(clazz);
    }

    public List<T> find(DetachedCriteria criteria) {
        return commonRepository.find(criteria);
    }

    public List find(String queryString, Object... values) {
        return commonRepository.find(queryString, values);
    }

    @Transactional(readOnly = false)
    public T save(T o) {
        commonRepository.save(o);
        return o;
    }

    @Transactional(readOnly = false)
    public void delete(T o) {
        commonRepository.delete(o);
    }

    public Paginater paginater(Paginater paginater) {
        return paginater(DetachedCriteria.forClass(clazz), paginater);
    }

    public Paginater paginater(DetachedCriteria detachedCriteria, Paginater paginater) {
        return commonRepository.paginater(detachedCriteria, paginater);
    }

    protected void eq(DetachedCriteria detachedCriteria, String property, Object value) {
        if (value == null) {
            return;
        }
        if (String.class.isInstance(value) && StringUtils.isBlank((String) value)) {
            return;
        }
        if (String.class.isInstance(value)) {
            detachedCriteria.add(Restrictions.eq(property, StringUtils.trim((String) value)));
        } else {
            detachedCriteria.add(Restrictions.eq(property, value));
        }
    }

    protected void notEq(DetachedCriteria detachedCriteria, String property, Object value) {
        if (value == null) {
            return;
        }
        if (String.class.isInstance(value) && StringUtils.isBlank((String) value)) {
            return;
        }
        if (String.class.isInstance(value)) {
            detachedCriteria.add(Restrictions.not(Restrictions.eq(property, StringUtils.trim((String) value))));
        } else {
            detachedCriteria.add(Restrictions.not(Restrictions.eq(property, value)));
        }
    }

    protected void ilike(DetachedCriteria detachedCriteria, String property, Object value) {
        if (value == null) {
            return;
        }
        if (String.class.isInstance(value) && StringUtils.isBlank((String) value)) {
            return;
        }
        if (String.class.isInstance(value)) {
            detachedCriteria.add(Restrictions.ilike(property, StringUtils.trim((String) value), MatchMode.ANYWHERE));
        } else {
            detachedCriteria.add(Restrictions.like(property, value));
        }
    }

    protected void notIlike(DetachedCriteria detachedCriteria, String property, Object value) {
        if (value == null) {
            return;
        }
        if (String.class.isInstance(value) && StringUtils.isBlank((String) value)) {
            return;
        }
        if (String.class.isInstance(value)) {
            detachedCriteria.add(Restrictions.not(Restrictions.ilike(property, StringUtils.trim((String) value), MatchMode.ANYWHERE)));
        } else {
            detachedCriteria.add(Restrictions.not(Restrictions.like(property, value)));
        }
    }

    public void lt(DetachedCriteria detachedCriteria, String property, Object value) {
        if (value == null) {
            return;
        }
        if (String.class.isInstance(value) && StringUtils.isBlank((String) value)) {
            return;
        }
        if (String.class.isInstance(value)) {
            detachedCriteria.add(Restrictions.lt(property, StringUtils.trim((String) value)));
        } else {
            detachedCriteria.add(Restrictions.lt(property, value));
        }
    }

    public void le(DetachedCriteria detachedCriteria, String property, Object value) {
        if (value == null) {
            return;
        }
        if (String.class.isInstance(value) && StringUtils.isBlank((String) value)) {
            return;
        }
        if (String.class.isInstance(value)) {
            detachedCriteria.add(Restrictions.le(property, StringUtils.trim((String) value)));
        } else {
            detachedCriteria.add(Restrictions.le(property, value));
        }
    }


    public void gt(DetachedCriteria detachedCriteria, String property, Object value) {
        if (value == null) {
            return;
        }
        if (String.class.isInstance(value) && StringUtils.isBlank((String) value)) {
            return;
        }
        if (String.class.isInstance(value)) {
            detachedCriteria.add(Restrictions.gt(property, StringUtils.trim((String) value)));
        } else {
            detachedCriteria.add(Restrictions.gt(property, value));
        }
    }

    public void ge(DetachedCriteria detachedCriteria, String property, Object value) {
        if (value == null) {
            return;
        }
        if (String.class.isInstance(value) && StringUtils.isBlank((String) value)) {
            return;
        }
        if (String.class.isInstance(value)) {
            detachedCriteria.add(Restrictions.ge(property, StringUtils.trim((String) value)));
        } else {
            detachedCriteria.add(Restrictions.ge(property, value));
        }
    }

    public void setCommonRepository(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }
}