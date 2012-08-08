package com.derbysoft.common.paginater;

import java.util.ArrayList;
import java.util.List;
/**
 * @since 2009-3-19
 * @author zhupan
 * @version 1.0
 */
public class Paginater {

    public static final int DEFAULT_PAGE_NUMBER = 1;

    public static final int DEFAULT_PAGE_SIZE = 20;

    private int pageNo = DEFAULT_PAGE_NUMBER;

    private int pageSize = DEFAULT_PAGE_SIZE;

    private String sortField;

    private String sortDirection;

    private long totalCount;

    private List objects = new ArrayList();

    public int getTotalPage() {
        return (int) Math.ceil(new Double(totalCount).doubleValue() / getPageSize());
    }

    public int getCurrentPage() {
        return pageNo;
    }

    public List getObjects() {
        return objects;
    }

    public void setObjects(List objects) {
        this.objects = objects;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public int getFirstResult() {
        int pageIndex = getPageNo() - 1;
        if (pageIndex < 0) {
            pageIndex = 0;
        }
        return pageIndex * getMaxResults();
    }

    public int getMaxResults() {
        return pageSize;
    }

    public List<PageNo> getPageItems() {
        return PageNoBuilder.getPageItems(this);
    }


}