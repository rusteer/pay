package com.pay.admin.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.pay.admin.entity.framework.IdEntity;

@Entity
@Table(name = "t_package_activate_stat")
public class PackageActivateStatEntity extends IdEntity {
    //uniqe keys
    private Long packageId;
    private String statDate;
    //dump keys
    private Long channelId;
    private Long productId;
    //data
    private int activateCount;
    public PackageActivateStatEntity() {}
    public PackageActivateStatEntity(Long id) {
        this.id = id;
    }
    public Long getPackageId() {
        return packageId;
    }
    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }
    public String getStatDate() {
        return statDate;
    }
    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }
    public Long getChannelId() {
        return channelId;
    }
    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public int getActivateCount() {
        return activateCount;
    }
    public void setActivateCount(int activateCount) {
        this.activateCount = activateCount;
    }
}
