package com.pay.server.bean;
import org.json.JSONObject;

public class PayReport extends Json {
    public String payInfo;
    public String paySdk;
    public int result;
    public String errorMessage;
    public int paySequence;
    @Override
    protected void init(JSONObject obj) {
        if (obj == null) return;
        payInfo = obj.optString("a");
        paySdk = obj.optString("b");
        result = obj.optInt("c");
        errorMessage = obj.optString("d");
        paySequence=obj.optInt("e");
    }
    @Override
    public JSONObject toJson() {
        JSONObject obj = super.toJson();
        put(obj, "a", payInfo);
        put(obj, "b", paySdk);
        put(obj, "c", result);
        put(obj, "d", errorMessage);
        put(obj, "e", paySequence);
        return obj;
    }
}
