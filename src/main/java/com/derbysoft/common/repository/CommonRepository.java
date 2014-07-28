package com.derbysoft.common.repository;

import com.derbysoft.common.paginater.Paginater;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.internal.CriteriaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-3-19
 */
@SuppressWarnings({"unchecked"})
public class CommonRepository {

    private SessionFactory sessionFactory;

    private static final int THREE = 3;

    public <T> T load(Class<T> clazz, Serializable id) {
        return (T) getSession().get(clazz, id);
    }

    public <T> T load(Class<T> clazz, String propertyName, Object propertyValue) {
        return load(clazz, new String[]{propertyName}, new Object[]{propertyValue});
    }

    public <T> T load(Class<T> clazz, String[] propertyNames, Object[] propertyValues) {
        return (T) createQuery(clazz, propertyNames, propertyValues).uniqueResult();
    }

    public <T> T load(DetachedCriteria criteria) {
        return (T) createCriteria(criteria).uniqueResult();
    }

    public List find(DetachedCriteria criteria) {
        return createCriteria(criteria).list();
    }

    public List findByQuery(String queryString, Object... values) {
        return createQuery(queryString, values).list();
    }

    public <T> List<T> find(Class<T> clazz, String propertyName, Object propertyValue) {
        return find(clazz, new String[]{propertyName}, new Object[]{propertyValue});
    }

    public <T> List<T> find(Class<T> clazz, String[] propertyNames, Object[] propertyValues) {
        return createQuery(clazz, propertyNames, propertyValues).list();
    }

    public <T> List<T> loadAll(Class<T> entityClass) {
        Criteria criteria = getSession().createCriteria(entityClass);
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    public void save(Object entity) {
        getSession().saveOrUpdate(entity);
    }

    public void delete(Object entity) {
        getSession().delete(entity);
    }

    public <T> List<T> find(DetachedCriteria detachedCriteria, Integer firstResult, Integer maxResults) {
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        return criteria.setFirstResult(firstResult).setMaxResults(maxResults).list();
    }

    public Paginater paginate(DetachedCriteria detachedCriteria, Paginater paginater) {
        sort(detachedCriteria, paginater);
        paginater.setTotalCount(count(detachedCriteria));
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        criteria.setFirstResult(paginater.getFirstResult());
        criteria.setMaxResults(paginater.getMaxResults());
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        List results = criteria.list();
        paginater.setObjects(results);
        return paginater;
    }

    private void sort(DetachedCriteria detachedCriteria, Paginater paginater) {
        if (StringUtils.isBlank(paginater.getSortField())) {
            return;
        }
        boolean isDesc = StringUtils.equalsIgnoreCase("desc", StringUtils.trim(paginater.getSortDirection()));
        detachedCriteria.addOrder(isDesc ? Order.desc(paginater.getSortField()) : Order.asc(paginater.getSortField()));
    }

    private long count(DetachedCriteria detachedCriteria) {
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        Projection originalProjection = ((CriteriaImpl) criteria).getProjection();
        Object count = criteria.setProjection(Projections.countDistinct("id")).uniqueResult();
        detachedCriteria.setProjection(originalProjection);
        if (count == null) {
            return 0;
        }
        return (Long) count;
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    private Criteria createCriteria(DetachedCriteria criteria) {
        return criteria.getExecutableCriteria(getSession()).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
    }

    private Query createQuery(Class<?> clazz, String[] propertyNames, Object[] values) {
        Query query = getSession().createQuery(createQueryString(clazz, propertyNames));
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(propertyNames[i], values[i]);
            }
        }
        return query;
    }

    private String createQueryString(Class<?> clazz, String[] propertyNames) {
        String hql = "from " + clazz.getSimpleName() + " _alias";
        StringBuilder sb = new StringBuilder();
        if (propertyNames != null) {
            for (String propertyName : propertyNames) {
                sb.append(" _alias.").append(propertyName).append(" = :").append(propertyName).append(" and");
            }
            hql = hql + " where " + sb.substring(0, sb.length() - THREE);
        }
        return hql;
    }

    private Query createQuery(String queryString, Object[] values) {
        Query query = getSession().createQuery(queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query;
    }

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}