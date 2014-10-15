package com.derbysoft.common.repository.ccs;

import com.derbysoft.ccs.core.MappingCache;
import com.derbysoft.ccs.core.MappingEntry;
import com.derbysoft.ccs.core.MappingQueryRestriction;
import com.derbysoft.common.util.SeparatorStringBuilder;

import java.util.List;

public abstract class AbstractRepository<T extends MappingEntry> implements Repository<T> {

    private static final String SEPARATOR = ":";

    protected MappingCache<T> mappingCache;

    public AbstractRepository(MappingCache<T> mappingCache) {
        this.mappingCache = mappingCache;
    }

    @Override
    public T get(String key) {
        return mappingCache.get(key);
    }

    @Override
    public T get(List<String> keys) {
        return get(keys, SEPARATOR);
    }

    @Override
    public T get(List<String> keys, String separator) {
        if (keys == null || keys.isEmpty()) {
            return null;
        }
        SeparatorStringBuilder value = new SeparatorStringBuilder(separator);
        for (String key : keys) {
            value.append(key);
        }
        return mappingCache.get(value.toString());
    }

    @Override
    public List<T> listAll() {
        return mappingCache.listAll();
    }

    @Override
    public List<T> listAllEnabled() {
        return mappingCache.listAllEnabled();
    }

    @Override
    public List<T> find(String field, String value) {
        return mappingCache.list(field, value);
    }

    @Override
    public List<T> find(List<MappingQueryRestriction> restrictions) {
        if (restrictions == null || restrictions.isEmpty()) {
            return listAll();
        }
        return mappingCache.list(restrictions);
    }


}
