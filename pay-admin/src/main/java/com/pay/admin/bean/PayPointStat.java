package com.pay.admin.bean;
public class PayPointStat {
    private String payName;
    private int price, result, statCount;
    public String getPayName() {
        return payName;
    }
    public int getPrice() {
        return price;
    }
    public int getResult() {
        return result;
    }
    public int getStatCount() {
        return statCount;
    }
    public void setPayName(String payName) {
        this.payName = payName;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setResult(int result) {
        this.result = result;
    }
    public void setStatCount(int statCount) {
        this.statCount = statCount;
    }
}
