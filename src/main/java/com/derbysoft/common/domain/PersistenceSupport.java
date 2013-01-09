package com.derbysoft.common.domain;

import com.derbysoft.common.util.CloneUtils;

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
    private Long id;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "last_modify_time")
    private Date lastModifyTime;

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
        return CloneUtils.clone(createTime);
    }

    public void setCreateTime(Date createTime) {
        this.createTime = CloneUtils.clone(createTime);
    }

    public Date getUpdateTime() {
        return getLastModifyTime();
    }

    public Date getLastModifyTime() {
        return CloneUtils.clone(lastModifyTime);
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = CloneUtils.clone(lastModifyTime);
    }

    public void setUpdateTime(Date lastModifyTime) {
        setLastModifyTime(lastModifyTime);
    }

    @Override
    public boolean equals(Object obj) {
        Class<? extends PersistenceSupport> clazz = getClass();
        if (!(clazz.isInstance(obj))) {
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