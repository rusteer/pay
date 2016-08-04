/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.pay.admin.entity;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;
import com.pay.admin.entity.framework.IdEntity;

@Entity
@Table(name = "t_channel")
public class ChannelEntity extends IdEntity {
    private String loginName;
    private String displayName;
    private String password;
    private Date createTime;
    private String shortName;//two chars
    private boolean showActivateCount;
    private boolean showPayCount;
    private boolean showPayEarning;
    private String info;
    private int hot;
    private boolean autoSync;
    private int discountStartCount;
    private int discountRate;
    public ChannelEntity() {}
    public ChannelEntity(Long id) {
        this.id = id;
    }
    @NotBlank
    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getShortName() {
        return shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public boolean isShowActivateCount() {
        return showActivateCount;
    }
    public boolean isShowPayCount() {
        return showPayCount;
    }
    public boolean isShowPayEarning() {
        return showPayEarning;
    }
    public String getInfo() {
        return info;
    }
    public int getHot() {
        return hot;
    }
    public void setShowActivateCount(boolean showActivateCount) {
        this.showActivateCount = showActivateCount;
    }
    public void setShowPayCount(boolean showPayCount) {
        this.showPayCount = showPayCount;
    }
    public void setShowPayEarning(boolean showPayEarning) {
        this.showPayEarning = showPayEarning;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public void setHot(int hot) {
        this.hot = hot;
    }
    public boolean isAutoSync() {
        return autoSync;
    }
    public int getDiscountRate() {
        return discountRate;
    }
    public void setAutoSync(boolean autoSync) {
        this.autoSync = autoSync;
    }
    public void setDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }
    public int getDiscountStartCount() {
        return discountStartCount;
    }
    public void setDiscountStartCount(int discountStartCount) {
        this.discountStartCount = discountStartCount;
    }
}