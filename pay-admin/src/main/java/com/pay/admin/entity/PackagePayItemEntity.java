package com.pay.admin.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pay.admin.entity.framework.IdEntity;

@Entity
@Table(name = "t_package_pay_item")
public class PackagePayItemEntity extends IdEntity {
    private Long packageId;
    private int payIndex;
    private Long paySdk1;
    private int paySdk1Price;
    private Long paySdk2;
    private int paySdk2Price;
    private Long paySdk3;
    private int paySdk3Price;
    private String payName;
    private int payPrice;
    public Long getPackageId() {
        return packageId;
    }
    public int getPayIndex() {
        return payIndex;
    }
    public Long getPaySdk1() {
        return paySdk1;
    }
    public int getPaySdk1Price() {
        return paySdk1Price;
    }
    public Long getPaySdk2() {
        return paySdk2;
    }
    public int getPaySdk2Price() {
        return paySdk2Price;
    }
    public Long getPaySdk3() {
        return paySdk3;
    }
    public int getPaySdk3Price() {
        return paySdk3Price;
    }
    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }
    public void setPayIndex(int payIndex) {
        this.payIndex = payIndex;
    }
    public void setPaySdk1(Long paySdk1) {
        this.paySdk1 = paySdk1;
    }
    public void setPaySdk1Price(int paySdk1Price) {
        this.paySdk1Price = paySdk1Price;
    }
    public void setPaySdk2(Long paySdk2) {
        this.paySdk2 = paySdk2;
    }
    public void setPaySdk2Price(int paySdk2Price) {
        this.paySdk2Price = paySdk2Price;
    }
    public void setPaySdk3(Long paySdk3) {
        this.paySdk3 = paySdk3;
    }
    public void setPaySdk3Price(int paySdk3Price) {
        this.paySdk3Price = paySdk3Price;
    }
    @Transient
    @JsonIgnore
    public String getPayName() {
        return payName;
    }
    public void setPayName(String payName) {
        this.payName = payName;
    }@Transient
    @JsonIgnore
    public int getPayPrice() {
        return payPrice;
    }
    public void setPayPrice(int payPrice) {
        this.payPrice = payPrice;
    }
}
