package com.pay.admin.controller.admin.bean;

import com.pay.admin.entity.ChannelEntity;
import com.pay.admin.entity.PackageEntity;
import com.pay.admin.entity.ProductEntity;

public class Stat {
    private String statDate;
    private long packageId;
    private PackageEntity pkg;
    private ChannelEntity channel;
    private ProductEntity product;
    private int activateCount;
    private int payCount;
    private int payEarning;
    private int syncEarning;
    private int discountActivateCount; //给渠道看的数据
    private int discountPayEarning;//扣量的付费金额
    private String promotionType;
    
    public Stat(long packageId){
        this.packageId=packageId;
    }
    
    public Stat(String statDate){
        this.statDate=statDate;
    }
    public String getStatDate() {
        return statDate;
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
    public int getDiscountActivateCount() {
        return discountActivateCount;
    }
    public int getDiscountPayEarning() {
        return discountPayEarning;
    }
    public void setStatDate(String statDate) {
        this.statDate = statDate;
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
    public void setDiscountActivateCount(int discountActivateCount) {
        this.discountActivateCount = discountActivateCount;
    }
    public void setDiscountPayEarning(int discountPayEarning) {
        this.discountPayEarning = discountPayEarning;
    }
    public String getPromotionType() {
        return promotionType;
    }
    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }
    public long getPackageId() {
        return packageId;
    }
    public PackageEntity getPkg() {
        return pkg;
    }

    public ChannelEntity getChannel() {
        return channel;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setPkg(PackageEntity pkg) {
        this.pkg = pkg;
    }

    public void setChannel(ChannelEntity channel) {
        this.channel = channel;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public void setPackageId(long packageId) {
        this.packageId = packageId;
    }
}
