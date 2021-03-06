package com.derbysoft.common.repository.partition;

import com.derbysoft.common.exception.SystemInternalException;
import com.derbysoft.common.hibernate.PersistenceSupportHibernateInterceptor;
import com.derbysoft.common.paginater.Paginater;
import com.derbysoft.common.util.ReflectionUtils;
import com.derbysoft.common.util.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.internal.CriteriaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Notes:
 * 1. Only support MYISAM engine.
 * 2. Only support annotation from javax.persistence.*;
 * <p>
 * <p>
 * <p>
 */
public abstract class AbstractPartitionTableRepository<T> implements PartitionTableRepository<T> {

    private static final ConcurrentMap<String, Boolean> EXISTED_TABLE_MAP = new ConcurrentHashMap<String, Boolean>();
    private static final ConcurrentMap<String, String> TABLE_TEMPLATE_MAP = new ConcurrentHashMap<String, String>();

    protected static final String LEFT_BRACKET = "(";
    protected static final String RIGHT_BRACKET = ")";

    private static final int VALUE_INDEX = 1;
    private static final String SQL_ESCAPE = "`";

    private static final String DEFAULT_ENGINE = "MYISAM";
    public static final String[] ENGINES = new String[]{"InnoDB", "MYISAM"};

    private boolean partitionRequired = true;

    @Autowired(required = false)
    @Qualifier("sessionFactory")
    protected SessionFactory sessionFactory;

    protected abstract String getTableSuffixForSave(T t);

    private Class<T> clazz;

    @SuppressWarnings("unchecked")
    public AbstractPartitionTableRepository() {
        this.clazz = ReflectionUtils.getSuperClassGenericType(getClass());
    }

    @Override
    public T save(T log) {
        List<T> logs = new ArrayList<T>();
        logs.add(log);
        saveAll(getTableSuffixForSave(log), logs);
        return log;
    }

    @Override
    public T save(String suffix, T log) {
        List<T> logs = new ArrayList<T>();
        logs.add(log);
        saveAll(suffix, logs);
        return log;
    }

