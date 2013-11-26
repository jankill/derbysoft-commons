package com.derbysoft.common.repository.partition;

public abstract class PartitionByMonthRepository<T> extends PartitionByDateRepository<T> {

    @Override
    public String getDateFormat() {
        return "yyyyMM";
    }
}
