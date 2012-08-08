package com.derbysoft.common.web.struts2.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class YYYYMMDDHHMMSSDateConverter extends AbstractDateConverter {

	@Override
	protected DateFormat getDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	}

}