    @Override
    public List<T> saveAll(String suffix, List<T> logs) {
        if (Lists.isEmpty(logs)) {
            return new ArrayList<T>();
        }
        Session session = null;
        try {
            createTableIfNotExisted(suffix);
            session = getSession(new InsertInterceptor(suffix));
            for (T log : logs) {
                session.save(log);
            }
        } catch (Exception ex) {
            throw new SystemInternalException(ex);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return logs;
    }

    @Override
    public List<T> saveAll(List<T> logs) {
        if (Lists.isEmpty(logs)) {
            return new ArrayList<T>();
        }
        return saveAll(getTableSuffixForSave(logs.get(0)), logs);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(String suffix, Long id) {
        Session session = null;
        try {
            if (notExist(suffix)) {
                return null;
            }
            session = getSession(new SelectInterceptor(suffix));
            T object = (T) session.get(clazz, id);
            loadOneToManyFields(object);
            return object;
        } catch (Exception ex) {
            throw new SystemInternalException(ex);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<T> find(String suffix, DetachedCriteria detachedCriteria) {
        return find(suffix, detachedCriteria, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> find(String suffix, DetachedCriteria detachedCriteria, boolean loadOneToManyFields) {
        Session session = null;
        try {
            if (notExist(suffix)) {
                return new ArrayList<T>();
            }
            session = getSession(new SelectInterceptor(suffix));
            List list = detachedCriteria.getExecutableCriteria(session).list();
            if (loadOneToManyFields) {
                loadOneToManyFields(list);
            }
            return list;
        } catch (Exception e) {
            /**
             * In order to prevent client receive 'TableNotFound' because of table has been dropped by table clean script.
             * That here resumed for this kind of exception.
             */
            if (e instanceof SQLGrammarException) {
                removeExistedTableName(suffix);
                return new ArrayList<T>();
            }
            throw new SystemInternalException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Paginater paginate(String suffix, DetachedCriteria detachedCriteria, Paginater paginater) {
        return paginate(suffix, detachedCriteria, paginater, false);
    }

    @Override
    public Paginater paginate(String suffix, DetachedCriteria detachedCriteria, Paginater paginater, boolean loadOneToManyFields) {
        sort(detachedCriteria, paginater);
        Session session = null;
        try {
            if (notExist(suffix)) {
                paginater.setTotalCount(0L);
                paginater.getPageItems().clear();
                return paginater;
            }
            session = getSession(new SelectInterceptor(suffix));
            Criteria criteria = detachedCriteria.getExecutableCriteria(session);
            paginater.setTotalCount(count(criteria, detachedCriteria));
            criteria.setFirstResult(paginater.getFirstResult());
            criteria.setMaxResults(paginater.getMaxResults());
            criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            List list = criteria.list();
            if (loadOneToManyFields) {
                loadOneToManyFields(list);
            }
            paginater.setObjects(list);
            return paginater;
        } catch (Exception e) {
            /**
             * In order to prevent client receive 'TableNotFound' because of table has been dropped by table clean script.
             * That here resumed for this kind of exception.
             */
            if (e instanceof SQLGrammarException) {
                removeExistedTableName(suffix);
                return paginater;
            }
            throw new SystemInternalException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void removeExistedTableName(String suffix) {
        for (String tableName : getPartitionalTables()) {
            String newTableName = getNewTableName(suffix, tableName);
            EXISTED_TABLE_MAP.remove(newTableName);
        }
    }

    private boolean notExist(String suffix) {
        for (String tableName : getPartitionalTables()) {
            String newTableName = getNewTableName(suffix, tableName);
            if (EXISTED_TABLE_MAP.get(newTableName) == null || !EXISTED_TABLE_MAP.get(newTableName)) {
                if (tableNotExist(newTableName)) {
                    return true;
                }
                EXISTED_TABLE_MAP.put(newTableName, true);
            }
        }
        return false;
    }

    private void sort(DetachedCriteria detachedCriteria, Paginater paginater) {
        if (StringUtils.isBlank(paginater.getSortField())) {
            return;
        }
        boolean isDesc = StringUtils.equalsIgnoreCase("desc", StringUtils.trim(paginater.getSortDirection()));
        detachedCriteria.addOrder(isDesc ? Order.desc(paginater.getSortField()) : Order.asc(paginater.getSortField()));
    }

    protected void createTableIfNotExisted(String suffix) {
        for (String tableName : getPartitionalTables()) {
            String newTableName = getNewTableName(suffix, tableName);
            if (tableIsNotExist(newTableName)) {
                createTable(suffix, tableName);
                EXISTED_TABLE_MAP.put(newTableName, true);
            }
        }
    }

    private boolean tableIsNotExist(String newTableName) {
        return (EXISTED_TABLE_MAP.get(newTableName) == null || !EXISTED_TABLE_MAP.get(newTableName)) && tableNotExist(newTableName);
    }

    private boolean tableNotExist(String tableName) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            String checkSQL = String.format("SHOW TABLES LIKE '%s';", tableName);
            return CollectionUtils.isEmpty(session.createSQLQuery(checkSQL).list());
        } catch (Exception ex) {
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void createTable(String suffix, String tableName) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            String template = TABLE_TEMPLATE_MAP.get(tableName);
            if (template == null) {
                Object[] keyValue = (Object[]) session.createSQLQuery("SHOW CREATE TABLE " + tableName + ";").list().get(0);
                template = StringUtils.replace(keyValue[VALUE_INDEX].toString(), "CREATE TABLE", "CREATE TABLE IF NOT EXISTS");
                if (StringUtils.isNotBlank(getEngine())) {
                    template = StringUtils.replaceEach(template, ENGINES, new String[]{getEngine(), getEngine()});
                }
                TABLE_TEMPLATE_MAP.put(tableName, template);
            }
            String newTableName = getNewTableName(suffix, tableName);

            session.createSQLQuery(StringUtils.replace(template, escape(tableName), escape(newTableName))).executeUpdate();
        } catch (Exception ex) {
            throw new SystemInternalException(ex);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private String escape(String name) {
        return StringUtils.join(SQL_ESCAPE, name, SQL_ESCAPE);
    }

    private long count(Criteria criteria, DetachedCriteria detachedCriteria) {
        Projection originalProjection = ((CriteriaImpl) criteria).getProjection();
        Object count = criteria.setProjection(Projections.countDistinct(getId())).uniqueResult();
        detachedCriteria.setProjection(originalProjection);
        if (count == null) {
            return 0;
        }
        return (Long) count;
    }

    private String getId() {
        List<Field> fields = getFields(clazz);
        for (Field field : fields) {
            if (matchAnnotation(field.getAnnotations(), Id.class)) {
                return field.getName();
            }
        }
        throw new SystemInternalException(String.format("Entity Id field not found : %s ", clazz.getName()));
    }

    private void loadOneToManyFields(List list) {
        for (Object object : list) {
            loadOneToManyFields(object);
        }
    }

    private void loadOneToManyFields(Object object) {
        for (String oneToManyField : getOneToManyFields(clazz)) {
            String getMethodName = String.format("get%s", oneToManyField.substring(0, 1).toUpperCase() + oneToManyField.substring(1));
            try {
                Hibernate.initialize(clazz.getMethod(getMethodName).invoke(object));
            } catch (Exception e) {
                throw new SystemInternalException(e);
            }
        }
    }

    private static List<Field> getFields(Class clazz) {
        List<Field> fields = new ArrayList<Field>();
        List<Class<?>> classes = ClassUtils.getAllSuperclasses(clazz);
        for (Class<?> aClass : classes) {
            fields.addAll(Arrays.asList(aClass.getDeclaredFields()));
        }
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return fields;
    }

    private List<String> getOneToManyFields(Class<T> clazz) {
        ArrayList<String> oneToManyFields = new ArrayList<String>();
        List<Field> fields = getFields(clazz);
        for (Field field : fields) {
            if (matchOneToMany(field)) {
                oneToManyFields.add(field.getName());
            }
        }
        return oneToManyFields;
    }

    protected List<String> getPartitionalTables() {
        List<String> partitionalTables = new ArrayList<String>();
        partitionalTables.add(getNamingStrategy(clazz.getSimpleName()));
        List<Field> fields = getFields(clazz);
        for (Field field : fields) {
            if (matchOneToMany(field)) {
                partitionalTables.add(getNamingStrategy(ReflectionUtils.getSuperClassGenericType(field.getGenericType()).getSimpleName()));
            }
            if (matchOneToOne(field)) {
                partitionalTables.add(getNamingStrategy(field.getType().getSimpleName()));
            }
        }
        return partitionalTables;
    }

    private boolean matchOneToOne(Field field) {
        return matchAnnotation(field.getAnnotations(), OneToOne.class) && isNotTransient(field.getAnnotations());
    }

    private boolean matchOneToMany(Field field) {
        return matchAnnotation(field.getAnnotations(), OneToMany.class) && isNotTransient(field.getAnnotations());
    }

    private boolean isNotTransient(Annotation[] annotations) {
        return !matchAnnotation(annotations, Transient.class);
    }

    private boolean matchAnnotation(Annotation[] annotations, Class clazz) {
        if (annotations == null || annotations.length == 0) {
            return false;
        }
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == clazz) {
                return true;
            }
        }
        return false;
    }

    protected static String getNamingStrategy(String entityName) {
        StringBuilder buf = new StringBuilder(entityName.replace('.', '_'));
        for (int i = 1; i < buf.length() - 1; i++) {
            if (Character.isLowerCase(buf.charAt(i - 1)) &&
                    Character.isUpperCase(buf.charAt(i)) &&
                    Character.isLowerCase(buf.charAt(i + 1))) {
                buf.insert(i++, '_');
            }
        }
        return buf.toString().toLowerCase();
    }

    protected Session getSession(Interceptor interceptor) {
        return sessionFactory.withOptions().interceptor(interceptor).openSession();
    }

    public String getEngine() {
        return DEFAULT_ENGINE;
    }

    private final class SelectInterceptor extends EmptyInterceptor {

        private String suffix;

        private SelectInterceptor(String suffix) {
            this.suffix = suffix;
        }

        @Override
        public String onPrepareStatement(String sql) {
            List<String> tableNames = getPartitionalTables();
            sortTableNames(tableNames);
            String columns = StringUtils.substringBetween(sql.toUpperCase(), "SELECT", "FROM");
            String fromTables = StringUtils.substringAfter(sql.toUpperCase(), "FROM");
            return String.format("SELECT %s FROM %s", columns.toLowerCase(), replaceTableName(tableNames, fromTables));
        }

        /**
         * You must at first match longer table name !! If not you possibly match to another column !!!
         * <p>
         * Eg: If exist 'change_log','rate_change_log','rate_change_log_detail' in your database,
         * you want replace named 'rate_change_log_detail' table,but you firstly use 'rate_change_log' so you will generate a wrong sql.
         */
        private void sortTableNames(List<String> partitionalTables) {
            Collections.sort(partitionalTables, new Comparator<String>() {
                @Override
                public int compare(String before, String after) {
                    return after.length() - before.length();
                }
            });
        }

        private String replaceTableName(List<String> partitionalTables, String tableAndAlias) {
            for (String partitionalTable : partitionalTables) {
                String oldTableAndAlias = tableAndAlias.toLowerCase();
                if (oldTableAndAlias.contains(partitionalTable)) {
                    String newTableAndAlias = getNewTableName(suffix, partitionalTable);
                    return StringUtils.replaceOnce(oldTableAndAlias, partitionalTable, newTableAndAlias);
                }
            }
            return tableAndAlias;
        }
    }

    private final class InsertInterceptor extends PersistenceSupportHibernateInterceptor {
        private String suffix;

        private InsertInterceptor(String suffix) {
            this.suffix = suffix;
        }

        @Override
        public String onPrepareStatement(String sql) {
            if (!sql.toUpperCase().contains("INSERT INTO")) {
                return sql;
            }
            String oldTable = StringUtils.substringBetween(sql.toUpperCase(), "INSERT INTO", LEFT_BRACKET);
            String[] matchedParts = StringUtils.substringsBetween(sql, LEFT_BRACKET, RIGHT_BRACKET);
            if (matchedParts == null || matchedParts.length != 2) {
                throw new IllegalStateException(String.format("Invalid insert sql :%s", sql));
            }
            String newTable = getNewTableName(suffix, oldTable.trim().toLowerCase());
            String columns = matchedParts[0];
            String placeHolders = matchedParts[1];
            return String.format("INSERT INTO %s (%s) VALUES (%s);", newTable.toLowerCase(), columns.toLowerCase(), placeHolders);
        }
    }

    protected String getNewTableName(String suffix, String tableName) {
        if (!getPartitionRequired()) {
            return tableName;
        }
        return String.format("%s_%s", tableName, suffix);
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

    protected void like(DetachedCriteria detachedCriteria, String property, Object value) {
        if (value == null) {
            return;
        }
        if (String.class.isInstance(value) && StringUtils.isBlank((String) value)) {
            return;
        }
        if (String.class.isInstance(value)) {
            detachedCriteria.add(Restrictions.like(property, StringUtils.trim((String) value), MatchMode.ANYWHERE));
        } else {
            detachedCriteria.add(Restrictions.like(property, value));
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
            detachedCriteria.add(Restrictions.ilike(property, value));
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
            detachedCriteria.add(Restrictions.not(Restrictions.ilike(property, value)));
        }
    }

    protected void notLike(DetachedCriteria detachedCriteria, String property, Object value) {
        if (value == null) {
            return;
        }
        if (String.class.isInstance(value) && StringUtils.isBlank((String) value)) {
            return;
        }
        if (String.class.isInstance(value)) {
            detachedCriteria.add(Restrictions.not(Restrictions.like(property, StringUtils.trim((String) value), MatchMode.ANYWHERE)));
        } else {
            detachedCriteria.add(Restrictions.not(Restrictions.like(property, value)));
        }
    }

    protected void lt(DetachedCriteria detachedCriteria, String property, Object value) {
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

    protected void le(DetachedCriteria detachedCriteria, String property, Object value) {
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

    protected void gt(DetachedCriteria detachedCriteria, String property, Object value) {
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

    protected void ge(DetachedCriteria detachedCriteria, String property, Object value) {
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

    public boolean getPartitionRequired() {
        return partitionRequired;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
