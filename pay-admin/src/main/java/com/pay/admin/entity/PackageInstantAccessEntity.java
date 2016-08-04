package com.pay.admin.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.pay.admin.entity.framework.IdEntity;


/**
 * 每日留存统计
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_package_instant_access")
public class PackageInstantAccessEntity extends IdEntity {
    private Long packageInstantId;
    private String statDate;
    private Integer accessCount;
    public PackageInstantAccessEntity() {}
    public PackageInstantAccessEntity(Long id) {
        this.id = id;
    }
    public Long getPackageInstantId() {
        return packageInstantId;
    }
    public void setPackageInstantId(Long packageInstantId) {
        this.packageInstantId = packageInstantId;
    }
    public String getStatDate() {
        return statDate;
    }
    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }
    public Integer getAccessCount() {
        return accessCount;
    }
    public void setAccessCount(Integer accessCount) {
        this.accessCount = accessCount;
    }
}
