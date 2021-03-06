package com.derbysoft.common.web.struts2.interceptor;

import com.derbysoft.common.exception.SystemInternalException;
import com.derbysoft.common.paginater.Paginater;
import com.derbysoft.common.web.struts2.support.PaginateActionSupport;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-3-19
 */
public class InitializePaginaterInterceptor extends AbstractInterceptor {

    private static final String PAGE_NO = "page";

    private static final String PAGE_SIZE = "pageSize";

    private static final String SORT_FIELD = "sort";

    private static final String SORT_DIRECTION = "dir";

    @Override
    public String intercept(ActionInvocation invocation) {
        Object action = invocation.getAction();
        if (PaginateActionSupport.class.isInstance(action)) {
            PaginateActionSupport actionSupport = (PaginateActionSupport) action;
            setPaginaterParameters(actionSupport.getPaginater());
        }
        try {
            return invocation.invoke();
        } catch (Exception e) {
            throw new SystemInternalException(e);
        }
    }

    public void setPaginaterParameters(Paginater paginater) {
        HttpServletRequest request = ServletActionContext.getRequest();
        String pageNo = request.getParameter(PAGE_NO);
        if (StringUtils.isNotBlank(pageNo)) {
            paginater.setPageNo(Integer.parseInt(pageNo));
        }
        String pageSize = request.getParameter(PAGE_SIZE);
        if (StringUtils.isNotBlank(pageSize)) {
            paginater.setPageSize(Integer.parseInt(pageSize));
        }
        String sortField = request.getParameter(SORT_FIELD);
        if (StringUtils.isNotBlank(sortField)) {
            paginater.setSortField(sortField);
        }
        String sortDirection = request.getParameter(SORT_DIRECTION);
        if (StringUtils.isNotBlank(sortDirection)) {
            paginater.setSortDirection(sortDirection);
        }
    }
}
