package com.pay.admin.entity;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.pay.admin.entity.framework.IdEntity;

@Entity
@Table(name = "t_sdk")
public class SdkEntity extends IdEntity {
    private String name;
    private String displayName;
    private String encodedName;
    private int profitRatio;//分成比例,1-100
    private int hot;
    public SdkEntity() {}
    public SdkEntity(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getEncodedName() {
        return encodedName;
    }
    public void setEncodedName(String encodedName) {
        this.encodedName = encodedName;
    }
    public int getProfitRatio() {
        return profitRatio;
    }
    public void setProfitRatio(int profitRatio) {
        this.profitRatio = profitRatio;
    }
    public int getHot() {
        return hot;
    }
    public void setHot(int hot) {
        this.hot = hot;
    }
}
