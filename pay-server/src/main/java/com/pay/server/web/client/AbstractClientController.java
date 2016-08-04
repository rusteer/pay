package com.pay.server.web.client;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import com.pay.server.bean.Device;
import com.pay.server.bean.Json;
import com.pay.server.entity.DeviceEntity;
import com.pay.server.entity.PackageEntity;
import com.pay.server.entity.PackageInstantEntity;
import com.pay.server.service.DeviceService;
import com.pay.server.service.PackageActivateStatService;
import com.pay.server.service.PackageInstantAccessService;
import com.pay.server.service.PackageInstantService;
import com.pay.server.service.PackageRetentionStatService;
import com.pay.server.service.PackageService;
import com.pay.server.service.PackageStatService;
import com.pay.server.service.SettingService;
import com.pay.server.utils.AES;
import com.pay.server.utils.FormatUtil;
import com.pay.server.web.AbstractController;

public abstract class AbstractClientController extends AbstractController {
    static final String INSTANT_ID_KEY = "instantId";
    static final String DEVICE_ID_KEY = "deviceId";
    static final String PACKAGE_ID_KEY = "packageId";
    static final String DEVICE_KEY = "device";
    @Autowired
    DeviceService deviceService;
    
    @Autowired
    SettingService settingService;
    @Autowired
    PackageService packagService;
    @Autowired
    PackageStatService packagStatService;
    @Autowired
    PackageInstantService packageInstantService;
    @Autowired
    PackageActivateStatService packageActivateStatService;
    @Autowired
    PackageRetentionStatService packageRetentionStatService;
    @Autowired
    PackageInstantAccessService packageInstantAccessService;
    protected DeviceEntity loadOrSave(String info) {
        Device device = Json.optObj(Device.class, new JSONObject(info));
        DeviceEntity entity = new DeviceEntity();
        entity.setAndroidId(device.androidId);
        entity.setBrand(device.brand);
        entity.setCreateTime(new Date());
        entity.setImei(device.imei);
        entity.setImsi(device.imsi);
        entity.setMacAddress(device.macAddress);
        entity.setManufacturer(device.manufacturer);
        entity.setModel(device.model);
        entity.setSdkVersion(device.sdkVersion);
        entity.setSerial(device.serial);
        return this.deviceService.loadOrSave(entity);
    }
    protected PackageInstantEntity loadPackageInstant(JSONObject requestObj) {
        long instantId = requestObj.optLong(INSTANT_ID_KEY);
        PackageInstantEntity instant = null;
        PackageEntity pkg = null;
        if (instantId > 0) {
            instant = this.packageInstantService.get(instantId);
            if (instant == null) { //
                throw new RuntimeException("can't get packageInsant from id" + instantId);
            }
            pkg = this.packagService.get(instant.getPackageId());
        } else {
            DeviceEntity device = null;
            long deviceId = requestObj.optLong(DEVICE_ID_KEY);
            String deviceInfo = requestObj.optString(DEVICE_KEY);
            if (deviceId > 0) {
                device = this.deviceService.get(deviceId);
                if (device == null) throw new RuntimeException("can't get Device from id:" + deviceId);
            } else if (StringUtils.isNotBlank(deviceInfo)) {
                device = this.loadOrSave(deviceInfo);
            } else {
                throw new RuntimeException("no device info proviced");
            }
            long packageId = requestObj.optLong(PACKAGE_ID_KEY);
            if (packageId == 0) { throw new RuntimeException("no packageId provided"); }
            pkg = this.packagService.get(packageId);
            if (pkg == null) { throw new RuntimeException("can't get package from id:" + packageId); }
            instant = this.packageInstantService.getByUniq(device.getId(), pkg.getId());
            if (instant == null) {
                instant = new PackageInstantEntity();
                instant.setDeviceId(device.getId());
                instant.setPackageId(pkg.getId());
                instant.setCreateTime(new Date());
                instant.setCreateDate(FormatUtil.format(new Date()));
                instant = this.packageInstantService.create(instant);
                if(StringUtils.isNotBlank(device.getImsi()) || !settingService.get().isImsiCheck()  ){
                    packageActivateStatService.increaseActivateCount(pkg);
                    packagStatService.increaseActivateCount(pkg);
                }
            }
        }
        if (instant == null) throw new RuntimeException("no packageInstant loaded");
        if (!packageInstantAccessService.increaseAccessCount(instant.getId())) {
            int retentionDays = (int) ((new Date().getTime() - instant.getCreateTime().getTime()) / 1000 / 3600 / 24);
            packageRetentionStatService.increaseRetentionCount(pkg, instant.getCreateDate(),retentionDays);
        }
        return instant;
    }
    protected void writeClientIds(PackageInstantEntity instant, JSONObject requestObj, JSONObject jsonResult) {
        if (requestObj.optLong(DEVICE_ID_KEY) == 0) {
            jsonResult.put(DEVICE_ID_KEY, instant.getDeviceId());
        }
        if (requestObj.optLong(INSTANT_ID_KEY) == 0) {
            jsonResult.put(INSTANT_ID_KEY, instant.getId());
        }
    }
    protected JSONObject handleError() {
        JSONObject obj = new JSONObject();
        obj.put("clearData", true);
        return obj;
    }
    protected void response(HttpServletRequest request, HttpServletResponse response, JSONObject requestObj, JSONObject responseObj, String password) {
        if (responseObj == null) responseObj = new JSONObject();
        JSONObject logObj = new JSONObject();
        logObj.put("requestObj", requestObj);
        logObj.put("responseObj", responseObj);
        logObj.put("ip", this.getRemoteAddr(request));
        logObj.put("path", this.getRequestPath(request));
        getLogger().info(logObj.toString() + "\n");
        this.write(response, AES.encode(responseObj.toString(), password));
    }
}
