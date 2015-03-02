package com.derbysoft.common.repository.ccs;

import com.derbysoft.ccs.core.MappingQueryRestriction;

import java.util.List;

public interface Repository<T> {

    T get(String key);

    T get(List<String> keys);

    T get(List<String> keys, String separator);

    List<T> listAll();

    List<T> listAllEnabled();

    List<T> find(String field, String value);

    List<T> find(List<MappingQueryRestriction> restrictions);

}
