package com.pay.admin.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.pay.admin.entity.framework.IdEntity;

@Entity
@Table(name = "t_pay_item_stat")
public class PayItemStatEntity extends IdEntity {
    private Long packageInstantId;
    private Long productId;
    private Long channelId;
    private Long packageId;
    private Long deviceId;
    private Integer payIndex;
    private String payName;
    private Integer price;
    //插件本身的统计数据
    private int successCount;
    private int failCount;
    private int cancelCount;
    private String statDate;
    public PayItemStatEntity() {}
    public PayItemStatEntity(Long id) {
        this.id = id;
    }
    public Long getPackageInstantId() {
        return packageInstantId;
    }
    public void setPackageInstantId(Long packageInstantId) {
        this.packageInstantId = packageInstantId;
    }
    public Integer getPayIndex() {
        return payIndex;
    }
    public void setPayIndex(Integer payIndex) {
        this.payIndex = payIndex;
    }
    public String getPayName() {
        return payName;
    }
    public void setPayName(String payName) {
        this.payName = payName;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public Long getChannelId() {
        return channelId;
    }
    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
    public Long getPackageId() {
        return packageId;
    }
    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }
    public Long getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
    public int getSuccessCount() {
        return successCount;
    }
    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }
    public int getFailCount() {
        return failCount;
    }
    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }
    public int getCancelCount() {
        return cancelCount;
    }
    public void setCancelCount(int cancelCount) {
        this.cancelCount = cancelCount;
    }
    public String getStatDate() {
        return statDate;
    }
    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }
}
