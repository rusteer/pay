package com.pay.server.web.client;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.pay.server.entity.ChannelEntity;
import com.pay.server.service.ChannelService;
import com.pay.server.service.SyncService;

@Controller
@RequestMapping("")
public class SyncController extends AbstractClientController {
    protected Logger logger = LoggerFactory.getLogger("sync");
    @Autowired
    SyncService syncService;
    @Autowired
    ChannelService channelService;    
    @RequestMapping(value = "/api/sync/myepay")
    public void myepay(HttpServletRequest request, HttpServletResponse response) {
        boolean success = false;
        String requestContent = null;
        try {
            requestContent = SyncMyEpayHelper.getRquestContent(request);
            logger.error(requestContent);
            success = syncService.create(SyncMyEpayHelper.composeEntity(requestContent));
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
        String responseText = SyncMyEpayHelper.composeResult(success);
        this.writeRes(request, response, requestContent, responseText);
    }
    
    
    @RequestMapping(value = "/api/sync/linkyun")
    public void linkyun(HttpServletRequest request, HttpServletResponse response) {
        boolean success = false;
        JSONObject requestContent = null;
        try {
            requestContent = SyncLinkyunHelper.getRquestContent(request);
            String channelShortName=requestContent.optString("cpparam").substring(0, 1);
            ChannelEntity channel=this.channelService.getByShortName(channelShortName);
            success = syncService.create(SyncLinkyunHelper.composeEntity(requestContent,channel.getId()));
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
        String responseText = SyncLinkyunHelper.composeResult(success);
        this.writeRes(request, response, requestContent.toString(), responseText);
    }
    
    
    @Override
    protected Logger getLogger() {
        return logger;
    }
    private void writeRes(HttpServletRequest request, HttpServletResponse response, String requestContent, String responseText) {
        if (responseText == null) responseText = " ";
        JSONObject logObj = new JSONObject();
        logObj.put("requestContent", requestContent);
        logObj.put("responseText", responseText);
        logObj.put("ip", this.getRemoteAddr(request));
        logObj.put("path", this.getRequestPath(request));
        getLogger().info(logObj.toString() + "\n");
        this.write(response, responseText.toString());
    }
}