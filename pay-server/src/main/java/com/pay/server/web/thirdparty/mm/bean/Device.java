package com.pay.server.web.thirdparty.mm.bean;
import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Device", namespace = "http://www.monternet.com/dsmp/schemas/")
public class Device implements Serializable {
    private static final long serialVersionUID = 4596898598547223545L;
    private String deviceType;
    private String deviceId;
    public String getDeviceType() {
        return deviceType;
    }
    @XmlElement(name = "DeviceType", namespace = "http://www.monternet.com/dsmp/schemas/", required = false)
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    public String getDeviceId() {
        return deviceId;
    }
    @XmlElement(name = "DeviceID", namespace = "http://www.monternet.com/dsmp/schemas/", required = false)
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
