package com.pay.admin.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.pay.admin.entity.framework.IdEntity;

/**
 * 包统计
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_package_payment_stat")
public class PackagePaymentStatEntity extends IdEntity {
    //uniqe keys
    private Long packageId;
    private Long sdkId;
    private String statDate;
    
    
    //dump keys
    private Long productId;
    private Long channelId;
    
    //成功付费数
    private int payCount;//真实成功付费数
    //成功付费金额
    private int payEarning; //真实付费金额
    //同步金额
    private int syncEarning;
    public PackagePaymentStatEntity() {}
    public PackagePaymentStatEntity(Long id) {
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
    public int getPayCount() {
        return payCount;
    }
    public void setPayCount(int payCount) {
        this.payCount = payCount;
    }
    public int getPayEarning() {
        return payEarning;
    }
    public void setPayEarning(int payEarning) {
        this.payEarning = payEarning;
    }
    public Long getSdkId() {
        return sdkId;
    }
    public void setSdkId(Long sdkId) {
        this.sdkId = sdkId;
    }
    public int getSyncEarning() {
        return syncEarning;
    }
    public void setSyncEarning(int syncEarning) {
        this.syncEarning = syncEarning;
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
}
