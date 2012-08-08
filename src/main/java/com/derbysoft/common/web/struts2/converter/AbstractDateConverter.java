package com.derbysoft.common.web.struts2.converter;

import com.opensymphony.xwork2.conversion.TypeConversionException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.util.StrutsTypeConverter;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

public abstract class AbstractDateConverter extends StrutsTypeConverter {

    protected Log logger = LogFactory.getLog(getClass());

    protected abstract DateFormat getDateFormat();

    @SuppressWarnings("unchecked")
    @Override
    public Object convertFromString(Map map, String[] arg, Class clazz) {
        if (clazz != Date.class) {
            return null;
        }

        String content = StringUtils.join(arg);
        if (StringUtils.isBlank(content)) {
            return null;
        }

        try {
            return getDateFormat().parse(content);
        } catch (Throwable t) {
            String message = "Failed convert string [" + content + "] to date use format [" + getDateFormat() + "]";
            logger.error(message, t);
            throw new TypeConversionException(message, t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public String convertToString(Map map, Object obj) {
        if (!(obj instanceof Date)) {
            return null;
        }
        return getDateFormat().format(obj);
    }
}
