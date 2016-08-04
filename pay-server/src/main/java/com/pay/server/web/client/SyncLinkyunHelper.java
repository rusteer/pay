package com.pay.server.web.client;
import java.util.Date;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.pay.server.entity.SyncEntity;
import com.pay.server.utils.FormatUtil;
import com.pay.server.utils.SdkConstants;

public class SyncLinkyunHelper {
    protected static Logger logger = LoggerFactory.getLogger("sync");
    public static JSONObject getRquestContent(HttpServletRequest request) {
        JSONObject obj = new JSONObject();
        Enumeration<String> e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            String value = request.getParameter(key);
            obj.put(key, value);
        }
        return obj;
    }
    public static SyncEntity composeEntity(JSONObject obj, Long channelId) {
        try {
            SyncEntity entity = new SyncEntity();
            entity.setSdk(SdkConstants.LINKYUN);
            entity.setLinkId(obj.getString("orderId"));
            //entity.setPackageInstantId(packageInstantId);
            entity.setPrice(Integer.valueOf(obj.optString("price")));
            entity.setRawInfo(obj.toString());
            entity.setResult(0);
            entity.setCreateDate(FormatUtil.format(new Date()));
            entity.setCreateTime(new Date());
            return entity;
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    public static String composeResult(boolean success) {
        return success ? "0" : "error";
    }
}
