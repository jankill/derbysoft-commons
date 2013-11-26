package com.derbysoft.common.repository.partition;

import com.derbysoft.common.paginater.Paginater;
import org.hibernate.criterion.DetachedCriteria;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public abstract class PartitionByDateRepository<T> extends AbstractPartitionTableRepository<T> {

    @Override
    protected String getTableSuffixForSave(T t) {
        return getDayFormatter().format(new Date());
    }

    public T save(Date date, T log) {
        super.save(formatDate(date), log);
        return log;
    }

    public List<T> saveAll(Date date, List<T> logs) {
        return super.saveAll(formatDate(date), logs);
    }

    public T get(Date date, Long id) {
        return super.get(formatDate(date), id);
    }

    public List<T> find(Date date, DetachedCriteria detachedCriteria) {
        return super.find(formatDate(date), detachedCriteria);
    }

    public List<T> find(Date date, DetachedCriteria detachedCriteria, boolean loadOneToManyFields) {
        return super.find(formatDate(date), detachedCriteria, loadOneToManyFields);
    }

    public Paginater paginate(Date date, DetachedCriteria detachedCriteria, Paginater paginater) {
        return super.paginate(formatDate(date), detachedCriteria, paginater);
    }

    public Paginater paginate(Date date, DetachedCriteria detachedCriteria, Paginater paginater, boolean loadOneToManyFields) {
        return super.paginate(formatDate(date), detachedCriteria, paginater, loadOneToManyFields);
    }

    private ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>() {
        protected synchronized SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };

    private SimpleDateFormat getDayFormatter() {
        return threadLocal.get();
    }

    private String formatDate(Date date) {
        return getDayFormatter().format(date);
    }

    protected abstract String getDateFormat();
}
