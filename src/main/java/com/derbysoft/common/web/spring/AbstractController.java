package com.derbysoft.common.web.spring;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.derbysoft.common.paginater.Paginater;
import com.derbysoft.common.util.ExceptionUtils;
import com.derbysoft.common.util.date.Dates;
import com.derbysoft.common.util.date.LocalDates;
import com.derbysoft.common.util.date.LocalTimes;
import com.derbysoft.common.util.xml.XmlFormatUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.util.*;

public abstract class AbstractController {

    protected static final String SUCCESS = "success";

    protected static final String MESSAGE = "message";

    protected static final String JSON = "application/json";

    protected Paginater paginater = new Paginater();

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String exceptionHandler(Exception ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return buildFailedJsonResult(ExceptionUtils.toString(ex));
    }

    protected String formatXml(String content) {
        try {
            return XmlFormatUtils.formatXml(content);
        } catch (Exception ex) {
            return content;
        }
    }

    protected String buildSucceededJsonResult(Object message, String... excludes) {
        return buildJsonResult(Boolean.TRUE, message, excludes);
    }

    protected String buildFailedJsonResult(Object message, String... excludes) {
        return buildJsonResult(Boolean.FALSE, message, excludes);
    }

    private String buildJsonResult(Boolean result, Object message, String... excludes) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(SUCCESS, result);
        resultMap.put(MESSAGE, message);
        return formatObjectToJson(resultMap, excludes);
    }

    protected String formatObjectToJson(Object object, String... excludes) {
        List<String> excludeFields = new ArrayList<String>();
        if (ArrayUtils.isNotEmpty(excludes)) {
            Collections.addAll(excludeFields, excludes);
        }
        return formatObjectToJson(object, excludeFields, new SerializerFeature[]{SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty});
    }

    protected String formatObjectToJson(Object object, List<String> excludes, SerializerFeature[] features) {
        SimplePropertyPreFilter simplePropertyPreFilter = new SimplePropertyPreFilter();
        for (String exclude : excludes) {
            simplePropertyPreFilter.getExcludes().add(exclude);
        }
        return JSONObject.toJSONString(object, simplePropertyPreFilter, features);
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    setValue(Dates.of(text));
                } catch (Exception e) {
                    setValue(null);
                }
            }

            @Override
            public String getAsText() throws IllegalArgumentException {
                return getValue().toString();
            }
        });
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    setValue(LocalDates.of(text));
                } catch (Exception e) {
                    setValue(null);
                }
            }

            @Override
            public String getAsText() throws IllegalArgumentException {
                return getValue().toString();
            }
        });
        binder.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    setValue(LocalTimes.of(text));
                } catch (Exception e) {
                    setValue(null);
                }
            }

            @Override
            public String getAsText() throws IllegalArgumentException {
                return getValue().toString();
            }
        });
    }

    public Paginater getPaginater() {
        return paginater;
    }

    public void setPaginater(Paginater paginater) {
        this.paginater = paginater;
    }
}
