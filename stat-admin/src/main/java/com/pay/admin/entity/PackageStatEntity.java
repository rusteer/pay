package com.pay.admin.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.pay.admin.entity.framework.IdEntity;

/**
 * 包统计,给渠道看的数据
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_package_stat")
public class PackageStatEntity extends IdEntity {
    //uniqe keys
    private Long packageId;
    private String statDate;
    //dump keys
    private Long channelId;
    private Long productId;
    //data
    private int activateCount;
    private int payCount;
    private int payEarning;
    private int syncEarning;
    private int discountActivateCount; //给渠道看的数据
    private int discountPayCount;//扣量的成功付费数
    private int discountPayEarning;//扣量的付费金额
    public PackageStatEntity() {}
    public PackageStatEntity(Long id) {
        this.id = id;
    }
    
    public PackageStatEntity(String statDate) {
        this.statDate = statDate;
    }
    public Long getPackageId() {
        return packageId;
    }
    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }
    public int getDiscountActivateCount() {
        return discountActivateCount;
    }
    public void setDiscountActivateCount(int discountActivateCount) {
        this.discountActivateCount = discountActivateCount;
    }
    public String getStatDate() {
        return statDate;
    }
    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }
    public int getDiscountPayCount() {
        return discountPayCount;
    }
    public void setDiscountPayCount(int discountPayCount) {
        this.discountPayCount = discountPayCount;
    }
    public int getDiscountPayEarning() {
        return discountPayEarning;
    }
    public void setDiscountPayEarning(int discountPayEarning) {
        this.discountPayEarning = discountPayEarning;
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
    public int getPayCount() {
        return payCount;
    }
    public int getPayEarning() {
        return payEarning;
    }
    public int getSyncEarning() {
        return syncEarning;
    }
    public void setActivateCount(int activateCount) {
        this.activateCount = activateCount;
    }
    public void setPayCount(int payCount) {
        this.payCount = payCount;
    }
    public void setPayEarning(int payEarning) {
        this.payEarning = payEarning;
    }
    public void setSyncEarning(int syncEarning) {
        this.syncEarning = syncEarning;
    }
}
