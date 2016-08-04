package com.pay.server.entity;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 防止同步记录重复
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_sync")
public class SyncEntity extends IdEntity {
    private String sdk;
    private String serialNumber;
    private Long packageInstantId;
    private int price;
    private int result;//0:成功,1:取消,2:失败
    private String provinceName;
    private String paymentType;//话费,支付宝,银联,充值卡游戏点卡
    private String rawInfo;
    private String linkId;//防止同步记录重复
    private Long channelId;
    private String createDate;
    private Date createTime;
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
    public Long getPackageInstantId() {
        return packageInstantId;
    }
    public void setPackageInstantId(Long packageInstantId) {
        this.packageInstantId = packageInstantId;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getResult() {
        return result;
    }
    public void setResult(int result) {
        this.result = result;
    }
    public String getProvinceName() {
        return provinceName;
    }
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
    public String getPaymentType() {
        return paymentType;
    }
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
    public String getRawInfo() {
        return rawInfo;
    }
    public void setRawInfo(String rawInfo) {
        this.rawInfo = rawInfo;
    }
    public String getLinkId() {
        return linkId;
    }
    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }
    public String getSdk() {
        return sdk;
    }
    public void setSdk(String sdk) {
        this.sdk = sdk;
    }
    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    public Long getChannelId() {
        return channelId;
    }
    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
}
