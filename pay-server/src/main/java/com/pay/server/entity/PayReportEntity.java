package com.pay.server.entity;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_pay_report")
public class PayReportEntity extends IdEntity {
    private Long packageInstantId;
    private Long productId;
    private Long channelId;
    private Long packageId;
    private Long deviceId;
    private Integer payIndex;
    private String payName;
    private Integer price;
    private int result;
    private String errorMessage;
    private Long sdkId;
    private String createDate;
    private Date createTime;
    public PayReportEntity() {}
    public PayReportEntity(Long id) {
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
    public String getCreateDate() {
        return createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
    public int getResult() {
        return result;
    }
    public void setResult(int result) {
        this.result = result;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public Long getSdkId() {
        return sdkId;
    }
    public void setSdkId(Long sdkId) {
        this.sdkId = sdkId;
    }
}
