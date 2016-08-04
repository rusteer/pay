package com.pay.admin.entity;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.pay.admin.entity.framework.IdEntity;

@Entity
@Table(name = "t_device")
public class DeviceEntity extends IdEntity {
    //unique fields
    private String imei;
    private String serial;
    private String androidId;
    private String macAddress;
    //
    private String imsi;
    private String manufacturer;
    private String model;
    private int sdkVersion;
    private String brand;
    private Date createTime;
    public DeviceEntity() {}
    public DeviceEntity(Long id) {
        this.id = id;
    }
    public String getImei() {
        return imei;
    }
    public void setImei(String imei) {
        this.imei = imei;
    }
    public String getSerial() {
        return serial;
    }
    public void setSerial(String serial) {
        this.serial = serial;
    }
    public String getAndroidId() {
        return androidId;
    }
    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }
    public String getMacAddress() {
        return macAddress;
    }
    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
    public String getImsi() {
        return imsi;
    }
    public void setImsi(String imsi) {
        this.imsi = imsi;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public int getSdkVersion() {
        return sdkVersion;
    }
    public void setSdkVersion(int sdkVersion) {
        this.sdkVersion = sdkVersion;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}