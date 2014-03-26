package com.derbysoft.common.domain;

import com.derbysoft.common.util.CloneUtils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zhupan
 * @version 1.3
 * @since 2013-1-20
 */
@MappedSuperclass
public abstract class BasePersistenceSupport implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return CloneUtils.clone(createTime);
    }

    public void setCreateTime(Date createTime) {
        this.createTime = CloneUtils.clone(createTime);
    }

    @Override
    public boolean equals(Object obj) {
        Class<? extends BasePersistenceSupport> clazz = getClass();
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
        BasePersistenceSupport support = (BasePersistenceSupport) obj;
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