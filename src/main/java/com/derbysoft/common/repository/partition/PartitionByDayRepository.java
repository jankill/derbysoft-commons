package com.derbysoft.common.repository.partition;

public abstract class PartitionByDayRepository<T> extends PartitionByDateRepository<T> {

    @Override
    public String getDateFormat() {
        return "yyyyMMdd";
    }

}
