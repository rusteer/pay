package com.pay.admin.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.pay.admin.entity.framework.IdEntity;

@Entity
@Table(name = "t_pay_stat")
public class PayStatEntity extends IdEntity {
    private Long packageInstantId;
    private Long productId;
    private Long channelId;
    private Long packageId;
    private Long deviceId;
    //插件本身的统计数据
    private int successCount;
    private int successEarning;
    //计费方给的数据
    private int syncSuccessCount;
    private int syncSuccessEarning;
    //给渠道看的数据
    private int discountSuccessCount;
    private int discountSuccessEarning;
    private int failCount;
    private int cancelCount;
    private String statDate;
    public PayStatEntity() {}
    public PayStatEntity(Long id) {
        this.id = id;
    }
    public Long getPackageInstantId() {
        return packageInstantId;
    }
    public void setPackageInstantId(Long packageInstantId) {
        this.packageInstantId = packageInstantId;
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
    public int getSuccessEarning() {
        return successEarning;
    }
    public void setSuccessEarning(int successEarning) {
        this.successEarning = successEarning;
    }
    public int getSyncSuccessCount() {
        return syncSuccessCount;
    }
    public void setSyncSuccessCount(int syncSuccessCount) {
        this.syncSuccessCount = syncSuccessCount;
    }
    public int getSyncSuccessEarning() {
        return syncSuccessEarning;
    }
    public void setSyncSuccessEarning(int syncSuccessEarning) {
        this.syncSuccessEarning = syncSuccessEarning;
    }
    public int getDiscountSuccessCount() {
        return discountSuccessCount;
    }
    public void setDiscountSuccessCount(int discountSuccessCount) {
        this.discountSuccessCount = discountSuccessCount;
    }
    public int getDiscountSuccessEarning() {
        return discountSuccessEarning;
    }
    public void setDiscountSuccessEarning(int discountSuccessEarning) {
        this.discountSuccessEarning = discountSuccessEarning;
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
