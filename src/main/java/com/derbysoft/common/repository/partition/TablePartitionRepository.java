package com.derbysoft.common.repository.partition;

import com.derbysoft.common.paginater.Paginater;
import org.hibernate.criterion.DetachedCriteria;

import java.util.List;

public interface TablePartitionRepository<T> {

    T save(T log);

    T save(String suffix, T log);

    List<T> saveAll(List<T> logs);

    List<T> saveAll(String suffix, List<T> logs);

    T get(String suffix, Long id);

    List<T> find(String suffix, DetachedCriteria detachedCriteria);

    List<T> find(String suffix, DetachedCriteria detachedCriteria, boolean loadOneToManyFields);

    Paginater paginate(String suffix, DetachedCriteria detachedCriteria, Paginater paginater);

    Paginater paginate(String suffix, DetachedCriteria detachedCriteria, Paginater paginater, boolean loadOneToManyFields);

}
