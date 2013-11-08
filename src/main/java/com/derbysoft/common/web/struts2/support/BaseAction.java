package com.derbysoft.common.web.struts2.support;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-3-19
 */
public abstract class BaseAction extends ActionSupport implements SessionAware, Preparable {

    protected static final String LIST = "list";

    protected Log logger = LogFactory.getLog(this.getClass());

    protected Map session;

    @Override
    public void prepare() {
    }

    @Action("save-init")
    public String saveInit() {
        return SUCCESS;
    }

    protected Long getId() {
        String id = getParameter("id");
        if (id == null || id.trim().equals("")) {
            return null;
        }
        return new Long(id);
    }

    public void setSession(Map session) {
        this.session = session;
    }

    public Map getSession() {
        return session;
    }

    protected String getParameter(String parameterName) {
        Object parameter = ActionContext.getContext().getParameters().get(parameterName);
        if (parameter == null) {
            return null;
        }
        if (String.class.isAssignableFrom(parameter.getClass())) {
            return parameter.toString();
        }
        Object[] parameters = (Object[]) parameter;
        if (parameters.length == 0) {
            return null;
        }
        return parameters[0].toString();
    }

    protected String[] getParameterValues(String parameterName) {
        String[] values = (String[]) ActionContext.getContext().getParameters().get(parameterName);
        if (values == null || values.length == 0) {
            return null;
        }
        return values;
    }

}