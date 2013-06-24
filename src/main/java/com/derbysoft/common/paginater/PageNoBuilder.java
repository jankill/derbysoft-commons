package com.derbysoft.common.paginater;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-3-19
 */
public abstract class PageNoBuilder {

    public static List<PageNo> getPageItems(Paginater paginater) {
        if (paginater.getTotalPage() <= Number.TEN) {
            return getMaxPageItems(paginater.getTotalPage());
        }
        if (paginater.getCurrentPage() <= Number.FIVE) {
            return getPrePageItems(paginater.getTotalPage());
        }
        if (paginater.getCurrentPage() > paginater.getTotalPage() - Number.FIVE) {
            return getLastPageItems(paginater.getTotalPage());
        }
        return getNormalItems(paginater);
    }

    private static List<PageNo> getNormalItems(Paginater paginater) {
        List<PageNo> items = new ArrayList<PageNo>();
        items.addAll(getStartPageNos());
        items.add(new PageNo(paginater.getCurrentPage() - 2));
        items.add(new PageNo(paginater.getCurrentPage() - 1));
        items.add(new PageNo(paginater.getCurrentPage()));
        items.add(new PageNo(paginater.getCurrentPage() + 1));
        items.add(new PageNo(paginater.getCurrentPage() + 2));
        items.addAll(getEndPageNos(paginater.getTotalPage()));
        return items;
    }

    private static List<PageNo> getLastPageItems(int totalPage) {
        List<PageNo> items = new ArrayList<PageNo>();
        items.addAll(getStartPageNos());
        for (int i = totalPage - Number.FIVE + 1; i <= totalPage; i++) {
            items.add(new PageNo(i));
        }
        return items;
    }

    private static List<PageNo> getPrePageItems(int totalPage) {
        List<PageNo> items = new ArrayList<PageNo>();
        for (int i = 1; i <= Number.FIVE; i++) {
            items.add(new PageNo(i));
        }
        items.addAll(getEndPageNos(totalPage));
        return items;
    }

    private static List<PageNo> getMaxPageItems(int totalPage) {
        List<PageNo> items = new ArrayList<PageNo>();
        for (int i = 1; i <= totalPage; i++) {
            items.add(new PageNo(i));
        }
        return items;
    }

    private static List<PageNo> getEndPageNos(int totalPage) {
        List<PageNo> items = new ArrayList<PageNo>();
        items.add(new PageNo(PageNo.Type.POINT));
        items.add(new PageNo(totalPage - 1));
        items.add(new PageNo(totalPage));
        return items;
    }

    private static List<PageNo> getStartPageNos() {
        List<PageNo> items = new ArrayList<PageNo>();
        items.add(new PageNo(1));
        items.add(new PageNo(2));
        items.add(new PageNo(PageNo.Type.POINT));
        return items;
    }

}