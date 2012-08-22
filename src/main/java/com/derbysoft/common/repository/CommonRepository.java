package com.derbysoft.common.repository;

import com.derbysoft.common.paginater.Paginater;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.internal.CriteriaImpl;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-3-19
 */
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
        getSession().setFlushMode(FlushMode.MANUAL);
        T entity = (T) createQuery(createQueryString(clazz, propertyNames), propertyValues).uniqueResult();
        getSession().setFlushMode(FlushMode.AUTO);
        return entity;
    }

    public Object load(DetachedCriteria criteria) {
        return createCriteria(criteria).uniqueResult();
    }

    public List find(DetachedCriteria criteria) {
        return createCriteria(criteria).list();
    }

    public List find(String queryString, Object... values) {
        return createQuery(queryString, values).list();
    }

    public <T> List<T> find(Class<T> clazz, String propertyName, Object propertyValue) {
        return find(clazz, new String[]{propertyName}, new Object[]{propertyValue});
    }

    @SuppressWarnings({"unchecked"})
    public <T> List<T> find(Class<T> clazz, String[] propertyNames, Object[] propertyValues) {
        return createQuery(createQueryString(clazz, propertyNames), propertyValues).list();
    }

    public <T> List<T> loadAll(final Class<T> entityClass) {
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

    public Paginater paginater(DetachedCriteria detachedCriteria, Paginater paginater) {
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
        if (paginater.getSortField() != null && !paginater.getSortField().trim().equals("") && "desc".equalsIgnoreCase(paginater.getSortDirection())) {
            detachedCriteria.addOrder(Order.desc(paginater.getSortField()));
        } else if (paginater.getSortField() != null && !paginater.getSortField().trim().equals("")) {
            detachedCriteria.addOrder(Order.asc(paginater.getSortField()));
        }
    }

    private long count(DetachedCriteria detachedCriteria) {
        Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
        Projection originalProjection = ((CriteriaImpl) criteria).getProjection();
        Object count = criteria.setProjection(Projections.countDistinct("id")).uniqueResult();
        detachedCriteria.setProjection(originalProjection);
        return (Long) count;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    private Criteria createCriteria(DetachedCriteria criteria) {
        return criteria.getExecutableCriteria(getSession()).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
    }

    private Query createQuery(String queryString, Object... values) {
        Query query = getSession().createQuery(queryString);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i, values[i]);
            }
        }
        return query;
    }

    private String createQueryString(Class<?> clazz, String[] propertyNames) {
        String hql = "from " + clazz.getSimpleName() + " _alias";
        StringBuffer sb = new StringBuffer();
        if (propertyNames != null) {
            for (String propertyName : propertyNames) {
                sb.append(" _alias.").append(propertyName).append("=? and");
            }
            hql = hql + " where " + sb.substring(0, sb.length() - THREE);
        }
        return hql;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}