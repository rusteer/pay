package com.pay.admin.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.pay.admin.entity.framework.IdEntity;

@Entity
@Table(name = "t_product_pay_item")
public class ProductPayItemEntity extends IdEntity {
    private Long productId;
    private int payIndex;
    private String payName;
    private int payPrice;
    public Long getProductId() {
        return productId;
    }
    public int getPayIndex() {
        return payIndex;
    }
    public String getPayName() {
        return payName;
    }
    public int getPayPrice() {
        return payPrice;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public void setPayIndex(int payIndex) {
        this.payIndex = payIndex;
    }
    public void setPayName(String payName) {
        this.payName = payName;
    }
    public void setPayPrice(int payPrice) {
        this.payPrice = payPrice;
    }
}
