package com.pay.server.entity;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 防止同步记录重复
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_setting")
public class SettingEntity extends IdEntity {
    private String hostName;
    private boolean imsiCheck;
    private int discountRate;
    public SettingEntity() {}
    public SettingEntity(Long id) {
        this.id = id;
    }
    public String getHostName() {
        return hostName;
    }
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
    public int getDiscountRate() {
        return discountRate;
    }
    public void setDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }
    public boolean isImsiCheck() {
        return imsiCheck;
    }
    public void setImsiCheck(boolean imsiCheck) {
        this.imsiCheck = imsiCheck;
    }
}
