package com.pay.server.service;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.server.entity.DeviceEntity;
import com.pay.server.repository.DeviceDao;

@Component
@Transactional
public class DeviceService extends AbstractService<DeviceEntity> {
    /**
     * 中国移动
     */
    public static final int CARRIER_OPERATOR_CHINAMOBILE = 1;
    /**
     * 中国联通
     */
    public static final int CARRIER_OPERATOR_CHINAUNION = 2;
    /**
     * 中国电信
     */
    public static final int CARRIER_OPERATOR_CHINATELECOM = 2;
    @Autowired
    private DeviceDao dao;
    @Override
    protected DeviceDao getDao() {
        return dao;
    }
    public DeviceEntity loadOrSave(DeviceEntity entity) {
        //private String imei;
        //private String serial;
        //private String androidId;
        //private String macAddress;
        String imei = entity.getImei();
        String androidId = entity.getAndroidId();
        String serial = entity.getSerial();
        String macAddress = entity.getMacAddress();
        DeviceEntity dbData = null;
        if (StringUtils.isNotBlank(imei)) {
            dbData = getFirstOne(this.dao.getByImei(imei));
        }
        if (dbData == null && StringUtils.isNotBlank(androidId)) {
            dbData = getFirstOne(this.dao.getByAndroid(androidId));
        }
        if (dbData == null && StringUtils.isNotBlank(macAddress)) {
            dbData = getFirstOne(this.dao.getByMacAddress(macAddress));
        }
        if (dbData == null && StringUtils.isNotBlank(serial)) {
            dbData = getFirstOne(this.dao.getByMacSerial(serial));
        }
        if (dbData == null) {
            dbData = this.save(entity);
        }
        return dbData;
    }
    private DeviceEntity getFirstOne(List<DeviceEntity> list) {
        return list != null && list.size() > 0 ? list.get(0) : null;
    }
    public int getCarrierOperator(Long deviceId) {
        DeviceEntity device = get(deviceId);
        if (device != null) {
            String imsi = device.getImsi();
            if (imsi != null) {
                if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {//因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号 
                    return CARRIER_OPERATOR_CHINAMOBILE;
                } else if (imsi.startsWith("46001")) {
                    //中国联通 
                    return CARRIER_OPERATOR_CHINAUNION;
                } else if (imsi.startsWith("46003")) {
                    //中国电信 
                    return CARRIER_OPERATOR_CHINATELECOM;
                }
            }
        }
        return -1;
    }
}
