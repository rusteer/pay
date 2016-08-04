package com.pay.server.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_package_retention_stat")
public class PackageRetentionStatEntity extends IdEntity{
    //uniqe keys
    private Long packageId;
    private String createDate;
    private String statDate;
    
    //dump keys
    private Long channelId;
    private Long productId;
    
    //data
    private int retentionCount;
    private int retentionDays;
    
    public PackageRetentionStatEntity() {}
    public PackageRetentionStatEntity(Long id) {
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
    public int getRetentionCount() {
        return retentionCount;
    }
    public void setRetentionCount(int retentionCount) {
        this.retentionCount = retentionCount;
    }
    public String getCreateDate() {
        return createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public int getRetentionDays() {
        return retentionDays;
    }
    public void setRetentionDays(int retentionDays) {
        this.retentionDays = retentionDays;
    }
}
