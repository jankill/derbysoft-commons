package com.derbysoft.common.web.struts2.converter;

import com.derbysoft.common.util.date.Dates;

import java.text.DateFormat;

public class YYYYMMDDDateConverter extends AbstractDateConverter {

    @Override
    protected DateFormat getDateFormat() {
        return Dates.getDayFormatter();
    }

}
