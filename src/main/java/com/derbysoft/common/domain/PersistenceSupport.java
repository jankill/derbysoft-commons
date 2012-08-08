package com.derbysoft.common.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-3-19
 */
@MappedSuperclass
public abstract class PersistenceSupport implements Serializable {

    @Id
    @GeneratedValue
    protected Long id;

    @Column(name = "create_time")
    protected Date createTime;

    @Column(name = "last_modify_time")
    protected Date lastModifyTime;

    @Version
    protected Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return getLastModifyTime();
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public void setUpdateTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(getClass().isInstance(obj))) {
            return false;
        }

        boolean superEquals = super.equals(obj);
        if (superEquals) {
            return true;
        }

        if (getId() == null) {
            return false;
        }

        PersistenceSupport support = (PersistenceSupport) obj;
        if (support.getId() == null) {
            return false;
        }
        return getId().equals(support.getId());
    }

    @Override
    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        }
        return super.hashCode();
    }

}