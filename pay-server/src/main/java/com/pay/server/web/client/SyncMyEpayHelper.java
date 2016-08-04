package com.pay.server.web.client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.pay.server.entity.SyncEntity;
import com.pay.server.utils.FormatUtil;
import com.pay.server.utils.SdkConstants;

public class SyncMyEpayHelper {
    protected static Logger logger = LoggerFactory.getLogger("sync");
    public static final String[] provinces = {
            //
            "3:北京    ",//
            "4:安徽    ",//
            "5:四川    ",//
            "6:重庆    ",//
            "7:福建    ",//
            "8:甘肃    ",//
            "9:广东    ",//
            "10:广西   ",//
            "11:贵州   ",//
            "12:海南   ",//
            "13:河北   ",//
            "14:河南   ",//
            "15:黑龙江 ",//
            "16:湖北   ",//
            "17:湖南   ",//
            "18:吉林   ",//
            "19:江苏   ",//
            "20:江西   ",//
            "21:辽宁   ",//
            "22:内蒙古 ",//
            "23:宁夏   ",//
            "24:青海   ",//
            "25:山东   ",//
            "26:山西   ",//
            "27:陕西   ",//
            "28:上海   ",//
            "29:天津   ",//
            "30:西藏   ",//
            "31:新疆   ",//
            "32:云南   ",//
            "33:浙江   ",//
    };
    public static String getRquestContent(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
        try {
            reader.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return sb.toString();
    }
    private static String getProvinceName(String provinceId) {
        String prefix = provinceId + ":";
        for (String s : provinces) {
            if (s.startsWith(prefix)) { return s.substring(prefix.length()); }
        }
        return null;
    }
    public static SyncEntity composeEntity(String content) {
        try {
            if (content.length() > 0) {
                JSONObject obj = new JSONObject(content);
                String provinceId = obj.optString("provinceId");
                String paymentType = obj.optString("paymentType");
                String serialNumber = obj.optString("serialNumber");
                String price = obj.optString("billFee");
                String result = obj.optString("result");//0：成功,1：失败
                String linkId = obj.optString("indexId");
                SyncEntity entity = new SyncEntity();
                entity.setSdk(SdkConstants.MYEPAY);
                entity.setLinkId(linkId);
                //entity.setPackageInstantId(packageInstantId);
                entity.setPaymentType(paymentType);
                entity.setPrice(Integer.valueOf(price));
                entity.setProvinceName(getProvinceName(provinceId));
                entity.setRawInfo(content);
                entity.setResult(Integer.valueOf(result));
                entity.setSerialNumber(serialNumber);
                entity.setCreateDate(FormatUtil.format(new Date()));
                entity.setCreateTime(new Date());
                return entity;
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    public static String composeResult(boolean success) {
        JSONObject obj = new JSONObject();
        obj.put("resultCode", success ? 0 : 1);
        obj.put("resultMsg", success ? "success" : "failure");
        return obj.toString();
    }
}
