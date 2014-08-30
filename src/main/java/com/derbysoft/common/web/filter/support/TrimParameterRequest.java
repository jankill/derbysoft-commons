package com.derbysoft.common.web.filter.support;

import com.derbysoft.common.util.collect.Lists;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrimParameterRequest extends HttpServletRequestWrapper {
    private TrimModel trimModel;

    public TrimParameterRequest(ServletRequest request, TrimModel trimModel) {
        super((HttpServletRequest) request);
        this.trimModel = trimModel;
    }

    @Override
    public String getParameter(String name) {
        return trim(super.getParameter(name));
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
        for (Map.Entry<String, String[]> entry : super.getParameterMap().entrySet()) {
            parameterMap.put(entry.getKey(), trimAll(entry.getValue()));
        }
        return parameterMap;
    }

    @Override
    public String[] getParameterValues(String name) {
        return trimAll(super.getParameterValues(name));
    }

    private String trim(String parameter) {
        return trimModel.trim(parameter);
    }

    private String[] trimAll(String[] values) {
        List<String> results = Lists.newArrayList();
        for (String value : values) {
            results.add(trim(value));
        }
        return results.toArray(new String[results.size()]);
    }
}