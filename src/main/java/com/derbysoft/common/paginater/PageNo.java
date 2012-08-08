package com.derbysoft.common.paginater;

public class PageNo {

    enum Type {
        PAGE,
        POINT;
    }

    private Type type;

    private Integer value;

    public PageNo(Type type) {
        this.type = type;
    }

    public PageNo(Integer value) {
        this.type = Type.PAGE;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}