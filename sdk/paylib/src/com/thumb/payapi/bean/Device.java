package com.thumb.payapi.bean;
import org.json.JSONObject;

public class Device extends Json {
    public String uuid;
    //unique fields
    public String imei;
    public String serial;
    public String androidId;
    public String macAddress;
    //
    public String imsi;
    public String manufacturer;
    public String model;
    public int sdkVersion;
    public String brand;
    @Override
    protected void init(JSONObject obj) {
        if (obj == null) return;
        uuid = obj.optString(/*const-replace-start*/"a");
        imei = obj.optString(/*const-replace-start*/"b");
        serial = obj.optString(/*const-replace-start*/"c");
        androidId = obj.optString(/*const-replace-start*/"d");
        macAddress = obj.optString(/*const-replace-start*/"e");
        imsi = obj.optString(/*const-replace-start*/"f");
        manufacturer = obj.optString(/*const-replace-start*/"g");
        model = obj.optString(/*const-replace-start*/"h");
        sdkVersion = obj.optInt(/*const-replace-start*/"i");
        brand = obj.optString(/*const-replace-start*/"j");
    }
    @Override
    public JSONObject toJson()  {
        JSONObject obj = super.toJson();
        put(obj, /*const-replace-start*/"a", uuid);
        put(obj, /*const-replace-start*/"b", imei);
        put(obj, /*const-replace-start*/"c", serial);
        put(obj, /*const-replace-start*/"d", androidId);
        put(obj, /*const-replace-start*/"e", macAddress);
        put(obj, /*const-replace-start*/"f", imsi);
        put(obj, /*const-replace-start*/"g", manufacturer);
        put(obj, /*const-replace-start*/"h", model);
        put(obj, /*const-replace-start*/"i", sdkVersion);
        put(obj, /*const-replace-start*/"j", brand);
        return obj;
    }
}
