package com.derbysoft.common.domain;

import com.derbysoft.common.util.CloneUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Date;

/**
 * @author zhupan
 * @version 1.0
 * @since 2009-3-19
 */
@MappedSuperclass
public abstract class PersistenceSupport extends BasePersistenceSupport {

    @Column(name = "last_modify_time")
    private Date lastModifyTime;

    @Version
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

}