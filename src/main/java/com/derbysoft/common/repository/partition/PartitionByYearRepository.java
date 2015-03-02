package com.derbysoft.common.repository.partition;

public abstract class PartitionByYearRepository<T> extends PartitionByDateRepository<T> {

    @Override
    public String getDateFormat() {
        return "yyyy";
    }
}
