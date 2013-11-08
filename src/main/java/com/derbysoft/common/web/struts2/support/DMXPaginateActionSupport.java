package com.derbysoft.common.web.struts2.support;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Results(value = {
        @Result(name = DMXPaginateActionSupport.DMX, location = "/WEB-INF/page/admin/dmx.jsp")
})
public abstract class DMXPaginateActionSupport extends PaginateActionSupport {
    public static final String DMX = "dmx";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String RESULT = "result";
    private static final String PAYLOAD = "payload";
    private static final String ERROR = "error";
    private String dmxResult;

    protected String populateSucceededResult(Object payload, String... excludes) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(RESULT, Boolean.TRUE);
        map.put(PAYLOAD, payload);
        dmxResult = toJsonString(map, Arrays.asList(excludes));
        return DMX;
    }

    protected String populateFailedResult(Exception exception, String... excludes) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(RESULT, Boolean.FALSE);
        map.put(ERROR, String.format("%s: %s", exception.getClass().getSimpleName(), ExceptionUtils.getStackTrace(exception)));
        dmxResult = toJsonString(map, Arrays.asList(excludes));
        return DMX;
    }

    public String toJsonString(Object obj) {
        return toJsonString(obj, null);
    }

    public static String toJsonString(Object obj, final Collection<String> excludes) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (excludes != null) {
            gsonBuilder.setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return excludes.contains(f.getName());
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            });
        }
        gsonBuilder.setDateFormat(DATE_TIME_FORMAT);

        return gsonBuilder.create().toJson(obj);
    }

    public String getDmxResult() {
        return dmxResult;
    }
}
