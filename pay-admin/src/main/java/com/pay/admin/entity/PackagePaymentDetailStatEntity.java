package com.pay.admin.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.pay.admin.entity.framework.IdEntity;

@Entity
@Table(name = "t_package_payment_detail_stat")
public class PackagePaymentDetailStatEntity extends IdEntity {
    //uniqe keys
    private Long packageId;
    private int retentionDays;
    private int carrierOperator;
    private Long sdkId;
    private int payIndex;
    private String statDate;
    //dump keys
    private Long productId;
    private Long channelId;
    //data
    private int successCount;
    private int cancelCount;
    private int failureCount;
    //
    public PackagePaymentDetailStatEntity() {}
    public PackagePaymentDetailStatEntity(Long id) {
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
    public int getRetentionDays() {
        return retentionDays;
    }
    public void setRetentionDays(int retentionDays) {
        this.retentionDays = retentionDays;
    }
    public int getCarrierOperator() {
        return carrierOperator;
    }
    public void setCarrierOperator(int carrierOperator) {
        this.carrierOperator = carrierOperator;
    }
    public Long getSdkId() {
        return sdkId;
    }
    public void setSdkId(Long sdkId) {
        this.sdkId = sdkId;
    }
    public int getPayIndex() {
        return payIndex;
    }
    public void setPayIndex(int payIndex) {
        this.payIndex = payIndex;
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
    
    public int getSuccessCount() {
        return successCount;
    }
    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }
    public int getCancelCount() {
        return cancelCount;
    }
    public void setCancelCount(int cancelCount) {
        this.cancelCount = cancelCount;
    }
    public int getFailureCount() {
        return failureCount;
    }
    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }
}
