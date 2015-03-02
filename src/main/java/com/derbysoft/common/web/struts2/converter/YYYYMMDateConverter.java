package com.derbysoft.common.web.struts2.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class YYYYMMDateConverter extends AbstractDateConverter {

    @Override
    protected DateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM");
    }

}
