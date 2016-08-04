package com.pay.server.web.thirdparty.qihoo;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.pay.server.web.AbstractController;
import com.pay.server.web.thirdparty.qihoo.msdk.QException;
import com.pay.server.web.thirdparty.qihoo.msdk.QOAuth2;

public abstract class AbstractQihooController extends AbstractController {
    Map<String, String> secretMap = new HashMap<String, String>();
    Map<String, QOAuth2> autoMap = new HashMap<String, QOAuth2>();
    protected AbstractQihooController() {
        //appkey-->appsecret
        secretMap.put("57635728cea2947cb2a596c2f55bdc12", "05a298e003de36d8e7e7ab0667e6cd57");//Sugar
        secretMap.put("77a53941d79c87d596a4a54484caa0c9", "dcbce6eee3420665effccf7488d7957f");//Running
        secretMap.put("74d928449a9ced702e132f602605aec2", "6d462c45daa8c625f6e6f0acfa1a3ba0");//糖果传奇 
        secretMap.put("e76c01f5a490a5ff33351f766e2d3527", "61a2a2236ae5b1d738d00595d5775a95");//暴龙战机
        secretMap.put("df6767e016e738f7ee949c73265d0062", "b6e8bf90533a3d890626e42ef04b4efc");//终极狂飙（太空版）
        for (String key : secretMap.keySet()) {
            try {
                autoMap.put(key, new QOAuth2(key, secretMap.get(key), ""));
            } catch (QException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
    protected Logger logger = LoggerFactory.getLogger("qihoo");
    protected   Logger getLogger(){
        return logger;
    }
    protected QOAuth2 getAuth(HttpServletRequest request) throws Throwable {
        String appKey = request.getParameter("appkey");
        return autoMap.get(appKey);
    }
    protected void handleError(HttpServletRequest request, HttpServletResponse response, Throwable e) {
        logger.error(e.getMessage(), e);
        //write(response,e.getMessage());
        write(response, "error");
    }
  

}
