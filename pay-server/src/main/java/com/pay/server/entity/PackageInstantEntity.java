package com.pay.server.entity;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_package_instant")
public class PackageInstantEntity extends IdEntity {
    private Long packageId;
    private Long deviceId;
    private Date createTime;
    private String createDate;
    public Long getPackageId() {
        return packageId;
    }
    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }
    public PackageInstantEntity() {}
    public PackageInstantEntity(Long id) {
        this.id = id;
    }
    public Long getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getCreateDate() {
        return createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}