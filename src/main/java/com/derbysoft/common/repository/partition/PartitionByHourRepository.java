package com.derbysoft.common.repository.partition;

public abstract class PartitionByHourRepository<T> extends PartitionByDateRepository<T> {

    @Override
    public String getDateFormat() {
        return "yyyyMMddHH";
    }

}
