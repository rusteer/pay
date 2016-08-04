package com.pay.admin.controller.admin.bean;
public class PayPointCount {
    private int price;
    private String payName;
    private int successCount;
    private int cancleCount;
    private int failureCount;
    private int allCount;
    public PayPointCount(String payName, int price) {
        this.payName = payName;
        this.price = price;
    }
    public int getPrice() {
        return price;
    }
    public String getPayName() {
        return payName;
    }
    public int getSuccessCount() {
        return successCount;
    }
    public int getCancleCount() {
        return cancleCount;
    }
    public int getFailureCount() {
        return failureCount;
    }
    public int getAllCount() {
        return allCount;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setPayName(String payName) {
        this.payName = payName;
    }
    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }
    public void setCancleCount(int cancleCount) {
        this.cancleCount = cancleCount;
    }
    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }
    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }
}
