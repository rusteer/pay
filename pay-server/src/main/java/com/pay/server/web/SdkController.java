package com.pay.server.web;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.pay.server.utils.SdkConstants;

@Controller
@RequestMapping("")
public class SdkController extends AbstractController {
    private static final String WEB_CONFIG_DISABLE_JD_EXIT_DIALOG_KEY = "A";
    private static final String WEB_CONFIG_SDK_ARRAY = "B";
    private static final String WEB_CONFIG_SDK_KEY = "C";
    protected Logger logger = LoggerFactory.getLogger("mm");
    @RequestMapping(value = "/api/paychannel")
    public void getPaySdk(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result = getPaySdk(request);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("paychannel", result);
        //result.put("paychannel", "360");
        this.writeWithLog(request, response, null, JSONObject.toJSONString(map));
    }
    @RequestMapping(value = "/api/config")
    public void getEncodedPaySdk(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result = getPaySdk(request);
        Map<String, Object> map = new HashMap<String, Object>();
        if (result != null) {
            map.put(WEB_CONFIG_SDK_KEY, SdkConstants.getEncodedSdk(result));
        }
        this.writeWithLog(request, response, null, JSONObject.toJSONString(map));
    }
    private String getPaySdk(HttpServletRequest request) {
        String result = null;
        String payChannel = request.getParameter("channel");
        int versionCode = Integer.valueOf(request.getParameter("versionCode"));
        String packageName = request.getParameter("package");
        if ("egame".equals(payChannel)) {
            result = "egame";
        } else if ("com.bo.games.runningeveryday".equals(packageName)) {
            result = getRunningChannel(payChannel, versionCode);
        } else if ("com.blueocean.spaceracing".equals(packageName)) {
            result = this.getSpaceChannel(payChannel, versionCode);
        } else if ("com.bo.szumath.superCandy".equals(packageName)) {
            result = this.getCandyChannel(payChannel, versionCode);
        } else if ("com.blueocean.games.leidian".equals(packageName)) {
            result = this.getThunderChannel(payChannel, versionCode);
        } else {
            result = "mm";
        }
        return result;
    }
    @Override
    protected Logger getLogger() {
        return logger;
    }
    private String getRunningChannel(String payChannel, int versionCode) {
        if (versionCode == 13) return SdkConstants.QIHOO;
        return SdkConstants.MM;
    }
    private String getCandyChannel(String payChannel, int versionCode) {
        if (versionCode == 7) {
            //if ("baidu".equals(payChannel)) return SdkConstants.BAIDU;
            //if ("qihoo".equals(payChannel)) return SdkConstants.QIHOO;
             // return "360";
        }
        //return SdkConstants.SKY;
        return SdkConstants.MM;
    }
    private String getSpaceChannel(String payChannel, int versionCode) {
        return SdkConstants.MM;
    }
    private String getThunderChannel(String payChannel, int versionCode) {
        return SdkConstants.MM;
    }
}