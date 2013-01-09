package com.derbysoft.common.web.struts2.support;

import com.derbysoft.common.paginater.Paginater;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-3-19
 */
public abstract class PaginateActionSupport extends ActionSupport {

    protected Paginater paginater = new Paginater();

    protected Log logger = LogFactory.getLog(this.getClass());

    @Action("execute")
    @Override
    public String execute() {
        return SUCCESS;
    }

    public void setPaginater(Paginater paginater) {
        this.paginater = paginater;
    }

    public Paginater getPaginater() {
        return paginater;
    }

}