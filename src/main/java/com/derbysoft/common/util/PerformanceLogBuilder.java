package com.derbysoft.common.util;

import com.derbysoft.common.util.date.LocalDateTimes;
import com.derbysoft.common.util.date.LocalDates;
import com.derbysoft.common.util.date.LocalTimes;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

public class PerformanceLogBuilder extends PerformanceLogs {

    public void append(Object name, LocalDate localDate) {
        if (localDate == null) {
            return;
        }
        appendInternal(name, LocalDates.format(localDate));
    }

    public void append(Object name, LocalTime localTime) {
        if (localTime == null) {
            return;
        }
        appendInternal(name, LocalTimes.format(localTime));
    }

    public void append(Object name, LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return;
        }
        appendInternal(name, LocalDateTimes.format(localDateTime));
    }

}
