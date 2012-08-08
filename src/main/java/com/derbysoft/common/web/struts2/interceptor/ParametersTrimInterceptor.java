package com.derbysoft.common.web.struts2.interceptor;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.interceptor.NoParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-3-19
 */
public class ParametersTrimInterceptor extends AbstractInterceptor {

    private static final String BLANK = "";

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        if ((!requiredTrim(invocation.getProxy().getMethod()))) {
            return invocation.invoke();
        }
        Object action = invocation.getAction();
        if (action instanceof NoParameters) {
            return invocation.invoke();
        }
        ActionContext actionContext = invocation.getInvocationContext();
        final Map parameters = actionContext.getParameters();
        if (parameters == null) {
            return invocation.invoke();
        }
        for (Object key : parameters.keySet()) {
            Object object = parameters.get(key);
            if (object == null || !String[].class.isAssignableFrom(object.getClass())) {
                continue;
            }
            String[] values = (String[]) object;
            List<String> result = new ArrayList<String>();
            for (String value : values) {
                result.add(value == null ? null : value.trim());
            }
            if (CollectionUtils.isEmpty(result)) {
                parameters.put(key, BLANK);
                continue;
            }
            if (result.size() > 1 && result.contains(BLANK)) {
                result.remove(BLANK);
            }
            parameters.put(key, result.toArray(new String[result.size()]));
        }
        return invocation.invoke();
    }

    private boolean requiredTrim(String methodName) {
        return (methodName.toLowerCase().indexOf("save") != -1 && methodName.toLowerCase().indexOf("saveinit") == -1)
                || (methodName.toLowerCase().indexOf("update") != -1 && methodName.toLowerCase().indexOf("updateinit") == -1);
    }

}